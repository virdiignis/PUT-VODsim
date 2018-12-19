import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

class Product {
    BufferedImage image;
    String name, desc;
    Date productionDate;
    int runtime, id;
    Set<String> productionPlaces;
    private Provider provider;
    float price, grade;
    private AtomicInteger views;

    @Override
    public String toString() {
        return name;
    }

    Product(Provider provider, float price) {
        this.provider = provider;
        this.price = price;
        views = new AtomicInteger(0);
        productionPlaces = new HashSet<>();

        grade = RandGen.randFloat(0, 10);
    }

    void view(){
        views.incrementAndGet();
    }

    int getViews(){
        return views.getAndSet(0);
    }

    void resetViews(){
        views.set(0);
    }

    float getBasePrice(){
        return price;
    }

    float getPromotionPrice(){
        return price;
    }
    public void setPromotion(Promotion p){}

}
