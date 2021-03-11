package me.nirmit.ready.models;

public class Question {

    private String date_created;
    private String question_id;
    private String test_id;
    private String topic;
    private String answer;
    // You could only have one or the other. NULL for the second case
    private String image_path;
    private String question_text;

    public Question() {};

    public Question(String date_created, String question_id, String test_id, String topic,
                    String answer, String image_path, String question_text) {
        this.date_created = date_created;
        this.question_id = question_id;
        this.test_id = test_id;
        this.topic = topic;
        this.answer = answer;
        this.image_path = image_path;
        this.question_text = question_text;
    }

    // --------- Getters ----------


    public String getDate_created() {
        return date_created;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public String getTest_id() {
        return test_id;
    }

    public String getTopic() {
        return topic;
    }

    public String getAnswer() {
        return answer;
    }

    public String getImage_path() {
        return image_path;
    }

    public String getQuestion_text() {
        return question_text;
    }


    // -------- Setters --------

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public void setTest_id(String test_id) {
        this.test_id = test_id;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public void setQuestion_text(String question_text) {
        this.question_text = question_text;
    }

    @Override
    public String toString() {
        return "Question{" +
                "date_created='" + date_created + '\'' +
                ", question_id='" + question_id + '\'' +
                ", test_id='" + test_id + '\'' +
                ", topic='" + topic + '\'' +
                ", answer='" + answer + '\'' +
                ", image_path='" + image_path + '\'' +
                ", question_text='" + question_text + '\'' +
                '}';
    }
}
