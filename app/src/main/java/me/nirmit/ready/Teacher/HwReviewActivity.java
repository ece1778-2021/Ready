package me.nirmit.ready.Teacher;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

import me.nirmit.ready.Login.MainActivity;
import me.nirmit.ready.R;
import me.nirmit.ready.Student.StudentAssgAdapter;
import me.nirmit.ready.Util.FirebaseMethods;
import me.nirmit.ready.models.Answer;
import me.nirmit.ready.models.Question;


public class HwReviewActivity extends AppCompatActivity {

    private static final String TAG = "HwReviewActivity";
    private static final String LOG = HwReviewActivity.class.getSimpleName();

    private TextView topBarTitle;
    private ProgressBar progressBar;
    private BottomNavigationView bottomView;
    private ImageView ivBackArrow, signoutBtn;
    private Context mContext;
    private String testId;
    private String testType;
    private String userId;
    private boolean status;

    private RecyclerView recyclerView;
    private HwReviewAdapter recyclerViewAdapter;
    private ArrayList<Question> testQuestions;
    private ArrayList<Answer> testAnswers;

    // Firebase stuff
    private FirebaseAuth mAuth;
    private FirebaseMethods firebaseMethods;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_review_adapter);

        topBarTitle = (TextView) findViewById(R.id.topBarTitle);
        topBarTitle.setText("Assessment Review Page");

        firebaseMethods = new FirebaseMethods(HwReviewActivity.this);
        mContext = HwReviewActivity.this;
        progressBar = findViewById(R.id.messg_progressbar);
        progressBar.setVisibility(View.VISIBLE);
        bottomView = findViewById(R.id.bottom_navigation);
        ivBackArrow = (ImageView) findViewById(R.id.backArrow);
        signoutBtn = (ImageView) findViewById(R.id.signout);

        testQuestions = new ArrayList<>();
        testAnswers = new ArrayList<>();

        testId = getIntent().getStringExtra("PUBLISHED_TEST_FIREBASE_ID");
        testType = getIntent().getStringExtra("TEST_TYPE");
        userId = getIntent().getStringExtra("USER_ID");
        status = getIntent().getBooleanExtra("STATUS", false);

        getTestQuestionsFirestore();
        setupFirebaseAuth();
        ivBackArrowLogic();
        signoutBtnLogic();
    }

    // ================= Firebase Method =================
    private void setupFirebaseAuth() {
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    // get test questions
    public void getTestQuestionsFirestore() {
        CollectionReference all_tests = db.collection("questions");
        Query query = all_tests.whereEqualTo("test_id", testId);

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null) {
                    Log.e(TAG, "onEvent: Listen failed:" + error);
                    return;
                }

                testQuestions.clear();
                testAnswers.clear();

                for (DocumentSnapshot document : value) {
                    Question question = document.toObject(Question.class);
                    testQuestions.add(question);
                }

                // sort the tests based on the creation time
                Collections.sort(testQuestions, new Comparator<Question>() {
                    @Override
                    public int compare(Question p1, Question p2) {
                        return p1.getDate_created().compareTo(p2.getDate_created());
                    }
                });

                for (int i = 0; i < testQuestions.size(); i++) {
                    testAnswers.add(new Answer());
                }

                for (int i = 0; i < testQuestions.size(); i++) {
                    getAnswersFirestore(testQuestions.get(i).getQuestion_id(), i);
                }


                recyclerViewAdapter =
                        new HwReviewAdapter(mContext, testQuestions, testAnswers, testType, testId, userId);
                recyclerView.setAdapter(recyclerViewAdapter);

            }
        });
    }

    public void getAnswersFirestore(final String question_id, final int i) {
        db.collection("answers")
                .whereEqualTo("question_id", question_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {  // no such answer on the database
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (Objects.equals(document.getString("user_id"), userId)) {
                                    Answer answer = document.toObject(Answer.class);
                                    testAnswers.set(i, answer);
                                }

                            }
                        }
                        recyclerViewAdapter.notifyItemChanged(i);
                    }

                });

    }


    // ================= Listeners ===================

    private void ivBackArrowLogic() {
        ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(LOG, "onClick: closing the activity");
                finish();
            }
        });
    }

    private void signoutBtnLogic() {
        signoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(LOG, "onClick: Signing out the user");
                Toast.makeText(mContext, "Signing out the user", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HwReviewActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                mAuth.signOut();
                startActivity(intent);
            }
        });
    }


}
