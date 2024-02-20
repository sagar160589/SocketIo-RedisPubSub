//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import io.socket.client.IO;
import io.socket.client.Socket;

import java.net.URISyntaxException;

public class ChatClient {

    public static void main(String[] args) throws URISyntaxException {
        Socket socket = IO.socket("http://localhost:8081");
        socket.on(Socket.EVENT_CONNECT, e->{
            System.out.println("Client Connected to server");
            socket.emit("client-1","Hello from client-1");
        });
        socket.on("message", m->{
            System.out.println("Message from server: " + m[0]);
        });
        socket.connect();
    }
}
