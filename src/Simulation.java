import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class Simulation extends Thread {
    private final LinkedList<Provider> providers;
    private final LinkedList<User> users;
    private final LinkedList<Promotion> promotions;
    private final LinkedList<Product> products;
    private final ReadWriteLock productsLock;
    private final AtomicBoolean pause;

    public Simulation() {
        providers = new LinkedList<>();
        users = new LinkedList<>();
        promotions = new LinkedList<>();
        products = new LinkedList<>();
        productsLock = new ReentrantReadWriteLock();
        pause = new AtomicBoolean(false);
        setDaemon(true);
    }

    private float account;

    public float getAccount() {
        return account;
    }

    public LinkedList<Provider> getProviders() {
        return providers;
    }

    public LinkedList<User> getUsers() {
        return users;
    }

    public LinkedList<Promotion> getPromotions() {
        return promotions;
    }

    public ReadWriteLock getProductsLock() {
        return productsLock;
    }

    private long simTime;

    public long getSimTime() {
        return simTime;
    }

    public synchronized void pause() {
        pause.set(!pause.get());
    }

    @Override
    public void run() {
        simTime = 0;
        while (true) {
            if (!pause.get()) {
                if (RandGen.randBool()) for (int i = 0; i < RandGen.randInt(1, providers.size()); i++)
                    synchronized (users) {
                        users.add(new User(products, productsLock, pause));
                    }
                if (simTime % 7 == 0) {
                    for (var user : users) account += user.pay();
                    for (var provider : providers) account -= provider.bill();
                    System.out.println(account);
                    System.out.flush();
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
            } else {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ignored) {
                }
            }
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

    public void deleteUser(int u) {
        synchronized (users) {
            users.remove(u);
        }
    }

    public Provider addProvider() {
        var pro = new Provider(productsLock, pause);
        providers.add(pro);
        pro.start();
        return pro;
    }
}
