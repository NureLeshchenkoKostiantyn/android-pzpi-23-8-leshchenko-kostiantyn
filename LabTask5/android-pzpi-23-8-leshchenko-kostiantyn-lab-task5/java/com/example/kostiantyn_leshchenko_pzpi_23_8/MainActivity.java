package com.example.kostiantyn_leshchenko_pzpi_23_8;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.Manifest;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_ADD_NOTE = 1;
    public static final int REQUEST_CODE_EDIT_NOTE = 2;
    public static final int REQUEST_CODE_PICK_IMAGE = 3;
    private static final int REQUEST_CODE_PERMISSION = 100;

    private RecyclerView recyclerView;
    private NoteAdapter adapter;
    private DatabaseHelper dbHelper;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Применение темы перед setContentView
        sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        applyTheme();
        applyFontSize();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Запрос разрешений
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
        }

        // Инициализация Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Инициализация RecyclerView
        recyclerView = findViewById(R.id.recycler_view);
        if (recyclerView == null) {
            Log.e("MainActivity", "RecyclerView not found in layout");
            return;
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Инициализация базы данных
        dbHelper = new DatabaseHelper(this);

        // Загрузка заметок из базы данных
        List<Note> notes = dbHelper.getAllNotes();
        adapter = new NoteAdapter(notes, this);
        recyclerView.setAdapter(adapter);

        // Кнопка для добавления заметки
        findViewById(R.id.add_note).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NoteActivity.class);
            startActivityForResult(intent, REQUEST_CODE_ADD_NOTE);
        });

        // Кнопка для переключения темы
        Button toggleThemeButton = findViewById(R.id.toggle_theme_button);
        toggleThemeButton.setOnClickListener(v -> toggleTheme());

        // Кнопка для перехода к настройкам
        Button settingsButton = findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        // Регистрация контекстного меню для RecyclerView
        registerForContextMenu(recyclerView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Обновление темы и размера шрифта при возвращении на экран
        applyTheme();
        applyFontSize();
    }

    private void applyTheme() {
        boolean isDarkTheme = sharedPreferences.getBoolean("isDarkTheme", false);
        if (isDarkTheme) {
            setTheme(R.style.AppTheme_Dark);
        } else {
            setTheme(R.style.AppTheme_Light);
        }
    }

    private void toggleTheme() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        boolean isDarkTheme = sharedPreferences.getBoolean("isDarkTheme", false);
        editor.putBoolean("isDarkTheme", !isDarkTheme); // Инвертируем текущую тему
        editor.apply();

        recreate(); // Перезапускаем активность для применения новой темы
    }

    private void applyFontSize() {
        int fontSize = sharedPreferences.getInt(Settings.PREF_FONT_SIZE, (int) Settings.DEFAULT_FONT_SIZE);

        // Пример применения размера шрифта к TextView
        TextView titleTextView = findViewById(R.id.title_text_view);
        if (titleTextView != null) {
            titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
        }

        // Обновление размера шрифта в адаптере
        if (adapter != null) {
            adapter.setFontSize(fontSize);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case REQUEST_CODE_ADD_NOTE: // Обработка добавления новой заметки
                case REQUEST_CODE_EDIT_NOTE: // Обработка редактирования заметки
                    Note note = (Note) data.getSerializableExtra("note");
                    if (note == null) {
                        Log.e("MainActivity", "Note is null in onActivityResult");
                        return;
                    }

                    if (requestCode == REQUEST_CODE_ADD_NOTE) {
                        // Добавление заметки в базу данных
                        dbHelper.addNote(note);
                    } else if (requestCode == REQUEST_CODE_EDIT_NOTE) {
                        // Обновление заметки в базе данных
                        dbHelper.updateNote(note);
                    }

                    // Обновление списка заметок в адаптере
                    adapter.updateNotes(dbHelper.getAllNotes());
                    break;

                case REQUEST_CODE_PICK_IMAGE: // Обработка выбора изображения
                    Uri selectedImageUri = data.getData();
                    if (selectedImageUri != null) {
                        // Установка изображения в ImageView
                        ImageView noteImage = findViewById(R.id.note_image);
                        noteImage.setImageURI(selectedImageUri);
                    }
                    break;
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position = adapter.getContextMenuPosition();
        if (position == -1) {
            Log.e("MainActivity", "Invalid position in context menu");
            return false;
        }

        Note note = adapter.getNoteAt(position);
        if (note == null) {
            Log.e("MainActivity", "Note is null at position: " + position);
            return false;
        }

        if (item.getItemId() == R.id.edit_note) {
            Intent intent = new Intent(this, NoteActivity.class);
            intent.putExtra("note", note);
            startActivityForResult(intent, REQUEST_CODE_EDIT_NOTE);
            return true;
        } else if (item.getItemId() == R.id.delete_note) {
            new AlertDialog.Builder(this)
                    .setTitle("Удалить заметку")
                    .setMessage("Вы уверены, что хотите удалить эту заметку?")
                    .setPositiveButton("Да", (dialog, which) -> {
                        dbHelper.deleteNote(note.getId()); // Удаление заметки
                        adapter.updateNotes(dbHelper.getAllNotes()); // Обновление списка
                    })
                    .setNegativeButton("Нет", null)
                    .show();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Поиск
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterNotesByText(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterNotesByText(newText);
                return true;
            }
        });

        // Фильтрация по важности
        MenuItem filterItem = menu.findItem(R.id.action_filter);
        filterItem.setOnMenuItemClickListener(item -> {
            showFilterDialog();
            return true;
        });

        return true;
    }

    private void filterNotesByText(String query) {
        List<Note> filteredNotes = dbHelper.searchNotes(query); // Фильтрация заметок
        adapter.updateNotes(filteredNotes); // Обновление списка
    }

    private void showFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Фильтрация по важности");
        builder.setItems(new String[]{"Низкая", "Средняя", "Высокая"}, (dialog, which) -> {
            List<Note> filteredNotes = dbHelper.filterNotesByPriority(which + 1); // Фильтрация по важности
            adapter.updateNotes(filteredNotes); // Обновление списка
        });
        builder.show();
    }
}