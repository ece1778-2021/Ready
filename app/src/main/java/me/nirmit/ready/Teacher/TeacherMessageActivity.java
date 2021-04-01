package me.nirmit.ready.Teacher;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import java.util.List;
import java.util.Objects;
import me.nirmit.ready.Login.MainActivity;
import me.nirmit.ready.R;
import me.nirmit.ready.Student.StudentMainActivity;
import me.nirmit.ready.Util.FirebaseMethods;
import me.nirmit.ready.models.Question;
import me.nirmit.ready.models.User;

public class TeacherMessageActivity extends AppCompatActivity {

    private static final String TAG = "TeacherMessageActivity";
    private static final String LOG = StudentMainActivity.class.getSimpleName();

    private RecyclerView recyclerView;
    private TextView topBarTitle;
    private MessageAdapter messageAdapter;
    private List<User> nameList;
    private List<Double> markList;
    private List<Boolean> statusList;
    private ProgressBar progressBar;
    private BottomNavigationView bottomView;
    private ImageView ivBackArrow, signoutBtn;
    private Context mContext;
    private String testId;
    private String testPublished;
    private String testType;


    // Firebase stuff
    private FirebaseAuth mAuth;
    private FirebaseMethods firebaseMethods;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_messg);
        topBarTitle = (TextView) findViewById(R.id.topBarTitle);
        topBarTitle.setText("Message Page");

        firebaseMethods = new FirebaseMethods(TeacherMessageActivity.this);
        mContext = TeacherMessageActivity.this;
        progressBar = findViewById(R.id.messg_progressbar);
        progressBar.setVisibility(View.VISIBLE);
        bottomView = findViewById(R.id.bottom_navigation);
        ivBackArrow = (ImageView) findViewById(R.id.backArrow);
        signoutBtn = (ImageView) findViewById(R.id.signout);

        nameList = new ArrayList<>();
        markList = new ArrayList<>();
        statusList = new ArrayList<>();
        testId = getIntent().getStringExtra("QUIZ_FIREBASE_ID");
        testPublished = getIntent().getStringExtra("IS_QUIZ_PUBLISHED");
        testType = getIntent().getStringExtra("TEST_TYPE");


        setupFirebaseAuth();
        ivBackArrowLogic();
        signoutBtnLogic();
        setupMessageAdapterWithFirestore();
        bottomViewListener();

        progressBar.setVisibility(View.INVISIBLE);

    }


    // ============= Listener =================
    public void bottomViewListener() {
        bottomView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.quiz_button:
                        Intent intent = new Intent(TeacherMessageActivity.this, TeacherQuizQuestionsActivity.class);
                        intent.putExtra("QUIZ_FIREBASE_ID", testId);
                        intent.putExtra("IS_QUIZ_PUBLISHED", testPublished);
                        startActivity(intent);
                        break;
                    case R.id.message_button:
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
                Intent intent = new Intent(TeacherMessageActivity.this, MainActivity.class);
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

    private void setupMessageAdapterWithFirestore() {

        CollectionReference all_users = db.collection("users");
        Query query = all_users.whereEqualTo("mode", "Student");

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e(TAG, "onEvent: Listen failed:" + error);
                    return;
                }

                nameList.clear();
                markList.clear();
                statusList.clear();

                for (DocumentSnapshot document : value) {
                    final User user = document.toObject(User.class);
                    nameList.add(user);
                    final String test_id = getIntent().getStringExtra("QUIZ_FIREBASE_ID");
                    Log.d(TAG, ""+getIntent().getStringExtra("QUIZ_FIREBASE_ID"));

                    db.collection("marks")
                            .whereEqualTo("test_id", test_id)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    boolean found = false;
                                    if (task.isSuccessful()) {
                                        if (task.getResult() != null &&
                                                !task.getResult().isEmpty()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                if (Objects.equals(document.getString("user_id"), user.getUser_id())) {
                                                    found = true;
                                                    markList.add(document.getDouble("mark"));
                                                    statusList.add(true);
                                                }
                                            }
                                        }
                                        if (!found)  {
                                            markList.add(0.);
                                            statusList.add(false);
                                        }
                                        recyclerView = findViewById(R.id.messg_student_card);
                                        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                                        messageAdapter = new MessageAdapter(mContext, nameList, markList, statusList, test_id, testType);
                                        recyclerView.setAdapter(messageAdapter);
                                    }

                                }

                            });


                }

            }
        });
    }
}