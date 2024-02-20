import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.store.RedissonStore;
import com.corundumstudio.socketio.store.RedissonStoreFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class ChatServer {
    static String redisHost = "localhost";
    static int redisPort = 6379;
    static String redisTopic = "socket.io";
    public static void main(String[] args) {

        Configuration config = new Configuration();
        config.setHostname("localhost");
        config.setPort(8081);

        final SocketIOServer socketIOServer = new SocketIOServer(config);
        socketIOServer.addConnectListener(socketIOClient -> System.out.println("Client Connected:" +socketIOClient.getSessionId()));
        socketIOServer.addDisconnectListener(socketIOClient -> System.out.println("Client Disconnected:" +socketIOClient.getSessionId()));
        socketIOServer.addEventListener("client-1",String.class,(socketIOClient, data, ackSender)->{
            System.out.println("Message received from " + socketIOClient.getSessionId() + ": " + data);
            try (Jedis jedis = new Jedis(redisHost, redisPort)) {
                jedis.publish(redisTopic, data);
            }
        });


        socketIOServer.start();
        System.out.println("Server started on port 8081");
        new Thread(() -> {
            try (Jedis jedis = new Jedis(redisHost, redisPort)) {
                jedis.subscribe(new JedisPubSub() {
                    @Override
                    public void onMessage(String channel, String message) {
                        socketIOServer.getBroadcastOperations().sendEvent("message", message);
                    }
                }, redisTopic);
            }
        }).start();

    }
}