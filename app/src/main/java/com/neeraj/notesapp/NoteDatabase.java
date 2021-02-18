package com.neeraj.notesapp;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.neeraj.notesapp.NoteDAO;

@Database(entities = {com.neeraj.notesapp.Note.class}, version = 10, exportSchema = false)
public abstract class NoteDatabase extends RoomDatabase {
    private static NoteDatabase instance;

    public abstract NoteDAO noteDAO();

    public static synchronized com.neeraj.notesapp.NoteDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    NoteDatabase.class, "note_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomcallback)
                    .build();
        }
        return instance;
    }

    private static Callback roomcallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private NoteDAO noteDAO;

        private PopulateDbAsyncTask(com.neeraj.notesapp.NoteDatabase db) {
            noteDAO = db.noteDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {


            return null;
        }
    }
}
