package com.example.appweather.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appweather.R;
import com.example.appweather.api.ApiClient;
import com.example.appweather.api.ApiService;
import com.example.appweather.model.RealtimeResponse;
import com.example.appweather.model.TimeLineResponse;
import com.example.appweather.model.TimelineRequest;
import com.example.appweather.model.WeatherResponse;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
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

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private long lastRequestTimestamp;
    private static final String API_KEY = "lmyTvA6zxjtNm0ZdzaiKwpZrFFt3X4eV";

    LinearLayout hourlyContainer, cityBar;
    TextView tvCity;
    TextView tvTemperature;
    ImageView iconSettings;
    RelativeLayout rootLayout;
    private TextView tvPrecipitationValue;
    private TextView tvHumidityValue;
    private TextView tvWindValue;
    private TextView tvAQIValue;
    private TextView tvAQIStatus;
    private ApiService apiService;
    private String currentLocation = "Hà nội";
    public interface HistoryCallback {
        void onSuccess(WeatherResponse.Values yesterdayValues);
        void onFailure(Throwable t);
    }
    public interface WeatherValues {
        Double getVisibility();
        Double getHumidity();
        Integer getWeatherCode();
        Double getWindSpeed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Khởi tạo API Service
        apiService = ApiClient.getClient().create(ApiService.class);
        cityBar = findViewById(R.id.cityBar);
        tvCity = findViewById(R.id.tvCity);
        tvTemperature = findViewById(R.id.tvTemperature);
        iconSettings = findViewById(R.id.iconSettings);
        hourlyContainer = findViewById(R.id.hourlyContainer);
        rootLayout = findViewById(R.id.rootLayout);
        tvPrecipitationValue = findViewById(R.id.tvPrecipitationValue);
        tvHumidityValue = findViewById(R.id.tvHumidityValue);
        tvWindValue = findViewById(R.id.tvWindValue);
        tvAQIValue = findViewById(R.id.tvAQIValue);
        tvAQIStatus = findViewById(R.id.tvAQIStatus);
        // Gọi API để lấy dữ liệu thời tiết thật
        layDuLieuThoiTiet(currentLocation);
        layDuLieuThoiTiet5Ngay(currentLocation);
        layVaCapNhatCards(currentLocation);
        scheduleDailyWeatherNotification();
        LinearLayout layoutFiveDay = findViewById(R.id.layoutFiveDay);
        cityBar.setOnClickListener(v -> showProvinceDialog());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
        }

        iconSettings.setOnClickListener(v -> Toast.makeText(this, "Mở cài đặt thời tiết", Toast.LENGTH_SHORT).show());
    }

    private void layDuLieuThoiTiet(String location) {
        Log.d(TAG, "Đang lấy dữ liệu thời tiết cho: " + location);
        final long requestTime = System.currentTimeMillis();
        if (location.equals(currentLocation)) {
            lastRequestTimestamp = requestTime;
        }

        Call<WeatherResponse> call = apiService.getForecast(
                location,
                API_KEY,
                "metric", // đơn vị đo (Celsius)
                "1h,1d" // bước thời gian (giờ, ngày)
        );
        Log.d(TAG, "URL yêu cầu: " + call.request().url());
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (requestTime < lastRequestTimestamp) {
                    Log.d(TAG, "Bỏ qua kết quả cũ từ layDuLieuThoiTiet.");
                    return;
                }
                try {
                    Log.d(TAG, "Mã phản hồi: " + response.code());
                    if (!response.isSuccessful()) {
                        String errorBody = response.errorBody() != null ? response.errorBody().string()
                                : "Lỗi không xác định";
                        Log.e(TAG, "Nội dung lỗi: " + errorBody);
                        Toast.makeText(MainActivity.this, "Lỗi API: " + response.code(), Toast.LENGTH_LONG).show();
                        return;
                    }
                    WeatherResponse duLieuThoiTiet = response.body();
                    if (duLieuThoiTiet == null) {
                        Log.e(TAG, "Phản hồi trả về null");
                        return;
                    }

                    Log.d(TAG, "✅ Dữ liệu thời tiết đã được phân tích! Vị trí: " +
                            (duLieuThoiTiet.getLocation() != null ? duLieuThoiTiet.getLocation().getName() : "null"));

                    capNhatUIVoiDuLieuThoiTiet(duLieuThoiTiet);
                } catch (Exception e) {
                    Log.e(TAG, "Lỗi phân tích dữ liệu: " + e.getMessage(), e);
                    Toast.makeText(MainActivity.this, "Lỗi xử lý dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Log.e(TAG, "Gọi API thất bại", t);

                String thongBaoLoi = t instanceof java.net.UnknownHostException
                        ? "Không có kết nối Internet"
                        : "Lỗi: " + t.getMessage();

                Log.e(TAG, "Thông báo lỗi: " + thongBaoLoi);
            }
        });
    }

    private void layDuLieuThoiTiet5Ngay(String location) {
        Log.d(TAG, "Lấy dữ liệu dự báo 5 ngày cho: " + location);
        final long requestTime = System.currentTimeMillis();

        List<String> cacTruong = Arrays.asList(
                "temperature",
                "temperatureMax",
                "temperatureMin",
                "weatherCode",
                "uvIndex");

        List<String> buocThoiGian = Arrays.asList("1d");

        SimpleDateFormat dinhDangISO = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        dinhDangISO.setTimeZone(TimeZone.getTimeZone("UTC"));
        String ngayHienTai = dinhDangISO.format(new Date());

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 5);
        String ngayHienTaiCong5Ngay = dinhDangISO.format(cal.getTime());

        TimelineRequest requestBody = new TimelineRequest(
                location,
                cacTruong,
                buocThoiGian,
                "metric", // đơn vị đo
                ngayHienTai, // startTime
                ngayHienTaiCong5Ngay, // endTime
                6 // step count (ví dụ)
        );

        Call<TimeLineResponse> call = apiService.getTimelines(API_KEY, requestBody);
        call.enqueue(new Callback<TimeLineResponse>() {
            @Override
            public void onResponse(Call<TimeLineResponse> call, Response<TimeLineResponse> response) {
                if (requestTime < lastRequestTimestamp) {
                    Log.d(TAG, "Bỏ qua kết quả cũ từ layDuLieuThoiTiet5Ngay.");
                    return;
                }
                if (!response.isSuccessful()) {
                    Log.e(TAG, "Lỗi API: " + response.code());
                    return;
                }
                TimeLineResponse duLieuTimeline = response.body();
                if (duLieuTimeline == null) {
                    Log.e(TAG, "Dữ liệu timeline trả về null");
                    return;
                }
                capNhatUIVoiDuLieuTimeline(duLieuTimeline);
            }

            @Override
            public void onFailure(Call<TimeLineResponse> call, Throwable t) {
                Log.e(TAG, "Gọi API thất bại", t);

                String thongBaoLoi = t instanceof java.net.UnknownHostException
                        ? "Không có kết nối Internet"
                        : "Lỗi: " + t.getMessage();
                Log.e(TAG, "Thông báo lỗi: " + thongBaoLoi);
            }
        });
    }

    private void capNhatUIVoiDuLieuTimeline(TimeLineResponse timelineData) {
        if (timelineData == null || timelineData.getData() == null) {
            Log.e(TAG, "Dữ liệu timeline null");
            return;
        }

        LinearLayout layout5Ngay = findViewById(R.id.layoutFiveDay);
        DuBao5NgayHelper.thietLapDuBao5NgayThuc(this, layout5Ngay, timelineData);

        Log.d(TAG, " UI đã được cập nhật với dữ liệu dự báo 5 ngày!");
    }

    private void capNhatUIVoiDuLieuThoiTiet(WeatherResponse weatherData) {
        // Cập nhật tên thành phố có fallback
        String cityName = (weatherData.getLocation() != null && weatherData.getLocation().getName() != null)
                ? weatherData.getLocation().getName()
                : currentLocation;
        tvCity.setText(cityName);

        // Cập nhật hourly forecast với dữ liệu thật
        if (weatherData.getTimelines() != null
                && weatherData.getTimelines().getHourly() != null
                && !weatherData.getTimelines().getHourly().isEmpty()) {
            setupHourlyForecastWithData(weatherData.getTimelines().getHourly());
        } else {
            Log.e(TAG, "Dữ liệu thời tiết không hợp lệ");
        }
    }

    private void setupHourlyForecastWithData(List<WeatherResponse.TimelineData> hourlyData) {
        LinearLayout container = findViewById(R.id.hourlyContainer);
        container.removeAllViews();

        LayoutInflater inflater = LayoutInflater.from(this);
        SimpleDateFormat hourFormat = new SimpleDateFormat("ha", Locale.getDefault());
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        int maxHours = Math.min(hourlyData.size(), 7);

        for (int i = 0; i < maxHours; i++) {
            WeatherResponse.TimelineData hourData = hourlyData.get(i);
            WeatherResponse.Values values = hourData.getValues();

            if (values == null)
                continue;

            LinearLayout itemView = (LinearLayout) inflater.inflate(R.layout.item_hour_forecast, container, false);

            TextView tvTemp = itemView.findViewById(R.id.tvTemp);
            TextView tvHour = itemView.findViewById(R.id.tvHour);
            ImageView ivWeather = itemView.findViewById(R.id.ivWeather);

            String timeLabel;
            int hourOfDay = 12;
            try {
                Date date = isoFormat.parse(hourData.getTime());
                if (date != null) {
                    Calendar cal = Calendar.getInstance(TimeZone.getDefault());
                    cal.setTime(date);
                    hourOfDay = cal.get(Calendar.HOUR_OF_DAY);

                    timeLabel = (i == 0) ? "Now" : hourFormat.format(date);

                    Log.d(TAG, "Hour " + i + ": UTC=" + hourData.getTime() +
                            " | Local hour=" + hourOfDay +
                            " | isNight=" + (hourOfDay >= 18 || hourOfDay < 6));
                } else {
                    timeLabel = (i == 0) ? "Now" : "N/A";
                }
            } catch (ParseException e) {
                Log.e(TAG, "Error parsing time: " + hourData.getTime(), e);
                timeLabel = (i == 0) ? "Now" : "N/A";
            }

            tvHour.setText(timeLabel);

            Double temp = values.getTemperature();
            if (temp != null) {
                tvTemp.setText(Math.round(temp) + "°");
            } else {
                tvTemp.setText("--°");
            }

            boolean isNight = (hourOfDay >= 18 || hourOfDay < 6);

            Integer weatherCode = values.getWeatherCode();
            int iconRes = getWeatherIcon(weatherCode, isNight);
            ivWeather.setImageResource(iconRes);

            if (i == 0) {
                String weatherCondition = getWeatherCondition(weatherCode);
                WeatherBackgroundHelper.updateBackground(rootLayout, weatherCondition, isNight);

                Log.d(TAG, "Current hour: " + hourOfDay +
                        " | isNight: " + isNight +
                        " | condition: " + weatherCondition);
            }

            container.addView(itemView);
        }
    }

    private void layVaCapNhatCards(String location) {
        Log.d(TAG, "Đang lấy dữ liệu realtime cho cards: " + location);
        final long requestTime = System.currentTimeMillis();

        Call<RealtimeResponse> call = apiService.getRealtimeWeather(
                location,
                API_KEY,
                "metric");

        Log.d(TAG, "URL realtime: " + call.request().url());
        call.enqueue(new Callback<RealtimeResponse>() {
            @Override
            public void onResponse(Call<RealtimeResponse> call, Response<RealtimeResponse> response) {
                if (requestTime < lastRequestTimestamp) {
                    Log.d(TAG, "Bỏ qua kết quả cũ từ layVaCapNhatCards.");
                    return;
                }
                try {
                    if (!response.isSuccessful()) {
                        String errorBody = response.errorBody() != null
                                ? response.errorBody().string()
                                : "Lỗi không xác định";
                        Log.e(TAG, "❌ Lỗi realtime API: " + response.code() + " - " + errorBody);
                        Toast.makeText(MainActivity.this, "Lỗi API: " + response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    RealtimeResponse realtimeData = response.body();
                    if (realtimeData == null || realtimeData.getData() == null) {
                        Log.e(TAG, "Realtime response null");
                        return;
                    }

                    RealtimeResponse.Values values = realtimeData.getData().getValues();
                    if (values == null) {
                        Log.e(TAG, "Realtime values null");
                        return;
                    }

                    if (tvTemperature != null && values.getTemperature() != null) {
                        tvTemperature.setText(Math.round(values.getTemperature()) + "°");
                    }

                    capNhatCards(location, values);

                } catch (Exception e) {
                    Log.e(TAG, "Lỗi xử lý realtime data: " + e.getMessage(), e);
                }
            }

            @Override
            public void onFailure(Call<RealtimeResponse> call, Throwable t) {
                Log.e(TAG, "Gọi realtime API thất bại: " + t.getMessage(), t);

                runOnUiThread(() -> {
                    String thongBaoLoi;
                    if (t instanceof java.net.UnknownHostException) {
                        thongBaoLoi = "Không có kết nối Internet";
                    } else if (t instanceof java.net.SocketTimeoutException) {
                        thongBaoLoi = "Timeout - API phản hồi quá lâu";
                    } else {
                        thongBaoLoi = "Lỗi: " + t.getMessage();
                    }
                    Toast.makeText(MainActivity.this, thongBaoLoi, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void capNhatCards(String location, RealtimeResponse.Values currentValues) {
        if (currentValues == null) {
            Log.w(TAG, "❌ Values null trong capNhatCards, bỏ qua.");
            return;
        }

        updateCardDisplayValues(currentValues);

        layDuLieuLichSu(location, new HistoryCallback() {
            @Override
            public void onSuccess(WeatherResponse.Values yesterdayValues) {
                Log.d(TAG, "Cài đặt OnClick với dữ liệu lịch sử thật.");
                setupCardClickListeners(currentValues, yesterdayValues);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, "Lỗi lấy lịch sử. Cài đặt OnClick với dữ liệu hôm qua là N/A.", t);
                setupCardClickListeners(currentValues, null);
            }
        });
    }

    private String getAQIShortDescription(int aqi) {
        if (aqi <= 50) return "Tốt";
        if (aqi <= 100) return "Trung bình";
        if (aqi <= 150) return "Không lành mạnh";
        if (aqi <= 200) return "Rất không lành mạnh";
        if (aqi <= 300) return "Nguy hại";
        return "Rất nguy hại";
    }

    private void layDuLieuLichSu(String location, HistoryCallback callback) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

        calendar.add(Calendar.HOUR, -24);
        String endTime = sdf.format(calendar.getTime());

        calendar.add(Calendar.HOUR, -1);
        String startTime = sdf.format(calendar.getTime());

        Log.d(TAG, "Đang lấy dữ liệu lịch sử từ " + startTime + " đến " + endTime);

        apiService.getForecastHistory(location, API_KEY, "metric", "1h", startTime, endTime)
                .enqueue(new Callback<WeatherResponse>() {
                    @Override
                    public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getTimelines() != null && !response.body().getTimelines().isEmpty()) {
                            var timeline = response.body().getTimelines();
                            var interval = timeline.get(0);

                            if (interval != null) {
                                Log.d(TAG, "✅ Lấy dữ liệu lịch sử thành công.");
                                callback.onSuccess(interval.getValues());
                            } else {
                                callback.onFailure(new Exception("Dữ liệu lịch sử (interval) trả về null."));
                            }
                        } else {
                            try {
                                String errorMsg = "Lỗi khi lấy dữ liệu lịch sử: " + response.code();
                                if (response.errorBody() != null) errorMsg += " - " + response.errorBody().string();
                                Log.e(TAG, errorMsg);
                                callback.onFailure(new Exception(errorMsg));
                            } catch (Exception e) {
                                callback.onFailure(e);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<WeatherResponse> call, Throwable t) {
                        Log.e(TAG, "API lịch sử thất bại", t);
                        callback.onFailure(t);
                    }
                });
    }

    private void updateCardDisplayValues(RealtimeResponse.Values currentValues) {
        String precipValueToday = (currentValues.getPrecipitationProbability() != null)
                ? Math.round(currentValues.getPrecipitationProbability()) + "%" : "0%";
        if (tvPrecipitationValue != null) tvPrecipitationValue.setText(precipValueToday);

        float currentHumidity = (currentValues.getHumidity() != null) ? currentValues.getHumidity().floatValue() : 0.0f;
        String humidityValueToday = Math.round(currentHumidity) + "%";
        if (tvHumidityValue != null) tvHumidityValue.setText(humidityValueToday);

        float currentWindSpeed = (currentValues.getWindSpeed() != null) ? currentValues.getWindSpeed().floatValue() : 0.0f;
        String windValueToday = String.format(Locale.getDefault(), "%.1f km/h", currentWindSpeed);
        if (tvWindValue != null) tvWindValue.setText(windValueToday);

        int aqiToday = uocLuongAQI(currentValues);
        if (tvAQIValue != null) tvAQIValue.setText(String.valueOf(aqiToday));
        if (tvAQIStatus != null) tvAQIStatus.setText(getAQIShortDescription(aqiToday));
    }

    private void setupCardClickListeners(RealtimeResponse.Values currentValues, @Nullable WeatherResponse.Values yesterdayValues) {
        String precipValueToday = (currentValues.getPrecipitationProbability() != null)
                ? Math.round(currentValues.getPrecipitationProbability()) + "%" : "0%";
        String yesterdayPrecipValue = (yesterdayValues != null && yesterdayValues.getPrecipitationProbability() != null)
                ? Math.round(yesterdayValues.getPrecipitationProbability()) + "%" : "N/A";
        LinearLayout cardPrecipitation = findViewById(R.id.cardPrecipitation);
        if (cardPrecipitation != null) {
            cardPrecipitation.setOnClickListener(v -> {
                String descToday = getPrecipitationDescription(currentValues.getPrecipitationProbability() != null ? currentValues.getPrecipitationProbability() : 0.0);
                String descYesterday = (yesterdayValues != null && yesterdayValues.getPrecipitationProbability() != null)
                        ? getPrecipitationDescription(yesterdayValues.getPrecipitationProbability()) : "Hôm qua";

                new InfoDetailDialog(this).show(
                        R.drawable.rainn, "Khả năng có mưa", precipValueToday,
                        descToday,
                        precipValueToday, descToday,
                        yesterdayPrecipValue, descYesterday);
            });
        }
        float currentHumidity = (currentValues.getHumidity() != null) ? currentValues.getHumidity().floatValue() : 0.0f;
        String humidityValueToday = Math.round(currentHumidity) + "%";
        String yesterdayHumidityValue = (yesterdayValues != null && yesterdayValues.getHumidity() != null)
                ? Math.round(yesterdayValues.getHumidity()) + "%" : "N/A";

        LinearLayout cardHumidity = findViewById(R.id.cardHumidity);
        if (cardHumidity != null) {
            cardHumidity.setOnClickListener(v -> {
                String descToday = getHumidityDescription(currentHumidity);
                String descYesterday = (yesterdayValues != null && yesterdayValues.getHumidity() != null)
                        ? getHumidityDescription(yesterdayValues.getHumidity().floatValue()) : "Hôm qua";

                new InfoDetailDialog(this).show(
                        R.drawable.humidity, "Độ ẩm", humidityValueToday,
                        descToday,
                        humidityValueToday, descToday,
                        yesterdayHumidityValue, descYesterday);
            });
        }
        float currentWindSpeed = (currentValues.getWindSpeed() != null) ? currentValues.getWindSpeed().floatValue() : 0.0f;
        String windValueToday = String.format(Locale.getDefault(), "%.1f km/h", currentWindSpeed);
        String yesterdayWindValue = (yesterdayValues != null && yesterdayValues.getWindSpeed() != null)
                ? String.format(Locale.getDefault(), "%.1f km/h", yesterdayValues.getWindSpeed()) : "N/A";
        LinearLayout cardWind = findViewById(R.id.cardWind);
        if (cardWind != null) {
            cardWind.setOnClickListener(v -> {
                String descToday = getWindDescription(currentWindSpeed);
                String descYesterday = (yesterdayValues != null && yesterdayValues.getWindSpeed() != null)
                        ? getWindDescription(yesterdayValues.getWindSpeed().floatValue()) : "Hôm qua";

                new InfoDetailDialog(this).show(
                        R.drawable.wind, "Gió", windValueToday,
                        descToday,
                        windValueToday, descToday,
                        yesterdayWindValue, descYesterday);
            });
        }
        int aqiToday = uocLuongAQI(currentValues);
        int yesterdayAQIInt = (yesterdayValues != null) ? uocLuongAQI(yesterdayValues) : -1;
        String yesterdayAQIValue = (yesterdayAQIInt != -1) ? String.valueOf(yesterdayAQIInt) : "N/A";
        String yesterdayAQIDesc = (yesterdayAQIInt != -1) ? getAQIShortDescription(yesterdayAQIInt) : "Hôm qua";

        LinearLayout cardAQI = findViewById(R.id.cardAQI);
        if (cardAQI != null) {
            cardAQI.setOnClickListener(v -> new InfoDetailDialog(this).show(
                    R.drawable.ic_aqi, "Chỉ số AQI", String.valueOf(aqiToday),
                    getAQIDescription(aqiToday),
                    String.valueOf(aqiToday), getAQIShortDescription(aqiToday),
                    yesterdayAQIValue, yesterdayAQIDesc));
        }
    }
    private int uocLuongAQI(WeatherResponse.Values values) {
        if (values == null)
            return 24;

        double baseAQI = 24;

        Double visibility = values.getVisibility();
        if (visibility != null) {
            if (visibility < 2) {
                baseAQI += 80; // Tầm nhìn rất thấp → Ô nhiễm nặng
            } else if (visibility < 5) {
                baseAQI += 50; // Tầm nhìn thấp → Ô nhiễm vừa
            } else if (visibility < 10) {
                baseAQI += 20; // Tầm nhìn khá → Ô nhiễm nhẹ
            } else if (visibility >= 15) {
                baseAQI -= 20; // Tầm nhìn tốt → Không khí trong
            }
        }

        // 2. ĐỘ ẨM (Humidity) - Ảnh hưởng đến cảm giác
        Double humidity = values.getHumidity();
        if (humidity != null) {
            if (humidity > 85) {
                baseAQI += 15; // Độ ẩm cao → Dễ giữ bụi mịn
            } else if (humidity < 30) {
                baseAQI += 10; // Độ ẩm thấp → Bụi bay nhiều
            }
        }

        // 3. MÃ THỜI TIẾT (Weather Code)
        Integer weatherCode = values.getWeatherCode();
        if (weatherCode != null) {
            // Mưa → Làm sạch không khí
            if (weatherCode >= 4000 && weatherCode < 5000) {
                baseAQI -= 30;
            }
            // Tuyết → Làm sạch không khí
            else if (weatherCode >= 5000 && weatherCode < 6000) {
                baseAQI -= 25;
            }
            // Trời quang + tầm nhìn thấp → Khói bụi
            else if (weatherCode == 1000 && visibility != null && visibility < 5) {
                baseAQI += 40;
            }
        }

        // 4. GIÓ (Wind Speed) - Gió mạnh thổi bay ô nhiễm
        Double windSpeed = values.getWindSpeed();
        if (windSpeed != null) {
            if (windSpeed > 20) {
                baseAQI -= 15; // Gió mạnh → Phân tán ô nhiễm
            } else if (windSpeed < 3) {
                baseAQI += 20; // Gió yếu → Ô nhiễm tích tụ
            }
        }

        // 5. CHỈ SỐ UV - UV cao thường đi kèm không khí trong
        Double uvIndex = Double.valueOf(values.getUvIndex());
        if (uvIndex != null && uvIndex > 5) {
            baseAQI -= 10;
        }

        // Giới hạn AQI trong khoảng 0-500
        int estimatedAQI = (int) Math.max(0, Math.min(500, baseAQI));

        return estimatedAQI;
    }

    private int uocLuongAQI(RealtimeResponse.Values values) {
        if (values == null)
            return 24;

        double baseAQI = 24;

        Double visibility = values.getVisibility();
        if (visibility != null) {
            if (visibility < 2) {
                baseAQI += 80;
            } else if (visibility < 5) {
                baseAQI += 50;
            } else if (visibility < 10) {
                baseAQI += 20;
            } else if (visibility >= 15) {
                baseAQI -= 20;
            }
        }

        Double humidity = values.getHumidity();
        if (humidity != null) {
            if (humidity > 85) {
                baseAQI += 15;
            } else if (humidity < 30) {
                baseAQI += 10;
            }
        }

        Integer weatherCode = values.getWeatherCode();
        if (weatherCode != null) {
            if (weatherCode >= 4000 && weatherCode < 5000) {
                baseAQI -= 30;
            } else if (weatherCode >= 5000 && weatherCode < 6000) {
                baseAQI -= 25;
            } else if (weatherCode == 1000 && visibility != null && visibility < 5) {
                baseAQI += 40;
            }
        }

        Double windSpeed = values.getWindSpeed();
        if (windSpeed != null) {
            if (windSpeed > 20) {
                baseAQI -= 15;
            } else if (windSpeed < 3) {
                baseAQI += 20;
            }
        }

        Double uvIndex = values.getUvIndex();
        if (uvIndex != null && uvIndex > 5) {
            baseAQI -= 10;
        }

        int estimatedAQI = (int) Math.max(0, Math.min(500, baseAQI));

        return estimatedAQI;
    }

    private String getAQIDescription(int aqi) {
        if (aqi <= 50) {
            return "Không khí trong lành, rất tốt cho sức khỏe.";
        } else if (aqi <= 100) {
            return "Chất lượng không khí chấp nhận được.";
        } else if (aqi <= 150) {
            return "Nhạy cảm với người có vấn đề về hô hấp.";
        } else if (aqi <= 200) {
            return "Không khí không lành mạnh, hạn chế ra ngoài.";
        } else if (aqi <= 300) {
            return "Chất lượng không khí rất kém, nguy hiểm.";
        } else {
            return "Nguy hiểm! Tránh ra ngoài.";
        }
    }

    private String getAQIColor(int aqi) {
        if (aqi <= 50)
            return "#00E400"; // Xanh lá
        else if (aqi <= 100)
            return "#FFFF00"; // Vàng
        else if (aqi <= 150)
            return "#FF7E00"; // Cam
        else if (aqi <= 200)
            return "#FF0000"; // Đỏ
        else if (aqi <= 300)
            return "#8F3F97"; // Tím
        else
            return "#7E0023"; // Nâu đỏ
    }

    private int getWeatherIcon(Integer weatherCode, boolean isNight) {
        if (weatherCode == null) {
            return R.drawable.ic_cloudy;
        }

        switch (weatherCode) {
            case 1000: // Clear/Sunny
                return isNight ? R.drawable.moon : R.drawable.ic_sunny;
            case 1100: // Mostly Clear
            case 1101: // Partly Cloudy
                return isNight ? R.drawable.moon : R.drawable.ic_sunny;
            case 1102: // Mostly Cloudy
            case 1001: // Cloudy
                return R.drawable.ic_cloudy;
            case 4000: // Drizzle
            case 4200: // Light Rain
                return R.drawable.ic_rainy;
            case 4001: // Rain
            case 4201: // Heavy Rain
                return R.drawable.drizzle;
            case 5000: // Snow
            case 5001: // Flurries
            case 5100: // Light Snow
            case 5101: // Heavy Snow
                return R.drawable.ic_rainy; // Dùng tạm, nên có icon snow riêng
            case 8000: // Thunderstorm
                return R.drawable.drizzle;
            default:
                return R.drawable.ic_cloudy;
        }
    }

    private String getPrecipitationDescription(double probability) {
        if (probability < 10) {
            return "Khả năng mưa rất thấp, thời tiết khô ráo.";
        } else if (probability < 30) {
            return "Khả năng mưa thấp, có thể mang ô dự phòng.";
        } else if (probability < 60) {
            return "Khả năng mưa trung bình, nên mang theo ô.";
        } else if (probability < 80) {
            return "Khả năng mưa cao, chắc chắn sẽ có mưa.";
        } else {
            return "Khả năng mưa rất cao, sẽ mưa to.";
        }
    }

    private String getHumidityDescription(float humidity) {
        if (humidity < 30) {
            return "Độ ẩm thấp, không khí khô.";
        } else if (humidity < 50) {
            return "Độ ẩm vừa phải, thoải mái.";
        } else if (humidity < 70) {
            return "Độ ẩm hơi cao, cảm giác hơi ẩm.";
        } else if (humidity < 85) {
            return "Độ ẩm cao, cảm giác hơi oi.";
        } else {
            return "Độ ẩm rất cao, rất oi bức.";
        }
    }

    private String getWindDescription(float windSpeed) {
        if (windSpeed < 5) {
            return "Gió rất nhẹ, gần như không có gió.";
        } else if (windSpeed < 15) {
            return "Gió nhẹ, thoải mái khi di chuyển ngoài trời.";
        } else if (windSpeed < 30) {
            return "Gió vừa phải, cảm nhận rõ khi ở ngoài.";
        } else if (windSpeed < 50) {
            return "Gió mạnh, nên hạn chế ra ngoài.";
        } else {
            return "Gió rất mạnh, nguy hiểm khi ra ngoài.";
        }
    }

    private String getWeatherCondition(Integer weatherCode) {
        if (weatherCode == null)
            return "cloudy";

        if (weatherCode == 1000 || weatherCode == 1100 || weatherCode == 1101) {
            return "sunny";
        } else if (weatherCode == 4000 || weatherCode == 4200) {
            return "rain_light";
        } else if (weatherCode == 4001 || weatherCode == 4201 || weatherCode == 8000) {
            return "rain_heavy";
        } else {
            return "cloudy";
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

        ProvinceAdapter adapter = new ProvinceAdapter(this, provinceList);
        listView.setAdapter(adapter);

        AlertDialog dialog = builder.create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_bg);
        }

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

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedProvince = adapter.getItem(position);
            tvCity.setText(selectedProvince + ", Việt Nam");
            lastRequestTimestamp = System.currentTimeMillis();
            // Cập nhật location và gọi API lại
            currentLocation = selectedProvince;
            layDuLieuThoiTiet(currentLocation);
            layDuLieuThoiTiet5Ngay(currentLocation);
            layVaCapNhatCards(currentLocation);
            Toast.makeText(this, "Đang tải thời tiết " + selectedProvince + "...", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }
    private void scheduleDailyWeatherNotification() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, WeatherNotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 33);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setRepeating(
                        AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY,
                        pendingIntent
                );
            } else {
                try {
                    Intent i = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                    startActivity(i);
                } catch (Exception e) {
                    Toast.makeText(this, "Không thể đặt báo thức chính xác", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
            );
        }
    }
}