import java.util.ArrayList;
import java.util.Date;

public class Series extends Product {
    class Episode{
        Date premiere;
        int duration;
    }
    ArrayList<ArrayList<Episode>> seasons;
    ArrayList<String> actors;

}
