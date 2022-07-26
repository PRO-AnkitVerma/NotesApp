package com.codeitsolo.notesapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.codeitsolo.notesapp.models.Notes;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class NoteTakerActivity extends AppCompatActivity {

    TextInputLayout tilTitle, tilDescription;

    EditText etTitle, etDescription;
    Notes notes;
    boolean isNoteToEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_taker);

        tilTitle = findViewById(R.id.til_title);
        tilDescription = findViewById(R.id.til_description);

        etTitle = tilTitle.getEditText();
        etDescription = tilDescription.getEditText();

        try {
            notes = (Notes) getIntent().getSerializableExtra("note_to_edit");
            etTitle.setText(notes.getTitle());
            etDescription.setText(notes.getDescription());
            isNoteToEdit = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        Objects.requireNonNull(getSupportActionBar())
                .setTitle(isNoteToEdit ? "Edit "+notes.getTitle() : "Add new notes");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_taker_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.save) {
            saveModifiedNote();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveModifiedNote() {
        String title = etTitle.getText().toString();
        String description = etDescription.getText().toString();

        if (title.isEmpty()) {
            Toast.makeText(this, "Please add title of the notes!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (description.isEmpty()) {
            Toast.makeText(this, "Please add some notes!", Toast.LENGTH_SHORT).show();
            return;
        }

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, d, MMM, yyyy HH:mm a");
        Date date = new Date();

        if (isNoteToEdit) {
            notes.setLastModifiedAt(formatter.format(date));
        }
        else {
            notes = new Notes();
            notes.setCreatedAt(formatter.format(date));
        }

        notes.setTitle(title);
        notes.setDescription(description);

        Intent intent = new Intent();
        intent.putExtra("note", notes);
        setResult(RESULT_OK, intent);
        finish();
    }
}