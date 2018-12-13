import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class Product {
    public enum Genre {
        Sensation, Comedy, Drama, Kids, Documental, Action
    }
    Genre genre;
    BufferedImage image;
    String name, desc;
    Date productionDate;
    int duration, grade;
    ArrayList<String> productionPlaces;
    Provider provider;
    float price;

    public Product(Provider provider, float price) {
        this.provider = provider;
        this.price = price;
        int productionPlacesNo = RandGen.randInt(1, 13);
        FileReader countries;
        try {
            countries = new FileReader("countries.txt");
        } catch (FileNotFoundException e){
            System.out.println("Countries list file not found!");
            System.exit(1);
        }
        for (int i=0; i<productionPlacesNo;++i){
            countries.
        }
    }
}
