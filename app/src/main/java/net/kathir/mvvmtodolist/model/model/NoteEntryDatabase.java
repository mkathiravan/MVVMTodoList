package net.kathir.mvvmtodolist.model.model;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {NotesEntry.class}, version = 1, exportSchema = false)

public abstract class NoteEntryDatabase extends RoomDatabase {

    //to turn this class to singleton
    private static NoteEntryDatabase instance;

    public abstract NotesEntryDao noteDao();

    public static synchronized NoteEntryDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    NoteEntryDatabase.class, "note_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback=new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();

        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void,Void,Void> {

        private NotesEntryDao noteDao;

        private PopulateDbAsyncTask(NoteEntryDatabase db) {
            this.noteDao = db.noteDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.insert(new NotesEntry("Kathiravan","Android Developer"));

            return null;
        }
    }
}
