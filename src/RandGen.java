import com.google.gson.JsonObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Random;
import java.util.Stack;

class RandGen {
    private static final Random rand = new Random(232423);

    private static final Stack<String> names = new Stack<>();
    private static final Stack<Integer> ids = new Stack<>();

    static int randInt(int start, int end) {
        if (end <= start) return 0;
        return start + rand.nextInt(end - start);
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
        return start + rand.nextFloat() % (end - start);
    }

    static Date randDate() {
        return new Date(randLong(0, new Date().getTime()));
    }

    static String randWord() {
        try {
            RandomAccessFile file = new RandomAccessFile("/home/prance/IntellijProjects/VODsim/src/countries.txt", "r");
            file.seek(randLong(0, file.length()));
            file.readLine();
            String ret = file.readLine();
            file.close();
            return ret;
        } catch (IOException e) {
            System.out.println("/home/prance/IntellijProjects/VODsim/src/countries.txt" + " cannot be opened or read.");
            return "";
        }
    }

    static String randPhrase() {
        JsonObject jsonObject;
        try {
            jsonObject = NetIO.getJsonFromURL("https://corporatebs-generator.sameerkumar.website/");
        } catch (IOException e) {
            System.err.println("Couldn't retrieve genius title for Live :/\n We'll go with this classic one.");
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

    static String randName() throws NullPointerException {
        if (names.isEmpty())
            NetIO.getRandomNames(names);
        return names.pop();
    }

    static int randMovieId() {
        return randProductId("movie", 1000);
    }

    static int randSeriesId() {
        return randProductId("tv", 164);
    }

    private static int randProductId(String type, int maxPages) {
        if (ids.isEmpty())
            NetIO.getRandomIds(type, maxPages, ids);
        return ids.pop();
    }

}
