package core;

import card.*;
import effects.*;
import exceptions.AgeException;
import exceptions.ModeException;
import exceptions.PlayerNumberException;
import io.socket.emitter.Emitter;
import network.Connexion;
import org.json.JSONObject;
import player.*;
import utility.RecapScore;
import utility.Utilities;
import utility.Writer;
import wonder.Wonder;
import wonder.WonderManager;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;

import static utility.Constante.*;

/**
 * Game class which will act as the core engine of the game.
 * Will be handled as a state machine
 */
public class Game {

    /**
     * Current round of the current age
     */
    private int round = 1;

    /**
     * Current age, once this hits 3 and last round is played, the game is finished
     */
    private int currentAge = 1;

    /**
     * Number of players in the game
     */
    private int players;

    /**
     * container of the players
     */
    private ArrayList<Player> playersArray;

    /**
     * Current state of the game
     */
    private GameState state;

    /**
     * Current pool of cards of the current age
     */
    private ArrayList<Card> deck;
    /**
     * Pool of wonders players choose one from
     */
    private ArrayList<Wonder> wonderArrayList;

    public static Boolean debug = false;

    /**
     * The field that contains the discard pile. It is used during the effect : TookDiscardedCardEffect.
     */
    private ArrayList<Card> discardCards = new ArrayList<>();


    public static void main(String[] args) throws IOException, ModeException, PlayerNumberException {

        int nbPlayers = Integer.parseInt(args[0]);
        String typePartie = args[1];

        if (typePartie.equals(GAME_MODE)) {
            Game game = new Game(nbPlayers);
            Writer.init(true);
            while (game.state != GameState.EXIT)
                game.process();
            Writer.close();
            Utilities.displayGameOutput();
            try {
                Writer.deleteFile();
            } catch (IOException e) {
                Writer.write("Could not delete file !");
            }
        } else if (typePartie.equals(STATS_MODE)) {
            Writer.init(false);
            RecapScore[] recapScores = new RecapScore[nbPlayers];
            for (int i = 0; i < recapScores.length; i++) {
                recapScores[i] = new RecapScore();
            }
            for (int i = 0; i < NB_GAMES_STATS_MODE; i++) {
                Game game = new Game(nbPlayers);
                while (game.state != GameState.EXIT)
                    game.process();
                RecapScore[] scoresTmp = game.displayPlayersRanking();
                for (int j = 0; j < recapScores.length; j++) {
                    recapScores[j].addRecap(scoresTmp[j]);
                }
            }

            Writer.write(BLUE_UNDERLINED + " ---- Analyzed games : " + NB_GAMES_STATS_MODE + "----\n" + RESET);
            Connexion.CONNEXION.startListening();
            for (int i = 0; i < nbPlayers; i++) {
                Connexion.CONNEXION.sendStats(STATS, recapScores[i]);
            }
            //disconnecting the socket once the event ENDCONNEXION is received
            Connexion.CONNEXION.receiveMessage(ENDCONNEXION, objects -> {
                Connexion.CONNEXION.stopListening();
                System.exit(0);
            });
        } else {
            throw new ModeException("Mode inexistant.");
        }
    }

    public Game(int players) throws IOException, PlayerNumberException {
        if (players < MIN_PLAYER || players > MAX_PLAYER)
            throw new PlayerNumberException("You must launch the game with 3 or 4 players");
        this.players = players;
        this.playersArray = new ArrayList<>(players);
        this.initPlayers();
        this.state = GameState.START;
        this.deck = new ArrayList<>();
        this.wonderArrayList = WonderManager.parseWonders();
        this.initPlayersWonders();
    }

    /**
     * Load the cards of the current age in the game engine
     */
    public void initDeck() throws IOException {
        this.deck = (ArrayList<Card>) CardManager.getAgeNDeck(this.currentAge);
    }

    /**
     * Instantiate the players
     */
    public void initPlayers() {

        this.playersArray.add(new Player("Bot1"));
        this.playersArray.add(new Player("Bot2"));
        this.playersArray.add(new Player("Bot3"));

        for (int i = 0; i < players; i++) {
            Player prevPlayer, nextPlayer;
            if (i > 0) {
                prevPlayer = this.playersArray.get(i - 1);
            } else {
                prevPlayer = this.playersArray.get(this.playersArray.size() - 1);
            }
            if (i < this.playersArray.size() - 1) {
                nextPlayer = this.playersArray.get(i + 1);
            } else {
                nextPlayer = this.playersArray.get(0);
            }
            this.playersArray.get(i).setPrevNeighbor(prevPlayer);
            this.playersArray.get(i).setNextNeighbor(nextPlayer);
        }
    }

    /**
     * Main function of the game, process based on Game's current state
     */
    private void process() throws IOException {
        switch (this.state) {
            case START:
                Writer.write("The game started with " + this.players + "players on the board");
                this.processNewAge();
                this.state = GameState.PLAY;
                break;

            case PLAY:
                Writer.write(YELLOW_UNDERLINED + "\n----ROUND " + getRound() + "----" + "\n" + RESET);
                this.processTurn();
                this.round++;
                this.processEndAge();
                break;

            case END:
                this.addVictoryPoints();
                this.displayPlayersRanking();
                Writer.write("core.Game has ended .. exiting");
                this.state = GameState.EXIT;
                break;
            default:
                break;
        }

    }

