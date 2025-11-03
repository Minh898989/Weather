package com.example.appweather.activity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.appweather.R;
import com.example.appweather.model.TimeLineResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class DuBao5NgayHelper {
    private static final String TAG = "DuBao5Ngay";

    public static void thietLapDuBao5NgayFake(Context context, LinearLayout layout) {
        if (layout == null)
            return;
        layout.removeAllViews();

        String[] ngayTrongTuan = new String[5];
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dinhDangNgay = new SimpleDateFormat("EEEE", new Locale("vi", "VN"));

        for (int i = 0; i < 5; i++) {
            String tenNgay = dinhDangNgay.format(calendar.getTime());
            tenNgay = tenNgay.substring(0, 1).toUpperCase() + tenNgay.substring(1);

            if (tenNgay.toLowerCase().contains("chủ nhật")) {
                tenNgay = "Chủ nhật";
            }

            ngayTrongTuan[i] = tenNgay;
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        String[] thoiTiet = { "sunny", "cloudy", "rain_light", "rain_heavy", "sunny" };
        String[] nhietDo = { "30° / 25°", "29° / 24°", "27° / 23°", "26° / 22°", "31° / 26°" };

        for (int i = 0; i < ngayTrongTuan.length; i++) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_five_day_forecast, layout, false);

            TextView tvTenNgay = itemView.findViewById(R.id.tvDayName);
            ImageView ivIcon = itemView.findViewById(R.id.ivDayIcon);
            TextView tvNhietDo = itemView.findViewById(R.id.tvTempRange);

            tvTenNgay.setText(i == 0 ? "Hôm nay" : ngayTrongTuan[i]);
            tvNhietDo.setText(nhietDo[i]);

            int iconRes = chonIconThoiTiet(thoiTiet[i]);
            ivIcon.setImageResource(iconRes);

            layout.addView(itemView);
        }
    }

    public static void thietLapDuBao5NgayThuc(Context context, LinearLayout layout,
                                              TimeLineResponse timelineResponse) {
        if (layout == null || timelineResponse == null || timelineResponse.getData() == null) {
            Log.e(TAG, "Không thể thiết lập dự báo: layout hoặc dữ liệu null");
            thietLapDuBao5NgayFake(context, layout);
            return;
        }

        layout.removeAllViews();

        TimeLineResponse.Data data = timelineResponse.getData();
        List<TimeLineResponse.Timeline> timelines = data.getTimelines();

        if (timelines == null || timelines.isEmpty()) {
            Log.e(TAG, "Không có dữ liệu timeline");
            thietLapDuBao5NgayFake(context, layout);
            return;
        }

        // Lấy timeline theo ngày (1d)
        TimeLineResponse.Timeline dailyTimeline = null;
        for (TimeLineResponse.Timeline timeline : timelines) {
            if ("1d".equals(timeline.getTimestep())) {
                dailyTimeline = timeline;
                break;
            }
        }

        if (dailyTimeline == null || dailyTimeline.getIntervals() == null) {
            Log.e(TAG, "Không tìm thấy interval hàng ngày");
            thietLapDuBao5NgayFake(context, layout);
            return;
        }

        List<TimeLineResponse.Interval> intervals = dailyTimeline.getIntervals();
        SimpleDateFormat dinhDangNgay = new SimpleDateFormat("EEEE", new Locale("vi", "VN"));
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        int soNgay = Math.min(5, intervals.size());

        for (int i = 0; i < soNgay; i++) {
            TimeLineResponse.Interval interval = intervals.get(i);
            TimeLineResponse.Values values = interval.getValues();

            if (values == null) {
                Log.w(TAG, "Ngày " + i + " có giá trị null, bỏ qua");
                continue;
            }

            View itemView = LayoutInflater.from(context).inflate(R.layout.item_five_day_forecast, layout, false);

            TextView tvTenNgay = itemView.findViewById(R.id.tvDayName);
            ImageView ivIcon = itemView.findViewById(R.id.ivDayIcon);
            TextView tvNhietDo = itemView.findViewById(R.id.tvTempRange);

            // 1. Tên ngày
            String tenNgay = layTenNgay(interval.getStartTime(), i, dinhDangNgay, isoFormat);
            tvTenNgay.setText(tenNgay);

            // 2. Nhiệt độ
            String nhietDoText = layKhoangNhietDo(values);
            tvNhietDo.setText(nhietDoText);

            // 3. Icon thời tiết
            int iconRes = chonIconTuWeatherCode(values.getWeatherCode());
            ivIcon.setImageResource(iconRes);

            layout.addView(itemView);
        }

        Log.d(TAG, "✅ Dự báo 5 ngày cập nhật thành công: " + soNgay + " ngày | temp + weatherCode");
    }

    private static String layTenNgay(String isoTime, int indexNgay, SimpleDateFormat dayFormat,
                                     SimpleDateFormat isoFormat) {
        if (indexNgay == 0) {
            return "Hôm nay";
        }

        try {
            Date date = isoFormat.parse(isoTime);
            if (date != null) {
                String tenNgay = dayFormat.format(date);
                tenNgay = tenNgay.substring(0, 1).toUpperCase() + tenNgay.substring(1);

                if (tenNgay.toLowerCase().contains("chủ nhật")) {
                    tenNgay = "Chủ nhật";
                }

                return tenNgay;
            }
        } catch (ParseException e) {
            Log.e(TAG, "Lỗi parse date: " + isoTime, e);
        }

        return "N/A";
    }

    // Lấy chuỗi nhiệt độ dạng "max° / min°"
    private static String layKhoangNhietDo(TimeLineResponse.Values values) {
        Double max = values.getTemperatureMax();
        Double min = values.getTemperatureMin();
        Double temp = values.getTemperature();

        if (max != null && min != null) {
            return Math.round(max) + "° / " + Math.round(min) + "°";
        }

        if (temp != null) {
            int t = (int) Math.round(temp);
            return t + "° / " + t + "°";
        }

        return "--° / --°";
    }

    private static int chonIconTuWeatherCode(Integer code) {
        if (code == null)
            return R.drawable.ic_cloudy;

        switch (code) {
            case 1000:
                return R.drawable.ic_sunny;
            case 1100:
            case 1101:
                return R.drawable.ic_sunny;
            case 1102:
            case 1001:
                return R.drawable.ic_cloudy;
            case 2000:
            case 2100:
                return R.drawable.ic_cloudy;
            case 4000:
            case 4200:
                return R.drawable.ic_rainy;
            case 4001:
            case 4201:
                return R.drawable.drizzle;
            case 5000:
            case 5001:
            case 5100:
            case 5101:
                return R.drawable.ic_cloudy;
            case 6000:
            case 6100:
            case 6200:
            case 6201:
                return R.drawable.drizzle;
            case 7000:
            case 7100:
            case 7101:
            case 7102:
                return R.drawable.drizzle;
            case 8000:
                return R.drawable.drizzle;
            default:
                return R.drawable.ic_cloudy;
        }
    }

    private static int chonIconThoiTiet(String weather) {
        switch (weather) {
            case "sunny":
                return R.drawable.ic_sunny;
            case "rain_light":
                return R.drawable.ic_rainy;
            case "rain_heavy":
                return R.drawable.drizzle;
            case "cloudy":
            default:
                return R.drawable.ic_cloudy;
        }
    }
}