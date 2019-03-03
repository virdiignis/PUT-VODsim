import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * Class representing content provider for VOD system. Each provider has it's own thread, and generates content for the system.
 */
public class Provider extends Thread {
    /**
     * Provider id
     */
    private final long uid;
    /**
     * Pay per view
     * false for monthly payment, true for charge per view
     */
    private final boolean ppv;
    /**
     * Base price for one Product show if ppv is true, or monthly quote to bill otherwise.
     */
    private float basePrice;

    private final List<Product> products;
    private final ReadWriteLock productsLock;
    private final AtomicBoolean pause;

    Provider(ReadWriteLock productsLock, AtomicBoolean pause) {
        this.productsLock = productsLock;
        this.pause = pause;
        products = new LinkedList<>();
        uid = RandGen.randLong();
        ppv = RandGen.randBool();
        if (ppv) basePrice = RandGen.randFloat(2, 10);
        else basePrice = RandGen.randFloat(500, 4000);
        setDaemon(true);
    }

    public long getUid() {
        return uid;
    }

    @Override
    public void run() {
        while (true) {
            if (!pause.get() && RandGen.randInt(0, 10) == 1) {
                newProduct();
                if (!ppv) renegotiate();
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

    public float bill() {
        if (!ppv) return basePrice;
        float sum = 0;
        for (var product : products) sum += product.getViews() * product.getBasePrice() * 0.9;
        return sum;
    }


    private Series createNewSeries() throws IOException {
        return new Series(this, RandGen.randFloat(3, 10));
    }

    private Movie createNewMovie() throws IOException {
        return new Movie(this, RandGen.randFloat(10, 50));
    }

    private Live createNewLive() {
        return new Live(this, RandGen.randFloat(5, 50));
    }

    private Product createNewProduct() throws IOException {
        int choice = RandGen.randInt(0, 10);
        if (choice < 5) return createNewMovie();
        if (choice == 5) return createNewLive();
        return createNewSeries();
    }

    public synchronized void deleteProduct(Product p) {
        productsLock.writeLock().lock();
        products.remove(p);
        productsLock.writeLock().unlock();
    }

    private synchronized void newProduct() {
        try {
            productsLock.writeLock().tryLock();
            Product p = null;
            do {
                try {
                    p = createNewProduct();
                } catch (IOException ignored) {
                }
            } while (p == null || products.contains(p));
            products.add(p);
            productsLock.writeLock().unlock();
        } catch (IllegalMonitorStateException ignored) {
        }
    }

    public synchronized List<Product> getProducts() {
        return products;
    }
}
