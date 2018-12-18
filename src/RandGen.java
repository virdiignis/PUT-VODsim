import com.google.gson.JsonObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

class RandGen {
    private static final Random rand = new Random();

    static int randInt(int start, int end) {
        if (end < start) return 0;
        return start + rand.nextInt(end);
    }

    static long randLong(long start, long end) {
        return start + Math.round(rand.nextFloat() * (end - start));
    }

    static long randLong() {
        return rand.nextLong();
    }

    static boolean randBool() {
        return rand.nextBoolean();
    }

    static float randFloat(float start, float end) {
        return start + rand.nextFloat() * (end - start);
    }

    static Date randDate() {
        return new Date(randLong(0, new Date().getTime()));
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
            System.out.println(filename + " cannot be opened or read.");
            return "";
        }
    }

    static String randPhrase() {
        JsonObject jsonObject;
        try {
            jsonObject = NetIO.getJsonFromURL("http://corporatebs-generator.sameerkumar.website/");
        } catch (IOException e) {
            System.err.println("Couldn't retrieve genius title for Live :/\n We'll go with this classic one.");
            e.printStackTrace();
            return "Winter is coming.";
        }
        return jsonObject.get("phrase").getAsString();
    }

    static String randQuote() {
        try (InputStream is = new URL("http://ron-swanson-quotes.herokuapp.com/v2/quotes").openStream()) {
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
        } catch (NullPointerException e) {
            System.err.println("Ow, cannot retrieve random name! Guess I'll just have to name all of 'em the same...");
            return "Oh Boi Jr.";
        }
    }

    static int randMovieId(){
        return randProductId("movie", 1000);
    }

    static int randSeriesId(){
        return randProductId("tv", 164);
    }

    private static int randProductId(String type, int maxPages) {
        try {
            JsonObject jsonObject = NetIO.getJsonFromURL(String.format("https://api.themoviedb.org/3/discover/%s/?api_key=%s&vote_count.gte=10&sort_by=popularity.desc&page=%d", type, Constants.API_KEY, randInt(1, maxPages)));
            return jsonObject.get("results").getAsJsonArray().get(rand.nextInt(19)).getAsJsonObject().get("id").getAsInt();
        } catch (IOException e) {
            e.printStackTrace();
            return 293660; //returns Deadpool on error. Seems like a good choice to make.
        }
    }

}
