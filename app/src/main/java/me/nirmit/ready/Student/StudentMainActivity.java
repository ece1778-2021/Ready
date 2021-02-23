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

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import me.nirmit.ready.Login.MainActivity;
import me.nirmit.ready.R;

public class StudentMainActivity extends AppCompatActivity {
    private static final String LOG = StudentMainActivity.class.getSimpleName();
    private static final String TAG = "StudentMainActivity";

    private RecyclerView recyclerView;
    private StudentAssgListAdapter recyclerViewAdapter;
    private List<String> assgList;
    private TextView topBarTitle;
    private ProgressBar progressBar;
    private ImageView ivBackArrow, signoutBtn;
    private Context mContext;

    // Firebase stuff
    private FirebaseAuth mAuth;

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

        assgList = new ArrayList<>();

        mContext = StudentMainActivity.this;
        progressBar = findViewById(R.id.student_main_progressbar);
        progressBar.setVisibility(View.VISIBLE);

        setupFirebaseAuth();
        ivBackArrowLogic();
        signoutBtnLogic();

        /*TO Delete*/
        assgList.add("Test");
        assgList.add("Test2");

        recyclerView = findViewById(R.id.student_card);
        recyclerViewAdapter = new StudentAssgListAdapter(assgList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerViewAdapter.setOnItemClickListener(new StudentAssgListAdapter.ClickListener<String>(){
            @Override
            public void onItemClick(String data) {
                Intent intent = new Intent(StudentMainActivity.this, StudentAssignmentActivity.class);
                //TODO: putextra
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(recyclerViewAdapter);
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
    }

}