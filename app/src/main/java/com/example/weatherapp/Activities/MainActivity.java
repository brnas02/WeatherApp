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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;  // Import SearchView
import androidx.core.app.ActivityCompat;
import androidx.annotation.NonNull;

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

    // Declare the UI components
    private TextView temperatureTextView;
    private TextView dateTextView;
    private TextView locationTextView;
    private TextView maxMinTempTextView;
    private TextView weatherConditionTextView;
    private TextView windSpeedTextView;
    private TextView rainTextView;
    private TextView humidityTextView;  // New TextView for humidity
    private ImageView weatherImageView;
    private SearchView searchView;  // Declare the SearchView

    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the UI components
        temperatureTextView = findViewById(R.id.textView4);
        dateTextView = findViewById(R.id.textView2);
        locationTextView = findViewById(R.id.textView);
        maxMinTempTextView = findViewById(R.id.textView5);
        weatherConditionTextView = findViewById(R.id.textView3);
        windSpeedTextView = findViewById(R.id.textView7);
        rainTextView = findViewById(R.id.textViewRain);
        humidityTextView = findViewById(R.id.textViewHumidity);
        weatherImageView = findViewById(R.id.weatherImageView);
        searchView = findViewById(R.id.searchView);  // Initialize the SearchView

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Check location permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getLastLocation();
        }

        // Set current date
        setCurrentDate();

        // Fetch weather data for a default location when the app starts
        fetchWeatherData("Bialystok");

        // Set the query listener for the search view
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // When the user presses "search" after entering a city name
                fetchWeatherData(query);  // Fetch weather data for the city
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // This can be used to filter results while typing (optional)
                return false;
            }
        });
    }

    private void setCurrentDate() {
        // Format current date to display
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd", Locale.ENGLISH);
        String currentDate = sdf.format(new Date());
        dateTextView.setText(currentDate);
    }

    private void fetchWeatherData(String city) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherApi weatherApi = retrofit.create(WeatherApi.class);

        // Fetch weather data from the API
        Call<WeatherResponse> call = weatherApi.getWeather(city, API_KEY, "metric");

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    double temp = response.body().getMain().getTemp();
                    double tempMax = response.body().getMain().getTempMax();
                    double tempMin = response.body().getMain().getTempMin();
                    String weatherCondition = response.body().getWeather().get(0).getDescription();
                    double windSpeed = response.body().getWind().getSpeed();
                    double rainVolume = response.body().getRain() != null ? response.body().getRain().getOneHourRain() : 0;
                    int humidity = response.body().getMain().getHumidity();

                    // Convert wind speed to km/h
                    double windSpeedKmH = windSpeed * 3.6;

                    // Update UI elements with fetched weather data
                    temperatureTextView.setText(Math.round(temp) + "ºC");
                    maxMinTempTextView.setText("Max: " + Math.round(tempMax) + "ºC | Low: " + Math.round(tempMin) + "ºC");
                    weatherConditionTextView.setText(weatherCondition);
                    locationTextView.setText(city);
                    windSpeedTextView.setText(Math.round(windSpeedKmH) + " km/h");

                    // Display rain data
                    if (rainVolume > 0) {
                        rainTextView.setText(Math.round(rainVolume) + " mm");
                    } else {
                        rainTextView.setText("No rain");
                    }

                    // Display humidity
                    humidityTextView.setText(humidity + "%");

                    // Update weather icon based on the weather condition
                    updateWeatherIcon(response.body().getWeather().get(0).getMain());
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

    private void updateWeatherIcon(String condition) {
        // Set weather icon based on the condition
        if (condition.contains("Clear")) {
            weatherImageView.setImageResource(R.drawable.sun);  // Sun icon for clear weather
        } else if (condition.contains("Rain")) {
            weatherImageView.setImageResource(R.drawable.rainy);  // Rain icon
        } else if (condition.contains("Clouds")) {
            weatherImageView.setImageResource(R.drawable.cloudy_3);  // Cloudy icon
        } else if (condition.contains("Snow")) {
            weatherImageView.setImageResource(R.drawable.snowy);  // Snow icon
        } else if (condition.contains("Thunderstorm")) {
            weatherImageView.setImageResource(R.drawable.storm);  // Thunderstorm icon
        }

        Log.d("MainActivity", "Weather condition: " + condition);
    }

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

    private void updateLocationUI(Location location) {
        // Get the address of the location using Geocoder
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<android.location.Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses != null && !addresses.isEmpty()) {
                String city = addresses.get(0).getLocality();
                locationTextView.setText(city);
                fetchWeatherData(city);  // Fetch weather data based on the current city
            }
        } catch (Exception e) {
            Log.e("MainActivity", "Error getting location address", e);
        }
    }
}
