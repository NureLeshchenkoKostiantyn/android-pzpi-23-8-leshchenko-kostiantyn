package com.example.kostiantyn_leshchenko_pzpi_23_8;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.util.TypedValue;
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
    private int contextMenuPosition = -1;
    private int fontSize;

    public NoteAdapter(List<Note> notes, Context context) {
        this.notes = notes;
        this.context = context;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
        notifyDataSetChanged();
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
        holder.description.setText(note.getDescription());

        // Установка размера шрифта
        holder.title.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
        holder.dateTime.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);

        // Установка изображения
        if (note.getImagePath() != null && !note.getImagePath().isEmpty()) {
            try {
                Uri imageUri = Uri.parse(note.getImagePath());
                holder.noteImage.setImageURI(imageUri);
            } catch (Exception e) {
                Log.e("NoteAdapter", "Error loading image: " + e.getMessage());
                holder.noteImage.setImageResource(R.drawable.ic_default_image);
            }
        } else {
            holder.noteImage.setImageResource(R.drawable.ic_default_image);
        }

        // Установка иконки важности
        int priorityIcon = R.drawable.ic_priority_low; // По умолчанию
        switch (note.getPriority()) {
            case 2: priorityIcon = R.drawable.ic_priority_medium; break;
            case 3: priorityIcon = R.drawable.ic_priority_high; break;
        }
        holder.priorityIcon.setImageResource(priorityIcon);

        // Долгое нажатие для контекстного меню
        holder.itemView.setOnLongClickListener(v -> {
            contextMenuPosition = position;
            ((MainActivity) context).openContextMenu(v);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public int getContextMenuPosition() {
        return contextMenuPosition;
    }

    public Note getNoteAt(int position) {
        return notes.get(position);
    }

    public void updateNotes(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, dateTime;
        ImageView priorityIcon, noteImage;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.note_title);
            dateTime = itemView.findViewById(R.id.note_date_time);
            description = itemView.findViewById(R.id.note_description);
            priorityIcon = itemView.findViewById(R.id.priority_icon);
            noteImage = itemView.findViewById(R.id.note_image);
        }
    }
}