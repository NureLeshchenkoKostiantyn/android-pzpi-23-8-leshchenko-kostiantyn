package com.example.kostiantyn_leshchenko_pzpi_23_8;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "notes.db";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_NOTES = "notes";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_PRIORITY = "priority";
    private static final String COLUMN_DATE_TIME = "date_time";
    private static final String COLUMN_IMAGE_PATH = "image_path";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_NOTES_TABLE = "CREATE TABLE " + TABLE_NOTES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_DESCRIPTION + " TEXT,"
                + COLUMN_PRIORITY + " INTEGER,"
                + COLUMN_DATE_TIME + " TEXT,"
                + COLUMN_IMAGE_PATH + " TEXT" + ")";
        db.execSQL(CREATE_NOTES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_NOTES + " ADD COLUMN " + COLUMN_PRIORITY + " INTEGER");
        }
    }

    public void addNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, note.getTitle());
        values.put(COLUMN_DESCRIPTION, note.getDescription());
        values.put(COLUMN_PRIORITY, note.getPriority());
        values.put(COLUMN_DATE_TIME, note.getDateTime());
        values.put(COLUMN_IMAGE_PATH, note.getImagePath());
        db.insert(TABLE_NOTES, null, values);
        db.close();
    }

    public List<Note> getAllNotes() {
        List<Note> notes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NOTES, null);

        if (cursor.moveToFirst()) {
            do {
                Note note = new Note(
                        cursor.getString(1), // title
                        cursor.getString(2), // description
                        cursor.getInt(3),    // priority
                        cursor.getString(4), // date_time
                        cursor.getString(5)  // image_path
                );
                note.setId(cursor.getInt(0)); // Установка id
                notes.add(note);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return notes;
    }

    public void updateNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, note.getTitle());
        values.put(COLUMN_DESCRIPTION, note.getDescription());
        values.put(COLUMN_PRIORITY, note.getPriority());
        values.put(COLUMN_DATE_TIME, note.getDateTime());
        values.put(COLUMN_IMAGE_PATH, note.getImagePath());

        db.update(TABLE_NOTES, values, COLUMN_ID + " = ?", new String[]{String.valueOf(note.getId())});
        db.close();
    }

    public void deleteNote(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTES, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public List<Note> searchNotes(String query) {
        List<Note> notes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_NOTES + " WHERE " + COLUMN_TITLE + " LIKE ? OR " + COLUMN_DESCRIPTION + " LIKE ?",
                new String[]{"%" + query + "%", "%" + query + "%"}
        );

        if (cursor.moveToFirst()) {
            do {
                Note note = new Note(
                        cursor.getString(1), // title
                        cursor.getString(2), // description
                        cursor.getInt(3),    // priority
                        cursor.getString(4), // date_time
                        cursor.getString(5)  // image_path
                );
                notes.add(note);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return notes;
    }

    public List<Note> filterNotesByPriority(int priority) {
        List<Note> notes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_NOTES + " WHERE " + COLUMN_PRIORITY + " = ?",
                new String[]{String.valueOf(priority)}
        );

        if (cursor.moveToFirst()) {
            do {
                Note note = new Note(
                        cursor.getString(1), // title
                        cursor.getString(2), // description
                        cursor.getInt(3),    // priority
                        cursor.getString(4), // date_time
                        cursor.getString(5)  // image_path
                );
                notes.add(note);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return notes;
    }
}