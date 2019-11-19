package net.kathir.mvvmtodolist.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import net.kathir.mvvmtodolist.R;
import net.kathir.mvvmtodolist.model.model.NotesEntry;

public class NoteListAdapter extends ListAdapter<NotesEntry, NoteListAdapter.NoteHolder> {

    private onItemClickListener listener;

    public NoteListAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<NotesEntry> DIFF_CALLBACK = new DiffUtil.ItemCallback<NotesEntry>() {
        @Override
        public boolean areItemsTheSame(@NonNull NotesEntry oldItem, @NonNull NotesEntry newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull NotesEntry oldItem, @NonNull NotesEntry newItem) {
            return ((oldItem.getTitle().equals(newItem.getTitle())) &&
                    (oldItem.getDescription().equals(newItem.getDescription())));
        }
    };

    public NotesEntry getNoteAt(int position) {
        return getItem(position);
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notes_list_item, viewGroup, false);
        return new NoteHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder noteHolder, int i) {
        NotesEntry currentNote = getItem(i);
        noteHolder.textViewTitle.setText(currentNote.getTitle());
        noteHolder.textViewDescription.setText(currentNote.getDescription());
    }

    class NoteHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewDescription;

        public NoteHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position));
                    }
                }
            });
        }
    }

    public interface onItemClickListener {
        void onItemClick(NotesEntry note);
    }

    public void setOnItemClickListner(onItemClickListener listener) {
        this.listener = listener;
    }

}
