package me.nirmit.ready.Teacher;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import me.nirmit.ready.Login.MainActivity;
import me.nirmit.ready.R;
import me.nirmit.ready.Util.FirebaseMethods;

public class HwQuestionActivity extends AppCompatActivity {
    private static final String TAG = "HwQuestionActivity";
    private static final String LOG = HwQuestionActivity.class.getSimpleName();

    private TextView topBarTitle;
    private ProgressBar teacherProgressBar;
    private ProgressBar studentProgressBar;
    private BottomNavigationView bottomView;
    private ImageView ivBackArrow, signoutBtn;
    private Context mContext;
    private String testType;
    private TextView teacherTextQ, teacherAns, studentAns;
    private ImageView teacherImgQ, studentImg;
    private String imagePath, questionText, teacherAnsText, ansImagePath, studentAnsText;


    // Firebase stuff
    private FirebaseAuth mAuth;
    private FirebaseMethods firebaseMethods;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_review_question);

        topBarTitle = (TextView) findViewById(R.id.topBarTitle);
        topBarTitle.setText("Question Review Page");

        firebaseMethods = new FirebaseMethods(HwQuestionActivity.this);
        mContext = HwQuestionActivity.this;
        teacherProgressBar = findViewById(R.id.teacher_review_topic_prog);
        teacherProgressBar.setVisibility(View.VISIBLE);
        studentProgressBar = findViewById(R.id.teacher_review_student_prog);
        studentProgressBar.setVisibility(View.VISIBLE);
        bottomView = findViewById(R.id.bottom_navigation);
        ivBackArrow = (ImageView) findViewById(R.id.backArrow);
        signoutBtn = (ImageView) findViewById(R.id.signout);

        teacherTextQ = findViewById(R.id.teacher_review_topic_question);
        teacherImgQ = findViewById(R.id.teacher_review_topic_img);
        studentImg = findViewById(R.id.teacher_review_student_img);
        studentAns = findViewById(R.id.teacher_review_student_ans);
        teacherAns = findViewById(R.id.teacher_review_teacher_ans);

        imagePath = getIntent().getStringExtra("questionImage");
        questionText = getIntent().getStringExtra("questionText");
        teacherAnsText = getIntent().getStringExtra("questionAns");
        ansImagePath = getIntent().getStringExtra("studentImage");
        studentAnsText = getIntent().getStringExtra("studentAns");
        testType = getIntent().getStringExtra("TEST_TYPE");

        getQuestion();
        getAnswer();
        setupFirebaseAuth();
        ivBackArrowLogic();
        signoutBtnLogic();
    }


    private void getQuestion() {
        Log.d(TAG, "Getting a question");
        if (imagePath != null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(
                    imagePath);
            storageReference.getBytes(1024*1024*7)
                    .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            teacherImgQ.setImageBitmap(bitmap);
                            teacherProgressBar.setVisibility(View.GONE);
                        }
                    });

            teacherImgQ.setVisibility(View.VISIBLE);
        } else { // text based question
            if (questionText.length() != 0) {
                teacherTextQ.setText(questionText);
                teacherTextQ.setVisibility(View.VISIBLE);
                teacherProgressBar.setVisibility(View.GONE);
            }
        }
        if (testType.equals("hw")){
            teacherAns.setVisibility(View.GONE);
        }
    }

    private void getAnswer() {
        if (ansImagePath != null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(
                    ansImagePath);
            storageReference.getBytes(1024*1024*7)
                    .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            studentImg.setImageBitmap(bitmap);
                            studentProgressBar.setVisibility(View.GONE);
                        }
                    });

            studentImg.setVisibility(View.VISIBLE);
        }

        // display answer
        if (testType.equals("test")) {
            studentAns.setText(studentAnsText);
            teacherAns.setText(teacherAnsText);
        }
        else {
            studentAns.setVisibility(View.GONE);
            teacherAns.setVisibility(View.GONE);
        }
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
                Intent intent = new Intent(HwQuestionActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                mAuth.signOut();
                startActivity(intent);
            }
        });
    }

    // ================= Firebase Method =================

    private void setupFirebaseAuth() {
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }
}
