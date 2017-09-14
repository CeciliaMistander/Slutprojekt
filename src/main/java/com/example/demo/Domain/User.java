package com.example.demo.Domain;

public class User {
    public long userId;
    public String name;
    public String username;
    public String password;
    public String admin;

    public User(long userId, String name, String username, String password, String admin) {
        this.userId = userId;
        this.name = name;
        this.username = username;
        this.password = password;
        this.admin = admin;
    }
}
