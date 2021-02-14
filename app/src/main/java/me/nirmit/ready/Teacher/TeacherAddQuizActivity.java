package me.nirmit.ready.Teacher;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
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
                showAddQuizDialog();
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

    private void showAddQuizDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.custom_quiz_dialog, null);

        final EditText quizName = view.findViewById(R.id.assessmentName);
        final CheckBox isHW = view.findViewById(R.id.checkboxHW);
        final CheckBox isUnitTest = view.findViewById(R.id.checkBoxUnitTest);
        Button acceptButton = view.findViewById(R.id.acceptButton);
        Button cancelButton = view.findViewById(R.id.cancelButton);

        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        alertDialog.show();

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: accept button");

                if (quizName.getText().toString().isEmpty()) {
                    Toast.makeText(TeacherAddQuizActivity.this, "Enter assessment name",
                            Toast.LENGTH_SHORT).show();
                } else if (isHW.isChecked() && isUnitTest.isChecked()) {
                    Toast.makeText(TeacherAddQuizActivity.this, "Select ONE assessment " +
                            "type only", Toast.LENGTH_SHORT).show();
                } else if (!isHW.isChecked() && !isUnitTest.isChecked()) {
                    Toast.makeText(TeacherAddQuizActivity.this, "Select assessment type ",
                            Toast.LENGTH_SHORT).show();
                } else {
                    quizNames.add(String.valueOf(quizName.getText()));
                    alertDialog.cancel();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: cancel button" );
                alertDialog.cancel();
            }
        });
    }

}
