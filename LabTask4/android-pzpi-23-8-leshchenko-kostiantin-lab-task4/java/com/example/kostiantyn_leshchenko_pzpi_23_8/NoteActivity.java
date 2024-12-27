package com.example.kostiantyn_leshchenko_pzpi_23_8;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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

        // Вибір зображення з галереї
        noteImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 1);
        });

        // Вибір дати та часу
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

        Note note = new Note(title, description, priority, dateTime, imagePath);
        Intent intent = new Intent();
        intent.putExtra("note", note);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            imagePath = selectedImage.toString();
            noteImage.setImageURI(selectedImage);
        }
    }
}