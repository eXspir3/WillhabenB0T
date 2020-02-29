
import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class WebServer {

    public static void main(String[] args) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 8001), 0);
            ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);

            server.setExecutor(threadPoolExecutor);
            server.createContext("/bot", new BotRequestHandler());
            server.createContext("/user", new UserRequestHandler());

            server.start();

            System.out.println(" Server started on port 8001");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
