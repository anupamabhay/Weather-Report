import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

public class WeatherForecast {
    public static void main(String[] args) throws IOException {
        //api
        var myApi = "5c861235f08072c0250247c8230180d0";
        //base url
//        final var urlGeolocation = "http://api.openweathermap.org/geo/1.0/direct?q={city name},{country code}&limit={limit}&appid={API key}";
//        final var urlWeatherForecast = "https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key}";
        final var url = "http:api.openweathermap.org/";
        Scanner scanner = new Scanner(System.in);
        String city, country;
        System.out.println("Enter the name of the city: ");
        city = scanner.next();
        System.out.println("Enter the name of the country: ");
        country = scanner.next();

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request requestGeolocation = new Request.Builder()
                .url(url+"geo/1.0/direct?q="+city+","+country+"&limit=1&appid="+myApi)
                .build();
        Response responseGeolocation = client.newCall(requestGeolocation)
                .execute();

        String messageGeolocation = Objects.requireNonNull(responseGeolocation.body()).string();

        JsonArray jArrayGeolocation = JsonParser.parseString(messageGeolocation).getAsJsonArray();
        System.out.println(jArrayGeolocation);

        JsonObject jObjectGeolocation = (JsonObject) jArrayGeolocation.get(0);

        String latitude = jObjectGeolocation.get("lat").toString();
        String longitude = jObjectGeolocation.get("lon").toString();
        String countryCode = jObjectGeolocation.get("country").toString();

        System.out.println("Latitude = "+latitude);
        System.out.println("Longitude = "+longitude);
        System.out.println("Country = "+countryCode);


    }
}
