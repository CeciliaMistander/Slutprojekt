package com.example.demo.Domain;

public class Errand {
    public long errandId;
    public String owner;
    public String topic;
    public String errand;
    public String status;
    public String administrator;
    public String created;

    public Errand(long errandId, String owner, String topic, String errand, String status, String administrator, String created) {
        this.errandId = errandId;
        this.owner = owner;
        this.topic = topic;
        this.errand = errand;
        this.status = status;
        this.administrator = administrator;
        this.created = created;
    }
}
