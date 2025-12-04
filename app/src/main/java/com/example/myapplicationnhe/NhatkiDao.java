package com.example.myapplicationnhe;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NhatkiDao {

    @Insert
    void insert(Nhatki nhatki);

    @Update
    void update(Nhatki nhatki);

    @Delete
    void delete(Nhatki nhatki);

    @Query("SELECT * FROM Nhat_ki ORDER BY id DESC")
    List<Nhatki> getAll();

    @Query("SELECT * FROM Nhat_ki WHERE id = :id")
    Nhatki getById(int id);

    @Query("DELETE FROM Nhat_ki")
    void deleteAll();
}