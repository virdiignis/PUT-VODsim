import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

public class Product {
    BufferedImage image;
    String name, desc;
    Date productionDate;
    int runtime, id;
    Set<String> productionPlaces;
    Provider provider;
    float price, grade;

    Product() {
    } //TODO... remove this on Live and Movie repair.

    public Product(Provider provider, float price) throws IOException {
        this.provider = provider;
        this.price = price;
        productionPlaces = new HashSet<>();

        grade = RandGen.randInt(0, 100);
        productionDate = RandGen.randDate();
        desc = RandGen.randQuote();
        image = NetIO.imageFromURL("https://picsum.photos/200/200/?random");
    }

}
