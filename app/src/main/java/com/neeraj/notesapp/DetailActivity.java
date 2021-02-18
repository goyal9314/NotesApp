package com.neeraj.notesapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    private TextView textTitle;
    private TextView textCreation_time;
    private TextView textDescription;
    private ImageView imageView;
    private ImageView touchImage;
    private NoteViewModel noteViewModel;

    public static final int PICK_IMAGE_REQUEST = 1;

    private EditText edit_title;
    private EditText edit_desc;
    private Button image_update;
    private Button update;
    private ImageView edit_image;

    private Uri mImageUri;


    private String title;
    private String description;
    private String creation_time;
    private int isImage;
    private byte[] image;
    private ArrayList<Note> noteArrayList;
    private int pos;
    private LinearLayout detail_ll;
    private LinearLayout edit_ll;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        setTitle("Note Detail ");

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(DetailActivity.this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                //UPDATE RECYCLERVIEW
                noteArrayList.addAll(notes);

            }
        });
        edit_ll = findViewById(R.id.edit_ll);
        detail_ll = findViewById(R.id.detail_ll);
        edit_ll.setVisibility(View.GONE);
        detail_ll.setVisibility(View.VISIBLE);
        textTitle = (TextView) findViewById(R.id.title);
        textDescription = findViewById(R.id.description);
        textCreation_time = findViewById(R.id.creation_time);
        imageView = findViewById(R.id.image_view);
        noteArrayList = new ArrayList<>();

        edit_title = findViewById(R.id.title_edit);
        edit_desc = findViewById(R.id.description_edit);
        image_update = findViewById(R.id.update_image);
        update = findViewById(R.id.update);
        edit_image = findViewById(R.id.image_view_edit);

        imageView.setVisibility(View.GONE);

        title = getIntent().getStringExtra(MainActivity.EXTRA_title);
        creation_time = getIntent().getStringExtra(MainActivity.EXTRA_CREATION_TIME);
        description = getIntent().getStringExtra(MainActivity.EXTRA_DESCRIPTION);
        isImage = getIntent().getIntExtra(MainActivity.EXTRA_is_image, 0);
        image = getIntent().getByteArrayExtra(MainActivity.EXTRA_image);
        pos = getIntent().getIntExtra(MainActivity.EXTRA_POS, 0);

        if (isImage == 1) {
            imageView.setVisibility(View.VISIBLE);
            edit_image.setVisibility(View.VISIBLE);

        } else {
            imageView.setVisibility(View.GONE);
            edit_image.setVisibility(View.GONE);
        }

        if (image != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);

            //Bitmap bitmap = Bitmap.createScaledBitmap(bitmap_or, (int) (1000*ratio), 1000, true);
            Bitmap gray = toGrayscale(bitmap);
            imageView.setImageBitmap(bitmap);
            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_DOWN) {
                        imageView.setImageBitmap(gray);
                    } else {
                        imageView.setImageBitmap(bitmap);
                    }
                    return true;
                }
            });
        }

        textTitle.setText(title);
        textDescription.setText(description);
        textCreation_time.setText(creation_time);
    }


    public Bitmap toGrayscale(Bitmap bmpOriginal) {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.note_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_note_menu:

                AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
                builder.setTitle("Are you sure?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Note note = new Note(title, description, image, creation_time, isImage);
                        noteViewModel.delete(noteArrayList.get(pos));
                        noteArrayList.remove(pos);
                        finish();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(DetailActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog ad = builder.create();
                ad.show();
                return true;
            case R.id.edit_note_menu:
                setTitle("Edit Note");
                editNote();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void editNote() {
        detail_ll.setVisibility(View.GONE);
        edit_ll.setVisibility(View.VISIBLE);
        edit_title.setText(title);
        edit_desc.setText(description);
        if (isImage == 1)
            edit_image.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));

        image_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();

            }
        });
        //note.setImage();
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Note note = noteArrayList.get(pos);
                int id = note.getId();

                if (mImageUri != null) {
                    try {
                        isImage = 1;
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageUri);
                        image = fromBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                String title = edit_title.getText().toString();
                String desc = edit_desc.getText().toString();
                if (title.length() == 0) {
                    edit_title.setError(" Please Enter Title ");
                    edit_title.requestFocus();
                    return;
                }
                if (desc.length() == 0) {
                    edit_desc.setError(" Please write Description ");
                    edit_desc.requestFocus();
                    return;
                }
                note.setTitle(title);
                note.setDescription(desc);
                String creation_time = new SimpleDateFormat("dd-MMM-YYYY, hh:mm:ss a").format(new Date());
                note.setCreation_time(creation_time);
                note.setId(id);
                note.setImage_byte(image);
                note.setIsImage(isImage);
                noteViewModel.update(note);

                finish();
            }
        });

    }

    private byte[] fromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        return outputStream.toByteArray();
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageUri);
                edit_image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
