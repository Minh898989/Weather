package com.example.appweather.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.appweather.R;
import com.example.appweather.model.CityWeatherItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CityWeatherAdapter extends ArrayAdapter<CityWeatherItem> {
    private final List<CityWeatherItem> originalList;
    private final List<CityWeatherItem> filteredList;
    private OnCityDeleteListener deleteListener;
    private static final List<String> DEFAULT_CITIES = Arrays.asList("Hà Nội", "Hồ Chí Minh");

    public interface OnCityDeleteListener {
        void onCityDelete(String cityName);
    }

    public CityWeatherAdapter(Context context, List<CityWeatherItem> cities) {
        super(context, 0, cities);
        this.originalList = new ArrayList<>(cities);
        this.filteredList = new ArrayList<>(cities);
    }

    public void setOnCityDeleteListener(OnCityDeleteListener listener) {
        this.deleteListener = listener;
    }

    @Override
    public int getCount() {
        return filteredList.size();
    }

    @Override
    public CityWeatherItem getItem(int position) {
        return filteredList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_city_weather, parent, false);

            holder = new ViewHolder();
            holder.cityItemContainer = convertView.findViewById(R.id.cityItemContainer);
            holder.tvCityName = convertView.findViewById(R.id.tvCityName);
            holder.tvCurrentTime = convertView.findViewById(R.id.tvCurrentTime);
            holder.tvTemperature = convertView.findViewById(R.id.tvTemperature);
            holder.tvWeatherCondition = convertView.findViewById(R.id.tvWeatherCondition);
            holder.tvHighLow = convertView.findViewById(R.id.tvHighLow);
            holder.ivWeatherIcon = convertView.findViewById(R.id.ivWeatherIcon);
            holder.progressBar = convertView.findViewById(R.id.progressBar);
            holder.btnDeleteCity = convertView.findViewById(R.id.btnDeleteCity);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CityWeatherItem item = filteredList.get(position);

        // Set city name
        holder.tvCityName.setText(item.getCityName());

        // Show/hide delete button (hide for default cities)
        if (DEFAULT_CITIES.contains(item.getCityName())) {
            holder.btnDeleteCity.setVisibility(View.GONE);
        } else {
            holder.btnDeleteCity.setVisibility(View.VISIBLE);
            holder.btnDeleteCity.setOnClickListener(v -> {
                if (deleteListener != null) {
                    deleteListener.onCityDelete(item.getCityName());
                }
            });
        }

        // Set current time
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        holder.tvCurrentTime.setText(timeFormat.format(new Date()));

        if (item.isLoading()) {
            // Show loading state
            holder.progressBar.setVisibility(View.VISIBLE);
            holder.tvTemperature.setVisibility(View.GONE);
            holder.tvWeatherCondition.setVisibility(View.GONE);
            holder.tvHighLow.setVisibility(View.GONE);
            holder.ivWeatherIcon.setVisibility(View.GONE);
        } else if (item.isHasError()) {
            // Show error state
            holder.progressBar.setVisibility(View.GONE);
            holder.tvTemperature.setVisibility(View.VISIBLE);
            holder.tvWeatherCondition.setVisibility(View.VISIBLE);
            holder.tvHighLow.setVisibility(View.GONE);
            holder.ivWeatherIcon.setVisibility(View.GONE);

            holder.tvTemperature.setText("--°");
            holder.tvWeatherCondition.setText("Không thể tải dữ liệu");
        } else {
            // Show data
            holder.progressBar.setVisibility(View.GONE);
            holder.tvTemperature.setVisibility(View.VISIBLE);
            holder.tvWeatherCondition.setVisibility(View.VISIBLE);
            holder.tvHighLow.setVisibility(View.VISIBLE);
            holder.ivWeatherIcon.setVisibility(View.VISIBLE);

            holder.tvTemperature.setText(item.getTemperature() != null ? item.getTemperature() : "--°");
            holder.tvWeatherCondition.setText(item.getWeatherCondition() != null ? item.getWeatherCondition() : "");
            holder.tvHighLow.setText(item.getHighLowTemp() != null ? item.getHighLowTemp() : "");

            // Set weather icon
            int iconRes = getWeatherIcon(item.getWeatherCode());
            holder.ivWeatherIcon.setImageResource(iconRes);

            // Set background gradient based on weather
            setBackgroundGradient(holder.cityItemContainer, item.getWeatherCode());
        }

        return convertView;
    }

    private int getWeatherIcon(Integer weatherCode) {
        if (weatherCode == null) {
            return R.drawable.ic_cloudy;
        }

        switch (weatherCode) {
            case 1000: // Clear/Sunny
                return R.drawable.ic_sunny;
            case 1100: // Mostly Clear
            case 1101: // Partly Cloudy
                return R.drawable.ic_sunny;
            case 1102: // Mostly Cloudy
            case 1001: // Cloudy
                return R.drawable.ic_cloudy;
            case 4000: // Drizzle
            case 4200: // Light Rain
                return R.drawable.ic_rainy;
            case 4001: // Rain
            case 4201: // Heavy Rain
                return R.drawable.drizzle;
            case 8000: // Thunderstorm
                return R.drawable.drizzle;
            default:
                return R.drawable.ic_cloudy;
        }
    }

    private void setBackgroundGradient(RelativeLayout container, Integer weatherCode) {
        if (weatherCode == null) {
            container.setBackgroundResource(R.drawable.city_item_bg);
            return;
        }

        int backgroundRes;
        if (weatherCode == 1000 || weatherCode == 1100 || weatherCode == 1101) {
            // Sunny - Blue gradient
            backgroundRes = R.drawable.city_item_bg_sunny;
        } else if (weatherCode >= 4000 && weatherCode < 5000) {
            // Rainy - Dark blue gradient
            backgroundRes = R.drawable.city_item_bg_rainy;
        } else {
            // Cloudy - Default gradient
            backgroundRes = R.drawable.city_item_bg;
        }

        container.setBackgroundResource(backgroundRes);
    }

    public void filter(String query) {
        filteredList.clear();
        if (query == null || query.isEmpty()) {
            filteredList.addAll(originalList);
        } else {
            String lowerCaseQuery = query.toLowerCase();
            for (CityWeatherItem item : originalList) {
                if (item.getCityName().toLowerCase().contains(lowerCaseQuery)) {
                    filteredList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void updateCityWeather(String cityName, CityWeatherItem updatedItem) {
        for (int i = 0; i < originalList.size(); i++) {
            if (originalList.get(i).getCityName().equals(cityName)) {
                originalList.set(i, updatedItem);
                break;
            }
        }
        for (int i = 0; i < filteredList.size(); i++) {
            if (filteredList.get(i).getCityName().equals(cityName)) {
                filteredList.set(i, updatedItem);
                break;
            }
        }
        notifyDataSetChanged();
    }

    public void removeCity(String cityName) {
        originalList.removeIf(item -> item.getCityName().equals(cityName));
        filteredList.removeIf(item -> item.getCityName().equals(cityName));
        notifyDataSetChanged();
    }

    public void addCity(CityWeatherItem item) {
        originalList.add(item);
        filteredList.add(item);
        notifyDataSetChanged();
    }

    static class ViewHolder {
        RelativeLayout cityItemContainer;
        TextView tvCityName;
        TextView tvCurrentTime;
        TextView tvTemperature;
        TextView tvWeatherCondition;
        TextView tvHighLow;
        ImageView ivWeatherIcon;
        ImageView btnDeleteCity;
        ProgressBar progressBar;
    }
}