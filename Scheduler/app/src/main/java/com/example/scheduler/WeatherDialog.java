/*
 * @author: Jacob Merki
 * @description: This file defines the WeatherDialog which shows the retrieved weather results from
 * the AsyncTask that makes a web request to the API in a clear and concise dialog.
 */
package com.example.scheduler;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import org.json.JSONObject;

import java.util.HashMap;

/*
 * This class defines the WeatherDialog class that displays weather information in a JSONObject
 * and determines the appropriate icon for the weather based on the words in the conditions field
 * of the object.
 */
public class WeatherDialog extends DialogFragment {

    //Keep track of the inflated view and the json weather data
    View inflatedView;
    JSONObject weatherData;

    /*
     * This constructor creates a new WeatherDialog and stores the json weather data.
     *
     * This method takes a JSONObject and returns a WeatherDialog.
     */
    public WeatherDialog(JSONObject data){
        this.weatherData = data;
    }

    /*
     * This method creates the dialog based on the desired layout and extracting the json weather
     * data to be displayed in TextViews. This method also determines the most appropriate icon
     * for the weather given the text in the conditions field of the json data.
     *
     * This method takes a Bundle and returns a Dialog.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder weatherBuilder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        final LayoutInflater inflater = requireActivity().getLayoutInflater();
        inflatedView = inflater.inflate(R.layout.dialog_weather_report, null);

        //Get all of necessary info
        String region = "", conditions = "";
        double temp = -1.0;
        int humid = -1;
        int iconId = R.drawable.moon;

        //Extract all needed JSON data
        try {
            region = weatherData.getJSONObject("location").getString("name") + ", " +
                    weatherData.getJSONObject("location").getString("region");
            temp = weatherData.getJSONObject("current").getDouble("temp_f");
            humid = weatherData.getJSONObject("current").getInt("humidity");
            conditions = weatherData.getJSONObject("current").getJSONObject("condition")
                    .getString("text");
        } catch (Exception e){ e.printStackTrace(); }

        //Determine best icon for conditions
        if(conditions.contains("Sunny") || conditions.contains("Clear")) iconId = R.drawable.sun;
        if(conditions.contains("cloud") || conditions.contains("Cloudy")) iconId = R.drawable.cloud;
        if(conditions.contains("wind")) iconId = R.drawable.wind;
        if(conditions.contains("rain")) iconId = R.drawable.rain;
        if(conditions.contains("thunder") || conditions.contains("Thunder")) iconId = R.drawable.storm;

        //Get all Views that need information set and set the desired info
        TextView location = (TextView) inflatedView.findViewById(R.id.weather_location);
        TextView conditionText = (TextView) inflatedView.findViewById(R.id.weather_condition);
        TextView tempText = (TextView) inflatedView.findViewById(R.id.weather_temp);
        TextView humidity = (TextView) inflatedView.findViewById(R.id.weather_humidity);
        ImageView weatherIcon = (ImageView) inflatedView.findViewById(R.id.weather_icon);

        location.setText(region);
        conditionText.setText(conditions);
        tempText.setText("Temperature: " + temp + " " + (char) 0x00b0 + "F");
        humidity.setText("Humidity: " + humid + "%");
        weatherIcon.setImageResource(iconId);

        //Set the dialog view from the layout and implement button listeners
        weatherBuilder.setView(inflatedView)
                // Add action buttons
                // Only need one button that cancels the dialog
                .setPositiveButton("Gotcha!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        WeatherDialog.this.getDialog().cancel();
                    }
                });


        return weatherBuilder.create();
    }
}
