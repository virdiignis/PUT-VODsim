import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReadWriteLock;


/**
 * Class modeling enduser of VOD system, each user has it's own thread and generates requests for VOD materials.
 */
class User extends Thread {
    private final Date birthday;
    private final String email;
    private final String creditCard;
    private final long uid;
    private final Semaphore debtSemaphore;
    private final LinkedList<Product> avaliableProducts;
    private final ReadWriteLock productsLock;
    private final AtomicBoolean pause;

    User(LinkedList<Product> avaliableProducts, ReadWriteLock productsLock, AtomicBoolean pause) {
        this.productsLock = productsLock;
        this.pause = pause;
        debtSemaphore = new Semaphore(1);
        this.avaliableProducts = avaliableProducts;
        debt = 0;
        birthday = RandGen.randDate();
        email = String.format("%s@gmail.com", RandGen.randName().replaceAll("\\s", "."));
        creditCard = String.format("%d %d %d %d %d", RandGen.randInt(1000, 9999), RandGen.randInt(1000, 9999), RandGen.randInt(1000, 9999), RandGen.randInt(1000, 9999), RandGen.randInt(1000, 9999));
        uid = RandGen.randLong();
        if (RandGen.randBool()) {
            int abo = RandGen.randInt(0, 2);
            abonament = new Abonament(abo);
        }
        setDaemon(true);
    }

    public Date getBirthday() {
        return birthday;
    }

    public String getEmail() {
        return email;
    }

    public String getCreditCard() {
        return creditCard;
    }

    public Abonament getAbonament() {
        return abonament;
    }

    public float getDebt() {
        return debt;
    }

    private Abonament abonament;
    private float debt;

    public Semaphore getDebtSemaphore() {
        return debtSemaphore;
    }

    public LinkedList<Product> getAvaliableProducts() {
        return avaliableProducts;
    }

    public ReadWriteLock getProductsLock() {
        return productsLock;
    }

    public AtomicBoolean getPause() {
        return pause;
    }

    public long getUid() {
        return uid;
    }

    synchronized float pay() {
        if (abonament != null) return abonament.getVersion().price;
        float tmp = 0;
        try {
            debtSemaphore.acquire();
            tmp = debt;
            debt = 0;
            debtSemaphore.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return tmp;
    }

    @Override
    public void run() {
        while (true) {
            if (!pause.get())
                watchSomething();
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void watchSomething() {
        productsLock.readLock().lock();
        Product p = avaliableProducts.get(RandGen.randInt(0, avaliableProducts.size()));
        p.view();
        if (p instanceof Live || abonament == null) debt += p.getPromotionPrice();
        productsLock.readLock().unlock();
    }
}
