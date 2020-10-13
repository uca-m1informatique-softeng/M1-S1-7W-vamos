package Network;

import Utility.Writer;
import Utility.RecapScore;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONObject;

import java.net.URISyntaxException;

public enum Connexion {
    CONNEXION;

    private Socket socket ;

    Connexion() {
        try {
            System.out.println("try");
            socket = IO.socket("http://127.0.0.1:10101");
        } catch (URISyntaxException ex) {
            Writer.write("Couldn't connect to the server !");
        }
    }

    public void startListening() {
        System.out.println("Listening");
        socket.connect() ;
    }

    public void stopListening() {
        socket.disconnect();
        System.out.println("Socket disconnected");
    }

    public void sendMessage(String tag,StringBuilder msg) {
        System.out.println("Emit");
        socket.emit(tag , msg);
    }

    public void sendStats(String tag, RecapScore score)
    {
        System.out.println("sending player score...");
        socket.emit(tag , new JSONObject(score));
    }
    public void receiveMessage(String event, Emitter.Listener fn) {
        socket.on(event, fn);
    }


}