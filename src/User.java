import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReadWriteLock;


/**
 * Class modeling enduser of VOD system, each user has it's own thread and generates requests for VOD materials.
 */
public class User extends Thread {
    private Date birthday;
    private String email, creditCard;
    private long uid;
    private Abonament abonament;
    private float debt;
    Semaphore debtSemaphore;
    private LinkedList<Product> avaliableProducts;
    private ReadWriteLock productsLock;

    User(LinkedList<Product> avaliableProducts, ReadWriteLock productsLock) {
        this.productsLock = productsLock;
        debtSemaphore = new Semaphore(1);
        this.avaliableProducts = avaliableProducts;
        debt = 0;
        birthday = RandGen.randDate();
        email = String.format("%s@gmail.com", RandGen.randName().replaceAll("\\s", "."));
        creditCard = String.format("%d %d %d %d %d", RandGen.randInt(1000, 9999), RandGen.randInt(1000, 9999), RandGen.randInt(1000, 9999), RandGen.randInt(1000, 9999), RandGen.randInt(1000, 9999));
        uid = RandGen.randLong(0, Long.MAX_VALUE);
        if (RandGen.randBool()) {
            int abo = RandGen.randInt(0, 2);
            abonament = new Abonament(abo);
        }
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
        //TODO todo
    }
}
