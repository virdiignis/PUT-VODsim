import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class Product {

    public enum Genre {
        Sensation, Comedy, Drama, Kids, Documental, Action
    }

    Genre genre;
    BufferedImage image;
    String name, desc;
    Date productionDate;
    int duration, grade;
    Set<String> productionPlaces;
    Provider provider;
    float price;

    public Product(){}

    public Product(Provider provider, float price) throws IOException {
        this.provider = provider;
        this.price = price;
        productionPlaces = new HashSet<>();
        for (int i = 0; i < RandGen.randInt(Constants.MIN_PRODUCTION_PLACES, Constants.MAX_PRODUCTION_PLACES); i++) {
            productionPlaces.add(RandGen.randWord("/home/prance/IdeaProjects/PUT-VODsim/src/Constants.java"));
        }
        grade = RandGen.randInt(0, 100);
        productionDate = RandGen.randDate();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < Constants.TITLES_LENGTH; i++) {
            stringBuilder.append(String.format("%s ", RandGen.randWord("/home/prance/IdeaProjects/PUT-VODsim/src/words_alpha.txt")));
        }
        name = stringBuilder.toString();
        stringBuilder = new StringBuilder();
        for (int i = 0; i < Constants.DESC_LENGTH; i++) {
            stringBuilder.append(String.format("%s ", RandGen.randWord("/home/prance/IdeaProjects/PUT-VODsim/src/words_alpha.txt")));
        }
        desc = stringBuilder.toString();
        image = ImageIO.read(new URL("https://picsum.photos/200/200/?random"));
        genre = Genre.values()[RandGen.randInt(0, 5)];

    }
}
