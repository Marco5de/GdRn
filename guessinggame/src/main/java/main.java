import com.google.gson.*;
import sun.net.www.protocol.http.HttpURLConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

//Todo parsing des namens und craft

public class main {
    public static void main(String args[]) throws IOException {

        URL url = new URL("http://api.open-notify.org/astros.json");
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("GET");

        int respCode = connection.getResponseCode();
        System.out.println("ResponseCode: " + respCode);
        if(respCode!=200)
            System.exit(1);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuffer response = new StringBuffer();

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            response.append(line);
        }

        String json = response.toString();

        JsonParser parser = new JsonParser();
        JsonElement ele = parser.parse(json);
        JsonObject obj = ele.getAsJsonObject();
        JsonArray jsonArray = obj.getAsJsonArray("people");

        System.out.println("Currently are " + jsonArray.size() + " people in space!");
        for(int i=0; i<jsonArray.size();i++) {
            //System.out.println("The " +i +"th person in space is " + jsonArray.get(i).toString());
            JsonObject x = jsonArray.get(i).getAsJsonObject();
            System.out.println("Name: " +x.get("name")+ " on craft " + x.get("craft"));
        }
        bufferedReader.close();
    }

}
