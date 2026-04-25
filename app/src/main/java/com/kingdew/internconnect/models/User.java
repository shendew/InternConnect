package com.kingdew.internconnect.models;

public class User {
    private String id;
    private String name;
    private String email;
    private String password;
    private String createdAt;

    // Constructors
    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
}