import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.imageio.ImageIO;
import javax.net.ssl.SSLHandshakeException;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Stack;

class NetIO {
    static String getRandomQuote() throws IOException {
        URL obj = new URL("https://andruxnet-random-famous-quotes.p.mashape.com/?cat=famous&count=1");
        HttpURLConnection con;
        con = (HttpURLConnection) obj.openConnection();
        con.setRequestProperty("X-Mashape-Key", "cGmpxRgH7YmshbZ5cWGXlAUSnSQ2p1AoUcVjsna1QDH9G6mwBX");
        con.setRequestProperty("Accept", "application/json");
        BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream(), Charset.forName("UTF-8")));
        var json = new JsonParser().parse(rd.readLine()).getAsJsonArray();
        return json.get(0).getAsJsonObject().get("quote").getAsString();
    }

    static JsonObject getJsonFromURL(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection con;
        try {
            con = getRedirectedURL(url);
        } catch (IOException ignored) {
            try {
                Thread.sleep(100);
                throw new ConnectException();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            throw new IOException();
        }
        InputStream is = con.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        return new JsonParser().parse(rd.readLine()).getAsJsonObject();
    }

    static BufferedImage imageFromURL(String url) throws IOException {
        HttpURLConnection con = getRedirectedURL(new URL(url));
        return ImageIO.read(con.getInputStream());
    }

    private static HttpURLConnection getRedirectedURL(URL url) throws IOException {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        try {
            con.connect();
            if (con.getResponseCode() == 301) {
                url = new URL(con.getHeaderField("Location"));
                con = (HttpURLConnection) url.openConnection();
                con.connect();
            }
        } catch (SSLHandshakeException e) {
            System.err.println(String.format("SSL error while connecting to %s", url));
        }
        if (con.getResponseCode() == 429) {
            try {
                System.err.println(String.format("Too many requests to MovieDB. %s sleeping for 10 seconds.", Thread.currentThread()));
                Thread.sleep(10000);
            } catch (InterruptedException ignored) {
            }
        }
        if (con.getResponseCode() != 200) {
            System.err.println(String.format("Connection to %s error: %d", url, con.getResponseCode()));
            throw new IOException(String.format("Connection error: %d", con.getResponseCode()));
        }
        return con;
    }

    static void getRandomNames(Stack<String> names) {
        JsonObject jsonObject;
        try {
            jsonObject = getJsonFromURL("https://randomuser.me/api/?inc=name&results=100");
        } catch (IOException e) {
            for (int i = 0; i < 100; i++) names.push("Mushaboom");
            return;
        }
        var arrrr = jsonObject.get("results").getAsJsonArray();
        for (var entry : arrrr) {
            var entryOb = entry.getAsJsonObject().get("name").getAsJsonObject();
            names.push(entryOb.get("first").getAsString() + " " + entryOb.get("last").getAsString());
        }
    }

    static void getRandomIds(String type, int maxPages, Stack<Integer> ids) {
        try {
            JsonObject jsonObject = NetIO.getJsonFromURL(String.format("https://api.themoviedb.org/3/discover/%s/?api_key=%s&vote_count.gte=10&sort_by=popularity.desc&page=%d", type, Constants.API_KEY, RandGen.randInt(1, maxPages)));
            for (var entry : jsonObject.get("results").getAsJsonArray())
                ids.push(entry.getAsJsonObject().get("id").getAsInt());
        } catch (IOException e) {
            ids.push(RandGen.randInt(1, 1024)); //maybe we'll get a lucky hit
        }
    }
}
