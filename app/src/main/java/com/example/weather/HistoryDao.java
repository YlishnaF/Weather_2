package com.example.weather;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Index;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


@Dao
public interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCity(HistoryWeather historyWeather);

    @Delete
    void deletHistory(HistoryWeather historyWeather);

    @Update
    void updateCities(HistoryWeather historyWeather);

    @Query("DELETE FROM historyweather WHERE id= :id")
    void deleteById(long id);

    @Query("SELECT * FROM historyweather")
    List<HistoryWeather> getAllCities();

    @Query("SELECT COUNT() FROM historyweather")
    long getCountWeather();
}
