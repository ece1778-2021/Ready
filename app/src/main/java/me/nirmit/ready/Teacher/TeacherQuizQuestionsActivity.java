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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import me.nirmit.ready.Login.MainActivity;
import me.nirmit.ready.R;
import me.nirmit.ready.Util.FirebaseMethods;
import me.nirmit.ready.models.Question;
import me.nirmit.ready.models.Test;

public class TeacherQuizQuestionsActivity extends AppCompatActivity {

    private static final String TAG = "TeacherQuizQuestionsAct";

    RecyclerView recyclerView;
    QuestionAdapter questionAdapter;
    ArrayList<Question> questions;
    private TextView topBarTitle;
    private Button btnAddQuestion;
    private BottomNavigationView bottomView;
    private ImageView ivBackArrow, signoutBtn;
    private Context mContext;
    private String testId;
    private String quizPublished;

    // Firebase stuff
    private FirebaseAuth mAuth;
    private FirebaseMethods firebaseMethods;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_add_questions);

        topBarTitle = (TextView) findViewById(R.id.topBarTitle);
        topBarTitle.setText("Quiz Questions");
        ivBackArrow = (ImageView) findViewById(R.id.backArrow);
        signoutBtn = (ImageView) findViewById(R.id.signout);
        bottomView = findViewById(R.id.bottom_navigation);

        firebaseMethods = new FirebaseMethods(TeacherQuizQuestionsActivity.this);
        mContext = TeacherQuizQuestionsActivity.this;
        btnAddQuestion = (Button) findViewById(R.id.btnAddQuestion);

        questions = new ArrayList<>();
        testId = getIntent().getStringExtra("QUIZ_FIREBASE_ID");
        quizPublished =  getIntent().getStringExtra("IS_QUIZ_PUBLISHED");

        setupFirebaseAuth();
        setupQuestionAdapterWithFirestore();
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
                        Intent intent = new Intent(TeacherQuizQuestionsActivity.this,
                                TeacherMessageActivity.class);
                        intent.putExtra("QUIZ_FIREBASE_ID", testId);
                        intent.putExtra("IS_QUIZ_PUBLISHED", quizPublished);
                        intent.putExtra("TEST_TYPE", getIntent().getStringExtra("TEST_TYPE"));
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
        if (quizPublished.equals("1")) {
            btnAddQuestion.setVisibility(View.GONE);
        }

        btnAddQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TeacherQuizQuestionsActivity.this,
                        "Adding a question", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(TeacherQuizQuestionsActivity.this,
                        TeacherQuestionCreationActivity.class);
                intent.putExtra("QUIZ_FIREBASE_ID", testId);
                intent.putExtra("IS_QUIZ_PUBLISHED", quizPublished);
                startActivity(intent);

            }
        });
    }

    private void signoutBtnLogic() {
        signoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        db = FirebaseFirestore.getInstance();
    }

    private void setupQuestionAdapterWithFirestore() {

        CollectionReference all_photos = db.collection("questions");
        Query query = all_photos.whereEqualTo("test_id",
                getIntent().getStringExtra("QUIZ_FIREBASE_ID"));

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e(TAG, "onEvent: Listen failed:" + error);
                    return;
                }

                questions.clear();

                for (DocumentSnapshot document : value) {
                    Question question = document.toObject(Question.class);
                    questions.add(question);
                }

                // sort the tests based on the creation time
                Collections.sort(questions, new Comparator<Question>() {
                    @Override
                    public int compare(Question p1, Question p2) {
                        return p1.getDate_created().compareTo(p2.getDate_created());
                    }
                });

                recyclerView = findViewById(R.id.rcvQuestions);
                recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                questionAdapter = new QuestionAdapter(mContext, questions,
                        getIntent().getStringExtra("IS_QUIZ_PUBLISHED").equals("1") ? true : false);
                recyclerView.setAdapter(questionAdapter);
            }
        });

    }
}
