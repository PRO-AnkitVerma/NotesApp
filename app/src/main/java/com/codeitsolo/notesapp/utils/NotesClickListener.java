package com.codeitsolo.notesapp.utils;

import androidx.cardview.widget.CardView;

import com.codeitsolo.notesapp.models.Notes;

public interface NotesClickListener {
    void onClick(Notes notes);

    void onLongClick(Notes notes, CardView cardView);
}
