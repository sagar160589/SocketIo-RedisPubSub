//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import io.socket.client.IO;
import io.socket.client.Socket;

import java.net.URISyntaxException;

public class ChatClient2 {

    public static void main(String[] args) throws URISyntaxException {
        Socket socket = IO.socket("http://localhost:8082");
        socket.on(Socket.EVENT_CONNECT, e->{
            System.out.println("Client Connected to server");
            socket.emit("client-2","Hello from client-2");
        });
        socket.on("message", m->{
            System.out.println("Message from server: " + m[0]);
        });
        socket.connect();
    }
}
