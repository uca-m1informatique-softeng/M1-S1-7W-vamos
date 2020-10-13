import Utility.RecapScore;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;

import static Utility.Constante.* ;

public class Server {
    public static void main(String[] args) {

        Configuration config = new Configuration();
        config.setHostname("127.0.0.1");
        config.setPort(10101);
        SocketIOServer server = new SocketIOServer(config);
        server.start();

        server.addConnectListener(socketIOClient -> System.out.println("Server and Socket are connected..."));

        server.addEventListener(STATS, RecapScore.class, new DataListener<RecapScore>() {
            @Override
            public void onData(SocketIOClient socketIOClient, RecapScore recapScore, AckRequest ackRequest) throws Exception {
                System.out.println("Stats received by the Server...");
                recapScore.processAvgScore(NB_GAMES_STATS_MODE);
                double victoires = recapScore.getNbVictory() / (double) (NB_GAMES_STATS_MODE) * 100;
                String joueur = recapScore.getStrategy();
                System.out.println(joueur + " gets an average score of  " + recapScore.getAvgScore());
                System.out.println(joueur + " has a  " + victoires + "% winrate");
                System.out.println(joueur + " gets " + recapScore.getMilitaryPoints() / (double) NB_GAMES_STATS_MODE + "military points per game");
                System.out.println(joueur + " gets   " + recapScore.getSciencePoints() / (double) NB_GAMES_STATS_MODE + " science points per game");
                System.out.println(joueur + " gets " + recapScore.getCoins() / (double) NB_GAMES_STATS_MODE + "coins per game");

            }
        } );

    }
}
