import java.util.Date;

public class Promotion {
    Date start, end;
    int discount; // 5% to 50%

    public float calculatePrice(Product p){
        return p.price - (((float) discount / (float) 100) * p.price);
    }
}
