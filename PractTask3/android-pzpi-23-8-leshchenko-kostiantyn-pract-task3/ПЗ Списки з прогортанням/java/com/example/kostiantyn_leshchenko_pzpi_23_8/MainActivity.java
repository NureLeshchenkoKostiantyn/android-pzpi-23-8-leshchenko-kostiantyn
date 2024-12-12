package com.example.kostiantyn_leshchenko_pzpi_23_8;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private List<String> dataset;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ініціалізація RecyclerView
        recyclerView = findViewById(R.id.recyclerView);

        // Створення даних
        dataset = new ArrayList<>();
        for (int i = 1; i <= 50; i++) { // Створення 50 елементів для демонстрації
            dataset.add("Item " + i);
        }

        // Ініціалізація адаптера
        adapter = new MyAdapter(dataset);

        // Налаштування LayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Підключення адаптера до RecyclerView
        recyclerView.setAdapter(adapter);
    }
}