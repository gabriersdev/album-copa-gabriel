import com.sun.net.httpserver.HttpServer;
import server.ApiHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class ApiMain {
    public static void main(String[] args) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(4321), 0);
            server.createContext("/api/", new ApiHandler());
            server.setExecutor(null); // creates a default executor
            server.start();
            System.out.println("Servidor API iniciado na porta 4321 (http://localhost:4321/api/)");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
