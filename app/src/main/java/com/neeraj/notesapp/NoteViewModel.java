package com.neeraj.notesapp;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {
    private NoteRepository repository;
    private LiveData<List<Note>> allNotes;
    private String TAG = this.getClass().getSimpleName();

    public NoteViewModel(Application application) {
        super(application);
        repository = new NoteRepository(application);
        allNotes = repository.getAllNotes();
    }

    public void insert(Note note) {
        repository.insert(note);
    }

    public void update(Note note) {
        repository.update(note);
    }

    public void delete(Note note) {
        repository.delete(note);
    }

    public void deleteAllNotes() {
        repository.deleteallnotes();
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.e(TAG, "ViewModel destroyed");
    }
}
