package com.example.kostiantyn_leshchenko_pzpi_23_8;

import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText resultEditText;
    private double num1 = 0, num2 = 0;
    private String operator = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Проверяем ориентацию экрана
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Если ландшафтная ориентация, используем activity_main_land.xml
            setContentView(R.layout.activity_main_land);
        } else {
            // Если портретная ориентация, используем activity_main.xml
            setContentView(R.layout.activity_main);
        }

        resultEditText = findViewById(R.id.resultEditText);

        // Восстановление состояния
        if (savedInstanceState != null) {
            resultEditText.setText(savedInstanceState.getString("result", "0"));
            num1 = savedInstanceState.getDouble("num1", 0);
            num2 = savedInstanceState.getDouble("num2", 0);
            operator = savedInstanceState.getString("operator", "");
        }

        // Настройка кнопок чисел
        setupButton(R.id.button0, "0");
        setupButton(R.id.button1, "1");
        setupButton(R.id.button2, "2");
        setupButton(R.id.button3, "3");
        setupButton(R.id.button4, "4");
        setupButton(R.id.button5, "5");
        setupButton(R.id.button6, "6");
        setupButton(R.id.button7, "7");
        setupButton(R.id.button8, "8");
        setupButton(R.id.button9, "9");

        // Настройка кнопок операций
        setupOperatorButton(R.id.buttonPlus, "+");
        setupOperatorButton(R.id.buttonMinus, "-");
        setupOperatorButton(R.id.buttonMultiply, "×");
        setupOperatorButton(R.id.buttonDivide, "÷");

        // Кнопка "="
        Button buttonEquals = findViewById(R.id.buttonEquals);
        buttonEquals.setOnClickListener(v -> calculateResult());

        // Кнопка "C" (очистка)
        Button buttonClear = findViewById(R.id.buttonClear);
        buttonClear.setOnClickListener(v -> {
            resultEditText.setText("0");
            num1 = 0;
            num2 = 0;
            operator = "";
        });
    }

    private void setupButton(int buttonId, String buttonText) {
        Button button = findViewById(buttonId);
        button.setOnClickListener(v -> {
            String currentText = resultEditText.getText().toString();
            if (currentText.equals("0")) {
                resultEditText.setText(buttonText);
            } else {
                resultEditText.setText(currentText + buttonText);
            }
        });
    }

    private void setupOperatorButton(int buttonId, String op) {
        Button button = findViewById(buttonId);
        button.setOnClickListener(v -> setOperator(op));
    }

    private void setOperator(String op) {
        String currentText = resultEditText.getText().toString();
        if (!currentText.isEmpty()) {
            num1 = Double.parseDouble(currentText);
            operator = op;
            resultEditText.setText("0");
        }
    }

    private void calculateResult() {
        String currentText = resultEditText.getText().toString();
        if (!currentText.isEmpty() && !operator.isEmpty()) {
            num2 = Double.parseDouble(currentText);
            double result = 0;
            switch (operator) {
                case "+":
                    result = num1 + num2;
                    break;
                case "-":
                    result = num1 - num2;
                    break;
                case "×":
                    result = num1 * num2;
                    break;
                case "÷":
                    if (num2 != 0) {
                        result = num1 / num2;
                    } else {
                        resultEditText.setText("Error");
                        return;
                    }
                    break;
            }
            resultEditText.setText(String.valueOf(result));
            operator = "";
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Сохраняем текущее состояние
        outState.putString("result", resultEditText.getText().toString());
        outState.putDouble("num1", num1);
        outState.putDouble("num2", num2);
        outState.putString("operator", operator);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Восстанавливаем состояние
        resultEditText.setText(savedInstanceState.getString("result", "0"));
        num1 = savedInstanceState.getDouble("num1", 0);
        num2 = savedInstanceState.getDouble("num2", 0);
        operator = savedInstanceState.getString("operator", "");
    }
}