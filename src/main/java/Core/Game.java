package Core;

import Utility.Utilities;
import Utility.Writer;
import java.text.ParseException;
import java.util.ArrayList;

import static Utility.Constante.*;

public class Game {

    private static int round = 1;

    private static int players;

    private static int currentAge = 1;

    private static ArrayList<Player> playersArray;

    private static GameState state;

    private ArrayList<Card> deck;


    public static void main(String[] args) throws ParseException {

        Writer.init(true);
        Game game = new Game(3);
        while(Game.state != GameState.EXIT)
            game.process();
        Writer.close();
        Utilities.displayGameOutput();
        Writer.deleteFile();
       // Writer.fermerWriter();

    }


    public Game (int players) {
        Game.players = players;
        Game.playersArray = new ArrayList<>(players);
        this.initPlayers();
        Game.state = GameState.START;
        this.deck = new ArrayList<>();

    }


    public static int getCurrentAge() {
        return currentAge;
    }

    public static ArrayList<Player> getPlayersArray() {
        return playersArray;
    }

    public static GameState getState() {
        return state;
    }


    private void initDeck() {

        ArrayList<Card> stack = CardManager.getAgeNDeck(Game.currentAge);
        for(int i = stack.size(); i < MAX_HAND * players;i++ )
            stack.add(stack.get(0));
        Writer.write("stack size : " + stack.size());
        this.deck = stack;

        // stack.clear();
        // Writer.ecrire(deck.size());
        /*   for (int i = 0; i < stack.size(); i++) {
            if(!stack.isEmpty()) {
                Card c = stack.remove(0);
                Writer.ecrire(stack.size());
                this.deck.add(c);
                Writer.ecrire(stack);
                Writer.ecrire("card added");
            }
        }*/
    }

    private void initPlayers() {
        for(int i = 0; i < players; i++)
            Game.playersArray.add(new Player("Bot" + i));


        Writer.write(players + " players have been initialized");
    }

    private void process() throws ParseException {
        switch (Game.state)
        {
            case START:
            {
                Writer.write("The game started with " + Game.players + "players on the board");
                this.processNewAge();
                Game.state = GameState.PLAY;
            }
            break;

            case PLAY:
            {
                Writer.write("Round Start");
                this.processTurn();
                Game.round++;
                Writer.write(" round : " + round);
                this.processEndAge();
            }
            break;

            case END:
            {
                this.displayPlayersRanking();
                Writer.write("Core.Game has ended .. exiting");
                Game.state = GameState.EXIT;

            }
            break;




    }

    }

    private void processTurn() {
        for(Player player : Game.playersArray)
            player.chooseCard();
        for(Player player : Game.playersArray)
            player.chooseAction();

        this.tradeCards(Game.currentAge);
    }

    private void tradeCards(int currentAge) {

        for(int i = 0; i < playersArray.size(); i++) {

            ArrayList<Card> tmpMain = playersArray.get(i).getHand();

            if (currentAge % 2 == 1) {
                //Clockwise trade

                if (i == playersArray.size() - 1) {
                    playersArray.get(i).setHand(playersArray.get(0).getHand());
                    playersArray.get(0).setHand(tmpMain);
                } else {
                    playersArray.get(i).setHand(playersArray.get(i + 1).getHand());
                    playersArray.get(i + 1).setHand(tmpMain);
                }

            } else {

                //Counter clockwise
                if (i == 0) {
                    playersArray.get(0).setHand(playersArray.get(playersArray.size() - 1).getHand());
                    playersArray.get(playersArray.size() - 1).setHand(tmpMain);
                } else {
                    playersArray.get(i).setHand(playersArray.get(i - 1).getHand());
                    playersArray.get(i - 1).setHand(tmpMain);

                }


            }
        }
    }

    private void processNewAge() {
        initDeck();
        initPlayersHand();
        Writer.write("Current age" + Game.currentAge);
        Writer.write("Each player drew " + MAX_HAND + "cards");
    }

    private void processEndAge() {
        this.battle();

        if (Game.round == MAX_ROUNDS && Game.currentAge  == MAX_AGE )
            Game.state = GameState.END;

        if (Game.round == 6 && Game.currentAge < MAX_AGE) {
            for(Player player : Game.playersArray)
                player.getHand().clear();

            Writer.write("Age has ended! ");
            Game.currentAge++;
            this.processNewAge();
            Game.round = 1;
        }

    }

    private void battle() {

        ArrayList<Player> players = Game.playersArray;

        Player p1, p2, p3;

        p1 = players.get(players.size() - 1);
        p2 = players.get(0);
        p3 = players.get(1);

        Game.fight(p1, p2);
        Game.fight(p2, p3);

        for (int i = 1; i < players.size() - 1; i++) {
            p1 = players.get(i-1);
            p2 = players.get(i);
            p3 = players.get(i+1);

            Game.fight(p1, p2);
            Game.fight(p2, p3);
        }

        p1 = players.get(players.size() - 2);
        p2 = players.get(players.size() - 1);
        p3 = players.get(0);

        Game.fight(p1, p2);
        Game.fight(p2, p3);
    }

    private static void fight(Player p1, Player p2) {
        if (p1.getPoints().get(CardPoints.MILITARY) > p2.getPoints().get(CardPoints.MILITARY)) {
            switch (Game.currentAge) {
                case 1 :
                    p1.addMilitaryPoints(1);
                    p2.addMilitaryPoints(-1);
                    break;
                case 2 :
                    p1.addMilitaryPoints(3);
                    p2.addMilitaryPoints(-3);
                    break;
                case 3 :
                    p1.addMilitaryPoints(5);
                    p2.addMilitaryPoints(-5);
                    break;
            }
        } else if (p1.getPoints().get(CardPoints.MILITARY) < p2.getPoints().get(CardPoints.MILITARY)) {
            switch (Game.currentAge) {
                case 1:
                    p1.addMilitaryPoints(-1);
                    p2.addMilitaryPoints(1);
                    break;
                case 2:
                    p1.addMilitaryPoints(-3);
                    p2.addMilitaryPoints(3);
                    break;
                case 3:
                    p1.addMilitaryPoints(-5);
                    p2.addMilitaryPoints(5);
                    break;
            }
        }
    }

    //Getter pour les tests
    public static int getRound() {
        return round;
    }

    public static int getPlayers() {
        return players;
    }

    private void displayPlayersRanking() {

        ArrayList<Player> players = Game.getPlayersArray();
        Player tmpWinner = players.remove(0);

        for(Player p : players) {
            Writer.write(p.getName() + " :  " + p.getCoins() + "coins");
            Writer.write(p.getName() + " :  " + p.getSciencePoint() + "science points");
            if  (p.computeScore() > tmpWinner.computeScore() ||
                (p.computeScore() == tmpWinner.computeScore() && p.getCoins() > tmpWinner.getCoins())) {
                tmpWinner = p;
            }
        }


        Writer.write(tmpWinner.getName() + " won the game with " + tmpWinner.computeScore() + " points !");

    }

    private void initPlayersHand() {
        for(Player player : playersArray)
            for(int i = 0; i < MAX_HAND; i++)
                player.getHand().add(this.deck.remove(0));
    }

}