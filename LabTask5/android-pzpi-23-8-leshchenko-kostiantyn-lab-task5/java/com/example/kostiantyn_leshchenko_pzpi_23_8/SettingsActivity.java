package com.example.kostiantyn_leshchenko_pzpi_23_8;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    private SeekBar fontSizeSeekBar;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE);

        fontSizeSeekBar = findViewById(R.id.font_size_seekbar);
        int savedFontSize = sharedPreferences.getInt(Settings.PREF_FONT_SIZE, (int) Settings.DEFAULT_FONT_SIZE);
        fontSizeSeekBar.setProgress(savedFontSize);

        findViewById(R.id.save_button).setOnClickListener(v -> saveFontSize());
    }

    private void saveFontSize() {
        int fontSize = fontSizeSeekBar.getProgress();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(Settings.PREF_FONT_SIZE, fontSize);
        editor.apply();
        finish(); // Закрываем активность после сохранения
    }
}