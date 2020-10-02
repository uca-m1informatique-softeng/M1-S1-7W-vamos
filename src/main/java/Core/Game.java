package Core;

import Card.*;
import Exceptions.WondersException;
import Player.*;
import Utility.RecapScore;
import Utility.Utilities;
import Utility.Writer;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static Utility.Constante.*;

public class Game {

    private int round = 1;

    private static int players;

    private int currentAge = 1;

    private static ArrayList<Player> playersArray;

    private GameState state;

    private ArrayList<Card> deck;

    private ArrayList<Wonder> wonderArrayList;

    public static Boolean debug = false;

    private static Random rand = new Random();


    public static void main(String[] args) throws ParseException, IOException, WondersException {

        int nbPlayers = 3;
        String typePartie  = STATS_MODE;

        if(typePartie.equals(GAME_MODE))
        {
            Game game = new Game(nbPlayers);
            Writer.init(true);
            while(game.state != GameState.EXIT)
                game.process();
            Writer.close();
            Utilities.displayGameOutput();
            Writer.deleteFile();
        }
        else if (typePartie.equals(STATS_MODE))
        {
            Writer.init(false);
            RecapScore[] recapScores = new RecapScore[nbPlayers];
            for(int i=0; i<recapScores.length ; i++)
            {
                recapScores[i] = new RecapScore();
            }
            for(int i = 0; i< NB_GAMES_STATS_MODE; i++)
            {
                Game game = new Game(nbPlayers);
                while(game.state != GameState.EXIT)
                    game.process();
                RecapScore[] scoresTmp = game.displayPlayersRanking();
                for(int j=0; j<recapScores.length ; j++)
                {
                    recapScores[j].addRecap(scoresTmp[j]);
                }
            }

            System.out.println(BLUE_UNDERLINED + " ---- Analyzed games : " + NB_GAMES_STATS_MODE + "----\n" + RESET);
            for(int i=0; i<playersArray.size() ; i++)
            {
                recapScores[i].processAvgScore(NB_GAMES_STATS_MODE);
                double victoires = (recapScores[i].getNbVictory()/(double) NB_GAMES_STATS_MODE)*100;
                String joueur= playersArray.get(i).toString();
                System.out.println(joueur +" gets an average score of  "+recapScores[i].getAvgScore());
                System.out.println(joueur +" has a  "+ victoires +"% winrate");
                System.out.println(joueur +" gets "+recapScores[i].getMilitaryPoints() /(double)NB_GAMES_STATS_MODE + "military points per game");
                System.out.println(joueur +" gets   "+recapScores[i].getSciencePoints() /(double)NB_GAMES_STATS_MODE+ " science points per game");
                System.out.println(joueur +" gets "+recapScores[i].getCoins()/(double)NB_GAMES_STATS_MODE + "coins per game");



            }
        }
        else
        {
            throw new RuntimeException("Mode inexistant.");
        }




    }

    public Game (int players) throws WondersException {
        Game.players = players;
        this.playersArray = new ArrayList<>(players);
        this.initPlayers();
        this.state = GameState.START;
        this.deck = new ArrayList<>();
        this.wonderArrayList = WonderManager.parseWonders();
        initPlayersWonders();

    }

    public void initDeck() {

        ArrayList<Card> stack = CardManager.getAgeNDeck(this.currentAge);
        for(int i = stack.size(); i < MAX_HAND * players;i++ )
            stack.add(stack.get(0));
        this.deck = stack;
    }

    public void initPlayers() {
        for (int i = 0; i < players; i++) {
            int randInt = Game.rand.nextInt(100);
            if (randInt < 30) {
                this.playersArray.add(new DumbPlayer("Stupid" + i));
            } else if (randInt < 60){
                this.playersArray.add(new MilitaryPlayer("Warrior" + i));
            } else {
                this.playersArray.add(new IA_One("IA_One" + i));
            }
        }

        for (int i = 0; i < players; i++) {
            Player prevPlayer, nextPlayer;
            if (i > 0) {
                prevPlayer = this.playersArray.get(i-1);
            } else {
                prevPlayer = this.playersArray.get(this.playersArray.size()-1);
            }
            if (i < this.playersArray.size()-1) {
                nextPlayer = this.playersArray.get(i+1);
            } else {
                nextPlayer = this.playersArray.get(0);
            }

            this.playersArray.get(i).setPrevNeighbor(prevPlayer);
            this.playersArray.get(i).setNextNeighbor(nextPlayer);
        }
    }

