package com.codeitsolo.notesapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.codeitsolo.notesapp.R;
import com.codeitsolo.notesapp.models.Notes;
import com.codeitsolo.notesapp.utils.ColorManager;
import com.codeitsolo.notesapp.utils.NotesClickListener;

import java.util.List;

public class NotesListAdapter extends RecyclerView.Adapter<NotesListAdapter.NotesViewHolder> {

    private List<Notes> notes;
    private final Context context;
    private final NotesClickListener listener;

    public NotesListAdapter(Context context, List<Notes> notes, NotesClickListener listener) {
        this.notes = notes;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notes_item, parent, false);
        return new NotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        Notes note = notes.get(position);

        holder.tvNotesTitle.setText(note.getTitle());
        holder.tvNotesTitle.setSelected(true);

        holder.tvNotesDescription.setText(note.getDescription());
        holder.tvNotesCreatedAt.setText(String.format("Created: %s", note.getCreatedAt()));
        holder.tvNotesCreatedAt.setSelected(true);

        holder.tvNotesLastModifiedAt.setText(note.getLastModifiedAt().equals("") ? "" :
                String.format("Last Modified: %s", note.getLastModifiedAt())
        );
        holder.tvNotesLastModifiedAt.setSelected(true);

        if (note.isPinned()) {
            holder.ivNotesPin.setImageResource(R.drawable.ic_star_pin);
        } else {
            holder.ivNotesPin.setImageResource(0);
        }

        holder.cvNotesItem.setCardBackgroundColor(ColorManager.getInstance(context).getRandomColor());

        holder.cvNotesItem.setOnClickListener(v -> listener.onClick(note));
        holder.cvNotesItem.setOnLongClickListener(v -> {
            listener.onLongClick(note, holder.cvNotesItem);
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    static class NotesViewHolder extends RecyclerView.ViewHolder {

        TextView tvNotesTitle, tvNotesDescription, tvNotesLastModifiedAt, tvNotesCreatedAt;
        ImageView ivNotesPin;
        CardView cvNotesItem;

        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNotesTitle = itemView.findViewById(R.id.tv_notes_title);
            tvNotesDescription = itemView.findViewById(R.id.tv_notes_description);
            tvNotesLastModifiedAt = itemView.findViewById(R.id.tv_notes_last_modified_at);
            tvNotesCreatedAt = itemView.findViewById(R.id.tv_notes_created_at);
            ivNotesPin = itemView.findViewById(R.id.iv_notes_pin);
            cvNotesItem = itemView.findViewById(R.id.cv_notes_item);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setfilteredNotes(List<Notes> filteredNotes) {
        notes = filteredNotes;
        notifyDataSetChanged();
    }
}
