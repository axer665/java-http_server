package org.example;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class Response  {
    private OutputStream out;
    private int codeNumber; // код ответа
    private String codeMessage; // расшифровка кода
    private Map<String, String> headers = new HashMap(); // заголовки
    private String body; // тело ответа (html)

    public Response(OutputStream out)  {
        this.out = out;
    }

    public void setResponseCode(int codeNumber, String codeMessage)  {
        this.codeNumber = codeNumber;
        this.codeMessage = codeMessage;
    }

    public void addHeader(String headerName, String headerValue)  {
        this.headers.put(headerName, headerValue);
    }

    public void addBody(String body)  {
        headers.put("Content-Length", Integer.toString(body.length()));
        this.body = body;
    }

    public void result() throws IOException {
        headers.put("Connection", "Close");
        out.write(("HTTP/1.1 " + codeNumber + " " + codeMessage + "\r\n").getBytes());
        for (String headerName : headers.keySet())  {
            out.write((headerName + ": " + headers.get(headerName) + "\r\n").getBytes());
        }
        out.write("\r\n".getBytes());
        if (body != null)  {
            out.write(body.getBytes());
        }
    }
}
