package me.nirmit.ready.models;

public class Answer {
    private String question_id;
    private String test_id;
    private String answer_id;
    private String user_id;
    private String answer;
    private String image_path;
    private String date_submitted;

    public Answer() {};

    public Answer(String test_id, String question_id, String answer_id, String user_id,
                  String date_submitted, String image_path, String answer) {
        this.test_id = test_id;
        this.question_id = question_id;
        this.answer_id = answer_id;
        this.user_id = user_id;
        this.date_submitted = date_submitted;
        this.image_path = image_path;
        this.answer = answer;
    }

    // Getters
    public String getTest_id() {
        return test_id;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public String getAnswer_id() {
        return answer;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getDate_submitted() {
        return date_submitted;
    }

    public String getImage_path() {
        return image_path;
    }

    public String getAnswer() {
        return answer;
    }

    // Setters
    public void setTest_id(String test_id) {
        this.test_id = test_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public void setAnswer_id(String answer_id) {
        this.answer_id = answer_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setDate_submitted(String date_submitted) {
        this.date_submitted = date_submitted;
    }

    public void setImage_path(String image_path){
        this.image_path = image_path;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "date_submitted='" + date_submitted + '\'' +
                ", question_id='" + question_id + '\'' +
                ", test_id='" + test_id + '\'' +
                ", answer_id='" + answer_id + '\'' +
                ", answer='" + answer + '\'' +
                ", image_path='" + image_path + '\'' +
                '}';
    }
}
