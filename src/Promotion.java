import java.util.Date;

public class Promotion {
    Date start, end;
    int discount;

    public float calculatePrice(Product p){
        return p.price - (((float) discount / (float) 100) * p.price);
    }
}
