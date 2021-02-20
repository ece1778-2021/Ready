package me.nirmit.ready.Teacher;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import me.nirmit.ready.Login.MainActivity;
import me.nirmit.ready.R;

public class TeacherQuestionCreationActivity extends AppCompatActivity {

    private static final String TAG = "TeacherQuestionCreation";

    private TextView topBarTitle;
    private EditText etTopic, etQuestion, etAnswer;
    private Button btnTypeQuestion, btnUploadQuestion, btnSave;
    private RelativeLayout relativeLayoutQuestion;
    private ImageView ivQuestion, ivBackArrow, signoutBtn;
    private Context mContext;

    // Firebase stuff
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_question_creation);

        // Top bar setup
        topBarTitle = (TextView) findViewById(R.id.topBarTitle);
        topBarTitle.setText("Question Setup");
        ivBackArrow = (ImageView) findViewById(R.id.backArrow);
        signoutBtn = (ImageView) findViewById(R.id.signout);

        mContext = TeacherQuestionCreationActivity.this;
        etTopic = (EditText) findViewById(R.id.topic);
        etQuestion = (EditText) findViewById(R.id.textQuestion);
        etAnswer = (EditText) findViewById(R.id.answer);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnTypeQuestion = (Button) findViewById(R.id.btnTypeQuestion);
        btnUploadQuestion = (Button) findViewById(R.id.btnUploadQuestion);
        ivQuestion = (ImageView) findViewById(R.id.ivQuestion);
        relativeLayoutQuestion = (RelativeLayout) findViewById(R.id.questionRelLayout);

        setupFirebaseAuth();
        btnTypeQuestionLogic();
        btnUploadQuestionLogic();
        btnSaveLogic();
        ivBackArrowLogic();
        signoutBtnLogic();
    }

    private void btnSaveLogic() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Saving question");
            }
        });
    }

    private void btnUploadQuestionLogic() {
        btnUploadQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Uploading Question");
                relativeLayoutQuestion.setVisibility(View.VISIBLE);
                etQuestion.setVisibility(View.GONE);
                ivQuestion.setVisibility(View.VISIBLE);
                ivQuestion.setImageResource(R.drawable.ic_launcher_background);
            }
        });
        
    }

    private void btnTypeQuestionLogic() {
        btnTypeQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Typeing question");
                relativeLayoutQuestion.setVisibility(View.VISIBLE);
                etQuestion.setVisibility(View.VISIBLE);
                ivQuestion.setVisibility(View.GONE);
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

    private void signoutBtnLogic() {
        signoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Signing out the user");
                Toast.makeText(mContext, "Signing out the user", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(TeacherQuestionCreationActivity.this, MainActivity.class);
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
