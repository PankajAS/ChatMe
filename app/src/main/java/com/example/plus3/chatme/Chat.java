package com.example.plus3.chatme;

/**
 * Created by Plus 3 on 03-02-2017.
 */

public class Chat {
    private String message;
    private String id;
    private String time;

    public Chat(String time, String message, String id) {
        this.message = message;
        this.id = id;
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public  String getTime(){ return  time;}

    public  void setTime(String time){ this.time = time;}
}

