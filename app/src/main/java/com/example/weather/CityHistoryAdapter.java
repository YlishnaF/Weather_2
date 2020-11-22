package com.example.weather;

import android.app.Activity;
import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class CityHistoryAdapter extends RecyclerView.Adapter<CityHistoryAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private HistorySource dataSource;
    private long menuPosition;
    private List<CityForHistory> historyList;
    Activity activity;

    public CityHistoryAdapter(Context context, HistorySource dataSource) {
        this.layoutInflater = LayoutInflater.from(context);
        this.dataSource = dataSource;
    }

    @NonNull
    @Override
    public CityHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_history_city, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CityHistoryAdapter.ViewHolder holder, final int position) {
        List<HistoryWeather> historyWeather = dataSource.getCities();
        HistoryWeather historyWeathers = historyWeather.get(position);
        holder.cityTv.setText(historyWeathers.cityName);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuPosition = position;
            }
        });

        if(activity!=null){
            activity.registerForContextMenu(holder.mView);
        }


    }

    @Override
    public int getItemCount() {
        return (int)dataSource.getCountCities();
    }
    public long getMenuPosition(){
        return menuPosition;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        final TextView cityTv;
        View mView;
        CardView cardView;
        public ViewHolder(View view) {
            super(view);
            mView = view;
            cityTv = mView.findViewById(R.id.city_name_history_tv);
            cardView = view.findViewById(R.id.card_view);
            cardView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(this.getAdapterPosition(), 1, 0, "delete");
        }
    }
}
