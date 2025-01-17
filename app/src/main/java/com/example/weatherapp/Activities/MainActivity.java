package com.example.weatherapp.Activities;

import java.util.List;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.weatherapp.API.WeatherApi;
import com.example.weatherapp.API.WeatherResponse;
import com.example.weatherapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String API_KEY = "4dde056f1ed6b2e7c171af2621f643a7";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/";

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;

    private TextView temperatureTextView;
    private TextView dateTextView;
    private TextView locationTextView;
    private TextView maxMinTempTextView;  // TextView for max and min temperatures
    private ImageView weatherImageView;  // ImageView for weather icon

    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find your TextViews and ImageView
        temperatureTextView = findViewById(R.id.textView4);
        dateTextView = findViewById(R.id.textView2);
        locationTextView = findViewById(R.id.textView);
        maxMinTempTextView = findViewById(R.id.textView5);  // Initialize max/min temperature TextView
        weatherImageView = findViewById(R.id.weatherImageView);  // Initialize weather ImageView

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Check for location permission and fetch location if granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getLastLocation();
        }

        // Set the current date
        setCurrentDate();

        // Fetch weather data for a default location (e.g., "Bialystok")
        fetchWeatherData("Bialystok");
    }

    private void setCurrentDate() {
        // Get the current date
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd", Locale.ENGLISH); // "EEE" = day of the week, "MMM" = month, "dd" = day of the month
        String currentDate = sdf.format(new Date());

        // Set the formatted date to the TextView
        dateTextView.setText(currentDate);
    }

    private void fetchWeatherData(String city) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherApi weatherApi = retrofit.create(WeatherApi.class);

        Call<WeatherResponse> call = weatherApi.getWeather(city, API_KEY, "metric");

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    double temp = response.body().getMain().getTemp();
                    double tempMax = response.body().getMain().getTempMax();  // Get max temperature
                    double tempMin = response.body().getMain().getTempMin();  // Get min temperature
                    temperatureTextView.setText(Math.round(temp) + "ºC");

                    // Set max and min temperature dynamically
                    String maxMinTemp = "Max:" + Math.round(tempMax) + "ºC" + " | " + "Low:" + Math.round(tempMin) + "ºC";
                    maxMinTempTextView.setText(maxMinTemp);

                    String weatherCondition = response.body().getWeather().get(0).getMain();  // Get weather condition ("Clear", "Rain", etc.)
                    updateWeatherIcon(weatherCondition);  // Update the icon based on condition

                    // Set the location
                    locationTextView.setText(city);
                } else {
                    Log.e("MainActivity", "Response unsuccessful");
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Log.e("MainActivity", "Failed to fetch weather data", t);
            }
        });
    }

    // Update the weather icon based on the condition
    private void updateWeatherIcon(String condition) {
        switch (condition) {
            case "Clear":
                weatherImageView.setImageResource(R.drawable.sun);  // Replace with your actual image
                break;
            case "Rain":
                weatherImageView.setImageResource(R.drawable.rainy);  // Replace with your actual image
                break;
            case "Clouds":
                weatherImageView.setImageResource(R.drawable.cloudy_3);  // Replace with your actual image
                break;
            case "Snow":
                weatherImageView.setImageResource(R.drawable.snowy);  // Replace with your actual image
                break;
            case "Thunderstorm":
                weatherImageView.setImageResource(R.drawable.storm);  // Replace with your actual image
                break;
        }
    }

    // Fetch the last known location
    private void getLastLocation() {
        fusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            Location location = task.getResult();
                            updateLocationUI(location);
                        } else {
                            Log.e("MainActivity", "Failed to get location");
                        }
                    }
                });
    }

    // Update the location TextView with the city name
    private void updateLocationUI(Location location) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<android.location.Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses != null && !addresses.isEmpty()) {
                String city = addresses.get(0).getLocality();
                locationTextView.setText(city); // Set the city to the TextView
                fetchWeatherData(city); // Fetch weather data for the current city
            }
        } catch (Exception e) {
            Log.e("MainActivity", "Error getting location address", e);
        }
    }
}
