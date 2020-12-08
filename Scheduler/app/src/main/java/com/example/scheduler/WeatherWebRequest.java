/*
 * @author: Jacob Merki
 * @description: This file defines the WeatherWebRequest which runs an asynchronous task that
 * requests weather data from a public API and returns it to other parts of the application
 */
package com.example.scheduler;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/*
 * The WeatherWebRequest class defines the AsyncTask that requests weather information from the
 * WeatherAPI based on the zip code given in the query string. Once the data has been retrieved the
 * class calls a function in MainActivity to show the weather dialog.
 */
public class WeatherWebRequest extends AsyncTask<Object, Void, JSONObject> {

    //Keep track of main activity instance to show weather dialog
    @SuppressLint("StaticFieldLeak")
    MainActivity mainActivity;
    //Use this URL for the web request
    String baseURL =
            "http://api.weatherapi.com/v1/current.json?key=e810857c37d24c879ea61929200312&q=85719";

    /*
     * This constructor creates a new WeatherWebRequest and set the main activity reference.
     *
     * This method takes MainActivity and returns a WeatherWebRequest.
     */
    public WeatherWebRequest(MainActivity main){
        this.mainActivity = main;
    }

    /*
     * This method constructs the URL for the web request, opens the reader to get json response
     * data and returns the string read in as a JSONObject.
     *
     * This method takes an array of Objects and returns a JSONObject.
     */
    @Override
    protected JSONObject doInBackground(Object[] objects){

        try {
            //Construct URL
            String weatherURL = baseURL;
            URL url = new URL(weatherURL);

            //OPen reader and get the data
            BufferedReader weatherData = new BufferedReader(new InputStreamReader(url.openStream()));
            String json = weatherData.readLine();
            weatherData.close();

            JSONObject weather = new JSONObject(json);

            return weather;
        } catch (Exception e) { e.printStackTrace(); }

        return null;
    }

    /*
     * This method executes after the web request finishes and calls the function in MainActivity
     * to show the dialog of weather information.
     */
    @Override
    protected void onPostExecute(JSONObject weather) {
        mainActivity.showWeatherDialog(weather);
    }
}
