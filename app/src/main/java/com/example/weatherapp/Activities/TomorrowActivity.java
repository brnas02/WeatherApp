package com.example.weatherapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.API.WeatherApi;
import com.example.weatherapp.API.WeatherResponse;
import com.example.weatherapp.Adapters.HourlyAdapter;
import com.example.weatherapp.Domains.Hourly;
import com.example.weatherapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TomorrowActivity extends AppCompatActivity {

    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/";
    private static final String API_KEY = "4dde056f1ed6b2e7c171af2621f643a7";
    private static final String UNITS = "metric";

    private WeatherApi weatherApi;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapterHourly;

    private TextView temperatureTextView;
    private TextView weatherConditionTextView;
    private TextView maxMinTempTextView;
    private TextView rainTextView;
    private TextView windTextView;
    private TextView humidityTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tomorrow);

        initApiClient();
        initUI();
        initRecyclerView();
        setBackButton();

        // Fetch weather data for tomorrow
        fetchWeatherData("Białystok");
    }

    private void initApiClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        weatherApi = retrofit.create(WeatherApi.class);
    }

    private void initUI() {
        temperatureTextView = findViewById(R.id.textView4);
        weatherConditionTextView = findViewById(R.id.textView5);
        maxMinTempTextView = findViewById(R.id.textViewMaxMin);
        rainTextView = findViewById(R.id.textView7);
        windTextView = findViewById(R.id.textViewRain);
        humidityTextView = findViewById(R.id.textViewHumidity);
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.view2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void setBackButton() {
        findViewById(R.id.back_btn).setOnClickListener(v ->
                startActivity(new Intent(TomorrowActivity.this, MainActivity.class)));
    }

    private void fetchWeatherData(String city) {
        Call<WeatherResponse> call = weatherApi.getWeather(city, API_KEY, UNITS);

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    updateUI(response.body());
                } else {
                    Toast.makeText(TomorrowActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Log.e("TomorrowActivity", "API call failed: " + t.getMessage());
                Toast.makeText(TomorrowActivity.this, "API call failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(WeatherResponse weatherResponse) {
        // Set main temperature
        temperatureTextView.setText(String.format("%sºC", weatherResponse.getMain().getTemp()));

        // Set weather condition
        weatherConditionTextView.setText(weatherResponse.getWeather().get(0).getMain());

        // Set max and min temperatures
        maxMinTempTextView.setText(String.format("Max: %sºC | Min: %sºC",
                weatherResponse.getMain().getTempMax(),
                weatherResponse.getMain().getTempMin()));

        // Set rain data
        rainTextView.setText(weatherResponse.getRain() != null ?
                String.format("%s mm", weatherResponse.getRain().getOneHourRain()) :
                "No rain");

        // Set wind speed (convert to km/h)
        String windSpeedKmH = String.format("%s km/h",
                Math.round(weatherResponse.getWind().getSpeed() * 3.6));
        windTextView.setText(windSpeedKmH);

        // Set humidity
        humidityTextView.setText(String.format("%s%%", weatherResponse.getMain().getHumidity()));

        // Update RecyclerView with hourly data
        updateHourlyRecyclerView(weatherResponse);
    }

    private void updateHourlyRecyclerView(WeatherResponse weatherResponse) {
        ArrayList<Hourly> items = new ArrayList<>();

        // Add hourly data (mock data for now, adjust if necessary)
        for (int i = 0; i < 8; i++) {
            items.add(new Hourly(
                    new SimpleDateFormat("ha", Locale.ENGLISH).format(new Date()),
                    (int) Math.round(Double.parseDouble(weatherResponse.getMain().getTemp())),
                    weatherResponse.getWeather().get(0).getMain()
            ));
        }

        adapterHourly = new HourlyAdapter(items);
        recyclerView.setAdapter(adapterHourly);
    }
}
