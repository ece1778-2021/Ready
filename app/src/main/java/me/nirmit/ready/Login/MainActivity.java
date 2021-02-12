package me.nirmit.ready.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import me.nirmit.ready.R;
import me.nirmit.ready.Student.StudentCourses;
import me.nirmit.ready.Teacher.TeacherQuizes;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Context mContext;
    private Button btnLogin;
    private Button btnSignup;
    private EditText mEmail, mPassword;
    private ProgressBar mProgressBar;
    private Button tempTeacher, tempStudent; // TODO: delete

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = MainActivity.this;
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnSignup = (Button) findViewById(R.id.btn_signup);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mEmail = (EditText) findViewById(R.id.input_email);
        mPassword = (EditText) findViewById(R.id.input_password);
        //TODO: delete the following
        tempTeacher = (Button) findViewById(R.id.teacherBtn);
        tempStudent = (Button) findViewById(R.id.studentBtn);

        mProgressBar.setVisibility(View.GONE);
        signupBtnLogic();
        loginBtnLogic();
        tempBtnLogic();
    }

    private void signupBtnLogic() {
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // [TODO] add logic here
            }
        });
    }

    private void loginBtnLogic() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // [TODO] add logic here
            }
        });
    }

    // TODO: delete ones layouts completed
    private void tempBtnLogic() {
        tempTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, TeacherQuizes.class);
                startActivity(intent);
            }
        });

        tempStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, StudentCourses.class);
                startActivity(intent);
            }
        });
    }

}
