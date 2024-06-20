import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Scanner;

public class WeatherReport {
    public static void main(String[] args) throws IOException, NullPointerException {
        //api
        var myApi = "YOUR_API_KEY";
        //base url
        final var url = "https:api.openweathermap.org/";
        //create an instance of OkHttpsClient for all the API calls
        OkHttpClient client = new OkHttpClient().newBuilder().build();

        //Code to get the latitude and longitude
        Scanner scanner = new Scanner(System.in);
        String city, country;
        System.out.println("Enter the name of the city: ");
        city = scanner.nextLine();
        //replace all whitespaces with '+'
        city = city.replaceAll("\\s+","+"); 
        System.out.println("Enter the name of the country: ");
        country = scanner.next();
        //Request for latitude & longitude
        Request requestGeolocation = new Request.Builder()
                .url(url+"geo/1.0/direct?q="+city+","+country+"&limit=1&appid="+myApi)
                .build();
        //response object (initialized with null)
        Response responseGeolocation = null; 
        String messageGeolocation = "";
        try{
            //making the API call to get latitude and longitude and storing it into the response object
            responseGeolocation = client.newCall(requestGeolocation).execute(); 
            //storing the response message as a string
            messageGeolocation = Objects.requireNonNull(responseGeolocation.body()).string();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        //Parsing response message as JsonArray
        JsonArray jArrayGeolocation = JsonParser.parseString(messageGeolocation).getAsJsonArray();
        //Storing the first value of jArrayGeolocation as an object
        JsonObject jObjectGeolocation = (JsonObject) jArrayGeolocation.get(0);
        //Storing the values of latitude, longitude & country code as String
        String location = jObjectGeolocation.get("name").toString();
        String latitude = jObjectGeolocation.get("lat").toString();
        String longitude = jObjectGeolocation.get("lon").toString();
        String countryCode = jObjectGeolocation.get("country").toString();
        System.out.println();
        //Print underlined "Location"
        System.out.print((char)27 +"[4mLocation");
        System.out.println((char)27 +"[0m"); //Reset underline property
        System.out.println("City: " + location + "," + countryCode + "\n" + "Latitude: " + latitude + ", " + "Longitude: " + longitude + "\n");

        //Request and Response for the weather stats
        Request requestWeather = new Request.Builder()
                .url(url+"data/2.5/weather?lat="+latitude+"&lon="+longitude+"&units=metric&appid="+myApi)
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
        //System.out.println(messageWeather); //prints the response message (object)
        //Parsing response message as JsonObject
        JsonObject jObjectWeather = JsonParser.parseString(messageWeather).getAsJsonObject();
        //Accessing the key:"weather" from jObjectWeather as JsonArray
        JsonArray jArraySky = jObjectWeather.get("weather").getAsJsonArray();
        //Storing the first value of jArraySky as an object
        JsonObject jsonObjectSky = (JsonObject) jArraySky.get(0);
        //Getting the value from key:"description" as a String
        String sky = jsonObjectSky.get("description").toString();

        //Getting the key:"main" to access the object with keys:"temp","feel_like", etc
        JsonObject jObjectTempStats = jObjectWeather.get("main").getAsJsonObject();
        String temperature = jObjectTempStats.get("temp").toString();
        String feelsLike = jObjectTempStats.get("feels_like").toString();
        String temperatureMin = jObjectTempStats.get("temp_min").toString();
        String temperatureMax = jObjectTempStats.get("temp_max").toString();
        String humidity = jObjectTempStats.get("humidity").toString();

        //Grabbing the key:"wind" to access the object with keys:"speed" & "gust"
        JsonObject jObjectWindStats = jObjectWeather.get("wind").getAsJsonObject();
        String windSpeed = jObjectWindStats.get("speed").toString();
        String windGust = jObjectWindStats.get("gust").toString();

        //Printing underlined "Weather Report"
        System.out.print((char)27 +"[4mWeather Report");
        System.out.println((char)27 +"[0m"); //reset underline property
        PrintStream out = new PrintStream( System.out, true, StandardCharsets.UTF_8 );
        out.println("Sky: " + sky
                + "\nTemp: " + temperature + "\u00B0" + "C" + " (Min: " + temperatureMin + "\u00B0" + "C" + ", Max: " + temperatureMax + "\u00B0" + "C" + ")"
                + "\nFeels like: " + feelsLike + "\u00B0" + "C"
                + "\nHumidity: " + humidity + "%"
                + "\nWind Speed: " + windSpeed + "m/s" + ", Gust: " + windGust + "m/s");
    }
    }
}
