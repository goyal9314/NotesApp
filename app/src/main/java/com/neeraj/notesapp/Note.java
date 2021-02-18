package com.neeraj.notesapp;


import android.graphics.Bitmap;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;

@Entity(tableName = "profile")
public class Note {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
    private byte[] image_byte;
    private int isImage;
    private String creation_time;

    public Note() {

    }

    public Note(String title, String description, byte[] image_byte, String creation_time, int isImage) {
        this.title = title;
        this.description = description;
        this.image_byte = image_byte;
        this.isImage = isImage;
        this.creation_time = creation_time;
    }

    public byte[] getImage_byte() {
        return image_byte;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }


    public void setImage_byte(byte[] image_byte) {
        this.image_byte = image_byte;
    }

    public int getIsImage() {
        return isImage;
    }

    public void setIsImage(int isImage) {
        this.isImage = isImage;
    }

    public String getCreation_time() {
        return creation_time;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public void setCreation_time(String creation_time) {
        this.creation_time = creation_time;
    }
}
