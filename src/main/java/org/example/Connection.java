package org.example;

import java.io.*;
import java.net.Socket;
import java.util.Map;

public class Connection extends Thread {
    private Socket socket;
    private BufferedReader in;
    private BufferedOutputStream out;

    private Map<String, Map> routes;

    public Connection(Socket socket, Map routes) {
        this.socket = socket;
        this.routes = routes;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedOutputStream(socket.getOutputStream());
            start(); // вызываем run()
        } catch (IOException e) {

        }
    }

    @Override
    public void run() {
        try {
            Request request = new Request(in);
            if (request.analyze()) {
                String method = request.getMethod();
                String path = request.getPath();

                Response response = new Response(this.out);

                if (routes.containsKey(path)) {
                    Map<String, Handler> methods = routes.get(path);
                    Handler handler = methods.get(method);
                    if (handler == null) {
                        // возращаем код 404
                        this.missing();
                        return;
                    }
                    handler.handle(request, response);
                    out.flush();
                } else {
                    // возращаем код 404
                    this.missing();
                }
            }
        } catch (IOException e){

        }
    }

    private void missing() throws IOException {
        out.write((
                "HTTP/1.1 404 Not Found\r\n" +
                        "Content-Length: 0\r\n" +
                        "Connection: close\r\n" +
                        "\r\n"
        ).getBytes());
        out.flush();
    }
}
