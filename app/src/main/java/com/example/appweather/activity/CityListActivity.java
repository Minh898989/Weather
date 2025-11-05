package com.example.appweather.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appweather.R;
import com.example.appweather.api.ApiClient;
import com.example.appweather.api.ApiService;
import com.example.appweather.model.CityWeatherItem;
import com.example.appweather.model.RealtimeResponse;
import com.example.appweather.model.TimeLineResponse;
import com.example.appweather.model.TimelineRequest;
import com.example.appweather.utils.CityManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CityListActivity extends AppCompatActivity {
    private static final String TAG = "CityListActivity";
    private static final String API_KEY = "ZBCh2mLhI9Bwo4Xk9umjRgHZ9JuASlZv";

    private ListView listViewCities;
    private EditText searchBox;
    private FloatingActionButton fabAddCity;
    private CityWeatherAdapter adapter;
    private List<CityWeatherItem> cityList;
    private ApiService apiService;
    private CityManager cityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);

        // Khởi tạo API Service và CityManager
        apiService = ApiClient.getClient().create(ApiService.class);
        cityManager = new CityManager(this);

        // Khởi tạo views
        searchBox = findViewById(R.id.searchBox);
        listViewCities = findViewById(R.id.listViewCities);
        fabAddCity = findViewById(R.id.fabAddCity);

        // Lấy danh sách thành phố đã lưu
        List<String> savedCities = cityManager.getSavedCities();
        cityList = new ArrayList<>();
        for (String city : savedCities) {
            cityList.add(new CityWeatherItem(city));
        }

        // Setup adapter
        adapter = new CityWeatherAdapter(this, cityList);
        adapter.setOnCityDeleteListener(this::onCityDelete);
        listViewCities.setAdapter(adapter);

        // Load weather data cho các thành phố đã lưu
        loadWeatherForSavedCities();

        // Xử lý nút thêm thành phố
        fabAddCity.setOnClickListener(v -> showAddCityDialog());

        // Xử lý tìm kiếm (ẩn mặc định)
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Xử lý click vào thành phố
        listViewCities.setOnItemClickListener((parent, view, position, id) -> {
            CityWeatherItem selectedCity = adapter.getItem(position);

            // Trả kết quả về MainActivity
            Intent resultIntent = new Intent();
            resultIntent.putExtra("selected_city", selectedCity.getCityName());
            setResult(RESULT_OK, resultIntent);
            finish();

            // Animation khi đóng
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        // Xử lý nút back
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    private void showAddCityDialog() {
        String[] allProvinces = getResources().getStringArray(R.array.vietnam_provinces);
        List<String> provinceList = new ArrayList<>(Arrays.asList(allProvinces));
        List<String> savedCities = cityManager.getSavedCities();

        // Loại bỏ các thành phố đã lưu
        provinceList.removeAll(savedCities);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_province_picker, null);
        builder.setView(dialogView);

        EditText dialogSearchBox = dialogView.findViewById(R.id.searchBox);
        ListView dialogListView = dialogView.findViewById(R.id.listViewProvinces);

        ProvinceAdapter dialogAdapter = new ProvinceAdapter(this, provinceList);
        dialogListView.setAdapter(dialogAdapter);

        AlertDialog dialog = builder.create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_bg);
        }

        dialogSearchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dialogAdapter.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        dialogListView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedCity = dialogAdapter.getItem(position);

            // Thêm vào danh sách lưu
            cityManager.addCity(selectedCity);

            // Thêm vào adapter
            CityWeatherItem newItem = new CityWeatherItem(selectedCity);
            adapter.addCity(newItem);

            // Load weather cho thành phố mới
            loadCityWeather(selectedCity);

            Toast.makeText(this, "Đã thêm " + selectedCity, Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }

    private void onCityDelete(String cityName) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa thành phố")
                .setMessage("Bạn có chắc muốn xóa " + cityName + " khỏi danh sách?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    // Xóa khỏi CityManager
                    cityManager.removeCity(cityName);

                    // Xóa khỏi adapter
                    adapter.removeCity(cityName);

                    Toast.makeText(this, "Đã xóa " + cityName, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void loadWeatherForSavedCities() {
        List<String> savedCities = cityManager.getSavedCities();
        for (String city : savedCities) {
            loadCityWeather(city);
        }
    }

    private void loadCityWeather(String cityName) {
        // Load realtime weather
        Call<RealtimeResponse> realtimeCall = apiService.getRealtimeWeather(
                cityName,
                API_KEY,
                "metric");

        realtimeCall.enqueue(new Callback<RealtimeResponse>() {
            @Override
            public void onResponse(Call<RealtimeResponse> call, Response<RealtimeResponse> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    updateCityError(cityName);
                    return;
                }

                RealtimeResponse realtimeData = response.body();
                if (realtimeData.getData() == null || realtimeData.getData().getValues() == null) {
                    updateCityError(cityName);
                    return;
                }

                RealtimeResponse.Values values = realtimeData.getData().getValues();

                // Load forecast để lấy high/low temp
                loadCityForecast(cityName, values);
            }

            @Override
            public void onFailure(Call<RealtimeResponse> call, Throwable t) {
                Log.e(TAG, "Failed to load weather for " + cityName, t);
                updateCityError(cityName);
            }
        });
    }

    private void loadCityForecast(String cityName, RealtimeResponse.Values currentValues) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        String startTime = sdf.format(new Date());

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        String endTime = sdf.format(cal.getTime());

        List<String> fields = Arrays.asList("temperatureMax", "temperatureMin");
        List<String> timesteps = Arrays.asList("1d");

        TimelineRequest requestBody = new TimelineRequest(
                cityName, fields, timesteps, "metric",
                startTime, endTime, 0);

        Call<TimeLineResponse> call = apiService.getTimelines(API_KEY, requestBody);
        call.enqueue(new Callback<TimeLineResponse>() {
            @Override
            public void onResponse(Call<TimeLineResponse> call, Response<TimeLineResponse> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    updateCityWithRealtimeOnly(cityName, currentValues);
                    return;
                }

                TimeLineResponse timelineData = response.body();
                updateCityWeatherData(cityName, currentValues, timelineData);
            }

            @Override
            public void onFailure(Call<TimeLineResponse> call, Throwable t) {
                Log.e(TAG, "Failed to load forecast for " + cityName, t);
                updateCityWithRealtimeOnly(cityName, currentValues);
            }
        });
    }

    private void updateCityWeatherData(String cityName, RealtimeResponse.Values values,
                                       TimeLineResponse timelineData) {
        CityWeatherItem item = new CityWeatherItem(cityName);
        item.setLoading(false);

        // Temperature
        if (values.getTemperature() != null) {
            item.setTemperature(Math.round(values.getTemperature()) + "°");
        }

        // Weather condition
        String condition = getWeatherConditionText(values.getWeatherCode());
        item.setWeatherCondition(condition);
        item.setWeatherCode(values.getWeatherCode());

        // High/Low temp
        String highLow = getHighLowTemp(timelineData, values.getTemperature());
        item.setHighLowTemp(highLow);

        adapter.updateCityWeather(cityName, item);
    }

    private void updateCityWithRealtimeOnly(String cityName, RealtimeResponse.Values values) {
        CityWeatherItem item = new CityWeatherItem(cityName);
        item.setLoading(false);

        if (values.getTemperature() != null) {
            item.setTemperature(Math.round(values.getTemperature()) + "°");
            int temp = (int) Math.round(values.getTemperature());
            item.setHighLowTemp("C:" + (temp + 1) + "° T:" + (temp - 2) + "°");
        }

        String condition = getWeatherConditionText(values.getWeatherCode());
        item.setWeatherCondition(condition);
        item.setWeatherCode(values.getWeatherCode());

        adapter.updateCityWeather(cityName, item);
    }

    private void updateCityError(String cityName) {
        CityWeatherItem item = new CityWeatherItem(cityName);
        item.setLoading(false);
        item.setHasError(true);

        adapter.updateCityWeather(cityName, item);
    }

    private String getHighLowTemp(TimeLineResponse timelineData, Double currentTemp) {
        if (timelineData == null || timelineData.getData() == null) {
            return "";
        }

        List<TimeLineResponse.Timeline> timelines = timelineData.getData().getTimelines();
        if (timelines == null || timelines.isEmpty()) {
            return "";
        }

        for (TimeLineResponse.Timeline timeline : timelines) {
            if ("1d".equals(timeline.getTimestep()) &&
                    timeline.getIntervals() != null &&
                    !timeline.getIntervals().isEmpty()) {

                TimeLineResponse.Interval interval = timeline.getIntervals().get(0);
                TimeLineResponse.Values values = interval.getValues();

                if (values != null) {
                    Double max = values.getTemperatureMax();
                    Double min = values.getTemperatureMin();

                    if (max != null && min != null) {
                        return "C:" + Math.round(max) + "° T:" + Math.round(min) + "°";
                    }
                }
            }
        }

        if (currentTemp != null) {
            int temp = (int) Math.round(currentTemp);
            return "C:" + (temp + 1) + "° T:" + (temp - 2) + "°";
        }

        return "";
    }

    private String getWeatherConditionText(Integer weatherCode) {
        if (weatherCode == null) {
            return "Không rõ";
        }

        switch (weatherCode) {
            case 1000:
                return "Trời quang đãng";
            case 1100:
                return "Ít mây";
            case 1101:
                return "Có mây vài nơi";
            case 1102:
                return "Nhiều mây";
            case 1001:
                return "Trời nhiều mây";
            case 2000:
            case 2100:
                return "Sương mù";
            case 4000:
                return "Mưa phùn";
            case 4200:
                return "Mưa nhẹ";
            case 4001:
                return "Mưa";
            case 4201:
                return "Mưa to";
            case 5000:
            case 5001:
            case 5100:
            case 5101:
                return "Tuyết";
            case 8000:
                return "Dông";
            default:
                return "Không rõ";
        }
    }
}