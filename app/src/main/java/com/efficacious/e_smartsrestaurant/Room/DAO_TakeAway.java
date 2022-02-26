package com.efficacious.e_smartsrestaurant.Room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DAO_TakeAway {

    @Insert
    void insertTakeAwayData(TakeAwayOrder takeAwayOrder);

}
