import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

class NetIO {
    static JsonObject getJsonFromURL(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.connect();
        if (con.getResponseCode() == 301) {
            url = new URL(con.getHeaderField("Location"));
            con = (HttpURLConnection) url.openConnection();
            con.connect();
        }

        if (con.getResponseCode() != 200) throw new ConnectException(String.format("Cannot download API contents from %s", con.getURL()));
        InputStream is = con.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        return new JsonParser().parse(rd.readLine()).getAsJsonObject();
    }

    static BufferedImage imageFromURL(String url) throws IOException {
        return ImageIO.read(new URL(url));
    }
}
