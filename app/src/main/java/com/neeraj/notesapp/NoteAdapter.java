package com.neeraj.notesapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<com.neeraj.notesapp.NoteAdapter.NoteHolder> {
    private List<Note> notes = new ArrayList<>();
    private Context mcontext;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public NoteAdapter(Context context, ArrayList<Note> noteArrayList) {
        mcontext = context;
        notes = noteArrayList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item, parent, false);

        return new NoteHolder(itemview);
    }


    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        com.neeraj.notesapp.Note currentNote = notes.get(position);
        holder.textViewDescription.setText(currentNote.getDescription());
        holder.textCreationTime.setText(currentNote.getCreation_time() + "");
        holder.textViewTitle.setText(currentNote.getTitle());


    }

    @Override
    public int getItemCount() {
        return notes.size();

    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    class NoteHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textCreationTime;
        private TextView textViewDescription;

        private ImageView imageView;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            textViewDescription = itemView.findViewById(R.id.description);
            textCreationTime = itemView.findViewById(R.id.creation_time);

            textViewTitle = itemView.findViewById(R.id.title);
            imageView = itemView.findViewById(R.id.image_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }
                }
            });

        }
    }

}
