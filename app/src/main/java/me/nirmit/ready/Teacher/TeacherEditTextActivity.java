package me.nirmit.ready.Teacher;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import me.nirmit.ready.Login.MainActivity;
import me.nirmit.ready.R;

public class TeacherEditTextActivity  extends AppCompatActivity {
    private static final String LOG = TeacherEditTextActivity.class.getSimpleName();
    private TextView topBarTitle;
    private CheckBox scoreCheckBox;
    private CheckBox statusCheckBox;
    private TextView message;
    private Button cancelButton;
    private Button sendButton;
    private ImageView ivBackArrow, signoutBtn;
    private Context mContext;
    private String mark;
    private String status;


    // Firebase stuff
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_edit_messg);
        topBarTitle = (TextView) findViewById(R.id.topBarTitle);
        topBarTitle.setText("Edit Message");

        mContext = TeacherEditTextActivity.this;
        scoreCheckBox = findViewById(R.id.score_checkbox);
        statusCheckBox = findViewById(R.id.submit_checkbox);
        message = findViewById(R.id.text_message);
        cancelButton = findViewById(R.id.cancel_button);
        sendButton = findViewById(R.id.send_button);
        ivBackArrow = (ImageView) findViewById(R.id.backArrow);
        signoutBtn = (ImageView) findViewById(R.id.signout);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mark = bundle.getString("mark");
        status = bundle.getString("status");
        Log.d(LOG, mark);

        scoreCheckBoxLogic();
        statusCheckBoxLogic();
        cancelButtonLogic();
        sendButtonLogic();
        ivBackArrowLogic();
        signoutBtnLogic();
        setupFirebaseAuth();

    }

    public void scoreCheckBoxLogic() {
        scoreCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((CompoundButton) view).isChecked()){
                    String newString = message.getText().toString() + mark;
                    message.setText(newString);
                    Log.d(LOG, "score button is checked");
                } else {
                    Log.d(LOG, "score button is unchecked");
                }
            }
        });
    }

    public void statusCheckBoxLogic() {
        statusCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((CompoundButton) view).isChecked()){
                    String newString = message.getText().toString() + status;
                    message.setText(newString);
                    Log.d(LOG, "status button is checked");
                } else {
                    Log.d(LOG, "status button is unchecked");
                }
            }
        });
    }

    public void cancelButtonLogic() {
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(LOG, "onClick: closing the activity");
                finish();
            }
        });
    }

    public void sendButtonLogic() {
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: send message
                Log.d(LOG, "onClick: closing the activity");
                finish();
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
                Intent intent = new Intent(TeacherEditTextActivity.this, MainActivity.class);
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
