import com.google.gson.JsonObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;

public class Movie extends Product {
    private enum Genre {
        Action,
        Adventure,
        Animation,
        Comedy,
        Crime,
        Documentary,
        Drama,
        Family,
        Fantasy,
        History,
        Horror,
        Music,
        Mystery,
        Romance,
        ScienceFiction,
        TVMovie,
        Thriller,
        War,
        Western
    }

    private HashSet<String> actors;
    private HashSet<String> trailers;


    Movie(Provider provider, float price) throws IOException {
        super(provider, price);
        int rentTime = RandGen.randInt(runtime, 10000);
        id = RandGen.randMovieId();
        MovieFromTMDB();
    }

    public HashSet<String> getActors() {
        return actors;
    }

    public HashSet<String> getTrailers() {
        return trailers;
    }

    private HashSet<Genre> genres;
    private Promotion promotion;

    public HashSet<Genre> getGenres() {
        return genres;
    }

    private void MovieFromTMDB() throws IOException {
        JsonObject jsonObject = null;
            jsonObject = NetIO.getJsonFromURL(String.format("https://api.themoviedb.org/3/movie/%d?api_key=%s", id, Constants.API_KEY));
        name = jsonObject.get("title").getAsString();
        try {
            desc = jsonObject.get("overview").getAsString();
        } catch (UnsupportedOperationException ignored) {
        }
        try {
            grade = jsonObject.get("vote_average").getAsFloat();
        } catch (UnsupportedOperationException ignored) {
        }
        try {
            runtime = jsonObject.get("runtime").getAsInt();
        } catch (UnsupportedOperationException ignored) {
        }

        genres = new HashSet<>();
        for (var gen : jsonObject.get("genres").getAsJsonArray())
            genres.add(Genre.valueOf(gen.getAsJsonObject().get("name").getAsString().replaceAll("\\s", "")));

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            productionDate = format.parse(jsonObject.get("release_date").getAsString());
        } catch (ParseException e) {
            System.err.println("Cannot parse date.");
        }

        try {
            image = NetIO.imageFromURL(String.format("https://image.tmdb.org/t/p/w600_and_h900_bestv2/%s", jsonObject.get("poster_path").getAsString()));
        } catch (IOException | UnsupportedOperationException e) {
            System.err.println("Couldn't retrieve poster image");
        }

        for (var country : jsonObject.get("production_countries").getAsJsonArray())
            productionPlaces.add(country.getAsJsonObject().get("name").getAsString());


        try {
            jsonObject = NetIO.getJsonFromURL(String.format("https://api.themoviedb.org/3/movie/%d/credits?api_key=%s", id, Constants.API_KEY));
            actors = new HashSet<>();
            for (var person : jsonObject.get("cast").getAsJsonArray())
                actors.add(person.getAsJsonObject().get("name").getAsString());

        } catch (IOException e) {
            System.err.println("Couldn't retrieve credits data. Not necessarily an error, because it could be unavailable.");
        }

        try {
            jsonObject = NetIO.getJsonFromURL(String.format("https://api.themoviedb.org/3/movie/%d/videos?api_key=%s", id, Constants.API_KEY));
            trailers = new HashSet<>();
            for (var trailer : jsonObject.get("results").getAsJsonArray()) {
                var tr = trailer.getAsJsonObject();
                if (tr.get("site").getAsString().equals("YouTube"))
                    trailers.add(String.format("https://www.youtube.com/watch?v=%s", tr.get("key").getAsString()));
            }
        } catch (IOException | NullPointerException e) {
            System.err.println("Couldn't retrieve trailers data. Not necessarily an error, because it could be unavailable.");
        }
    }

    @Override
    float getPromotionPrice() {
        if (promotion != null) return price * (1 - promotion.calculatePrice(this));
        return price;
    }

    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }
}
