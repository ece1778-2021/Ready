package me.nirmit.ready.models;

public class Mark {
    private String user_id;
    private String test_id;
    private double mark;

    public Mark() {};

    public Mark(String user_id, String test_id, double mark) {
        this.user_id = user_id;
        this.test_id = test_id;
        this.mark = mark;
    }

    // Getters
    public String getUser_id() {
        return user_id;
    }

    public String getTest_id() {
        return test_id;
    }

    public double mark() {
        return mark;
    }

    // Setters

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setTest_id(String test_id) {
        this.test_id = test_id;
    }

    public void setMark(double mark) {
        this.mark = mark;
    }

    @Override
    public String toString() {
        return "Mark{" +
                ", user_id='" + user_id + '\'' +
                ", test_id='" + test_id + '\'' +
                ", mark='" + mark + '\'' +
                '}';
    }
}
