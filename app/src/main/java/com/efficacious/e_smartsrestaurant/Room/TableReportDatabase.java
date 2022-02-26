package com.efficacious.e_smartsrestaurant.Room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {TableReport.class},version = 1,exportSchema = false)
public abstract class TableReportDatabase extends RoomDatabase {
    public abstract DAO_TableReport dao_tableReport();
}
