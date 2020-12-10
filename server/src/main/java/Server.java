import utility.RecapScore;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static utility.Constante.* ;

public class Server {
    /**
     *   Variable socketIOServer, le serveur.
     */
    private SocketIOServer server;

    public Server(SocketIOServer server){
        this.server = server ;

        server.addConnectListener(socketIOClient -> System.out.println("Server and Socket are connected..."));

        server.addEventListener(STATS, RecapScore.class, (socketIOClient, recapScore, ackRequest) -> {
            System.out.println("Stats received by the Server...");
            recapScore.processAvgScore(NB_GAMES_STATS_MODE);

            double victoires = recapScore.getNbVictory() / (double) (NB_GAMES_STATS_MODE) * 100;
            String joueur = recapScore.getStrategy();
            System.out.println(joueur + " gets an average score of " + recapScore.getAvgScore());
            System.out.println(joueur + " has a " + victoires + "% winrate");
            System.out.println(joueur + " gets " + recapScore.getMilitaryPoints() / (double) NB_GAMES_STATS_MODE + "military points per game");
            System.out.println(joueur + " gets " + recapScore.getSciencePoints() / (double) NB_GAMES_STATS_MODE + " science points per game");
            System.out.println(joueur + " gets " + recapScore.getCoins() / (double) NB_GAMES_STATS_MODE + "coins per game");
            System.out.println(joueur + " gets " + recapScore.getCivilianPoints() / (double) NB_GAMES_STATS_MODE + "civilian point per game");

            File f = new File(DATA_TABLE);
            boolean file = false;
            if(f.exists() && !f.isDirectory()) {
               file = true;
            }

            try (PrintWriter writer = new PrintWriter(new FileOutputStream(new File(DATA_TABLE),true))) {

                StringBuilder sb = new StringBuilder();
                if(!file) {
                    sb.append("Date");
                    sb.append(",");
                    sb.append("Stratégie");
                    sb.append(',');
                    sb.append("AvgScore");
                    sb.append(',');
                    sb.append("MP");
                    sb.append(',');
                    sb.append("SP");
                    sb.append(',');
                    sb.append("Coin");
                    sb.append(',');
                    sb.append("CivilianP");
                    sb.append('\n');
                }
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                sb.append(dtf.format(now));
                sb.append(',');
                sb.append(joueur);
                sb.append(',');
                sb.append(recapScore.getAvgScore());
                sb.append(',');
                sb.append( recapScore.getMilitaryPoints() / (double) NB_GAMES_STATS_MODE);
                sb.append(',');
                sb.append( recapScore.getSciencePoints() / (double) NB_GAMES_STATS_MODE);
                sb.append(',');
                sb.append( recapScore.getCoins() / (double) NB_GAMES_STATS_MODE);
                sb.append(',');
                sb.append(recapScore.getCivilianPoints() / (double) NB_GAMES_STATS_MODE);
                sb.append('\n');

                writer.write(sb.toString());
                writer.close();

                System.out.println("Done writing data to csv file!");

            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
            }

        });
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
