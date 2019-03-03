import java.util.Date;

class Promotion {
    private final Date start;
    private final Date end;
    private final int discount; // 5% to 50%

    public Promotion(Date start, Date end, int discount) {
        this.start = start;
        this.end = end;
        this.discount = discount;
    }

    public Promotion() {
        start = new Date();
        end = new Date(start.getTime() + RandGen.randLong(10, 300));
        discount = RandGen.randInt(5, 50);
    }

    public Promotion(long start) {
        this.start = new Date(start);
        end = new Date(start + RandGen.randLong(10, 300));
        discount = RandGen.randInt(5, 50);
    }

    public Date getEnd() {
        return end;
    }

    float calculatePrice(Product p){
        Date now = new Date();
        if (start.before(now) && end.after(now)) return p.getBasePrice() * (1 - (float) discount / (float) 100);
        return p.getBasePrice();
    }
}
