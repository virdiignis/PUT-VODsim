import java.io.IOException;
import java.util.ArrayList;

public class Movie extends Product {
    ArrayList<String> actors, trailers;
    int  rentTime;
    Promotion promotion;

    public Movie(Provider provider, float price) throws IOException {
        super(provider, price);
        rentTime = RandGen.randInt(duration, 10000);

    }
}
