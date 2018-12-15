import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

class RandGen {
    static final Random rand = new Random();

    static int randInt(int start, int end) {
        return start + rand.nextInt(end);
    }

    static long randLong(long start, long end) {
        return start + Math.round(rand.nextFloat() * (end - start));
    }

    static boolean randBool() {
        return rand.nextBoolean();
    }

    static float randFloat(float start, float end) {
        return start + rand.nextFloat() * (end - start);
    }

    static Date randDate() {
        var gc = GregorianCalendar.getInstance();
        gc.set(Calendar.YEAR, RandGen.randInt(1900, 2015));
        gc.set(Calendar.DAY_OF_YEAR, RandGen.randInt(1, gc.getActualMaximum(Calendar.DAY_OF_YEAR)));
        return gc.getTime();
    }

    static String randWord(String filename) {
        try {
            RandomAccessFile file = new RandomAccessFile(filename, "r");
            file.seek(randLong(0, file.length()));
            file.readLine();
            String ret = file.readLine();
            file.close();
            return ret;
        } catch (IOException e) {
            System.out.println(filename + "cannot be opened or read.");
            return "";
        }
    }

    static String randQuote() {
        try (InputStream is = new URL("https://ron-swanson-quotes.herokuapp.com/v2/quotes").openStream()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            StringBuilder b = new StringBuilder();
            b.append(rd.readLine());
            b.delete(0, 2);
            b.delete(b.length() - 2, b.length());
            return b.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    static String randName() {
        try (InputStream is = new URL("http://randomprofile.com/api/api.php?countries=GBR&format=csv").openStream()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            StringBuilder b = new StringBuilder();
            rd.readLine();
            String[] vals = rd.readLine().split(",");
            b.append(vals[1]);
            b.append(" ");
            b.append(vals[2]);
            return b.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    static int randMovieId(){
        try {
            URL url = new URL(String.format("https://api.themoviedb.org/3/discover/movie/?api_key=%s&vote_count.gte=10&page=%d", Constants.API_KEY, randInt(1, 1000)));
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.connect();
            if(con.getResponseCode() == 301) {
                url = new URL(con.getHeaderField("Location"));
                con = (HttpURLConnection) url.openConnection();
                con.connect();
            }
            if (con.getResponseCode() != 200) throw new ConnectException("Cannot download API contents.");
            InputStream is = con.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            JsonObject jsonObject = new JsonParser().parse(rd.readLine()).getAsJsonObject();
            return jsonObject.get("results").getAsJsonArray().get(rand.nextInt(19)).getAsJsonObject().get("id").getAsInt();
        } catch (IOException e) {
            e.printStackTrace();
            return 293660; //returns Deadpool on error. Seems like a good choice to make.
        }
    }
}
