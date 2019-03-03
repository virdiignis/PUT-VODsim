import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

class Product {
    BufferedImage image;
    String name, desc;
    Date productionDate;

    final Set<String> productionPlaces;
    final float price;
    private final Provider provider;

    int runtime, id;
    private final AtomicInteger views;
    float grade;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;

        Product product = (Product) o;

        if (!name.equals(product.name)) return false;
        return Objects.equals(productionDate, product.productionDate);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (productionDate != null ? productionDate.hashCode() : 0);
        return result;
    }

    public int getRuntime() {
        return runtime;
    }

    public String getName() {
        return name;
    }

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

    public BufferedImage getImage() {
        return image;
    }

    public String getDesc() {
        return desc;
    }

    public float getGrade() {
        return grade;
    }

    public Provider getProvider() {
        return provider;
    }
}
