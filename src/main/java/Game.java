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
        playersArray = new ArrayList<>(players);
        initPlayers();
        state = GameState.START;

    }
    public static void main(String[] args) {

        Game game = new Game(2);
        while(game.state != GameState.END)
            game.process();

    }



    private void initPlayers()
    {
        for(int i = 0; i < players; i++)
            playersArray.add(new Player("test" + i));


        System.out.println(players + " players have been initialized");
    }

    private void process()
    {
        switch (state)
        {
            case START:
            {
                System.out.println("The game started with " + players + "players on the board");
                processNewAge();
                state = GameState.PLAY;
            }
            break;

            case PLAY:
            {

                System.out.println("Round Start");
                round++;
                System.out.println("Round has ended, current round : " +  round);
                processEndAge();
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

    private void processNewAge()
    {
      for(Player player : playersArray)
           player.initPlayerHand();

        System.out.println("Current age" + currentAge);
        System.out.println("Each player drew 7 cards");
    }


    private void processEndAge()
    {
        if(round == 8 && currentAge  == 3 )
            state = GameState.END;
        if(round == 8 && currentAge < 3)
        {
            System.out.println("Age has ended! ");
            currentAge++;
            processNewAge();
            round = 1;
        }

    }


    private void displayPlayersRanking() {
    }


}