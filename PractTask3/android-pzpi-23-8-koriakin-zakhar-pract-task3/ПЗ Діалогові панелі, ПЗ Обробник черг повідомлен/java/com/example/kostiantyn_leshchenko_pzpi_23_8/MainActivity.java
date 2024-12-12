package com.example.kostiantyn_leshchenko_pzpi_23_8;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

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
        findViewById(R.id.openDialogActivityButton).setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, DialogActivity.class)));
        findViewById(R.id.openHandlerActivityButton).setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, HandlerActivity.class)));



    }
}