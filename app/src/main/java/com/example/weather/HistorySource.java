package com.example.weather;

import java.util.List;
import java.util.Set;

public class HistorySource {
    private List<HistoryWeather> list;
    private final HistoryDao historyDao;

    public HistorySource(HistoryDao historyDao) {
        this.historyDao = historyDao;
    }

    public List<HistoryWeather> getCities(){
        if(list==null){
            LoadCities();
        }
        return list;
    }

    public void LoadCities(){
        list = historyDao.getAllCities();
    }

    public long getCountCities(){
        return historyDao.getCountWeather();

    }

    public void addHistory(HistoryWeather historyWeather){
        historyDao.insertCity(historyWeather);
        LoadCities();
    }

    public void updateHistory(HistoryWeather historyWeather){
        historyDao.updateCities(historyWeather);
        LoadCities();
    }

    public void removeHistory(long id){
        historyDao.deleteById(id);
        LoadCities();
    }
}
