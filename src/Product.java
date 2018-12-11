import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Date;

public class Product {
    public enum Genre {
        Sensacion, Comedy, Drama, Kids, Documental, Action;
    }
    BufferedImage image;
    String name, desc;
    Date productionDate;
    int duration, grade;
    ArrayList<String> productionPlaces;
    Provider provider;
    float price;

}
