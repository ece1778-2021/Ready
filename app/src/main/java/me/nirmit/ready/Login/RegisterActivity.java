package me.nirmit.ready.Login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import me.nirmit.ready.R;
import me.nirmit.ready.Student.StudentMainActivity;
import me.nirmit.ready.Teacher.TeacherAddQuizActivity;
import me.nirmit.ready.Util.FirebaseMethods;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private Context mContext;
    private TextView topBarTitle;
    private EditText name, email, password, conPassword, ID, phone;
    private ImageView signoutBtn;
    private Button signUp;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private ProgressBar mProgressBar;

    // Firebase stuff
    private FirebaseAuth mAuth;
    private FirebaseMethods firebaseMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        topBarTitle = (TextView) findViewById(R.id.topBarTitle);
        topBarTitle.setText("Register");
        signoutBtn = (ImageView) findViewById(R.id.signout);
        signoutBtn.setVisibility(View.GONE);

        firebaseMethods = new FirebaseMethods(RegisterActivity.this);
        mContext = RegisterActivity.this;
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        conPassword = findViewById(R.id.pw2);
        name = findViewById(R.id.name);
        ID = findViewById(R.id.id);
        phone = findViewById(R.id.phone);
        signUp = findViewById(R.id.signup_button);
        radioGroup = findViewById(R.id.radio_group);
        mProgressBar = findViewById(R.id.registerProgressBar);
        mProgressBar.setVisibility(View.GONE);

        signUpLogic();
        setupFirebaseAuth();

    }

    public boolean validate() {
        if (password.getText().toString().equals("") ||
                email.getText().toString().equals("") ||
                conPassword.getText().toString().equals("") ||
                name.getText().toString().equals("") ||
                ID.getText().toString().equals("") ||
                phone.getText().toString().equals("")){
            mProgressBar.setVisibility(View.GONE);
            Toast.makeText(mContext, "Missing Text", Toast.LENGTH_SHORT).show();
            return false;
        } else if (radioButton == null) {
            mProgressBar.setVisibility(View.GONE);
            Toast.makeText(mContext, "Select Mode: Teacher or Student", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!password.getText().toString().equals(conPassword.getText().toString())) {
            mProgressBar.setVisibility(View.GONE);
            Toast.makeText(mContext, "Password does not match!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public void onRadioButtonClicked(View view) {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(selectedId);
        Log.d(TAG, radioButton.getText().toString());
    }

    private void signUpLogic() {
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressBar.setVisibility(View.VISIBLE);

                if (validate()) {
                    String mode = radioButton.getText().toString();

                    firebaseMethods.registerNewEmail(
                            email.getText().toString(),
                            password.getText().toString());

                    if (mode.equals("Teacher")) {
                        Intent intent = new Intent(mContext, TeacherAddQuizActivity.class);
                        startActivity(intent);
                    } else { // Student mode
                        Intent intent = new Intent(mContext, StudentMainActivity.class);
                        startActivity(intent);
                    }

                    mProgressBar.setVisibility(View.GONE);
                    finish();
                } else Log.e(TAG, "invalid user info");
            }
        });
    }

    // ============= Firebase Methods & Logic ===============

    private void setupFirebaseAuth() {
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
    }

}
