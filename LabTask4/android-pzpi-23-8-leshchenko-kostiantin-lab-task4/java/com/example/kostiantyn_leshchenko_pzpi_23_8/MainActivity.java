package com.example.kostiantyn_leshchenko_pzpi_23_8;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.widget.Toolbar; // Правильний імпорт

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private NoteAdapter adapter;
    private List<Note> notes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ініціалізація Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); // Встановлення Toolbar як ActionBar
        // Ініціалізація RecyclerView
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Ініціалізація адаптера
        adapter = new NoteAdapter(notes, this);
        recyclerView.setAdapter(adapter);

        // Додавання нотатки через кнопку
        findViewById(R.id.add_note).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NoteActivity.class);
            startActivityForResult(intent, 1);
        });

        // Реєстрація контекстного меню для RecyclerView
        registerForContextMenu(recyclerView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Отримання нової нотатки з NoteActivity
            Note note = (Note) data.getSerializableExtra("note");
            notes.add(note);
            adapter.notifyDataSetChanged(); // Оновлення списку
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        // Завантаження контекстного меню з XML
        getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // Отримання позиції елемента з адаптера
        int position = adapter.getContextMenuPosition();

        if (item.getItemId() == R.id.edit_note) {
            // Редагування нотатки
            Intent intent = new Intent(this, NoteActivity.class);
            intent.putExtra("note", notes.get(position));
            startActivityForResult(intent, 2);
            return true;
        } else if (item.getItemId() == R.id.delete_note) {
            // Видалення нотатки
            new AlertDialog.Builder(this)
                    .setTitle("Видалити нотатку")
                    .setMessage("Ви впевнені, що хочете видалити цю нотатку?")
                    .setPositiveButton("Так", (dialog, which) -> {
                        notes.remove(position);
                        adapter.notifyDataSetChanged(); // Оновлення списку
                    })
                    .setNegativeButton("Ні", null)
                    .show();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Завантаження меню з XML
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Пошук
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterNotesByText(query); // Фільтрація за текстом
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterNotesByText(newText); // Фільтрація за текстом
                return true;
            }
        });

        // Фільтрація за важливістю
        MenuItem filterItem = menu.findItem(R.id.action_filter);
        filterItem.setOnMenuItemClickListener(item -> {
            showFilterDialog(); // Відображення діалогу фільтрації
            return true;
        });

        return true;
    }

    // Фільтрація нотаток за текстом
    private void filterNotesByText(String query) {
        List<Note> filteredNotes = new ArrayList<>();
        for (Note note : notes) {
            if (note.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                    note.getDescription().toLowerCase().contains(query.toLowerCase())) {
                filteredNotes.add(note);
            }
        }
        adapter = new NoteAdapter(filteredNotes, this);
        recyclerView.setAdapter(adapter);
    }

    // Діалог фільтрації за важливістю
    private void showFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Фільтрація за важливістю");
        builder.setItems(new String[]{"Низька", "Середня", "Висока"}, (dialog, which) -> {
            List<Note> filteredNotes = new ArrayList<>();
            for (Note note : notes) {
                if (note.getPriority() == which + 1) {
                    filteredNotes.add(note);
                }
            }
            adapter = new NoteAdapter(filteredNotes, this);
            recyclerView.setAdapter(adapter);
        });
        builder.show();
    }
}