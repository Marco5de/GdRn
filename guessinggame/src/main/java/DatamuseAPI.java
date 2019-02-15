import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DatamuseAPI {

    public static void main(String args[]) throws IOException {
        URL url = new URL("https://api.datamuse.com/words?rel_rhy=forgetful");
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("GET");

        if(connection.getResponseCode()!=200)
            System.exit(1);
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuffer response = new StringBuffer();

        String line;
        while((line=br.readLine())!=null){
            response.append(line);
        }

        String jsonResp = response.toString();
        System.out.println(jsonResp);
        JsonParser parser = new JsonParser();
        JsonElement root = parser.parse(jsonResp);
        JsonArray jsonArray = root.getAsJsonArray();

        System.out.println("Word that rhyme with ");
        for(JsonElement x : jsonArray){
           JsonObject obj = x.getAsJsonObject();
           System.out.println("Wort" + obj.get("word") + " mit score " + obj.get("score") + " mit " + obj.get("numSyllables") + " Silben");
        }
    }
}
