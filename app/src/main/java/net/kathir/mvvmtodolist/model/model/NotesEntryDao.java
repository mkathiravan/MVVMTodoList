package net.kathir.mvvmtodolist.model.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NotesEntryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(NotesEntry note);

    @Update
    void update(NotesEntry note);

    @Delete
    void delete(NotesEntry note);

    @Query("DELETE FROM notes_table")
    void deleteAllNotes();

    @Query("SELECT * FROM notes_table")
    LiveData<List<NotesEntry>> getAllNotes();

}
