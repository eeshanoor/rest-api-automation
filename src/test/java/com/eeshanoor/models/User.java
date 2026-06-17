package com.eeshanoor.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    private int id;
    private String email;
    private String first_name;
    private String last_name;
    private String avatar;

    public User() {}
    public User(String name, String job) { this.first_name = name; }

    public int getId() { return id; }
    public String getEmail() { return email; }
    public String getFirstName() { return first_name; }
    public String getLastName() { return last_name; }
    public void setId(int id) { this.id = id; }
    public void setEmail(String email) { this.email = email; }
    public void setFirstName(String firstName) { this.first_name = firstName; }
    public void setLastName(String lastName) { this.last_name = lastName; }
}