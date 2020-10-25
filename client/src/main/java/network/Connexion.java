package network;

import utility.Writer;
import utility.RecapScore;
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
            Writer.write("Trying to connect to server ...");
            socket = IO.socket("http://127.0.0.1:10101");
            Writer.write("Success !");
        } catch (URISyntaxException ex) {
            Writer.write("Couldn't connect to the server !");
        }
    }

    public void startListening() {
        socket.connect() ;
        Writer.write("Listening on port 10101 ...");
    }

    public void stopListening() {
        socket.disconnect();
        Writer.write("Socket disconnected.");
    }

    public void sendMessage(String tag,StringBuilder msg) {
        Writer.write("Sending message ...");
        socket.emit(tag , msg);
        Writer.write("Message sent !");
    }

    public void sendStats(String tag, RecapScore score)
    {
        Writer.write("Sending players' scores ...");
        socket.emit(tag , new JSONObject(score));
        Writer.write("Players' scores sent !");
    }
    public void receiveMessage(String event, Emitter.Listener fn) {
        socket.on(event, fn);
    }

}