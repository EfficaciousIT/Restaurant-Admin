package com.efficacious.e_smartsrestaurant.Room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DAO_TableReport {
    //Table Report
    @Insert
    void tableReportData(TableReport tableReport);

    @Query("SELECT * FROM TableReport")
    List<TableReport> getTableReportData();

    @Query("SELECT * FROM TableReport WHERE createdDate = :date")
    List<TableReport> getDateWiseReport(String date);

    @Query("DELETE FROM TableReport")
    void deleteAllData();
}
