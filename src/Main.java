import com.sun.net.httpserver.HttpServer;
import controller.Controller;
import server.ApiHandler;
import utils.Util;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    // Ponto de entrada para execução através do Terminal
    public static void main(String[] args) {
        Util.printStringArray(new String[]{
                "Você deseja:",
                "- [0] Digitar as informações",
                "- [1] Ler um arquivo"
        }, 2);

        switch (Controller.getDesiredOption()) {
            case 0 -> Controller.inputData(1);
            case 1 -> Controller.tryReadFile(1);
        }

        Controller.nextStep();
    }

    public static class ApiMain {
        public static void main(String[] args) {
            try {
                HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
                server.createContext("/api/", new ApiHandler());
                server.setExecutor(null); // creates a default executor
                server.start();
                System.out.println("Servidor API iniciado na porta 8080 (http://localhost:8080/api/)");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
