import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Simulation extends Thread {
    private LinkedList<Provider> providers;
    private LinkedList<User> users;
    private LinkedList<Promotion> promotions;
    private LinkedList<Product> products;
    ReadWriteLock productsLock;
    private AtomicBoolean pause;
    private float account;
    private long simTime;

    public Simulation() {
        providers = new LinkedList<>();
        users = new LinkedList<>();
        promotions = new LinkedList<>();
        products = new LinkedList<>();
        productsLock = new ReentrantReadWriteLock();
        pause = new AtomicBoolean(false);
    }

    public synchronized void pause() {
        pause.set(!pause.get());
    }

    @Override
    public void run() {
        simTime = 0;
        while (!pause.get()) {
            if (RandGen.randBool()) for (int i = 0; i < RandGen.randInt(1, providers.size()); i++)
                users.add(new User(products, productsLock));
            if (simTime % 7 == 0) {
                for (var user : users) account += user.pay();
                for (var provider : providers) account -= provider.bill();
            }
            if (RandGen.randInt(0, 14) == 0) {
                var p = new Promotion(simTime);
                promotions.add(p);
                for (var product : products)
                    if (RandGen.randBool()) product.setPromotion(p);
            }
            LinkedList<Promotion> remove = new LinkedList<>();
            for (var promotion : promotions) if (promotion.getEnd().getTime() <= simTime) remove.add(promotion);
            for (var promotion : remove) promotions.remove(promotion);

            productsLock.writeLock().lock();
            products.clear();
            for (var provider : providers) products.addAll(provider.getProducts());
            productsLock.writeLock().unlock();


            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            simTime++;
        }
    }

    public int getUsersNo() {
        return users.size();
    }

    public int getProductsNo() {
        return products.size();
    }

    public LinkedList<Product> getProducts() {
        return products;
    }

    public void addProvider() {
        var pro = new Provider(productsLock);
        providers.add(pro);
        pro.start();
    }
}
