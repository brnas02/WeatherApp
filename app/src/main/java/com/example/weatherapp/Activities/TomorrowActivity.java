package com.example.weatherapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.API.TomorrowResponse;
import com.example.weatherapp.API.WeatherApi;
import com.example.weatherapp.API.WeatherResponse;
import com.example.weatherapp.Adapters.TomorrowAdapter;
import com.example.weatherapp.Domains.Hourly;
import com.example.weatherapp.Domains.Tomorrow;
import com.example.weatherapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

    private ImageView weatherImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tomorrow);

        initApiClient();
        initUI();
        initRecyclerView();
        setBackButton();

        String city = getIntent().getStringExtra("city");

        // Fetch weather data for tomorrow
        fetchWeatherData(city);
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
        rainTextView = findViewById(R.id.textViewRain1);
        windTextView = findViewById(R.id.textView9);
        humidityTextView = findViewById(R.id.textViewHumidity1);
        weatherImageView = findViewById(R.id.imageView);
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.view2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void setBackButton() {
        findViewById(R.id.back_btn).setOnClickListener(v ->
                startActivity(new Intent(TomorrowActivity.this, MainActivity.class)));
    }

    private void fetchWeatherData(String city) {
        Call<TomorrowResponse> call = weatherApi.getWeatherForecast(city, API_KEY, UNITS);

        call.enqueue(new Callback<TomorrowResponse>() {
            @Override
            public void onResponse(Call<TomorrowResponse> call, Response<TomorrowResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    updateUI(response.body());
                } else {
                    Toast.makeText(TomorrowActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TomorrowResponse> call, Throwable t) {
                Log.e("TomorrowActivity", "API call failed: " + t.getMessage());
                Toast.makeText(TomorrowActivity.this, "API call failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(TomorrowResponse tomorrowResponse) {
        WeatherResponse weatherResponse = tomorrowResponse.getList().get(0);

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat hourOutputFormat = new SimpleDateFormat("HH:mm");

        String formattedHour = "Tomorrow";

        try {
            Date date = inputFormat.parse(weatherResponse.getDate());

            // Format the date and time separately
            formattedHour = hourOutputFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        TextView tomorrow = findViewById(R.id.textView3);
        tomorrow.setText(formattedHour);

        // Set main temperature
        temperatureTextView.setText(String.format("%sºC", Math.round(Double.parseDouble(weatherResponse.getMain().getTemp()))));

        // Set weather condition
        weatherConditionTextView.setText(weatherResponse.getWeather().get(0).getMain());

        // Set max and min temperatures
        maxMinTempTextView.setText(String.format("Max: %sºC | Min: %sºC",
                Math.round(Float.parseFloat(weatherResponse.getMain().getTempMax())),
                Math.round(Float.parseFloat(weatherResponse.getMain().getTempMin()))));

        // Set rain data
        rainTextView.setText(weatherResponse.getRain() != null ?
                String.format("%s mm", Math.round(Float.parseFloat(weatherResponse.getRain().getOneHourRain()))) :
                "No rain");

        // Set wind speed (convert to km/h)
        String windSpeedKmH = String.format("%s km/h",
                Math.round(weatherResponse.getWind().getSpeed() * 3.6));

        windTextView.setText(windSpeedKmH);

        // Set humidity
        humidityTextView.setText(String.format("%s%%", weatherResponse.getMain().getHumidity()));

        updateWeatherIcon(weatherResponse.getWeather().get(0).getMain());

        // Update RecyclerView with hourly data
        updateHourlyRecyclerView(tomorrowResponse);
    }

    private void updateHourlyRecyclerView(TomorrowResponse tomorrowResponse) {
        ArrayList<Tomorrow> items = new ArrayList<>();

        List<WeatherResponse> forecast = tomorrowResponse.getList();
        forecast.remove(0);

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateOutputFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat hourOutputFormat = new SimpleDateFormat("HH:mm");

        String formattedHour = "??:??";
        String formattedDate = "??/??/????";

        for (WeatherResponse data : forecast) {
            try {
                Date date = inputFormat.parse(data.getDate());

                // Format the date and time separately
                formattedHour = hourOutputFormat.format(date);
                formattedDate = dateOutputFormat.format(date);
            } catch (Exception e) {
                e.printStackTrace();
            }

            items.add(new Tomorrow(
                    formattedHour,
                    formattedDate,
                    Math.round(Float.parseFloat(data.getMain().getTempMin())) + "ºC",
                    data.getWeather().get(0).getMain().toLowerCase(),
                    data.getWeather().get(0).getMain(),
                    Math.round(Float.parseFloat(data.getMain().getTempMax())) + "ºC"
            ));
        }

        adapterHourly = new TomorrowAdapter(items);
        recyclerView.setAdapter(adapterHourly);
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
        } else if (condition.contains("PartlyCloud")) {
            weatherImageView.setImageResource(R.drawable.cloudy_sunny);  // Partly Cloud icon
        }

        Log.d("TomorrowActivity", "Weather condition: " + condition);
    }
}
