package com.example.kostiantyn_leshchenko_pzpi_23_8;

import static com.example.kostiantyn_leshchenko_pzpi_23_8.MainActivity.REQUEST_CODE_PICK_IMAGE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import com.example.kostiantyn_leshchenko_pzpi_23_8.DatabaseHelper;
public class NoteActivity extends AppCompatActivity {
    private EditText titleInput, descriptionInput;
    private RadioGroup priorityGroup;
    private Button saveButton, cancelButton, selectDateTimeButton;
    private ImageView noteImage;
    private String imagePath = "";
    private String selectedDateTime = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        titleInput = findViewById(R.id.title_input);
        descriptionInput = findViewById(R.id.description_input);
        priorityGroup = findViewById(R.id.priority_group);
        saveButton = findViewById(R.id.save_button);
        cancelButton = findViewById(R.id.cancel_button);
        selectDateTimeButton = findViewById(R.id.select_date_time);
        noteImage = findViewById(R.id.note_image);

        // Получаем объект Note из Intent
        Note note = (Note) getIntent().getSerializableExtra("note");
        if (note != null) {
            // Заполняем поля данными из Note
            titleInput.setText(note.getTitle());
            descriptionInput.setText(note.getDescription());
            switch (note.getPriority()) {
                case 1: priorityGroup.check(R.id.priority_low); break;
                case 2: priorityGroup.check(R.id.priority_medium); break;
                case 3: priorityGroup.check(R.id.priority_high); break;
            }
            selectedDateTime = note.getDateTime();
            imagePath = note.getImagePath();
        }

        // Выбор изображения из галереи
        noteImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, MainActivity.REQUEST_CODE_PICK_IMAGE);
        });

        // Выбор даты и времени
        selectDateTimeButton.setOnClickListener(v -> showDateTimePicker());

        saveButton.setOnClickListener(v -> saveNote());
        cancelButton.setOnClickListener(v -> finish());
    }

    private void showDateTimePicker() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view1, hourOfDay, minute) -> {
                calendar.set(year, month, dayOfMonth, hourOfDay, minute);
                selectedDateTime = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(calendar.getTime());
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
            timePickerDialog.show();
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void saveNote() {
        String title = titleInput.getText().toString();
        String description = descriptionInput.getText().toString();
        int priority = priorityGroup.getCheckedRadioButtonId() == R.id.priority_high ? 3 :
                priorityGroup.getCheckedRadioButtonId() == R.id.priority_medium ? 2 : 1;
        String dateTime = selectedDateTime.isEmpty() ?
                new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date()) :
                selectedDateTime;

        // Отримуємо початковий об'єкт Note з Intent
        Note originalNote = (Note) getIntent().getSerializableExtra("note");

        // Створюємо новий об'єкт Note з оновленими даними
        Note note = new Note(title, description, priority, dateTime, imagePath);

        // Якщо це редагування нотатки, зберігаємо той самий id
        if (originalNote != null) {
            note.setId(originalNote.getId());
        }

        // Повертаємо результат у MainActivity
        Intent intent = new Intent();
        intent.putExtra("note", note);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MainActivity.REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                // Установка изображения в ImageView
                noteImage.setImageURI(selectedImageUri);

                // Сохранение Uri изображения
                imagePath = selectedImageUri.toString();
            }
        }
    }
}
