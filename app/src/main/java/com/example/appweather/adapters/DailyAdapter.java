package com.example.appweather.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.appweather.R;
import com.example.appweather.models.WeatherItem;

import java.util.List;

public class DailyAdapter extends RecyclerView.Adapter<DailyAdapter.ViewHolder> {
    private List<WeatherItem> items;

    public DailyAdapter(List<WeatherItem> items) {
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_daily, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        WeatherItem item = items.get(position);
        holder.tvDay.setText(item.getTime());
        holder.tvTempRange.setText(item.getTemperature());
        holder.ivWeather.setImageResource(item.getIcon());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDay, tvTempRange;
        ImageView ivWeather;

        ViewHolder(View itemView) {
            super(itemView);
            tvDay = itemView.findViewById(R.id.tvDay);
            tvTempRange = itemView.findViewById(R.id.tvTempRange);
            ivWeather = itemView.findViewById(R.id.ivWeather);
        }
    }
}
