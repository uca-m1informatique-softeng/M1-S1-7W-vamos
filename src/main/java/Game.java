import java.util.ArrayList;

public class Game {

    private int round = 1;

    private int players;

    private ArrayList<Player> playersArray;

    private GameState state;



    public Game (int players)
    {
        this.players = players;
        playersArray = new ArrayList<>(players);
        initPlayers();
        state = GameState.START;

    }
    public static void main(String[] args) {

        Game game = new Game(2);
        while(game.round != 8)
            game.process();

    }



    private void initPlayers()
    {
        for(int i = 0; i < players; i++)
            playersArray.add(new Player());


        System.out.println(players + " players have been initialized");
    }

    private void process()
    {
        switch (state)
        {
            case START:
            {
                System.out.println("The game started with " + players + "players on the board");
                state = GameState.PLAY;
            }
            break;

            case PLAY:
            {
                System.out.println("Round Start");



                round++;
                System.out.println("Round has ended, current round : " +  round);
            }
            break;

            case END:
            {
                displayPlayersRanking();
                System.out.println("Game has ended .. exiting");
                break;

            }





    }




}

    private void displayPlayersRanking() {
    }


}