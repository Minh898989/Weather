package com.example.appweather.activity;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appweather.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class InfoDetailDialog {

    private final Activity activity;
    private final BottomSheetDialog dialog;

    private final ImageView imgIcon;
    private final TextView tvTitle;
    private final TextView tvValue;
    private final TextView tvDescription;
    private final TextView tvTodayValue;
    private final TextView tvTodayDesc;
    private final TextView tvYesterdayValue;
    private final TextView tvYesterdayDesc;

    public InfoDetailDialog(Activity activity) {
        this.activity = activity;

        dialog = new BottomSheetDialog(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_info_detail, null);
        dialog.setContentView(view);

        imgIcon = view.findViewById(R.id.imgIcon);
        tvTitle = view.findViewById(R.id.tvTitle);
        tvValue = view.findViewById(R.id.tvValue);
        tvDescription = view.findViewById(R.id.tvDescription);
        tvTodayValue = view.findViewById(R.id.tvTodayValue);
        tvTodayDesc = view.findViewById(R.id.tvTodayDesc);
        tvYesterdayValue = view.findViewById(R.id.tvYesterdayValue);
        tvYesterdayDesc = view.findViewById(R.id.tvYesterdayDesc);
    }
    public void show(int iconResId, String title, String mainValue, String description,
                     String todayValue, String todayDesc, String yesterdayValue, String yesterdayDesc) {

        imgIcon.setImageResource(iconResId);
        tvTitle.setText(title);
        tvValue.setText(mainValue);
        tvDescription.setText(description);

        tvTodayValue.setText(todayValue);
        tvTodayDesc.setText(todayDesc);
        tvYesterdayValue.setText(yesterdayValue);
        tvYesterdayDesc.setText(yesterdayDesc);

        dialog.show();
    }
}
