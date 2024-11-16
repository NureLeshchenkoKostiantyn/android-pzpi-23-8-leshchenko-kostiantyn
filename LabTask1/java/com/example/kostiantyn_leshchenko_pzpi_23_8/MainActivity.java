package com.example.labtask1;

import android.os.Bundle;
import android.util.Log;

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
        Log.d("Життевий цикл", "onCreate");
    }
    @Override
    protected void onStart(){
        super.onStart();
        Log.d("Життевий цикл", "onStart");
    }
    @Override
    protected void onResume(){
        super.onResume();
        Log.d("Життевий цикл", "onResume");
    }
    @Override
    protected void onPause(){
        super.onPause();
        Log.d("Життевий цикл", "onPause");
    }
    @Override
    protected void onStop(){
        super.onStop();
        Log.d("Життевий цикл", "onStop");
    }
    @Override
    protected void onRestart(){
        super.onRestart();
        Log.d("Життевий цикл", "onRestart");
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d("Життевий цикл", "onDestroy");
    }
}