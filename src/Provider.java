import java.util.LinkedList;
import java.util.List;

/**
 * Class representing content provider for VOD system. Each provider has it's own thread, and generates content for the system.
 */
public class Provider extends Thread {
    /**
     * Provider id
     */
    long uid;
    /**
     * Pay per view
     * false for monthly payment, true for charge per view
     */
    boolean ppv;
    /**
     * Base price for one Product show if ppv is true, or monthly quote to bill otherwise.
     */
    float basePrice;

    List<Product> products;

    public Provider() {
        products = new LinkedList<>();
        uid = RandGen.randLong(0, Long.MAX_VALUE);
        ppv = RandGen.randBool();
        if (ppv) basePrice = RandGen.randFloat(2, 10);
        else basePrice = RandGen.randFloat(500, 4000);
    }
}
