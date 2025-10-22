package com.example.appweather;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    LinearLayout hourlyContainer, cityBar;
    TextView tvCity;
    ImageView iconSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupHourlyForecast();
        cityBar = findViewById(R.id.cityBar);
        tvCity = findViewById(R.id.tvCity);
        iconSettings = findViewById(R.id.iconSettings);
        hourlyContainer = findViewById(R.id.hourlyContainer);

        cityBar.setOnClickListener(v -> showProvinceDialog());

        iconSettings.setOnClickListener(v ->
                Toast.makeText(this, "Mở cài đặt thời tiết", Toast.LENGTH_SHORT).show()
        );
    }
    private void setupHourlyForecast() {
        LinearLayout container = findViewById(R.id.hourlyContainer);
        container.removeAllViews();

        LayoutInflater inflater = LayoutInflater.from(this);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat hourFormat = new SimpleDateFormat("ha", Locale.getDefault());

        int[] fakeTemps = {27, 28, 29, 30, 29, 28, 27};
        String[] fakeWeather = {"cloudy", "sunny", "rain", "overcast", "sunny", "cloudy", "rain"};

        for (int i = 0; i < fakeTemps.length; i++) {
            LinearLayout itemView = (LinearLayout) inflater.inflate(R.layout.item_hour_forecast, container, false);

            TextView tvTemp = itemView.findViewById(R.id.tvTemp);
            TextView tvHour = itemView.findViewById(R.id.tvHour);
            ImageView ivWeather = itemView.findViewById(R.id.ivWeather);

            String label = hourFormat.format(calendar.getTime());
            tvHour.setText(label);
            tvTemp.setText(fakeTemps[i] + "°");

            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            String weather = fakeWeather[i];
            int iconRes;


            if (!weather.equals("rain") && (hour >= 19 || hour < 6)) {
                iconRes = R.drawable.moon;
            } else {
                switch (weather) {
                    case "sunny":
                        iconRes = R.drawable.ic_sunny;
                        break;
                    case "rain":
                        iconRes = R.drawable.ic_rainy;
                        break;
                    case "overcast":
                        iconRes = R.drawable.drizzle;
                        break;
                    case "moon":
                        iconRes = R.drawable.moon;
                        break;
                    case "cloudy":
                    default:
                        iconRes = R.drawable.ic_cloudy;
                        break;
                }
            }

            ivWeather.setImageResource(iconRes);
            container.addView(itemView);
            calendar.add(Calendar.HOUR_OF_DAY, 1);
        }
    }


    private void showProvinceDialog() {

        String[] provinces = getResources().getStringArray(R.array.vietnam_provinces);
        List<String> provinceList = new ArrayList<>(Arrays.asList(provinces));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_province_picker, null);
        builder.setView(dialogView);

        EditText searchBox = dialogView.findViewById(R.id.searchBox);
        ListView listView = dialogView.findViewById(R.id.listViewProvinces);

        ProvinceAdapter adapter = new ProvinceAdapter(provinceList);
        listView.setAdapter(adapter);

        AlertDialog dialog = builder.create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_bg);
        }

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedProvince = adapter.getItem(position);
            tvCity.setText(selectedProvince + ", Việt Nam");
            Toast.makeText(this, "Đã chọn: " + selectedProvince, Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }

    private class ProvinceAdapter extends ArrayAdapter<String> {
        private List<String> originalList;
        private List<String> filteredList;

        public ProvinceAdapter(List<String> provinces) {
            super(MainActivity.this, 0, provinces);
            this.originalList = new ArrayList<>(provinces);
            this.filteredList = new ArrayList<>(provinces);
        }

        @Override
        public int getCount() {
            return filteredList.size();
        }

        @Override
        public String getItem(int position) {
            return filteredList.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext())
                        .inflate(R.layout.item_province, parent, false);
            }

            TextView tvProvinceName = convertView.findViewById(R.id.tvProvinceName);
            tvProvinceName.setText(filteredList.get(position));

            return convertView;
        }

        public void filter(String query) {
            filteredList.clear();
            if (query.isEmpty()) {
                filteredList.addAll(originalList);
            } else {
                String lowerCaseQuery = query.toLowerCase();
                for (String province : originalList) {
                    if (province.toLowerCase().contains(lowerCaseQuery)) {
                        filteredList.add(province);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }
}