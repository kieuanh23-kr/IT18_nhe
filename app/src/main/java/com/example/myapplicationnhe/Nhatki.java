package com.example.myapplicationnhe;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "Nhat_ki")
public class Nhatki {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private String content;
    private int srcImage;
    private int srcBack;
    private String date;
    private String time;
    private String type;

    public Nhatki() {}

    @Ignore
    public Nhatki(String title, String content, int srcImage, int srcBack, String date, String time, String type) {
        this.title = title;
        this.content = content;
        this.srcImage = srcImage;
        this.srcBack = srcBack;
        this.date = date;
        this.time = time;
        this.type = type;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public int getSrcImage() { return srcImage; }
    public void setSrcImage(int srcImage) { this.srcImage = srcImage; }

    public int getSrcBack() { return srcBack; }
    public void setSrcBack(int srcBack) { this.srcBack = srcBack; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public String getType() { return type; }
    public void setType (String type) { this.type = type; }
}
