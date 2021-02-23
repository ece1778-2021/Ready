package me.nirmit.ready.Teacher;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import me.nirmit.ready.Login.MainActivity;
import me.nirmit.ready.R;

public class TeacherQuizQuestionsActivity extends AppCompatActivity {

    private static final String TAG = "TeacherQuizQuestionsAct";

    RecyclerView recyclerView;
    QuestionAdapter questionAdapter;
    ArrayList<String> questionNames;
    private TextView topBarTitle;
    private Button btnAddQuestion;
    private BottomNavigationView bottomView;
    private ImageView ivBackArrow, signoutBtn;
    private Context mContext;

    // Firebase stuff
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_add_questions);

        topBarTitle = (TextView) findViewById(R.id.topBarTitle);
        topBarTitle.setText("Quiz Questions");
        ivBackArrow = (ImageView) findViewById(R.id.backArrow);
        signoutBtn = (ImageView) findViewById(R.id.signout);
        bottomView = findViewById(R.id.bottom_navigation);


        mContext = TeacherQuizQuestionsActivity.this;
        btnAddQuestion = (Button) findViewById(R.id.btnAddQuestion);

        questionNames = new ArrayList<>();
        questionNames.add("Q1");
        questionNames.add("Q2");
        questionNames.add("Q3");

        recyclerView = findViewById(R.id.rcvQuestions);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        questionAdapter = new QuestionAdapter(this,  questionNames);
        recyclerView.setAdapter(questionAdapter);

        setupFirebaseAuth();
        btnAddQuestionLogic();
        ivBackArrowLogic();
        signoutBtnLogic();
        bottomViewListener();

    }

    public void bottomViewListener() {
        bottomView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.quiz_button:
                        break;
                    case R.id.message_button:
                        Intent intent = new Intent(TeacherQuizQuestionsActivity.this, TeacherMessageActivity.class);
                        startActivity(intent);
                        break;
                }
                return true;
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

    private void btnAddQuestionLogic() {
        btnAddQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TeacherQuizQuestionsActivity.this,
                        "Adding a question", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(TeacherQuizQuestionsActivity.this,
                        TeacherQuestionCreationActivity.class);
                intent.putExtra("QUIZ_FIREBASE_ID",
                        getIntent().getStringExtra("QUIZ_FIREBASE_ID"));
                startActivity(intent);

            }
        });
    }

    private void signoutBtnLogic() {
        signoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Signing out the user");
                Toast.makeText(mContext, "Signing out the user", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(TeacherQuizQuestionsActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                mAuth.signOut();
                startActivity(intent);
            }
        });
    }


    // ============= Firebase Methods & Logic ===============

    private void setupFirebaseAuth() {
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
    }
}
