package Core;

import java.util.ArrayList;

import static Utility.Constante.*;

public class Game {


    private static int round = 1;

    private static int players;

    private static int currentAge = 1;

    private static ArrayList<Player> playersArray;

    private static GameState state;



    public Game (int players)
    {
        this.players = players;
        this.playersArray = new ArrayList<>(players);
        this.initPlayers();
        this.state = GameState.START;

    }
    public static void main(String[] args) {

        Game game = new Game(2);
        while(game.state != GameState.EXIT)
            game.process();

    }

    private void initPlayers()
    {
        for(int i = 0; i < players; i++)
            this.playersArray.add(new Player("Bot" + i));


        System.out.println(players + " players have been initialized");
    }

    private void process()
    {
        switch (this.state)
        {
            case START:
            {
                System.out.println("The game started with " + this.players + "players on the board");
                this.processNewAge();
                this.state = GameState.PLAY;
            }
            break;

            case PLAY:
            {

                System.out.println("Round Start");
                this.processTurn();
                this.round++;
                System.out.println("Round has ended, current round : " +  round);
                this.processEndAge();
            }
            break;

            case END:
            {
                this.displayPlayersRanking();
                System.out.println("Core.Game has ended .. exiting");
                this.state = GameState.EXIT;
                break;

            }





    }

    }

    private void processTurn() {
        for(Player player : this.playersArray)
            player.chooseCard();
        for(Player player : this.playersArray)
            player.chooseAction();

        this.tradeCards(this.currentAge);
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
      for(Player player : this.playersArray)
           player.initPlayerHand();

        System.out.println("Current age" + this.currentAge);
        System.out.println("Each player drew " + MAX_HAND + "cards");
    }


    private void processEndAge()
    {
        if(this.round == MAX_ROUNDS && this.currentAge  == MAX_AGE )
            this.state = GameState.END;
        if(this.round == 6 && this.currentAge < MAX_AGE)
        {
            for(Player player : this.playersArray)
                player.getHand().clear();
            System.out.println("Age has ended! ");
            this.currentAge++;
            this.processNewAge();
            this.round = 1;
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

        Player tmpWinner = this.playersArray.get(0);
        for(Player player : this.playersArray) {
            System.out.println(player.getName() + " :  " + player.getCoins() + "coins");
            if(player.getCoins() > tmpWinner.getCoins())
                tmpWinner = player;
        }

        System.out.println(tmpWinner.getName() + " won the game with " + tmpWinner.getCoins());



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