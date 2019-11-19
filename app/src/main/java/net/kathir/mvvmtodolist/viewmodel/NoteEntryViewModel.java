package net.kathir.mvvmtodolist.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import net.kathir.mvvmtodolist.model.model.NotesEntry;
import net.kathir.mvvmtodolist.model.repository.NotesEntryRepository;

import java.util.List;

public class NoteEntryViewModel extends AndroidViewModel {

    private NotesEntryRepository notesEntryRepository;
    private LiveData<List<NotesEntry>> allLocalNotes;


    public NoteEntryViewModel(@NonNull Application application) {
        super(application);
        notesEntryRepository = new NotesEntryRepository(application);
        allLocalNotes=notesEntryRepository.getLocalAllNotes();
    }

    public void insert(NotesEntry note){
        notesEntryRepository.insert(note);
    }

    public void update(NotesEntry note){
        notesEntryRepository.update(note);
    }

    public void delete(NotesEntry note){
        notesEntryRepository.delete(note);
    }

    public void deleteAllNotes() {
        notesEntryRepository.deleteAllNotes();
    }

    public LiveData<List<NotesEntry>> getAllLocalNotes() {
        return allLocalNotes;
    }


}
