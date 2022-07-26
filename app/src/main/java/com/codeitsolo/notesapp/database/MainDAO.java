package com.codeitsolo.notesapp.database;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.codeitsolo.notesapp.models.Notes;

import java.util.List;

@Dao
public interface MainDAO {

    @Insert(onConflict = REPLACE)
    void insert(Notes notes);

    @Query("SELECT * FROM notes ORDER BY pinned DESC, id DESC")
    List<Notes> getAll();

    @Update
    void update(Notes notes);

    @Delete
    void delete(Notes notes);

    @Query("UPDATE notes SET pinned = :pinned WHERE id = :id")
    void pin(int id, boolean pinned);
}
