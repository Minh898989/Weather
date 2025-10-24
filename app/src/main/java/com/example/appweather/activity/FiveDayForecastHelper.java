package com.example.appweather.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.appweather.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class FiveDayForecastHelper {

    public static void setupFiveDayForecast(Context context, LinearLayout layout) {
        if (layout == null) return;
        layout.removeAllViews();

        String[] days = new String[5];
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", new Locale("vi", "VN")); // Thứ Hai, Thứ Ba...

        for (int i = 0; i < 5; i++) {
            String dayName = dayFormat.format(calendar.getTime());

            dayName = dayName.substring(0, 1).toUpperCase() + dayName.substring(1);

            if (dayName.toLowerCase().contains("chủ nhật")) {
                dayName = "Chủ nhật";
            }

            days[i] = dayName;
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        String[] weathers = {"sunny", "cloudy", "rain_light", "rain_heavy", "sunny"};
        String[] temps = {"30° / 25°", "29° / 24°", "27° / 23°", "26° / 22°", "31° / 26°"};

        for (int i = 0; i < days.length; i++) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_five_day_forecast, layout, false);

            TextView tvDayName = itemView.findViewById(R.id.tvDayName);
            ImageView ivDayIcon = itemView.findViewById(R.id.ivDayIcon);
            TextView tvTempRange = itemView.findViewById(R.id.tvTempRange);


            if (i == 0) {
                tvDayName.setText("Hôm nay");
            } else {
                tvDayName.setText(days[i]);
            }

            tvTempRange.setText(temps[i]);

            int iconRes;
            switch (weathers[i]) {
                case "sunny":
                    iconRes = R.drawable.ic_sunny;
                    break;
                case "rain_light":
                    iconRes = R.drawable.ic_rainy;
                    break;
                case "rain_heavy":
                    iconRes = R.drawable.drizzle;
                    break;
                case "cloudy":
                default:
                    iconRes = R.drawable.ic_cloudy;
                    break;
            }

            ivDayIcon.setImageResource(iconRes);
            layout.addView(itemView);
        }
    }
}
