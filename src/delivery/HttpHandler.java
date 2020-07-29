package delivery;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class HttpHandler {
    public HttpHandler(){
        try {
            runHttpServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void runHttpServer() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8050), 0);
        HttpContext context_test = server.createContext("/test");
        context_test.setHandler(HttpHandler::handleRequest);

        HttpContext context_test2 = server.createContext("/test2");
        context_test2.setHandler(HttpHandler::handleRequest2);
        server.start();
    }

    private static void handleRequest(HttpExchange exchange) throws IOException {
        System.out.println(exchange.getHttpContext().getPath());
        String response = "Hi there!";
        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private static void handleRequest2(HttpExchange exchange) throws IOException {
        System.out.println(exchange.getHttpContext().getPath());
        String response = "Hi there2!";
        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

}
