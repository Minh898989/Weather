package com.example.appweather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.appweather.adapters.DailyAdapter;
import com.example.appweather.adapters.HourlyAdapter;
import com.example.appweather.models.WeatherItem;
import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        AppBarLayout appBarLayout = findViewById(R.id.appBarLayout);
        LinearLayout headerLayout = findViewById(R.id.headerLayout);

        appBarLayout.addOnOffsetChangedListener((appBar, verticalOffset) -> {
            int totalScrollRange = appBar.getTotalScrollRange();
            float ratio = 1 - (float) Math.abs(verticalOffset) / totalScrollRange;


            headerLayout.setScaleX(0.85f + 0.15f * ratio);
            headerLayout.setScaleY(0.85f + 0.15f * ratio);
            headerLayout.setAlpha(0.5f + 0.5f * ratio);
        });



        RecyclerView recyclerHourly = findViewById(R.id.recyclerHourly);
        RecyclerView recyclerDaily = findViewById(R.id.recyclerDaily);

        recyclerHourly.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerDaily.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        List<WeatherItem> hourly = new ArrayList<>();
        hourly.add(new WeatherItem("23 giờ", "21°", R.drawable.ic_cloud));
        hourly.add(new WeatherItem("00 giờ", "20°", R.drawable.ic_moon));
        hourly.add(new WeatherItem("01 giờ", "20°", R.drawable.ic_moon));
        hourly.add(new WeatherItem("02 giờ", "19°", R.drawable.ic_moon));

        List<WeatherItem> daily = new ArrayList<>();
        daily.add(new WeatherItem("Hôm nay", "20° - 29°", R.drawable.ic_cloud));
        daily.add(new WeatherItem("Th 4", "18° - 27°", R.drawable.ic_sunny));
        daily.add(new WeatherItem("Th 5", "18° - 24°", R.drawable.ic_cloud));
        daily.add(new WeatherItem("Th 6", "19° - 25°", R.drawable.ic_sunny));
        daily.add(new WeatherItem("Th 7", "18° - 27°", R.drawable.ic_sunny));

        recyclerHourly.setAdapter(new HourlyAdapter(hourly));
        recyclerDaily.setAdapter(new DailyAdapter(daily));
    }
}
