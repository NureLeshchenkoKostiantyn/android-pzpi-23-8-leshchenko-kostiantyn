package com.example.kostiantyn_leshchenko_pzpi_23_8;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private EditText editTextName, editTextAge, editTextFile;
    private TextView textViewUsers, textViewFileContent, textViewPrefs;
    private SharedPreferences sharedPreferences;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        editTextName = findViewById(R.id.editTextName);
        editTextAge = findViewById(R.id.editTextAge);
        editTextFile = findViewById(R.id.editTextFile);
        textViewUsers = findViewById(R.id.textViewUsers);
        textViewFileContent = findViewById(R.id.textViewFileContent);
        textViewPrefs = findViewById(R.id.textViewPrefs);

        Button buttonSavePrefs = findViewById(R.id.buttonSavePrefs);
        Button buttonShowPrefs = findViewById(R.id.buttonShowPrefs); // Додали кнопку для відображення даних
        Button buttonAddUser = findViewById(R.id.buttonAddUser);
        Button buttonSaveFile = findViewById(R.id.buttonSaveFile);
        Button buttonLoadFile = findViewById(R.id.buttonLoadFile);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        // Initialize SQLite database
        dbHelper = new DBHelper(this);

        // Load data from SharedPreferences
        String name = sharedPreferences.getString("name", "");
        int age = sharedPreferences.getInt("age", 0);
        editTextName.setText(name);
        editTextAge.setText(String.valueOf(age));

        // Save to SharedPreferences
        buttonSavePrefs.setOnClickListener(v -> {
            String newName = editTextName.getText().toString();
            int newAge = Integer.parseInt(editTextAge.getText().toString());

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("name", newName);
            editor.putInt("age", newAge);
            editor.apply();
        });

        // Відображення даних з SharedPreferences при натисканні кнопки
        buttonShowPrefs.setOnClickListener(v -> displayPrefsData());

        // Add user to SQLite
        buttonAddUser.setOnClickListener(v -> {
            String userName = editTextName.getText().toString();
            int userAge = Integer.parseInt(editTextAge.getText().toString());

            dbHelper.insertUser(userName, userAge);
            textViewUsers.setText(dbHelper.getAllUsers());
        });

        // Save to file
        buttonSaveFile.setOnClickListener(v -> {
            String data = editTextFile.getText().toString();
            saveToFile(data);
        });

        // Load from file
        buttonLoadFile.setOnClickListener(v -> {
            String data = loadFromFile();
            textViewFileContent.setText(data);
        });
    }

    // Метод для відображення даних з SharedPreferences
    private void displayPrefsData() {
        String name = sharedPreferences.getString("name", "No name");
        int age = sharedPreferences.getInt("age", 0);
        textViewPrefs.setText("Name: " + name + ", Age: " + age);
    }

    // SQLite Helper Class
    class DBHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "UserDB";
        private static final int DATABASE_VERSION = 1;
        private static final String TABLE_NAME = "users";
        private static final String COLUMN_ID = "id";
        private static final String COLUMN_NAME = "name";
        private static final String COLUMN_AGE = "age";

        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_AGE + " INTEGER)";
            db.execSQL(createTableQuery);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }

        public long insertUser(String name, int age) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, name);
            values.put(COLUMN_AGE, age);
            return db.insert(TABLE_NAME, null, values);
        }

        public String getAllUsers() {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
            StringBuilder result = new StringBuilder();
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                int age = cursor.getInt(cursor.getColumnIndex(COLUMN_AGE));
                result.append("Name: ").append(name).append(", Age: ").append(age).append("\n");
            }
            cursor.close();
            return result.toString();
        }
    }

    // File operations
    private void saveToFile(String data) {
        try (FileOutputStream fos = openFileOutput("myfile.txt", MODE_PRIVATE)) {
            fos.write(data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String loadFromFile() {
        StringBuilder data = new StringBuilder();
        try (FileInputStream fis = openFileInput("myfile.txt")) {
            int c;
            while ((c = fis.read()) != -1) {
                data.append((char) c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data.toString();
    }
}