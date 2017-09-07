package com.example.demo.Domain;

public class Errand {
    public long errandId;
    public String name;
    public String topic;
    public String errand;
    public String status;

    public Errand(long errandId, String name, String topic, String errand, String status) {
        this.errandId = errandId;
        this.name = name;
        this.topic = topic;
        this.errand = errand;
        this.status = status;
    }
}
