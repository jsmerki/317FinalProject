package com.example.scheduler;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class WeatherWebRequest extends AsyncTask<Object, Void, JSONObject> {

    String baseURL = "http://api.weatherapi.com/v1/current.json?key=e810857c37d24c879ea61929200312&q=85719";
    @Override
    protected JSONObject doInBackground(Object[] objects){

        try {

            //FIXME: get permission to get user zip code!!!
            String weatherURL = baseURL;//+ q=zipcode
            URL url = new URL(weatherURL);

            BufferedReader weatherData = new BufferedReader(new InputStreamReader(url.openStream()));
            String json = weatherData.readLine();
            weatherData.close();

            System.out.println("WEATHER: " + json);

            JSONObject weather = new JSONObject(json);

            return weather;
        } catch (Exception e) { e.printStackTrace(); }

        return null;
    }

}
