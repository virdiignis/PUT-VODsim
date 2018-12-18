import java.util.LinkedList;
import java.util.List;

/**
 * Class representing content provider for VOD system. Each provider has it's own thread, and generates content for the system.
 */
public class Provider extends Thread {
    /**
     * Provider id
     */
    private long uid;
    /**
     * Pay per view
     * false for monthly payment, true for charge per view
     */
    private boolean ppv;
    /**
     * Base price for one Product show if ppv is true, or monthly quote to bill otherwise.
     */
    private float basePrice;

    private List<Product> products;

    Provider() {
        products = new LinkedList<>();
        uid = RandGen.randLong();
        ppv = RandGen.randBool();
        if (ppv) basePrice = RandGen.randFloat(2, 10);
        else basePrice = RandGen.randFloat(500, 4000);
    }

    @Override
    public void run() {
        while (true){
            if (RandGen.randInt(0, 5) == 1) {
                newProduct();
                if(!ppv) renegotiate();
            }
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void renegotiate() {
        basePrice = products.size() * 10 * RandGen.randFloat(2, 40);
    }

    public float bill(){
        if(!ppv) return basePrice;
        float sum = 0;
        for (var product: products) sum += product.getViews()*product.getBasePrice()*0.9;
        return sum;
    }



    private Series createNewSeries(){
        return new Series(this, RandGen.randFloat(3, 10));
    }
    private Movie createNewMovie(){
        return new Movie(this, RandGen.randFloat(10, 50));
    }

    private Live createNewLive(){
        return new Live(this, RandGen.randFloat(5, 50));
    }

    private Product createNewProduct(){
        int choice = RandGen.randInt(0, 3);
        if (choice == 0) return createNewMovie();
        if (choice == 1) return createNewLive();
        return createNewSeries();
    }

    private synchronized void newProduct(){
        products.add(createNewProduct());
    }

    public synchronized List<Product> getProducts() {
        return products;
    }
}
