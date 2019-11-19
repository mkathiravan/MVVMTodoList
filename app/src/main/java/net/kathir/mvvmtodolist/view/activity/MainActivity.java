package net.kathir.mvvmtodolist.view.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.kathir.mvvmtodolist.R;
import net.kathir.mvvmtodolist.model.model.NotesEntry;
import net.kathir.mvvmtodolist.view.NoteListAdapter;
import net.kathir.mvvmtodolist.viewmodel.NoteEntryViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int ADD_NOTE_REQUEST = 101;
    public static final int EDIT_NOTE_REQUEST = 102;

    private NoteEntryViewModel noteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton buttonAddNote = findViewById(R.id.button_add_note);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UpdateNoteEntryActivity.class);
                startActivityForResult(intent, ADD_NOTE_REQUEST);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final NoteListAdapter adapter = new NoteListAdapter();
        recyclerView.setAdapter(adapter);

        //To remove the item using swipe logic
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                noteViewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Note deleted.", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);


        noteViewModel = ViewModelProviders.of(this).get(NoteEntryViewModel.class);

        noteViewModel.getAllLocalNotes().observe(this, new Observer<List<NotesEntry>>() {
            @Override
            public void onChanged(@Nullable List<NotesEntry> notes) {
                adapter.submitList(notes);
            }
        });


        //edit notes on item click
        adapter.setOnItemClickListner(new NoteListAdapter.onItemClickListener() {
            @Override
            public void onItemClick(NotesEntry note) {
                Intent intent = new Intent(MainActivity.this, UpdateNoteEntryActivity.class);
                intent.putExtra(UpdateNoteEntryActivity.EXTRA_ID, note.getId());
                intent.putExtra(UpdateNoteEntryActivity.EXTRA_TITLE, note.getTitle());
                intent.putExtra(UpdateNoteEntryActivity.EXTRA_DESCRIPTION, note.getDescription());
                startActivityForResult(intent, EDIT_NOTE_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {
                String title = data.getStringExtra(UpdateNoteEntryActivity.EXTRA_TITLE);
                String description = data.getStringExtra(UpdateNoteEntryActivity.EXTRA_DESCRIPTION);

                NotesEntry note = new NotesEntry(title, description);
                noteViewModel.insert(note);
                Toast.makeText(this, "Note Saved.", Toast.LENGTH_SHORT).show();
            } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {
                int id = data.getIntExtra(UpdateNoteEntryActivity.EXTRA_ID, -1);

                if (id == -1) {
                    Toast.makeText(this, "Note can't be Updated.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String title = data.getStringExtra(UpdateNoteEntryActivity.EXTRA_TITLE);
                String description = data.getStringExtra(UpdateNoteEntryActivity.EXTRA_DESCRIPTION);

                NotesEntry note = new NotesEntry(title, description);
                note.setId(id);
                noteViewModel.update(note);

                Toast.makeText(this, "Note Updated.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Note didn't Saved.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_notes:
                noteViewModel.deleteAllNotes();
                Toast.makeText(MainActivity.this, "All notes deleted.", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}