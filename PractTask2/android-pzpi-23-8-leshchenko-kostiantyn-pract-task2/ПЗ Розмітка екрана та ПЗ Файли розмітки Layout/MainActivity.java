package com.example.practtask2_1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.textView3);
        EditText editText = findViewById(R.id.editTextText);
        Button buttonChange = findViewById(R.id.button10);
        Button buttonShow = findViewById(R.id.button11);
        buttonChange.setOnClickListener(view -> {
            String newText = editText.getText().toString();
            textView.setText(newText.isEmpty() ? "Порожній" : newText);
        });
        buttonShow.setOnClickListener(view -> {
            Toast.makeText(MainActivity.this, "Повідомлення виведено!", Toast.LENGTH_SHORT).show();
        });
    }

}
