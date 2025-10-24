package com.example.appweather;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

public class InfoDetailDialog {

    private final Activity activity;

    public InfoDetailDialog(Activity activity) {
        this.activity = activity;
    }

    public void show(String title, String value, String description, int iconResId) {
        BottomSheetDialog dialog = new BottomSheetDialog(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_info_detail, null);

        ImageView imgIcon = view.findViewById(R.id.imgIcon);
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        TextView tvValue = view.findViewById(R.id.tvValue);
        TextView tvDescription = view.findViewById(R.id.tvDescription);
        TextView tvTodayValue = view.findViewById(R.id.tvTodayValue);
        TextView tvTodayDesc = view.findViewById(R.id.tvTodayDesc);
        TextView tvYesterdayValue = view.findViewById(R.id.tvYesterdayValue);
        TextView tvYesterdayDesc = view.findViewById(R.id.tvYesterdayDesc);

        imgIcon.setImageResource(iconResId);
        tvTitle.setText(title);
        tvValue.setText(value);
        tvDescription.setText(description);
        tvTodayValue.setText("45");
        tvTodayDesc.setText("Tốt");
        tvYesterdayValue.setText("60");
        tvYesterdayDesc.setText("Trung bình");

        dialog.setContentView(view);
        dialog.show();
    }
}
