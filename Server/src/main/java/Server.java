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

        // creation du serveur
        SocketIOServer server = new SocketIOServer(config);
        server.start();

        server.addConnectListener(new ConnectListener() {
            @Override
            public void onConnect(SocketIOClient socketIOClient) {
                System.out.println("Server and Socket are connected");
            }
        });

        server.addEventListener(STATS, String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient socketIOClient, String string, AckRequest ackRequest) throws Exception {
                System.out.println("STATS :\n" + string);
            }
        });

    }
}
