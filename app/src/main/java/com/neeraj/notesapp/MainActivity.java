package com.neeraj.notesapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.SimpleTimeZone;

import static com.neeraj.notesapp.AddNoteActivity.EXTRA_IS_IMAGE;

public class MainActivity extends AppCompatActivity {

    public static final int Add_note_request = 1;
    private static final String TAG = "Neeraj";
    public static final String EXTRA_title = "TITLE";
    public static final String EXTRA_CREATION_TIME = "TIME";
    public static final String EXTRA_DESCRIPTION = "DES";
    public static final String EXTRA_is_image = "is_image";
    public static final String EXTRA_image = "image";
    public static final String EXTRA_POS = "12";
    public NoteViewModel noteViewModel;
    private RecyclerView recyclerView;
    private NoteAdapter adapter;
    private Comparator<Note> comparator;
    private ArrayList<Note> noteArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        noteArrayList = new ArrayList<>();


        FloatingActionButton button = findViewById(R.id.add_note);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                startActivityForResult(intent, Add_note_request);
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapter = new NoteAdapter(this, noteArrayList);
        recyclerView.setAdapter(adapter);

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                //UPDATE RECYCLERVIEW
                //setListData((ArrayList<Note>) notes);
                noteArrayList.clear();
                noteArrayList.addAll(notes);
                adapter.setNotes(notes);

            }
        });
        adapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                Note note = noteArrayList.get(position);
                intent.putExtra(EXTRA_title, note.getTitle());
                intent.putExtra(EXTRA_CREATION_TIME, note.getCreation_time());
                intent.putExtra(EXTRA_DESCRIPTION, note.getDescription());
                intent.putExtra(EXTRA_is_image, note.getIsImage());
                intent.putExtra(EXTRA_image, note.getImage_byte());

                intent.putExtra(EXTRA_POS, position);
                startActivity(intent);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult: " + requestCode);
        if (requestCode == Add_note_request && resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddNoteActivity.EXTRA_description);
            byte[] image = data.getByteArrayExtra(AddNoteActivity.EXTRA_IMAGE);
            String creation_time = data.getStringExtra(AddNoteActivity.EXTRA_CREATION_TIME);
            int isImage = data.getIntExtra(AddNoteActivity.EXTRA_IS_IMAGE, 0);

            Note note = new Note(title, description, image, creation_time, isImage);
            //noteArrayList.add(note);
            noteViewModel.insert(note);


            Toast.makeText(this, "NOTE SAVED", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "NOTE NOT SAVED", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_title_asc:
                sort_title_asc();
                return true;
            case R.id.sort_title_desc:
                sort_title_desc();
                return true;
            case R.id.sort_created_asc:
                sort_created_asc();
                return true;
            case R.id.sort_created_desc:
                sort_created_desc();
                return true;
            case R.id.have_image:
                have_image();
                return true;
            case R.id.not_have_image:
                not_have_image();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void sort_title_asc() {
        comparator = new Comparator<Note>() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-YYYY, hh:mm:ss a");

            @Override
            public int compare(Note o1, Note o2) {

                return o1.getTitle().compareTo(o2.getTitle());

            }
        };
        noteViewModel.getAllNotes().observe(MainActivity.this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                //UPDATE RECYCLERVIEW
                Collections.sort(notes, comparator);

                //Collections.reverse(notes);
                noteArrayList.clear();
                noteArrayList.addAll(notes);
                adapter.setNotes(notes);
            }
        });

    }

    private void sort_title_desc() {
        comparator = new Comparator<Note>() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-YYYY, hh:mm:ss a");

            @Override
            public int compare(Note o1, Note o2) {

                return o1.getTitle().compareTo(o2.getTitle());

            }
        };
        noteViewModel.getAllNotes().observe(MainActivity.this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                //UPDATE RECYCLERVIEW
                Collections.sort(notes, comparator);

                Collections.reverse(notes);
                noteArrayList.clear();
                noteArrayList.addAll(notes);
                adapter.setNotes(notes);
            }
        });

    }

    private void have_image() {
        comparator = new Comparator<Note>() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-YYYY, hh:mm:ss a");

            @Override
            public int compare(Note o1, Note o2) {

                return o1.getTitle().compareTo(o2.getTitle());

            }
        };
        noteViewModel.getAllNotes().observe(MainActivity.this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                List<Note> temp = new ArrayList<>();
                for (Note i : notes) {
                    if (i.getIsImage() == 1) {
                        temp.add(i);
                    }
                }

                //Collections.reverse(notes);
                noteArrayList.clear();
                noteArrayList.addAll(temp);
                adapter.setNotes(temp);
            }
        });

    }

    private void not_have_image() {
        comparator = new Comparator<Note>() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-YYYY, hh:mm:ss a");

            @Override
            public int compare(Note o1, Note o2) {

                if (o1.getIsImage() >= o2.getIsImage()) {
                    return 1;
                } else {
                    return 0;
                }

            }
        };
        noteViewModel.getAllNotes().observe(MainActivity.this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                //UPDATE RECYCLERVIEW
                //Collections.sort(notes, comparator);
                List<Note> temp = new ArrayList<>();
                for (Note i : notes) {
                    if (i.getIsImage() == 0) {
                        temp.add(i);
                    }
                }

                //Collections.reverse(notes);
                noteArrayList.clear();
                noteArrayList.addAll(temp);
                adapter.setNotes(temp);
            }
        });

    }

    private void sort_created_desc() {
        comparator = new Comparator<Note>() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-YYYY, hh:mm:ss a");

            @Override
            public int compare(Note o1, Note o2) {

                try {
                    return dateFormat.parse(o1.getCreation_time()).compareTo(dateFormat.parse(o2.getCreation_time()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;

            }
        };
        noteViewModel.getAllNotes().observe(MainActivity.this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                //UPDATE RECYCLERVIEW
                Collections.sort(notes, comparator);


                Collections.reverse(notes);
                noteArrayList.clear();
                noteArrayList.addAll(notes);
                adapter.setNotes(notes);
            }
        });

    }


    private void sort_created_asc() {
        comparator = new Comparator<Note>() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-YYYY, hh:mm:ss a");

            @Override
            public int compare(Note o1, Note o2) {

                try {
                    return dateFormat.parse(o1.getCreation_time()).compareTo(dateFormat.parse(o2.getCreation_time()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;

            }
        };
        noteViewModel.getAllNotes().observe(MainActivity.this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                //UPDATE RECYCLERVIEW
                Collections.sort(notes, comparator);

                //Collections.reverse(notes);
                noteArrayList.clear();
                noteArrayList.addAll(notes);
                adapter.setNotes(notes);
            }
        });

    }


}
