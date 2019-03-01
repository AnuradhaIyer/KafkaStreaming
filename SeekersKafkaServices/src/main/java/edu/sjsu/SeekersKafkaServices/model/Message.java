package edu.sjsu.SeekersKafkaServices.model;


import java.io.Serializable;

public class Message  {

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private String msg;

    public Message(String msg) {
        this.msg = msg;
    }

    public Message(){}
}
