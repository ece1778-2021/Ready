package me.nirmit.ready.models;

public class User {

    private String name;
    private String email;
    private String user_id;
    private String id;
    private String phoneNumber;
    private String mode; // Teacher or Student

    public User() {

    }

    public User(String name, String email, String user_id, String id, String phoneNumber, String mode) {
        this.name = name;
        this.email = email;
        this.user_id = user_id;
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.mode = mode;
    }

// ------- Getters -------

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getId() {
        return id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getMode() {
        return mode;
    }

    // ------- Setters -------

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", user_id=" + user_id +
                ", id=" + id +
                ", phoneNumber=" + phoneNumber +
                ", mode='" + mode + '\'' +
                '}';
    }
}
