package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Request  {
    private BufferedReader in;
    private String method;
    private String path;
    private String url;
    private Map<String, String> headers = new HashMap();
    private Map<String, String> parameters = new HashMap();

    public Request(BufferedReader in) throws IOException {
        this.in = in;
    }

    public String getMethod()  {
        return method;
    }

    public String getPath()  {
        return path;
    }

    public String getUrl()  {
        return url;
    }

    public boolean analyze() throws IOException  {
        String initialLine = in.readLine();
        System.out.println(initialLine);
        if (initialLine == null) {
            return false;
        }

        String[] parts = initialLine.split(" ");

        if (parts.length != 3) {
            return false;
        }

        method = parts[0];
        url = parts[1];

        // отсечем параметры
        if (parts[1].indexOf("?") == -1)  {
            path = parts[1];
        } else  {
            path = parts[1].substring(0, parts[1].indexOf("?"));
        }

        return true;
    }
}
