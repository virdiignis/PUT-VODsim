import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
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

    static String randWord(String filename){
        try{
            RandomAccessFile file = new RandomAccessFile(filename, "r");
            file.seek(randLong(0, file.length()));
            file.readLine();
            String ret = file.readLine();
            file.close();
            return ret;
        } catch (IOException e){
            System.out.println(filename + "cannot be opened or read.");
            return "";
        }
    }
}
