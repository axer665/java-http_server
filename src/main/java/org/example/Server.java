package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private ExecutorService threadPool = Executors.newFixedThreadPool(64);
    private int port;

    public Server(int port) {
        this.port = port;
    }

    public void start() {
        try {
            ServerSocket server = new ServerSocket(port);
            try {
                while (true) {
                    Socket socket = server.accept();
                    Connection c = new Connection(socket);
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
