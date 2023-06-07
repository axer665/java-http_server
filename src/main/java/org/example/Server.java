package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private ExecutorService threadPool = Executors.newFixedThreadPool(64);
    private int port;

    public Server(int port) {
        this.port = port;
    }

    Map<String, Map> routes = new HashMap();

    public void addHandler(String method, String route, Handler handler) {
        if (routes.containsKey(route) == false) {
            Map<String, Handler> methods = new HashMap();
            methods.put(method, handler);
            this.routes.put(route,methods);
        } else {
            Map<String, Handler> routeMethod = routes.get(route);
            routeMethod.put(method, handler);
            this.routes.put(route, routeMethod);
        }
    }

    public void start() {
        try {
            ServerSocket server = new ServerSocket(port);
            try {
                while (true) {
                    Socket socket = server.accept();
                    Connection c = new Connection(socket, this.routes);
                    threadPool.execute(c);
                }
            } finally {
                threadPool.shutdown();
                server.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
