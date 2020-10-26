import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import org.mockito.Mockito;

import static utility.Constante.ENDCONNEXION;

class ServerTest {

    private Server server;
    private SocketIOClient socketIOClient;

    @BeforeEach
    void setUp(){
        server = new Server(Mockito.mock(SocketIOServer.class));
        server = Mockito.spy(server);
        socketIOClient = Mockito.mock(SocketIOClient.class);
    }

    @Test
    void disconnectSocket(){
        server.disconnectSocket(socketIOClient);
        Mockito.verify(socketIOClient).sendEvent(Mockito.eq(ENDCONNEXION) , Mockito.anyString());
    }

}
