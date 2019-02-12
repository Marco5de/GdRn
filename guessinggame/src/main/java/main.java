import com.google.gson.*;
import sun.net.www.protocol.http.HttpURLConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;


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
        System.out.println(json);

        JsonParser parser = new JsonParser();
        JsonElement jsonTree = parser.parse(json);

        System.out.println(jsonTree.toString());
        JsonObject obj=null;
        if(jsonTree.isJsonObject()){
            System.out.println("\nGetting as Json Object");
            obj = jsonTree.getAsJsonObject();
        }else if(jsonTree.isJsonArray()){
            System.out.println("\nGetting as Json Array");
            JsonArray jsonArr= jsonTree.getAsJsonArray();
        }else if(jsonTree.isJsonPrimitive()){
            System.out.println("\nGetting as Json Primitive");
            JsonPrimitive jsonObject = jsonTree.getAsJsonPrimitive();
        }else if(jsonTree.isJsonNull()){
            throw new NullPointerException("Json NULL");
        }



        JsonElement ele = obj.get("people");
        JsonObject obj2 = null;

        System.out.println(ele.toString());

        if(ele.isJsonArray())
            System.out.println("Is json array");

        JsonArray jsonArray = ele.getAsJsonArray();
        System.out.println("\nPrint single entries\n");
        for(JsonElement x : jsonArray)
            System.out.println(x.toString());

        bufferedReader.close();
    }

}
