package me.nirmit.ready.Teacher;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import me.nirmit.ready.Login.MainActivity;
import me.nirmit.ready.R;
import me.nirmit.ready.Student.StudentMainActivity;

public class TeacherMessageActivity extends AppCompatActivity {
    private static final String LOG = StudentMainActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private TextView topBarTitle;
    private MessageAdapter messageAdapter;
    private List<String> nameList;
    private List<Double> markList;
    private List<Boolean> statusList;
    private ProgressBar progressBar;
    private BottomNavigationView bottomView;
    private ImageView ivBackArrow, signoutBtn;
    private Context mContext;

    // Firebase stuff
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_messg);
        topBarTitle = (TextView) findViewById(R.id.topBarTitle);
        topBarTitle.setText("Message Page");

        mContext = TeacherMessageActivity.this;
        progressBar = findViewById(R.id.messg_progressbar);
        progressBar.setVisibility(View.VISIBLE);
        bottomView = findViewById(R.id.bottom_navigation);
        ivBackArrow = (ImageView) findViewById(R.id.backArrow);
        signoutBtn = (ImageView) findViewById(R.id.signout);

        nameList = new ArrayList<>();
        markList = new ArrayList<>();
        statusList = new ArrayList<>();

        //TODO: Remove
        nameList.add("Irene");
        markList.add(100.);
        statusList.add(true);
        nameList.add("Bob");
        markList.add(90.);
        statusList.add(false);

        setupFirebaseAuth();
        ivBackArrowLogic();
        signoutBtnLogic();

        recyclerView = findViewById(R.id.messg_student_card);
        messageAdapter = new MessageAdapter(nameList, markList, statusList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(messageAdapter);

        messgAdapterListener();
        bottomViewListener();

        progressBar.setVisibility(View.INVISIBLE);

    }



    // ============= Listener =================

    private void messgAdapterListener() {
        messageAdapter.setOnItemClickListener(new MessageAdapter.ClickListener<String, String>(){
            @Override
            public void onItemClick(String mark, String status) {
                // TODO
                Intent intent = new Intent(TeacherMessageActivity.this, TeacherEditTextActivity.class);
                intent.putExtra("mark", mark);
                intent.putExtra("status", status);
                startActivity(intent);
            }
        });
    }

    public void bottomViewListener() {
        bottomView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.quiz_button:
                        Intent intent = new Intent(TeacherMessageActivity.this, TeacherQuizQuestionsActivity.class);
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
    }

}

