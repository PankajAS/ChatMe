package com.plusOneSoftwares.plusOneSoftwares.chatme;

/**
 * Created by Plus 3 on 03-02-2017.
 */

public class Chat {
    private String body;
    private String MessageBy;
    private String time;

    public Chat(){

    }

    public Chat(String time, String body, String MessageBy) {
        this.body = body;
        this.MessageBy = MessageBy;
        this.time = time;
    }

    public  String getBody(){return body;}

    public  void setBody(String body){this.body = body;}

    public  String getMessageBy(){return MessageBy;}

    public  void setMessageBy(String MessageBy){ this.MessageBy = MessageBy;}

    public  String getTime(){ return  time;}

    public  void setTime(String time){ this.time = time;}
}

