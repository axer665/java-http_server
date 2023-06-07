package org.example;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

public class Request  {
    private BufferedReader in;
    private String method;
    private String path;
    private String url;
    private Map<String, String> headers = new HashMap();
    private Map<String, String> parameters = new HashMap();
    private Map<String, String> queryParameters = new HashMap();

    public static final String GET = "GET";
    public static final String POST = "POST";

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

    public String getHeader(String headerName)  {
        return headers.get(headerName);
    }

    public String getParameter(String paramName)  {
        return queryParameters.get(paramName);
    }

    public String getParameters()  {
        StringBuilder params = new StringBuilder();
        for (var param : queryParameters.entrySet()) {
            params.append(param.getKey() + " = " + param.getValue() + "<br>");
        }
        return params.toString();
    }

    public boolean analyze() throws IOException, URISyntaxException {
        String initialLine = in.readLine();
        if (initialLine == null) {
            return false;
        }

        final var parts = initialLine.split(" ");

        if (parts.length != 3) {
            return false;
        }

        method = parts[0];
        url = parts[1];
        path = parts[1];

        // собираем заголовки
        while (true)  {
            String headerLine = in.readLine();
            if (headerLine == null) {
                return false;
            }
            if (headerLine.length() == 0)  {
                break;
            }
            int separator = headerLine.indexOf(":");
            if (separator == -1)  {
                return false;
            }
            headers.put(headerLine.substring(0, separator),
                    headerLine.substring(separator + 1));
        }

        // обрабатываем GET-запрос
        if (!method.equals(POST)) {
            // используем URLEncodedUtils по условиям задачи (но использовать не будем =))
            try {
                URI urlObject = new URI(path);
                List<NameValuePair> formparams = URLEncodedUtils.parse(urlObject,"UTF-8");
                System.out.println(formparams);
            } catch (RuntimeException e){

            }

            // вручную парсим параметры
            if (parts[1].indexOf("?") != -1)  {
                path = parts[1].substring(0, parts[1].indexOf("?"));
                parseParameters(parts[1].substring(parts[1].indexOf("?") + 1));
            }
        }

        // обрабатываем POST-запрос
        if (!method.equals(GET)) {
            int contentLength = 0;
            try {
                contentLength = Integer.parseInt(headers.get("Content-Length").trim());
            } catch (NumberFormatException e) {

            }

            // Парсим тело запроса
            String body = null;
            if (0 < contentLength) {
                char[] c = new char[contentLength];
                in.read(c);
                body = new String(c);
            }
            parseParameters(body);
        }
        return true;
    }

    private void parseParameters(String paramsString) {
        String[] params = paramsString.split("&");
        int separator = paramsString.indexOf('=');
        if (separator > -1) {
            for(String param : params) {
                String[] paramData = param.split("=");
                queryParameters.put(paramData[0], paramData.length > 1 ? paramData[1] : "" );
            }
        }
    }
}
