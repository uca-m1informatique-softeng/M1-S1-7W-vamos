import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;

/**
 * Class Main who launchs the server
 */
public class Main {
    public static void main(String[] args) {
        Configuration config = new Configuration();
        config.setHostname("127.0.0.1");
        config.setPort(10101);

        /**
         * Server creation
          */
        SocketIOServer server = new SocketIOServer(config);

        Server serveur = new Server(server);
        serveur.startServer();
    }
}
