package com.example.appweather.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.appweather.R;

import java.util.ArrayList;
import java.util.List;

public class ProvinceAdapter extends ArrayAdapter<String> {
    private final List<String> originalList;
    private final List<String> filteredList;

    public ProvinceAdapter(Context context, List<String> provinces) {
        super(context, 0, provinces);
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
        if (query == null || query.isEmpty()) {
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
