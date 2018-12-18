import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

public class Simulation extends Thread {
    private LinkedList<Provider> providers;
    private LinkedList<User> users;
    private LinkedList<Promotion> promotions;
    private LinkedList<Product> products;
    Semaphore productsSemaphore;
    private AtomicBoolean pause;
    private float account;
    private long simTime;

    public Simulation() {
        providers = new LinkedList<>();
        users = new LinkedList<>();
        promotions = new LinkedList<>();
        products = new LinkedList<>();
        productsSemaphore = new Semaphore(1);
        pause = new AtomicBoolean(false);
    }

    public synchronized void pause() {
        pause.set(!pause.get());
    }

    @Override
    public void run() {
        simTime=0;
        while (!pause.get()) {
            if (RandGen.randBool()) for (int i = 0; i < RandGen.randInt(1, providers.size()); i++)
                users.add(new User(products));
            if (simTime % 7 == 0) {
                for (var user : users) account += user.pay();
                for (var provider : providers) account -= provider.bill();
            }
            if (RandGen.randInt(0, 14) == 0) {
                var p = new Promotion(simTime);
                promotions.add(p);
                for (var product: products)
                    if (RandGen.randBool()) product.setPromotion(p);
            }
            LinkedList<Promotion> remove = new LinkedList<>();
            for (var promotion : promotions) if(promotion.getEnd().getTime() <= simTime) remove.add(promotion);
            for (var promotion : remove) promotions.remove(promotion);

            products.clear();
            for (var provider : providers) products.addAll(provider.getProducts());

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

    public void addProvider() {
        var pro = new Provider();
        providers.add(pro);
        pro.start();
    }
}
