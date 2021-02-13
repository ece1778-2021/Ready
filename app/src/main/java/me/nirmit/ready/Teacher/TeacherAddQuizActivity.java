package me.nirmit.ready.Teacher;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import me.nirmit.ready.R;

public class TeacherAddQuizActivity extends AppCompatActivity {

    private static final String TAG = "TeacherAddQuizActivity";
    
    RecyclerView recyclerView;
    QuizAdapter quizAdapter;
    ArrayList<String> quizNames;
    private TextView topBarTitle;
    private Button btnAddQuiz;
    ImageView ivBackArrow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_add_quiz);

        topBarTitle = (TextView) findViewById(R.id.topBarTitle);
        topBarTitle.setText("Assessments");
        btnAddQuiz = (Button) findViewById(R.id.btnAddQuiz);
        ivBackArrow = (ImageView) findViewById(R.id.backArrow);

        quizNames = new ArrayList<>();
        quizNames.add("Test 1");
        quizNames.add("Test 2");
        quizNames.add("Test 3");

        recyclerView = findViewById(R.id.rcvQuizzes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        quizAdapter = new QuizAdapter(this,  quizNames);
        recyclerView.setAdapter(quizAdapter);

        btnAddQuizLogic();
        ivBackArrowLogic();
    }

    private void btnAddQuizLogic() {
        btnAddQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddQuizDialog(TeacherAddQuizActivity.this);
            }
        });
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

    private void showAddQuizDialog(Context c) {
        final EditText taskEditText = new EditText(c);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Add an Assessment")
                .setMessage("Assessment Name: ")
                .setView(taskEditText)
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String task = String.valueOf(taskEditText.getText());
                        quizNames.add(task);
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }
}
