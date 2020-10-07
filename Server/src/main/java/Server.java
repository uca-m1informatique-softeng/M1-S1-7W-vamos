import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;

import static Utility.Constante.* ;

public class Server {
    public static void main(String[] args) {

        Configuration config = new Configuration();
        config.setHostname("127.0.0.1");
        config.setPort(10101);
        SocketIOServer server = new SocketIOServer(config);
        server.start();

        server.addConnectListener(socketIOClient -> System.out.println("Server and Socket are connected..."));

        server.addEventListener(STATS, String.class, (socketIOClient, string, ackRequest) -> System.out.println("STATS :\n" + string));

    }
}
