package me.nirmit.ready.models;

public class Test {

    private String date_created;
    private String deadline_date;
    private String deadline_time;
    private String testname;
    private String type;
    private int status; // 0 = not released, 1 = released
    private String course_id; // null for now
    private String test_id;
    private String teacher_id;

    public Test() {}

    public Test(String date_created, String deadline_date, String deadline_time, String testname,
                String type, int status, String course_id, String test_id, String teacher_id) {
        this.date_created = date_created;
        this.deadline_date = deadline_date;
        this.deadline_time = deadline_time;
        this.testname = testname;
        this.type = type;
        this.status = status;
        this.course_id = course_id;
        this.test_id = test_id;
        this.teacher_id = teacher_id;
    }

    // ------ Getters ------

    public String getDate_created() {
        return date_created;
    }

    public String getDeadline_date() {
        return deadline_date;
    }

    public String getDeadline_time() {
        return deadline_time;
    }

    public String getTestname() {
        return testname;
    }

    public String getType() {
        return type;
    }

    public int getStatus() {
        return status;
    }

    public String getCourse_id() {
        return course_id;
    }

    public String getTest_id() {
        return test_id;
    }

    public String getTeacher_id() {
        return teacher_id;
    }

    // ------ setters --------


    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public void setDeadline_date(String deadline_date) {
        this.deadline_date = deadline_date;
    }

    public void setDeadline_time(String deadline_time) {
        this.deadline_time = deadline_time;
    }

    public void setTestname(String testname) {
        this.testname = testname;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public void setTest_id(String test_id) {
        this.test_id = test_id;
    }

    public void setTeacher_id(String teacher_id) {
        this.teacher_id = teacher_id;
    }

    @Override
    public String toString() {
        return "Test{" +
                "date_created='" + date_created + '\'' +
                ", deadline_date='" + deadline_date + '\'' +
                ", deadline_time='" + deadline_time + '\'' +
                ", testname='" + testname + '\'' +
                ", type='" + type + '\'' +
                ", status=" + status +
                ", course_id='" + course_id + '\'' +
                ", test_id='" + test_id + '\'' +
                ", teacher_id='" + teacher_id + '\'' +
                '}';
    }
}
