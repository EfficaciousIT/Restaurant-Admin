package com.efficacious.e_smartsrestaurant.Room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {MenuData.class},version = 1,exportSchema = false)
public abstract class MenuDatabase extends RoomDatabase {
    public abstract DAO dao();
}
