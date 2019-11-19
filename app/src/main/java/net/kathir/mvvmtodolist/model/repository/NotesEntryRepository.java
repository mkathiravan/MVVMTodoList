package net.kathir.mvvmtodolist.model.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import net.kathir.mvvmtodolist.model.model.NoteEntryDatabase;
import net.kathir.mvvmtodolist.model.model.NotesEntry;
import net.kathir.mvvmtodolist.model.model.NotesEntryDao;

import java.util.List;

public class NotesEntryRepository {

    private NotesEntryDao noteDao;
    private LiveData<List<NotesEntry>> localAllNotes;
    private MutableLiveData<List<NotesEntry>> notes;

    public NotesEntryRepository(Application application) {
        NoteEntryDatabase database = NoteEntryDatabase.getInstance(application);
        noteDao = database.noteDao();
        notes = new MutableLiveData<>();
        localAllNotes = noteDao.getAllNotes();
    }

    public void insert(NotesEntry note) {
        new InsertNoteAsyncTask(noteDao).execute(note);
    }

    public void update(NotesEntry note) {
        new UpdateNoteAsyncTask(noteDao).execute(note);
    }

    public void delete(NotesEntry note) {
        new DeleteNoteAsyncTask(noteDao).execute(note);
    }

    public void deleteAllNotes() {
        new DeleteAllNotesAsyncTask(noteDao).execute();
    }

    public LiveData<List<NotesEntry>> getLocalAllNotes() {
        return localAllNotes;
    }

    private static class InsertNoteAsyncTask extends AsyncTask<NotesEntry, Void, Void> {

        private NotesEntryDao noteDao;

        private InsertNoteAsyncTask(NotesEntryDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(NotesEntry... notes) {
            long id = noteDao.insert(notes[0]);
            notes[0].setId((int) id);
            return null;
        }
    }

    private static class UpdateNoteAsyncTask extends AsyncTask<NotesEntry, Void, Void> {

        private NotesEntryDao noteDao;

        private UpdateNoteAsyncTask(NotesEntryDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(NotesEntry... notes) {
            noteDao.update(notes[0]);
            return null;
        }
    }

    private static class DeleteNoteAsyncTask extends AsyncTask<NotesEntry, Void, Void> {

        private NotesEntryDao noteDao;


        private DeleteNoteAsyncTask(NotesEntryDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(NotesEntry... notes) {
            noteDao.delete(notes[0]);
            return null;
        }
    }

    private static class DeleteAllNotesAsyncTask extends AsyncTask<Void, Void, Void> {

        private NotesEntryDao noteDao;

        private DeleteAllNotesAsyncTask(NotesEntryDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.deleteAllNotes();
            return null;
        }
    }
}

