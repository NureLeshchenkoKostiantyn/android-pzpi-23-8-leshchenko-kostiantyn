package com.example.practtask2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        Button finishButton = findViewById(R.id.button3);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    @Override
    protected void onStart(){
        super.onStart();
        Log.d("Акт.2", "onStart called");
    }
    @Override
    protected void  onResume(){
        super.onResume();
        Log.d("Акт.2", "onResume called");
    }
    @Override
    protected void onPause(){
        super.onPause();
        Log.d("Акт.2", "onPause called");
    }
    @Override
    protected void  onStop(){
        super.onStop();
        Log.d("Акт.2", "onStop called");
    }
    @Override
    protected void  onDestroy(){
        super.onDestroy();
        Log.d("Акт.2", "onDestroy called");
    }
}
