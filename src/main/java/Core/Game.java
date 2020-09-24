package Core;

import java.util.ArrayList;

public class Game {

    private int round = 1;

    private int players;

    private int currentAge = 1;

    private ArrayList<Player> playersArray;

    private GameState state;



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
            this.playersArray.add(new Player("test" + i));


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
    }

    private void processNewAge()
    {
      for(Player player : this.playersArray)
           player.initPlayerHand();

        System.out.println("Current age" + this.currentAge);
        System.out.println("Each player drew 7 cards");
    }


    private void processEndAge()
    {
        if(this.round == 6 && this.currentAge  == 3 )
            this.state = GameState.END;
        if(this.round == 6 && this.currentAge < 3)
        {
            for(Player player : this.playersArray)
                player.getHand().clear();
            System.out.println("Age has ended! ");
            this.currentAge++;
            this.processNewAge();
            this.round = 1;
        }

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


}