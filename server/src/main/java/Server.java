import utility.RecapScore;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;

import static utility.Constante.* ;

public class Server {
    /**
     *   Variable socketIOServer, le serveur.
     */
    private SocketIOServer server;

    public Server(SocketIOServer server){
        this.server = server ;

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

                disconnectSocket(socketIOClient) ;
            }
        } );
    }

    /**
     * Method who sends an event ENDCONNEXION with a socket to disconnect the client's socket
     * @param socketIOClient the socket who sends the event
     */
    public void disconnectSocket(SocketIOClient socketIOClient){
        socketIOClient.sendEvent(ENDCONNEXION , "Disconnecting the socket");
    }

    /**
     * Method to launch the server
     */
    public void startServer() {
            server.start();
    }
}
