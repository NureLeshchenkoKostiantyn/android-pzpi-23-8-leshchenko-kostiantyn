package com.example.practtask2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

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
        Log.d("Акт.1", "onCreate called");
        findViewById(R.id.button).setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, MainActivity2.class));
        });
        if (savedInstanceState != null) {
            String savedText = savedInstanceState.getString("KEY");
            EditText editText = findViewById(R.id.editTextText2);
            editText.setText(savedText);
        }
    }



    @Override
    protected void onStart(){
        super.onStart();
        Log.d("Акт.1", "onStart called");
    }
    @Override
    protected void  onResume(){
        super.onResume();
        Log.d("Акт.1", "onResume called");
    }
    @Override
    protected void onPause(){
        super.onPause();
        Log.d("Акт.1", "onPause called");
    }
    @Override
    protected void  onStop(){
        super.onStop();
        Log.d("Акт.1", "onStop called");
    }
    @Override
    protected void  onDestroy(){
        super.onDestroy();
        Log.d("Акт.1", "onDestroy called");
    }
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        EditText editText = findViewById(R.id.editTextText2);
        outState.putString("KEY", editText.getText().toString());
        Log.d("Акт.1", "onSaveInstanceState called");
    }
    @Override
    protected void onRestoreInstanceState(Bundle SavedInstanceState){
        super.onRestoreInstanceState(SavedInstanceState);
        Log.d("Акт.1", "onRestoreInstanceState");
    }

}
