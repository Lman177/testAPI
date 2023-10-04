package vn.edu.usth.gmail;

import java.sql.Timestamp;

public class Email {
    String name;
    String head_mail;
    String content;
    String date;
    int image;
    String id;
    Timestamp timestamp;




    public Email() {

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHead_mail() {
        return head_mail;
    }

    public void setHead_mail(String head_mail) {
        this.head_mail = head_mail;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}

