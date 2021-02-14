package me.nirmit.ready.Teacher;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import me.nirmit.ready.R;

public class TeacherQuizQuestionsActivity extends AppCompatActivity {

    private static final String TAG = "TeacherQuizQuestionsAct";

    RecyclerView recyclerView;
    QuestionAdapter questionAdapter;
    ArrayList<String> questionNames;
    private TextView topBarTitle;
    private Button btnAddQuestion;
    private ImageView ivBackArrow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_add_questions);

        topBarTitle = (TextView) findViewById(R.id.topBarTitle);
        topBarTitle.setText("Quiz Questions");
        btnAddQuestion = (Button) findViewById(R.id.btnAddQuestion);
        ivBackArrow = (ImageView) findViewById(R.id.backArrow);

        questionNames = new ArrayList<>();
        questionNames.add("Q1");
        questionNames.add("Q2");
        questionNames.add("Q3");

        recyclerView = findViewById(R.id.rcvQuestions);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        questionAdapter = new QuestionAdapter(this,  questionNames);
        recyclerView.setAdapter(questionAdapter);

        btnAddQuestionLogic();
        ivBackArrowLogic();
    }

    private void ivBackArrowLogic() {
        ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: closing the activity");
                finish();
            }
        });
    }

    private void btnAddQuestionLogic() {
        btnAddQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TeacherQuizQuestionsActivity.this,
                        "Adding a question", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(TeacherQuizQuestionsActivity.this,
                        TeacherQuestionCreationActivity.class);
                startActivity(intent);

            }
        });
    }
}
