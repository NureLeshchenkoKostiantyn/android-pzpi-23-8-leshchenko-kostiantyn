package com.example.kostiantyn_leshchenko_pzpi_23_8;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HandlerActivity extends AppCompatActivity {

    private Handler mainHandler;
    private Handler backgroundHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler);

        // Инициализация главного Handler
        mainHandler = new Handler(Looper.getMainLooper());

        // Пример 1: Задержка выполнения с Handler
        Button startHandlerButton = findViewById(R.id.startHandlerButton);
        TextView handlerMessageTextView = findViewById(R.id.handlerMessageTextView);

        startHandlerButton.setOnClickListener(v -> {
            mainHandler.postDelayed(() ->
                            handlerMessageTextView.setText("Handler executed after delay"),
                    2000 // 2 секунды задержки
            );
        });

        // Пример 2: Взаимодействие между потоками
        Button backgroundTaskButton = findViewById(R.id.backgroundTaskButton);
        backgroundTaskButton.setOnClickListener(v -> {
            new Thread(() -> {
                try {
                    Thread.sleep(3000); // Имитация долгой операции
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                mainHandler.post(() ->
                        handlerMessageTextView.setText("Updated from background thread")
                );
            }).start();
        });

        // Пример 3: Использование сообщений через Handler
        Button sendMessageButton = findViewById(R.id.sendMessageButton);
        Handler messageHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                handlerMessageTextView.setText("Message received: " + msg.what);
            }
        };

        sendMessageButton.setOnClickListener(v -> {
            new Thread(() -> {
                try {
                    Thread.sleep(2000); // Задержка перед отправкой сообщения
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Message message = messageHandler.obtainMessage();
                message.what = 1; // Код сообщения
                messageHandler.sendMessage(message);
            }).start();
        });

        // Пример 4: HandlerThread
        HandlerThread handlerThread = new HandlerThread("BackgroundThread");
        handlerThread.start();
        backgroundHandler = new Handler(handlerThread.getLooper());

        Button handlerThreadButton = findViewById(R.id.handlerThreadButton);
        handlerThreadButton.setOnClickListener(v ->
                backgroundHandler.post(() -> {
                    // Фоновые операции
                    try {
                        Thread.sleep(1000); // Имитация фоновой работы
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    mainHandler.post(() ->
                            handlerMessageTextView.setText("HandlerThread completed operation")
                    );
                })
        );
    }
}