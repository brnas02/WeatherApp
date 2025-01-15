package com.example.weatherapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.weatherapp.Adapters.TomorrowAdapter;
import com.example.weatherapp.Domains.TomorrowDomain;
import com.example.weatherapp.R;
import java.util.ArrayList;

public class TomorrowActivity extends AppCompatActivity {

    private RecyclerView.Adapter adapterTomorrow;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tomorrow);

        initRecyclerView();
        setVariable();
    }

    private void setVariable() {
        ConstraintLayout backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(v -> startActivity(new Intent(TomorrowActivity.this, MainActivity.class)));
    }

    private void initRecyclerView() {
        ArrayList<TomorrowDomain> items = new ArrayList<>();

        items.add(new TomorrowDomain("Sat",10, "storm", "Storm",25));
        items.add(new TomorrowDomain("Sun",16, "cloudy", "Rain_sunny",24));
        items.add(new TomorrowDomain("Mon",15, "cloudy_3", "Cloudy",29));
        items.add(new TomorrowDomain("Tue",13, "cloudy_sunny", "Cloudy-Sunny",22));
        items.add(new TomorrowDomain("Wen",11, "sun", "Sunny",28));
        items.add(new TomorrowDomain("Thu",12, "rainy", "Rainy",23));

        //ver view1, porque é view2
        recyclerView = findViewById(R.id.view1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        adapterTomorrow = new TomorrowAdapter(items);
        recyclerView.setAdapter(adapterTomorrow);
    }
}
