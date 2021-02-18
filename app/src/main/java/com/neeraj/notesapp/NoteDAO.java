package com.neeraj.notesapp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NoteDAO {

    @Insert
    void insert(Note note);

    @Update
    void Update(Note note);

    @Delete
    void Delete(Note note);

    @Query("DELETE FROM profile")
    void deleteAllNotes();

    @Query("SELECT * FROM profile")
    LiveData<List<Note>> getAllNotes();


}
