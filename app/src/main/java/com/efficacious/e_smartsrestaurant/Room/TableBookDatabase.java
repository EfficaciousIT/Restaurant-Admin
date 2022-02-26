package com.efficacious.e_smartsrestaurant.Room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {TableBook.class},version = 1,exportSchema = false)
public abstract class TableBookDatabase extends RoomDatabase {
    public abstract DAO_TableBook dao_tableBook();
}
