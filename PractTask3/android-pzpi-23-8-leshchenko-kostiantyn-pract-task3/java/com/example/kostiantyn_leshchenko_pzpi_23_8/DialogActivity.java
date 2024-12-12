package com.example.kostiantyn_leshchenko_pzpi_23_8;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class DialogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        // AlertDialog
        Button showDialogButton = findViewById(R.id.showDialogButton);
        showDialogButton.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Діалог")
                    .setMessage("Це приклад AlertDialog.")
                    .setPositiveButton("OK", (dialog, which) ->
                            Toast.makeText(this, "Натиснуто OK", Toast.LENGTH_SHORT).show())
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .show();
        });

        // DatePickerDialog
        Button showDatePickerButton = findViewById(R.id.showDatePickerButton);
        showDatePickerButton.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, selectedYear, selectedMonth, selectedDay) ->
                            Toast.makeText(this, "Обрана дата: " + selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear,
                                    Toast.LENGTH_SHORT).show(),
                    year, month, day);
            datePickerDialog.show();
        });

        // Custom Dialog
        Button showCustomDialogButton = findViewById(R.id.showCustomDialogButton);
        showCustomDialogButton.setOnClickListener(v -> {
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.custom_dialog, null);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(dialogView)
                    .setPositiveButton("OK", (dialog, id) -> {
                        EditText input = dialogView.findViewById(R.id.customDialogInput);
                        Toast.makeText(this, "Введено: " + input.getText().toString(), Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", (dialog, id) -> dialog.dismiss());
            builder.create().show();
        });
    }
}