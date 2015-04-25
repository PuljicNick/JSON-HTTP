package http_request;

import javafx.concurrent.Task;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
//Group developed in class - Not part of IA Code just for HTTP usage

public class HttpRequestTask extends Task<String> {
    private static byte GET = 1;
    private static byte POST = 2;
    private byte type;
    private FormData data;
    URL url;

    public static void get(String urlText, HttpResponseHandler handler) {
        new HttpRequestTask(GET, urlText, null, handler);
    }

    public static void get(String urlText, FormData data, HttpResponseHandler handler) {
        new HttpRequestTask(GET, urlText, data, handler);
    }

    public static void post(String urlText, FormData data, HttpResponseHandler handler) {
        new HttpRequestTask(POST, urlText, data, handler);
    }

    public HttpRequestTask(byte type, String urlText, FormData data, HttpResponseHandler successHandler) {
        super();
        this.type = type;
        this.data = data;
        try {
            if ((type == GET) && (data != null)) {
                if (urlText.contains("?")) {
                    urlText += "&" + data.toString();
                } else {
                    urlText += "?" + data.toString();
                }
            }
            url = new URL(urlText);
            this.setOnSucceeded(successHandler);
            Thread th = new Thread(this);
            th.setDaemon(true);
            th.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String call() throws Exception {
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Language", "en-US");
            conn.setUseCaches(false);
            if (type == POST) {
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");

                conn.setRequestProperty("Content-Length", "" +
                        Integer.toString(data.toString().getBytes().length));

                conn.setDoInput(true);
                conn.setDoOutput(true);

                DataOutputStream wr = new DataOutputStream (
                        conn.getOutputStream ());
                wr.writeBytes(data.toString());
                wr.flush();
                wr.close();
            }
            try {
                Integer responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();

                    String line;

                    while ((line = rd.readLine()) != null) {
                        sb.append(line);
                        sb.append('\n');
                    }

                    return sb.toString();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
