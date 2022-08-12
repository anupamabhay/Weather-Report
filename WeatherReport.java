import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

public class WeatherReport {
    public static void main(String[] args) throws IOException, NullPointerException {
        //api
        var myApi = "5c861235f08072c0250247c8230180d0";
        //base url
//        final var urlGeolocation = "http://api.openweathermap.org/geo/1.0/direct?q={city name},{country code}&limit={limit}&appid={API key}";
//        final var urlWeatherForecast = "https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key}";
        final var url = "http:api.openweathermap.org/";
        //Build new client for api calls
        OkHttpClient client = new OkHttpClient().newBuilder().build();

        //Code to get the latitude and longitude
        Scanner scanner = new Scanner(System.in);
        String city, country;
        System.out.println("Enter the name of the city: ");
        city = scanner.nextLine();
        city = city.replaceAll("\\s+","+");
        System.out.println("Enter the name of the country: ");
        country = scanner.next();
        //Request for latitude & longitude
        Request requestGeolocation = new Request.Builder()
                .url(url+"geo/1.0/direct?q="+city+","+country+"&limit=1&appid="+myApi)
                .build();
        Response responseGeolocation = null;
        String messageGeolocation = "";
        try{
            responseGeolocation = client.newCall(requestGeolocation).execute();
            messageGeolocation = Objects.requireNonNull(responseGeolocation.body()).string();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        //Parse response message as JsonArray
        JsonArray jArrayGeolocation = JsonParser.parseString(messageGeolocation).getAsJsonArray();
        //Get the JsonArray as a JsonObject to access key-value entries
        JsonObject jObjectGeolocation = (JsonObject) jArrayGeolocation.get(0);
        String location = jObjectGeolocation.get("name").toString();
        String latitude = jObjectGeolocation.get("lat").toString();
        String longitude = jObjectGeolocation.get("lon").toString();
        String countryCode = jObjectGeolocation.get("country").toString();
        System.out.println("Location: " + location + "," + countryCode + "\n" + "Latitude: " + latitude + ", " + "Longitutde: " + longitude);

        //Request and Response for the weather stats
        Request requestWeather = new Request.Builder()
                .url(url+"data/2.5/weather?lat="+latitude+"&lon="+longitude+"&appid="+myApi)
                .build();
        Response responseWeather = null;
        String messageWeather = "";
        try{
            responseWeather = client.newCall(requestWeather).execute();
            messageWeather = Objects.requireNonNull(responseWeather.body()).string();
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        System.out.println(messageWeather);
    }
}