    /**
     * This function adds victory points to the player based on the card he built
     */
    public void addVictoryPoints() {
        JSONObject cardCount = new JSONObject();
        cardCount.put("brownCards", 0);
        cardCount.put("greyCards", 0);
        cardCount.put("yellowCards", 0);

        for (Player player : this.playersArray) {
            ArrayList<Card> builtCards = player.getBuiltCards();
            Card.countCards(cardCount, builtCards);
            for (Card card : player.getBuiltCards()) {
                if (card.getEffect() instanceof CoinCardEffect) {
                    /*
                     * coinCardEffect is null when it is applied on a wonder (e.g. "PYRAMID" for card "arena)
                     * otherwise it is assigned a color
                     */
                    if (card.getCoinCardEffect() == null) {
                        player.getPoints().put(CardPoints.VICTORY, player.getWonder().getState() * 3);
                    } else {
                        switch (card.getCoinCardEffect()) {
                            case BROWN:
                                System.out.println("aqui brown");
                                player.getPoints().put(CardPoints.VICTORY, cardCount.getInt("brownCards"));
                                break;
                            case GREY:
                                System.out.println("aqui grey");
                                player.getPoints().put(CardPoints.VICTORY, cardCount.getInt("greyCards"));
                                break;
                            case YELLOW:
                                player.getPoints().put(CardPoints.VICTORY, cardCount.getInt("yellowCards"));
                                break;
                            default:
                        }
                        break;
                    }
                }
            }
        }
    }

    /**
     * Function to process one round during the game
     */
    private void processTurn() {
        for (Player player : this.playersArray) {
            for (Effect e : player.getWonderEffectNotApply()) {
                if (e instanceof TookDiscardCardEffect) {
                    e.applyEffect(player, null, null, null, discardCards);
                    player.getWonderEffectNotApply().remove(e);
                    discardCards.remove(player.getChosenCard());
                    break;
                }
            }
            player.play();
            if (player.getDumpCard() != null) {
                discardCards.add(player.getDumpCard());
            }
        }
        this.swapHands(this.currentAge);
    }

    public void swapHands(int currentAge) {
        ArrayList<ArrayList<Card>> tmpList = new ArrayList<>();
        for (Player player : playersArray)
            tmpList.add(new ArrayList<>(player.getHand()));

        if (debug) {
            for (Player player : playersArray)
                Writer.write("Hand before swapping  : " + player.getHand().toString());
        }
        for (int i = 0; i < playersArray.size(); i++) {
            if (currentAge % 2 == 1) {
                //Clockwise trade

                if (i != 0)
                    playersArray.get(i).setHand(tmpList.get(i - 1));
                else
                    playersArray.get(i).setHand(tmpList.get(playersArray.size() - 1));

            } else {

                //Counter clockwise
                if (i == 0)
                    playersArray.get(playersArray.size() - 1).setHand(tmpList.get(i));
                else
                    playersArray.get(i - 1).setHand(tmpList.get(i));
            }
        }
        if (debug) {
            for (Player player : playersArray)
                Writer.write("Hand after swapping  : " + player.getHand().toString());
        }
    }

    private void processNewAge() throws IOException {
        initDeck();
        initPlayersHand();
        Writer.write("\n" + BLUE_UNDERLINED + "---- CURRENT AGE : " + currentAge + " ----" + RESET + "\n");
        Writer.write("Each player drew " + MAX_HAND + "cards");
    }

    private void checkEffect(Class effectClass) {
        for (Player player : this.playersArray) {
            for (int i = 0; i < player.getWonder().effects.size(); i++) {
                if  (player.getWonder().getEffects().get(i) != null && (player.getWonder().getEffects().get(i).getClass()).equals(effectClass)) {
                    if (effectClass == PlaySeventhCardEffect.class) {
                        player.getWonder().getEffects().get(i).applyEffect(player, null, null, null, null);
                        Writer.write("player was able to play seventh card");
                    } else if (effectClass == CopyOneGuildEffect.class) {
                        player.getWonder().getEffects().get(i).applyEffect(player, CardColor.PURPLE, null, null, null);
                        Writer.write("player was able to copy a guild");
                    }
                }
            }

        }
    }

    private void processEndAge() throws IOException {
        if (this.round == MAX_ROUNDS) {
            this.checkEffect(PlaySeventhCardEffect.class);

            for (Player player : this.playersArray)
                player.getHand().clear();
            try {
                this.battle();
            } catch (AgeException e) {
                Writer.write(e.getMessage());
            }
            Writer.write("Age has ended! ");

            if (this.currentAge < MAX_AGE) {
                this.currentAge++;
                this.processNewAge();
                this.round = 1;
            } else {
                this.checkEffect(CopyOneGuildEffect.class);
                this.state = GameState.END;
            }
        }
    }