    private void process() throws ParseException {
        switch (this.state)
        {
            case START:
            {
                Writer.write("The game started with " + Game.players + "players on the board");
                this.processNewAge();
                this.state = GameState.PLAY;
            }
            break;

            case PLAY:
            {
                Writer.write(YELLOW_UNDERLINED + "\n----ROUND " + getRound() + "----" + "\n" + RESET);
                this.processTurn();
                this.round++;
                this.processEndAge();
            }
            break;

            case END:
            {
                this.displayPlayersRanking();
                Writer.write("Core.Game has ended .. exiting");
                this.state = GameState.EXIT;

            }
            break;




    }

    }

    private void processTurn() {
        for(Player player : this.playersArray)
            player.chooseCard();
        for(Player player : this.playersArray)
            player.chooseAction();

        this.swapHands(this.currentAge);
    }

    private void swapHands(int currentAge) {

        ArrayList<ArrayList<Card>> tmpList = new ArrayList<>();
        for(Player player : playersArray)
            tmpList.add(new ArrayList<>(player.getHand()));

        if(debug) {
            for (Player player : playersArray)
                Writer.write("Hand before swapping  : " + player.getHand().toString());
        }
        for(int i = 0; i < playersArray.size(); i++) {

            ArrayList<Card> tmpMain = new ArrayList<>(playersArray.get(i).getHand());

            if (currentAge % 2 == 1) {
                //Clockwise trade

                if (i != 0 )
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
        if(debug) {
            for (Player player : playersArray)
                Writer.write("Hand after swapping  : " + player.getHand().toString());
        }

    }

    private void processNewAge() {
        initDeck();
        initPlayersHand();
        Writer.write("\n" +BLUE_UNDERLINED + "---- CURRENT AGE : " + currentAge  + " ----" + RESET + "\n");
        Writer.write("Each player drew " + MAX_HAND + "cards");
    }

    private void processEndAge() {


        if (this.round == MAX_ROUNDS && this.currentAge  == MAX_AGE )
            this.state = GameState.END;

        if (this.round == 6 && this.currentAge < MAX_AGE) {
            for(Player player : this.playersArray)
                player.getHand().clear();

            this.battle();
            Writer.write("Age has ended! ");
            this.currentAge++;
            this.processNewAge();
            this.round = 1;
        }

    }

    private void battle() {
        for (Player p : this.playersArray) {
            this.fight(p, p.getPrevNeighbor());
            this.fight(p, p.getNextNeighbor());
        }
    }

    private void fight(Player p1, Player p2) {
        if (p1.getPoints().get(CardPoints.MILITARY) > p2.getPoints().get(CardPoints.MILITARY)) {
            switch (this.currentAge) {
                case 1 :
                    p1.addMilitaryPoints(1);
                    Writer.write(p1 + " fought " + p2 + " and won 1 Military Point.");
                    break;
                case 2 :
                    p1.addMilitaryPoints(3);
                    Writer.write(p1 + " fought " + p2 + " and won 3 Military Point.");
                    break;
                case 3 :
                    p1.addMilitaryPoints(5);
                    Writer.write(p1 + " fought " + p2 + " and won 5 Military Point.");
                    break;
            }
        } else if (p1.getPoints().get(CardPoints.MILITARY) < p2.getPoints().get(CardPoints.MILITARY)) {
            p1.addMilitaryPoints(-1);
            Writer.write(p1 + " fought " + p2 + " and lost 1 Military Point.");
        }
    }

    private RecapScore[] displayPlayersRanking() {

        ArrayList<Player> players = this.getPlayersArray();
        Player tmpWinner = players.get(0);
        RecapScore[] playerScores = new RecapScore[players.size()];
        for(Player p : players) {
            Writer.write(p.getName() + " :  " + p.getCoins() + "coins");
            Writer.write(p.getName() + " :  " + p.getSciencePoint() + "science points");
            Writer.write(p.getName() + " :  " + p.getMilitaryPoints() + "military points");
            if  (p.computeScore() > tmpWinner.computeScore() ||
                (p.computeScore() == tmpWinner.computeScore() && p.getCoins() > tmpWinner.getCoins())) {
                tmpWinner = p;
            }
        }

        for(int i = 0; i < players.size();i++)
            playerScores[i] = new RecapScore(players.get(i),players.get(i).equals(tmpWinner));

        Writer.write(tmpWinner.getName() + " won the game with " + tmpWinner.computeScore() + " points !");

        return playerScores;

    }

    private void initPlayersWonders()
    {
        if(debug)
            Writer.write("wonderList size before init : " + wonderArrayList.size());
        for(Player player : playersArray)
        {
            Collections.shuffle(wonderArrayList);
            player.setWonder(wonderArrayList.remove(0));
            Writer.write(player.getName() + " chose " + player.getWonder().getName() + " wonder for this game");

        }
        if(debug)
            Writer.write("wonderList size after init : " + wonderArrayList.size());

    }

    private void initPlayersHand() {
        for(Player player : playersArray)
            for(int i = 0; i < MAX_HAND; i++)
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