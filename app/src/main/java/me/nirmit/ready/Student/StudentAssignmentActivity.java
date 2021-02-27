package me.nirmit.ready.Student;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.List;

import me.nirmit.ready.Login.MainActivity;
import me.nirmit.ready.R;
import me.nirmit.ready.Util.FirebaseMethods;
import me.nirmit.ready.models.Question;
import me.nirmit.ready.models.Test;

public class StudentAssignmentActivity extends AppCompatActivity {
    
    private static final String LOG = StudentAssignmentActivity.class.getSimpleName();
    private static final String TAG = "StudentAssignmentActivi";

    private RecyclerView recyclerView;
    private StudentAssgAdapter recyclerViewAdapter;
    private List<String> assgList;
    private ArrayList<Question> testQuestions;

    private TextView topBarTitle;
    private ProgressBar progressBar;
    private ImageView ivBackArrow, signoutBtn;
    private Context mContext;

    // Firebase stuff
    private FirebaseAuth mAuth;
    private FirebaseMethods firebaseMethods;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_assg);

        // Top Bar setup
        topBarTitle = (TextView) findViewById(R.id.topBarTitle);
        topBarTitle.setText("Assignments");
        signoutBtn = (ImageView) findViewById(R.id.signout);
        ivBackArrow = (ImageView) findViewById(R.id.backArrow);

        assgList = new ArrayList<>();
        testQuestions = new ArrayList<>();

        firebaseMethods = new FirebaseMethods(StudentAssignmentActivity.this);
        mContext = StudentAssignmentActivity.this;
        progressBar = findViewById(R.id.student_assg_progressbar);
        progressBar.setVisibility(View.VISIBLE);

        setupFirebaseAuth();
        ivBackArrowLogic();
        signoutBtnLogic();
        getTestQuestionsFirestore(
                getIntent().getStringExtra("PUBLISHED_TEST_FIREBASE_ID"),
                getIntent().getStringExtra("TEST_TYPE"));

        progressBar.setVisibility(View.INVISIBLE);
    }

    private void signoutBtnLogic() {
        signoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Signing out the user");
                Toast.makeText(mContext, "Signing out the user", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(StudentAssignmentActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                mAuth.signOut();
                startActivity(intent);
            }
        });

    }

    private void ivBackArrowLogic() {
        ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(LOG, "onClick: closing the activity");
                finish();
            }
        });
    }

    // ============= Firebase Methods & Logic ===============

    private void setupFirebaseAuth() {
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    // get test questions
    public void getTestQuestionsFirestore(String testId, final String testType) {

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

                for (DocumentSnapshot document : value) {
                    Question question = document.toObject(Question.class);
                    testQuestions.add(question);
                }

                recyclerView = findViewById(R.id.student_qt_rv);
                recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                StudentAssgAdapter studentAssgAdapter =
                        new StudentAssgAdapter(mContext, testQuestions, testType);
                recyclerView.setAdapter(studentAssgAdapter);
            }
        });
    }



}
