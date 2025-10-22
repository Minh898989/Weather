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

public class HourlyAdapter extends RecyclerView.Adapter<HourlyAdapter.ViewHolder> {
    private List<WeatherItem> items;

    public HourlyAdapter(List<WeatherItem> items) {
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_hourly, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        WeatherItem item = items.get(position);
        holder.tvTime.setText(item.getTime());
        holder.tvTemp.setText(item.getTemperature());
        holder.ivWeather.setImageResource(item.getIcon());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime, tvTemp;
        ImageView ivWeather;

        ViewHolder(View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvTemp = itemView.findViewById(R.id.tvTemp);
            ivWeather = itemView.findViewById(R.id.ivWeather);
        }
    }
}
