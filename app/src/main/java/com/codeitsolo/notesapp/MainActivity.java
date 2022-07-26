package com.codeitsolo.notesapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.codeitsolo.notesapp.adapters.NotesListAdapter;
import com.codeitsolo.notesapp.database.RoomDB;
import com.codeitsolo.notesapp.models.Notes;
import com.codeitsolo.notesapp.utils.NotesClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int ADD_NOTE_REQUEST_CODE = 101;
    public static final int EDIT_NOTE_REQUEST_CODE = 102;

    private RecyclerView recyclerView;
    private NotesListAdapter adapter;

    private List<Notes> notes = new ArrayList<>();
    private RoomDB database;
    private FloatingActionButton fabAdd;
    private SearchView svSearchNote;

    private final NotesClickListener listener = new NotesClickListener() {
        @Override
        public void onClick(Notes notes) {
            Intent intent = new Intent(MainActivity.this, NoteTakerActivity.class);
            intent.putExtra("note_to_edit", notes);
            startActivityForResult(intent, EDIT_NOTE_REQUEST_CODE);
        }

        @Override
        public void onLongClick(Notes notes, CardView cardView) {
            showNotesPopup(notes, cardView);
        }
    };

    @SuppressLint({"NonConstantResourceId", "NotifyDataSetChanged"})
    private void showNotesPopup(Notes selectedNote, CardView cardView) {
        PopupMenu notesPopupMenu = new PopupMenu(this, cardView);
        notesPopupMenu.inflate(R.menu.notes_popup_menu);

        if (selectedNote.isPinned()) {
            Menu popupMenu = notesPopupMenu.getMenu();
            popupMenu.getItem(0).setTitle("Unpin");
        }

        notesPopupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.pin:
                    database.mainDAO().pin(selectedNote.getId(), !selectedNote.isPinned());
                    notes.clear();
                    notes.addAll(database.mainDAO().getAll());
                    adapter.notifyDataSetChanged();
                    return true;

                case R.id.delete:
                    database.mainDAO().delete(selectedNote);
                    notes.clear();
                    notes.addAll(database.mainDAO().getAll());
                    adapter.notifyDataSetChanged();
                    return true;
            }
            return false;
        });

        notesPopupMenu.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fabAdd = findViewById(R.id.fab_add);
        svSearchNote = findViewById(R.id.sv_search_note);
        recyclerView = findViewById(R.id.rv_home);

        database = RoomDB.getInstance(this);
        notes = database.mainDAO().getAll();

        updateRecyclerView(notes);
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NoteTakerActivity.class);
            startActivityForResult(intent, ADD_NOTE_REQUEST_CODE);
        });

        svSearchNote.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterNotes(newText);
                return true;
            }
        });
    }

    private void filterNotes(String text) {
        List<Notes> filteredNotes = new ArrayList<>();
        for (Notes note : notes) {
            if (note.getTitle().toLowerCase().contains(text.toLowerCase())
                    || note.getDescription().toLowerCase().contains(text.toLowerCase())) {
                filteredNotes.add(note);
            }
        }

        if (filteredNotes.isEmpty()) {
            filteredNotes = database.mainDAO().getAll();
        }

        adapter.setfilteredNotes(filteredNotes);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == ADD_NOTE_REQUEST_CODE) {
                if (data != null) {
                    Notes newNote = (Notes) data.getSerializableExtra("note");
                    database.mainDAO().insert(newNote);
                    notes.clear();
                    notes.addAll(database.mainDAO().getAll());
                    adapter.notifyDataSetChanged();
                }
            } else if (requestCode == EDIT_NOTE_REQUEST_CODE) {
                if (data != null) {
                    Notes modifiedNote = (Notes) data.getSerializableExtra("note");
                    database.mainDAO().update(modifiedNote);

                    notes.clear();
                    notes.addAll(database.mainDAO().getAll());
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void updateRecyclerView(List<Notes> notes) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        adapter = new NotesListAdapter(this, notes, listener);
        recyclerView.setAdapter(adapter);
    }
}