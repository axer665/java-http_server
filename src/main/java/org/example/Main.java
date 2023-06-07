package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static final String GET = "GET";
    public static final String POST = "POST";
    public static void main(String[] args) {

        Server server = new Server(9999);

        server.addHandler("GET", "/messages", new Handler() {
            public void handle(Request request, Response response) throws IOException {

                final var filePath = Path.of(".", "public", "/default-get2.html");
                final var thmlCode = Files.readString(filePath);

                final var mimeType = Files.probeContentType(filePath);
                final var length = Files.size(filePath);

                response.setResponseCode(200, "OK");
                response.addHeader("Content-Type", mimeType);
                response.addHeader("Content-Length", String.valueOf(length).toString());

                response.addBody(thmlCode);
                response.result();
            }
        });

        server.addHandler("GET", "/index", new Handler() {
            public void handle(Request request, Response response) throws IOException {

                final var filePath = Path.of(".", "public", "/index.html");
                final var thmlCode = Files.readString(filePath);

                final var mimeType = Files.probeContentType(filePath);
                final var length = Files.size(filePath);

                response.setResponseCode(200, "OK");
                response.addHeader("Content-Type", mimeType);
                response.addHeader("Content-Length", String.valueOf(length).toString());

                response.addBody(thmlCode);
                response.result();
            }
        });

        server.addHandler("GET", "/date", new Handler() {
            public void handle(Request request, Response response) throws IOException {

                final var filePath = Path.of(".", "public", "/classic.html");
                final var template = Files.readString(filePath);
                final var htmlCode = template.replace(
                        "{time}",
                        LocalDateTime.now().toString()
                );

                final var mimeType = Files.probeContentType(filePath);
                final var length = Files.size(filePath);

                response.setResponseCode(200, "OK");
                response.addHeader("Content-Type", mimeType);
                response.addHeader("Content-Length", String.valueOf(length).toString());

                response.addBody(htmlCode);
                response.result();
            }
        });

        server.addHandler("POST", "/params", new Handler() {
            public void handle(Request request, Response response) throws IOException {
                final var filePath = Path.of(".", "public", "/params.html");
                final var template = Files.readString(filePath);
                final var htmlCode = template.replace(
                        "{{method}}",
                        request.getMethod()
                ).replace("{{params}}", request.getParameters());

                final var mimeType = Files.probeContentType(filePath);
                final var length = Files.size(filePath);

                response.setResponseCode(200, "OK");
                response.addHeader("Content-Type", mimeType);
                response.addHeader("Content-Length", String.valueOf(length).toString());

                response.addBody(htmlCode);
                response.result();
            }
        });

        server.start();
    }
}


