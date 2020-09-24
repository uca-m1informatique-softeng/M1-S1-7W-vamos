package Core;

import java.util.ArrayList;

import static Utility.Constante.*;

public class Game {


    private static int round = 1;

    private static int players;

    private static int currentAge = 1;

    private static ArrayList<Player> playersArray;

    private static GameState state;

    private ArrayList<Card> deck;


    public Game (int players)
    {
        Game.players = players;
        Game.playersArray = new ArrayList<>(players);
        this.initPlayers();
        Game.state = GameState.START;
        this.deck = new ArrayList<Card>();

    }

    public static void main(String[] args) {

        Game game = new Game(2);
        while(Game.state != GameState.EXIT)
            game.process();

    }

    private void initDeck() {
        for (int i = 0; i < MAX_HAND*Game.players; i++) {
            this.deck.add(null);
        }
    }

    private void initPlayers()
    {
        for(int i = 0; i < players; i++)
            Game.playersArray.add(new Player("Bot" + i));


        System.out.println(players + " players have been initialized");
    }

    private void process()
    {
        switch (Game.state)
        {
            case START:
            {
                System.out.println("The game started with " + Game.players + "players on the board");
                this.processNewAge();
                Game.state = GameState.PLAY;
            }
            break;

            case PLAY:
            {

                System.out.println("Round Start");
                this.processTurn();
                Game.round++;
                System.out.println("Round has ended, current round : " +  round);
                this.processEndAge();
            }
            break;

            case END:
            {
                this.displayPlayersRanking();
                System.out.println("Core.Game has ended .. exiting");
                Game.state = GameState.EXIT;
                break;

            }





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

    private void processNewAge()
    {
        initPlayersHand();
        System.out.println("Current age" + Game.currentAge);
        System.out.println("Each player drew " + MAX_HAND + "cards");
    }


    private void processEndAge()
    {
        if(Game.round == MAX_ROUNDS && Game.currentAge  == MAX_AGE )
            Game.state = GameState.END;
        if(Game.round == 6 && Game.currentAge < MAX_AGE)
        {
            for(Player player : Game.playersArray)
                player.getHand().clear();
            System.out.println("Age has ended! ");
            Game.currentAge++;
            this.processNewAge();
            Game.round = 1;
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

        Player tmpWinner = Game.playersArray.get(0);
        for(Player player : Game.playersArray) {
            System.out.println(player.getName() + " :  " + player.getCoins() + "coins");
            if(player.getCoins() > tmpWinner.getCoins())
                tmpWinner = player;
        }

        System.out.println(tmpWinner.getName() + " won the game with " + tmpWinner.getCoins());



    }

    private void initPlayersHand() {
        for(Player player : playersArray)
            for(int i = 0; i < MAX_HAND; i++)
                player.getHand().add(this.deck.get(0));
                this.deck.remove(0);
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
}