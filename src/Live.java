import java.io.IOException;

public class Live extends Product {
    private Promotion promotion;

    Live(Provider provider, float price) {
        super(provider, price);
        name = RandGen.randPhrase();
        productionPlaces.add(RandGen.randWord("/home/prance/IdeaProjects/PUT-VODsim/src/countries.txt"));
        runtime = RandGen.randInt(30, 240);
        id = RandGen.randInt(0, Integer.MAX_VALUE);
        productionDate = RandGen.randDate();
        desc = RandGen.randQuote();
        try {
            image = NetIO.imageFromURL("https://picsum.photos/600/900/?random");
        } catch (IOException e) {
            System.err.println("Couldn't download Live image.");
        }
    }

    @Override
    float getPromotionPrice() {
        if (promotion != null) return price*(1-promotion.calculatePrice(this));
        return price;
    }

    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }
}
