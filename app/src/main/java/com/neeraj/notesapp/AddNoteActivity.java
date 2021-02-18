package com.neeraj.notesapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddNoteActivity extends AppCompatActivity {
    private EditText editTexttitle;
    private EditText editTextdescription;
    private ImageView image;
    private Button select;
    private Button upload;

    private Uri mImageUri;
    private static final int PICK_IMAGE_REQUEST = 1;

    public static final String EXTRA_TITLE = "com.example.roomexercise.EXTRA_TITLE";
    public static final String EXTRA_description = "com.example.roomexercise.EXTRA_DESCRIPTION";
    public static final String EXTRA_IMAGE = "IMAGE";
    public static final String EXTRA_CREATION_TIME = "00:00";
    public static final String EXTRA_IS_IMAGE = "false";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("Add Note");

        editTexttitle = findViewById(R.id.edit_text_title);
        editTextdescription = findViewById(R.id.edit_text_description);
        image = findViewById(R.id.image_view);
        select = findViewById(R.id.select);
        upload = findViewById(R.id.upload);

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });

        setTitle("Add Note");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageUri);
                image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveNote() {
        String title = editTexttitle.getText().toString();

        String creation_time = new SimpleDateFormat("dd-MMM-YYYY, hh:mm:ss a").format(new Date());

        String description = editTextdescription.getText().toString();

        if (title.length() == 0) {
            editTexttitle.setError("Please Enter Title ");
            editTexttitle.requestFocus();
            return;
        }
        if (description.length() == 0) {
            editTextdescription.setError("Please write description ");
            editTextdescription.requestFocus();
            return;
        }

        byte[] image = null;
        int isImage = 0;
        if (mImageUri != null) {
            try {
                isImage = 1;
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageUri);
                image = fromBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_CREATION_TIME, creation_time);
        data.putExtra(EXTRA_description, description);
        data.putExtra(EXTRA_IMAGE, image);
        data.putExtra(EXTRA_IS_IMAGE, isImage);

        setResult(RESULT_OK, data);

        finish();
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
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_REQUEST);
    }

}
