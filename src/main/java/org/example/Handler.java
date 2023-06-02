package org.example;

import java.io.BufferedOutputStream;
import java.io.IOException;

public interface Handler {
    public void handle(Request request, Response response) throws IOException;
}
