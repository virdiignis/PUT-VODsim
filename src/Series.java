import com.google.gson.JsonObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

public class Series extends Product {
    private ArrayList<Season> seasons;
    private HashSet<String> actors;
    private HashSet<Genre> genres;

    public Series(Provider provider, float price) throws IOException {
        super(provider, price);
        id = RandGen.randSeriesId();
        SeriesFromTMDB();
    }

    public ArrayList<Season> getSeasons() {
        return seasons;
    }

    public HashSet<String> getActors() {
        return actors;
    }

    public HashSet<Genre> getGenres() {
        return genres;
    }

    private void SeriesFromTMDB() throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        JsonObject jsonObject = NetIO.getJsonFromURL(String.format("https://api.themoviedb.org/3/tv/%d?api_key=%s", id, Constants.API_KEY));

        name = jsonObject.get("name").getAsString();
        desc = jsonObject.get("overview").getAsString();
        grade = jsonObject.get("vote_average").getAsFloat();
        int runtime = 0;
        try {
            runtime = jsonObject.get("episode_run_time").getAsJsonArray().get(0).getAsInt();
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Episode runtime not present.");
        }

        genres = new HashSet<>();
        for (var gen : jsonObject.get("genres").getAsJsonArray())
            genres.add(Genre.valueOf(gen.getAsJsonObject().get("name").getAsString().replaceAll("[\\s-&]+", "")));

        try {
            productionDate = format.parse(jsonObject.get("first_air_date").getAsString());
        } catch (ParseException | UnsupportedOperationException e) {
            System.err.println("Cannot parse date.");
        }

        try {
            seasons = new ArrayList<>();
            for (var season : jsonObject.get("seasons").getAsJsonArray()) {
                var jseason = season.getAsJsonObject();
                seasons.add(new Season(jseason.get("name").getAsString(), jseason.get("season_number").getAsInt()));
            }
        } catch (NullPointerException e) {
            System.err.println("Cannot download seasons data. Not necessarily an error.");
        }

        try {
            image = NetIO.imageFromURL(String.format("https://image.tmdb.org/t/p/w600_and_h900_bestv2/%s", jsonObject.get("poster_path").getAsString()));
        } catch (IOException | UnsupportedOperationException e) {
            System.err.println("Couldn't retrieve poster image");
        }

        for (var country : jsonObject.get("origin_country").getAsJsonArray())
            productionPlaces.add(country.getAsString());

        try {
            jsonObject = NetIO.getJsonFromURL(String.format("https://api.themoviedb.org/3/tv/%d/credits?api_key=%s", id, Constants.API_KEY));
            actors = new HashSet<>();
            for (var person : jsonObject.get("cast").getAsJsonArray())
                actors.add(person.getAsJsonObject().get("name").getAsString());

        } catch (IOException e) {
            System.err.println("Couldn't retrieve credits data. Not necessarily an error, because it could be unavailable.");
        }

        for (var season : seasons) {
            try {
                jsonObject = NetIO.getJsonFromURL(String.format("https://api.themoviedb.org/3/tv/%d/season/%d?api_key=%s", id, season.number, Constants.API_KEY));
                for (var episode : jsonObject.get("episodes").getAsJsonArray()) {
                    var jepisode = episode.getAsJsonObject();
                    Date d = format.parse(jepisode.get("air_date").getAsString());
                    season.add(new Episode(d, runtime));
                }
            } catch (IOException e) {
                System.err.println("Couldn't retrieve season data. Not necessarily an error, because it could be unavailable.");
            } catch (ParseException e) {
                System.err.println("Invalid date format in episode date.");
            } catch (UnsupportedOperationException e) {
                System.err.println("Air date not present in episode json.");
            }
        }
    }

    public enum Genre {
        Action,
        Adventure,
        ActionAdventure,
        Animation,
        Comedy,
        Crime,
        Documentary,
        Drama,
        Fantasy,
        Family,
        Kids,
        Mystery,
        News,
        Reality,
        Romance,
        SciFiFantasy,
        ScienceFiction,
        Soap,
        Talk,
        Thriller,
        WarPolitics,
        Western
    }

    class Episode {
        final Date premiere;
        final int duration;

        Episode(Date premiere, int duration) {
            this.premiere = premiere;
            this.duration = duration;
        }

        @Override
        public String toString() {
            return duration + "mins, " + new SimpleDateFormat("dd.MM.yyyy").format(premiere);
        }
    }

    class Season extends ArrayList<Episode> {
        final String name;
        final int number;

        Season(String name, int number) {
            this.name = name;
            this.number = number;
        }
    }
}
