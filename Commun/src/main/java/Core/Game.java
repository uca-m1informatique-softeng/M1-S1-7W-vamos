package Core;

import Card.*;
import Effects.CoinCardEffect;
import Effects.CopyOneGuildEffect;
import Effects.PlaySeventhCardEffect;
import Exceptions.WondersException;
import Network.Connexion;
import Player.*;
import Utility.RecapScore;
import Utility.Utilities;
import Utility.Writer;
import Wonder.Wonder;
import Wonder.WonderManager;

import javax.imageio.IIOException;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;

import static Utility.Constante.*;

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
     * Number of players in the game
     */

    private static int players;

    /**
     * Current age, once this hits 3 and last round is played, the game is finished
     */
    private int currentAge = 1;

    /**
     * container of the players
     */
    private static ArrayList<Player> playersArray;

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

    private static SecureRandom rand = new SecureRandom();


    public static void main(String[] args) throws WondersException, IOException {
        StringBuilder stringBuilder = new StringBuilder();

        int nbPlayers = Integer.parseInt(args[0]);
        String typePartie  = args[1];
        /**
         String typePartie  = GAME_MODE;
         /*
         *  Game mode, normal game, game output is displayed
         */
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
        }
        /**
         *  Stats mode, no game output is displayed, only end game stats.
         */
        else if (typePartie.equals(STATS_MODE)) {
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

            System.out.println(BLUE_UNDERLINED + " ---- Analyzed games : " + NB_GAMES_STATS_MODE + "----\n" + RESET);
            for (int i = 0; i < playersArray.size(); i++) {
                recapScores[i].processAvgScore(NB_GAMES_STATS_MODE);
                double victoires = (recapScores[i].getNbVictory() / (double) NB_GAMES_STATS_MODE) * 100;
                String joueur = playersArray.get(i).toString();
                System.out.println(joueur + " gets an average score of  " + recapScores[i].getAvgScore());
                System.out.println(joueur + " has a  " + victoires + "% winrate");
                System.out.println(joueur + " gets " + recapScores[i].getMilitaryPoints() / (double) NB_GAMES_STATS_MODE + "military points per game");
                System.out.println(joueur + " gets   " + recapScores[i].getSciencePoints() / (double) NB_GAMES_STATS_MODE + " science points per game");
                System.out.println(joueur + " gets " + recapScores[i].getCoins() / (double) NB_GAMES_STATS_MODE + "coins per game");

                stringBuilder.append(joueur + " gets an average score of  " + recapScores[i].getAvgScore() + "\n");
                stringBuilder.append(joueur + " has a  " + victoires + "% winrate \n");
                stringBuilder.append(joueur + " gets " + recapScores[i].getMilitaryPoints() / (double) NB_GAMES_STATS_MODE + "military points per game \n");
                stringBuilder.append(joueur + " gets   " + recapScores[i].getSciencePoints() / (double) NB_GAMES_STATS_MODE + " science points per game \n");
                stringBuilder.append(joueur + " gets " + recapScores[i].getCoins() / (double) NB_GAMES_STATS_MODE + "coins per game \n");
            }
            //Stats sent to the server
            Connexion.CONNEXION.startListening();
            Connexion.CONNEXION.sendMessage(STATS, stringBuilder);
            //Connexion.CONNEXION.stopListening();
        } else {
            throw new RuntimeException("Mode inexistant.");
        }
    }

    public Game(int players) throws IOException {
        if (players < MIN_PLAYER || players > MAX_PLAYER)
            throw new RuntimeException("You must launch the game with 3 or 4 players");
        Game.players = players;
        Game.playersArray = new ArrayList<>(players);
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
        ArrayList<Card> stack = CardManager.getAgeNDeck(this.currentAge);
        this.deck = stack;
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
                prevPlayer = Game.playersArray.get(i - 1);
            } else {
                prevPlayer = Game.playersArray.get(Game.playersArray.size() - 1);
            }
            if (i < Game.playersArray.size() - 1) {
                nextPlayer = Game.playersArray.get(i + 1);
            } else {
                nextPlayer = Game.playersArray.get(0);
            }
            Game.playersArray.get(i).setPrevNeighbor(prevPlayer);
            Game.playersArray.get(i).setNextNeighbor(nextPlayer);
        }
    }

    /**
     * Main function of the game, process based on Game's current state
     */
    private void process() throws IOException {
        switch (this.state) {
            case START:
                Writer.write("The game started with " + Game.players + "players on the board");
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
                Writer.write("Core.Game has ended .. exiting");
                this.state = GameState.EXIT;
                break;
            default:
                break;
        }

    }

    /**
     * This function adds victory points to the player based on the card he built
     */
    private void addVictoryPoints() {
        for (Player player : this.playersArray) {
            Integer brownCards = 0;
            Integer greyCards = 0;
            Integer yellowCards = 0;
            ArrayList<Card> builtCards = player.getBuiltCards();
            for (Card card : builtCards) {
                switch (card.getColor()) {
                    case BROWN:
                        brownCards += 1;
                        break;
                    case GREY:
                        greyCards += 1;
                        break;
                    case YELLOW:
                        yellowCards += 1;
                        break;
                    default:
                        break;
                }
            }
            for (Card card : player.getBuiltCards()) {
                if (card.getEffect() instanceof CoinCardEffect) {
                    if (card.getCoinCardEffect() == null) {
                        player.getPoints().put(CardPoints.VICTORY, player.getWonder().getState() * 3);
                        continue;
                    }
                    switch (card.getCoinCardEffect()) {
                        case BROWN:
                            player.getPoints().put(CardPoints.VICTORY, brownCards);
                            break;
                        case GREY:
                            player.getPoints().put(CardPoints.VICTORY, greyCards);
                            break;
                        case YELLOW:
                            player.getPoints().put(CardPoints.VICTORY, yellowCards);
                            break;
                        default:
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
        for (Player player : Game.playersArray) {
            player.play();
        }
        this.swapHands(this.currentAge);
    }

    private void swapHands(int currentAge) {
        ArrayList<ArrayList<Card>> tmpList = new ArrayList<>();
        for (Player player : playersArray)
            tmpList.add(new ArrayList<>(player.getHand()));

        if (debug) {
            for (Player player : playersArray)
                Writer.write("Hand before swapping  : " + player.getHand().toString());
        }
        for (int i = 0; i < playersArray.size(); i++) {
            ArrayList<Card> tmpMain = new ArrayList<>(playersArray.get(i).getHand());

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
        for (Player player : Game.playersArray) {
            for (int i = 0; i < player.getWonder().effects.size(); i++) {
                if (player.getWonder().getEffects().get(i) != null) {
                    if ((player.getWonder().getEffects().get(i).getClass()).equals(effectClass)) {
                        if (effectClass == PlaySeventhCardEffect.class) {
                            player.getWonder().getEffects().get(i).applyEffect(player, null, null, null);
                            Writer.write("Player was able to play seventh card");
                        } else if (effectClass == CopyOneGuildEffect.class) {
                            player.getWonder().getEffects().get(i).applyEffect(player, CardColor.PURPLE, null, null);
                            Writer.write("Player was able to copy a guild");
                        }
                    }
                }
            }

        }
    }

    private void processEndAge() throws IOException {
        if (this.round == MAX_ROUNDS) {
            this.checkEffect(PlaySeventhCardEffect.class);
        }
        if (this.round == MAX_ROUNDS && this.currentAge < MAX_AGE) {
            for (Player player : Game.playersArray)
                player.getHand().clear();
            this.battle();
            Writer.write("Age has ended! ");
            this.currentAge++;
            this.processNewAge();
            this.round = 1;
        } else if (this.round == MAX_ROUNDS && this.currentAge == MAX_AGE) {
            this.checkEffect(CopyOneGuildEffect.class);
            this.state = GameState.END;
        }
    }

    /**
     * Launches fights between all players in playersArray
     */
    private void battle() {
        Writer.write(BLUE_BOLD + "\nPlayers start fighting !" + RESET);
        for (Player p : Game.playersArray) {
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
    private void fight(Player p1, Player p2) {
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
                    throw new RuntimeException("Age cannot be superior to 3 or inferior to 1!");
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
        ArrayList<Player> players = this.getPlayersArray();
        Player tmpWinner = players.get(0);
        RecapScore[] playerScores = new RecapScore[players.size()];
        Writer.write("\n" + BLUE_UNDERLINED + "----SCORES----");
        Writer.write(YELLOW_BRIGHT);
        for (Player p : players) {
            Writer.write(p.getName() + " :  " + p.getCoins() + " coins");
            Writer.write(p.getName() + " :  " + p.getSciencePoint() + " science points");
            Writer.write(p.getName() + " :  " + p.getMilitaryPoints() + " military points");
            Writer.write(p.getName() + " :  " + p.getPoints().get(CardPoints.VICTORY) + " victory points");
            if (p.computeScore() > tmpWinner.computeScore() ||
                    (p.computeScore() == tmpWinner.computeScore() && p.getCoins() > tmpWinner.getCoins())) {
                tmpWinner = p;
            }
        }

        for (int i = 0; i < players.size(); i++)
            playerScores[i] = new RecapScore(players.get(i), players.get(i).equals(tmpWinner));

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
        for (Player player : Game.playersArray) {
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

    private void initPlayersHand() {
        for (Player player : Game.playersArray)
            for (int i = 0; i < MAX_HAND; i++)
                player.getHand().add(this.deck.remove(0));
    }

    //Getter pour les tests
    public int getRound() {
        return round;
    }

    public static int getPlayers() {
        return players;
    }

    public int getCurrentAge() {
        return currentAge;
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