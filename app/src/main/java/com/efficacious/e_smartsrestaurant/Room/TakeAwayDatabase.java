package com.efficacious.e_smartsrestaurant.Room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {TakeAwayOrder.class},version = 1,exportSchema = false)
public abstract class TakeAwayDatabase extends RoomDatabase {
    public abstract DAO_TakeAway dao_takeAway();
}
