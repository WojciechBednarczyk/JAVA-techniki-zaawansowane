package Api;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class HTTPUrlConnector {

    private static JSONObject jobj2;
    private static JSONArray jsonarr_1;

    static String urlsForAnswers[] = {
            "https://api.teleport.org/api/continents/"
    };

    static String urlsForQuestions[] = {
            "https://api.teleport.org/api/continents/",
            "https://api.teleport.org/api/countries/",
            "https://api.teleport.org/api/countries/",
    };

    public static String sendGetForAnswer(int questionID, String question) throws IOException, ParseException {

        URL url = null;
        String result = null;
        switch (questionID) {
            case 0:
                switch (question) {
                    case "Africa":
                        url = new URL(urlsForAnswers[questionID] + "geonames:AF/countries/");
                        break;
                    case "Antarctica":
                        url = new URL(urlsForAnswers[questionID] + "geonames:AN/countries/");
                        break;
                    case "Asia":
                        url = new URL(urlsForAnswers[questionID] + "geonames:AS/countries/");
                        break;
                    case "Europe":
                        url = new URL(urlsForAnswers[questionID] + "geonames:EU/countries/");
                        break;
                    case "North America":
                        url = new URL(urlsForAnswers[questionID] + "geonames:NA/countries/");
                        break;
                    case "Oceania":
                        url = new URL(urlsForAnswers[questionID] + "geonames:OC/countries/");
                        break;
                    case "South America":
                        url = new URL(urlsForAnswers[questionID] + "geonames:SA/countries/");
                        break;
                }
                break;
            case 1:
                JSONObject jsonObject;
                for (int i = 0; i < jsonarr_1.size(); i++) {
                    jsonObject = (JSONObject) jsonarr_1.get(i);
                    if (jsonObject.get("name").equals(question)) {
                        url = new URL(jsonObject.get("href").toString());
                        break;
                    }
                }
                break;
            case 2:
                for (int i = 0; i < jsonarr_1.size(); i++) {
                    jsonObject = (JSONObject) jsonarr_1.get(i);
                    if (jsonObject.get("name").equals(question)) {
                        url = new URL(jsonObject.get("href").toString() + "admin1_divisions/");
                        break;
                    }
                }
                break;


        }
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        int responseCode = connection.getResponseCode();
        if (responseCode != 200) throw new RuntimeException("HttpResponseCode: " + responseCode);
        else {
            Scanner sc = new Scanner(url.openStream());
            String inline = "";
            while (sc.hasNext()) {
                inline += sc.nextLine();
            }
            sc.close();

            JSONParser parse = new JSONParser();
            JSONObject jobj = (JSONObject) parse.parse(inline);
            JSONObject jobj3;

            switch (questionID) {
                case 0:
                    result = jobj.get("count").toString();
                    break;
                case 1:
                    jobj2 = (JSONObject) jobj.get("_links");
                    jobj3 = (JSONObject) jobj2.get("country:continent");
                    result = jobj3.get("name").toString();
                    break;
                case 2:
                    result = jobj.get("count").toString();
                    break;
            }
        }

        return result;
    }

    public static ArrayList<String> sendGetForQuestions(int questionID) throws IOException, ParseException {

        ArrayList<String> result = new ArrayList<>();
        URL url = new URL(urlsForQuestions[questionID]);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        int responseCode = connection.getResponseCode();
        if (responseCode != 200) throw new RuntimeException("HttpResponseCode: " + responseCode);
        else {
            Scanner sc = new Scanner(url.openStream());
            String inline = "";
            while (sc.hasNext()) {
                inline += sc.nextLine();
            }
            sc.close();

            JSONParser parse = new JSONParser();
            JSONObject jobj = (JSONObject) parse.parse(inline);


            switch (questionID) {
                case 0:
                    jobj2 = (JSONObject) jobj.get("_links");
                    jsonarr_1 = (JSONArray) jobj2.get("continent:items");

                    for (int i = 0; i < jsonarr_1.size(); i++) {
                        JSONObject jsonobj_1 = (JSONObject) jsonarr_1.get(i);
                        result.add(jsonobj_1.get("name").toString());
                    }
                    break;
                case 1:
                    jobj2 = (JSONObject) jobj.get("_links");
                    jsonarr_1 = (JSONArray) jobj2.get("country:items");

                    for (int i = 0; i < jsonarr_1.size(); i++) {
                        JSONObject jsonobj_1 = (JSONObject) jsonarr_1.get(i);
                        result.add(jsonobj_1.get("name").toString());
                    }
                    break;
                case 2:
                    jobj2 = (JSONObject) jobj.get("_links");
                    jsonarr_1 = (JSONArray) jobj2.get("country:items");

                    for (int i = 0; i < jsonarr_1.size(); i++) {
                        JSONObject jsonobj_1 = (JSONObject) jsonarr_1.get(i);
                        result.add(jsonobj_1.get("name").toString());
                    }
                    break;
            }
        }
        return result;
    }
}