    /**
     * Launches fights between all players in playersArray
     */
    private void battle() throws AgeException {
        Writer.write(BLUE_BOLD + "\nPlayers start fighting !" + RESET);
        for (Player p : this.playersArray) {
            this.fight(p, p.getPrevNeighbor());
            this.fight(p, p.getNextNeighbor());
        }
    }

    /**
     * This function handles the fight mechanism between a player and another
     *
     * @param p1 first player
     * @param p2 second one
     */
    public void fight(Player p1, Player p2) throws AgeException {
        if (p1.getPoints().get(CardPoints.MILITARY) > p2.getPoints().get(CardPoints.MILITARY)) {
            switch (this.currentAge) {
                case 1:
                    p1.addMilitaryPoints(1);
                    Writer.write(String.format(STR_BATTLE_FORMAT, p1, p2, 1));
                    break;
                case 2:
                    p1.addMilitaryPoints(3);
                    Writer.write(String.format(STR_BATTLE_FORMAT, p1, p2, 3));
                    break;
                case 3:
                    p1.addMilitaryPoints(5);
                    Writer.write(String.format(STR_BATTLE_FORMAT, p1, p2, 5));
                    break;
                default:
                    throw new AgeException("Age cannot be superior to 3 or inferior to 1!");
            }
        } else if (p1.getPoints().get(CardPoints.MILITARY) < p2.getPoints().get(CardPoints.MILITARY)) {
            p1.addMilitaryPoints(-1);

            p1.addDefeatToken(1);

            Writer.write(p1 + " fought " + p2 + " and lost 1 Military Point.");
        }
    }

    /**
     * This function handles end game's output
     *
     * @return a recap score for stats mode
     */
    private RecapScore[] displayPlayersRanking() {
        ArrayList<Player> playersCopy = this.getPlayersArray();
        Player tmpWinner = playersCopy.get(0);
        RecapScore[] playerScores = new RecapScore[playersCopy.size()];

        Writer.write("\n" + BLUE_UNDERLINED + "----SCORES----");
        Writer.write(YELLOW_BRIGHT);
        for (Player p : playersCopy) {
            Writer.write(p + " : ");
            Writer.write("\t" + p.getCoins() + " coins");
            Writer.write("\t" + p.getSciencePoint() + " science points");
            Writer.write("\t" + p.getMilitaryPoints() + " military points");
            Writer.write("\t" + p.getPoints().get(CardPoints.VICTORY) + " victory points");
            Writer.write("\t" + p.computeScore() + " score points");

            if  (p.computeScore() > tmpWinner.computeScore() ||
                (p.computeScore() == tmpWinner.computeScore() && p.getCoins() > tmpWinner.getCoins())) {
                tmpWinner = p;
            }
        }

        for (int i = 0; i < playersCopy.size(); i++)
            playerScores[i] = new RecapScore(playersCopy.get(i), playersCopy.get(i).equals(tmpWinner));

        Writer.write(WHITE_BOLD + tmpWinner.getName() + " won the game with " + tmpWinner.computeScore() + " points !" + RESET);

        return playerScores;

    }

    /**
     * each player chooses a wonder at the beginning of the game
     */
    private void initPlayersWonders() {
        ArrayList<String> bannedWonders = new ArrayList<>();

        if (debug)
            Writer.write("wonderList size before init : " + wonderArrayList.size());
        for (Player player : this.playersArray) {
            Collections.shuffle(wonderArrayList);
            Wonder tmpWonder = wonderArrayList.get(0);

            while (bannedWonders.contains(tmpWonder.getName().substring(0, tmpWonder.getName().length() - 1))) {
                Collections.shuffle(wonderArrayList);
                tmpWonder = wonderArrayList.get(0);
            }

            bannedWonders.add(tmpWonder.getName().substring(0, tmpWonder.getName().length() - 1));
            tmpWonder.setName(tmpWonder.getName().substring(0, tmpWonder.getName().length() - 1));
            player.setWonder(wonderArrayList.remove(0));
            player.getResources().put(player.getWonder().getProducedResource(), player.getResources().get(player.getWonder().getProducedResource()) + 1);
            player.chooseStrategy();

        }
        if (debug)
            Writer.write("wonderList size after init : " + wonderArrayList.size());

    }

    public void initPlayersHand() {
        for (Player player : this.playersArray)
            for (int i = 0; i < MAX_HAND; i++)
                player.getHand().add(this.deck.remove(0));
    }

    //Getter pour les tests
    public int getRound() {
        return round;
    }

    public int getPlayers() {
        return this.players;
    }

    public int getCurrentAge() {
        return currentAge;
    }

    public void setCurrentAge(int currentAge) {
        this.currentAge = currentAge;
    }

    public ArrayList<Player> getPlayersArray() {
        return playersArray;
    }

    public GameState getState() {
        return state;
    }

    public ArrayList<Card> getDeck() {
        return deck;
    }
}