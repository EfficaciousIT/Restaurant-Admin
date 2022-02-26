package com.efficacious.e_smartsrestaurant.Room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DAO_TableBook {
    //Table book
    @Insert
    void tableBookData(TableBook tableBook);

    @Query("SELECT * FROM TableBook")
    List<TableBook> getTableData();

    @Query("DELETE FROM TableBook")
    void deleteAllData();
}
