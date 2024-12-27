package com.example.kostiantyn_leshchenko_pzpi_23_8;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private List<Note> notes;
    private Context context;
    private int contextMenuPosition = -1; // Зберігає позицію елемента для контекстного меню

    public NoteAdapter(List<Note> notes, Context context) {
        this.notes = notes;
        this.context = context;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.title.setText(note.getTitle());
        holder.dateTime.setText(note.getDateTime());

        // Встановлення іконки важливості
        int priorityIcon = R.drawable.ic_priority_low; // За замовчуванням
        switch (note.getPriority()) {
            case 2: priorityIcon = R.drawable.ic_priority_medium; break;
            case 3: priorityIcon = R.drawable.ic_priority_high; break;
        }
        holder.priorityIcon.setImageResource(priorityIcon);

        // Встановлення зображення нотатки
        if (note.getImagePath() != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(note.getImagePath());
            holder.noteImage.setImageBitmap(bitmap);
        }

        // Довге натискання для контекстного меню
        holder.itemView.setOnLongClickListener(v -> {
            contextMenuPosition = position; // Зберігаємо позицію елемента
            ((MainActivity) context).openContextMenu(v); // Відкриваємо контекстне меню
            return true;
        });
    }

    @Override
    public int getItemCount() { return notes.size(); }

    // Метод для отримання позиції елемента для контекстного меню
    public int getContextMenuPosition() {
        return contextMenuPosition;
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView title, dateTime;
        ImageView priorityIcon, noteImage;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.note_title);
            dateTime = itemView.findViewById(R.id.note_date_time);
            priorityIcon = itemView.findViewById(R.id.priority_icon);
            noteImage = itemView.findViewById(R.id.note_image);
        }
    }
}