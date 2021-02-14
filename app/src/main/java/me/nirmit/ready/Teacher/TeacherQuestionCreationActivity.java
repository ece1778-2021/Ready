package me.nirmit.ready.Teacher;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import me.nirmit.ready.R;

public class TeacherQuestionCreationActivity extends AppCompatActivity {

    private static final String TAG = "TeacherQuestionCreation";

    private TextView topBarTitle;
    private EditText etTopic, etQuestion, etAnswer;
    private Button btnTypeQuestion, btnUploadQuestion, btnSave;
    private RelativeLayout relativeLayoutQuestion;
    private ImageView ivQuestion, ivBackArrow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_question_creation);

        topBarTitle = (TextView) findViewById(R.id.topBarTitle);
        topBarTitle.setText("Question Setup");
        etTopic = (EditText) findViewById(R.id.topic);
        etQuestion = (EditText) findViewById(R.id.textQuestion);
        etAnswer = (EditText) findViewById(R.id.answer);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnTypeQuestion = (Button) findViewById(R.id.btnTypeQuestion);
        btnUploadQuestion = (Button) findViewById(R.id.btnUploadQuestion);
        ivQuestion = (ImageView) findViewById(R.id.ivQuestion);
        ivBackArrow = (ImageView) findViewById(R.id.backArrow);
        relativeLayoutQuestion = (RelativeLayout) findViewById(R.id.questionRelLayout);

        btnTypeQuestionLogic();
        btnUploadQuestionLogic();
        btnSaveLogic();
        ivBackArrowLogic();
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

}
