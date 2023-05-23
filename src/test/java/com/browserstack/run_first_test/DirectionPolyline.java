package com.browserstack.run_first_test;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class DirectionPolyline {

    public static  List<LatLng> GetDirections (String origin,String destination){

        String apiKey = "AIzaSyB1j-XO-y-RUnGobIxZZJuxx5qDAw8Lt5w";
        // URL with Parameter to pass
        String url = "https://maps.googleapis.com/maps/api/directions/json?origin="
                + origin
                + "&destination="
                + destination
                + "&key="
                + apiKey;

        try {
            //HTTP Request
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
                    .url(url)
                    .method("GET",null)
                    .build();
            Response response = client.newCall(request).execute();




//            URL requestUrl = new URL(URLEncoder.encode(url, StandardCharsets.UTF_8.toString()));
//
//            HttpURLConnection conn = (HttpURLConnection) requestUrl.openConnection();
//            conn.setRequestMethod("GET");
//            InputStream inputStream = conn.getInputStream();
//            Scanner scanner = new Scanner(inputStream);
//            StringBuffer response = new StringBuffer();
//            while (scanner.hasNext()) {
//                response.append(scanner.nextLine());
//            }
//            scanner.close();
            // Get JSON from response
            JSONObject json = new JSONObject(response.body().string().toString());
            // Get list of available routes from JSON
            JSONArray routes = json.getJSONArray("routes");
            //Get First route from list
            JSONObject route = routes.getJSONObject(0);
            //Get polyline for that specific route
            JSONObject overviewPolyline = route.getJSONObject("overview_polyline");
            //Get Points from polyline
            String encodedPolyline = overviewPolyline.getString("points");

            System.out.println("Encoded polyline: " + encodedPolyline);
            return decodePolyline(encodedPolyline);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
    }

// Decode polyline string to fetch lat long from it
    public static List<LatLng> decodePolyline(String encodedPolyline) {
        List<LatLng> points = new ArrayList<>();
        int index = 0, len = encodedPolyline.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encodedPolyline.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encodedPolyline.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng point = new LatLng(lat / 1E5, lng / 1E5);
            points.add(point);
        }

        return points;
    }


}