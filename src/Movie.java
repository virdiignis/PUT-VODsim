import com.google.gson.JsonObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;

public class Movie extends Product {
    public enum Genre {
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

    HashSet<String> actors, trailers;
    HashSet<Genre> genres;
    int rentTime;
    Promotion promotion;

    public Movie(Provider provider, float price) throws IOException {
        super(provider, price);
        rentTime = RandGen.randInt(runtime, 10000);
        id = RandGen.randMovieId();
        MovieFromTMDB();
    }

    private void MovieFromTMDB() {
        JsonObject jsonObject = null;
        try {
            jsonObject = NetIO.getJsonFromURL(String.format("https://api.themoviedb.org/3/movie/%d?api_key=%s", id, Constants.API_KEY));
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert jsonObject != null;
        name = jsonObject.get("title").getAsString();
        desc = jsonObject.get("overview").getAsString();
        grade = jsonObject.get("vote_average").getAsFloat();
        runtime = jsonObject.get("runtime").getAsInt();

        genres = new HashSet<>();
        for(var gen : jsonObject.get("genres").getAsJsonArray())
            genres.add(Genre.valueOf(gen.getAsJsonObject().get("name").getAsString().replaceAll("\\s", "")));

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            productionDate = format.parse(jsonObject.get("release_date").getAsString());
        } catch (ParseException e) {
            System.err.println("Cannot parse date.");
            e.printStackTrace();
        }

        try {
            image = NetIO.imageFromURL(String.format("https://image.tmdb.org/t/p/w600_and_h900_bestv2/%s", jsonObject.get("poster_path").getAsString()));
        } catch (IOException e) {
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


}
