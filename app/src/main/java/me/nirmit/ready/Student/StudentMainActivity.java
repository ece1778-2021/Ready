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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import me.nirmit.ready.Login.MainActivity;
import me.nirmit.ready.R;
import me.nirmit.ready.Teacher.QuestionAdapter;
import me.nirmit.ready.Teacher.TeacherQuizQuestionsActivity;
import me.nirmit.ready.Util.FirebaseMethods;
import me.nirmit.ready.models.Question;
import me.nirmit.ready.models.Test;

public class StudentMainActivity extends AppCompatActivity {
    private static final String LOG = StudentMainActivity.class.getSimpleName();
    private static final String TAG = "StudentMainActivity";

    private RecyclerView recyclerView;
    private StudentAssgListAdapter recyclerViewAdapter;
    private TextView topBarTitle;
    private ProgressBar progressBar;
    private ImageView ivBackArrow, signoutBtn;
    private Context mContext;
    private ArrayList<Test> publishedTests;

    // Firebase stuff
    private FirebaseAuth mAuth;
    private FirebaseMethods firebaseMethods;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

        // Top bat setup
        topBarTitle = (TextView) findViewById(R.id.topBarTitle);
        topBarTitle.setText("Student Assessments");
        signoutBtn = (ImageView) findViewById(R.id.signout);
        ivBackArrow = (ImageView) findViewById(R.id.backArrow);
        ivBackArrow.setVisibility(View.GONE);

        publishedTests = new ArrayList<>();

        firebaseMethods = new FirebaseMethods(StudentMainActivity.this);
        mContext = StudentMainActivity.this;
        progressBar = findViewById(R.id.student_main_progressbar);
        progressBar.setVisibility(View.VISIBLE);

        setupFirebaseAuth();
        getStudentsTestsFirestore("");
        ivBackArrowLogic();
        signoutBtnLogic();

        progressBar.setVisibility(View.INVISIBLE);

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

    private void signoutBtnLogic() {
        signoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Signing out the user");
                Toast.makeText(mContext, "Signing out the user", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(StudentMainActivity.this, MainActivity.class);
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

    // get student tests (ones that are published by the teacher)
    public void getStudentsTestsFirestore(String course_id) {

        CollectionReference all_tests = db.collection("tests");
        Query query = all_tests.whereEqualTo("status", 1);

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null) {
                    Log.e(TAG, "onEvent: Listen failed:" + error);
                    return;
                }

                publishedTests.clear();

                for (DocumentSnapshot document : value) {
                    Test test = document.toObject(Test.class);
                    publishedTests.add(test);
                }

                // sort based on deadlines
                Collections.sort(publishedTests, new Comparator<Test>() {
                    @Override
                    public int compare(Test p1, Test p2) {
                        return p1.getDeadline_date().compareTo(p2.getDeadline_date()) &
                                p1.getDeadline_time().compareTo(p2.getDeadline_time());
                    }
                });

                recyclerView = findViewById(R.id.student_card);
                recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                StudentAssgListAdapter studentAssgListAdapter =
                        new StudentAssgListAdapter(mContext, publishedTests);
                recyclerView.setAdapter(studentAssgListAdapter);

            }
        });

    }

}