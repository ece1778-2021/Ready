package me.nirmit.ready.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import me.nirmit.ready.R;
import me.nirmit.ready.Student.StudentMainActivity;
import me.nirmit.ready.Teacher.TeacherAddQuizActivity;
import me.nirmit.ready.Util.FirebaseMethods;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Context mContext;
    private Button btnLogin;
    private Button btnSignup;
    private EditText mEmail, mPassword;
    private ProgressBar mProgressBar;

    // Firebase stuff
    private FirebaseAuth mAuth;
    private FirebaseMethods firebaseMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseMethods = new FirebaseMethods(MainActivity.this);
        mContext = MainActivity.this;
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnSignup = (Button) findViewById(R.id.btn_signup);
        mProgressBar = (ProgressBar) findViewById(R.id.student_assg_progressbar);
        mEmail = (EditText) findViewById(R.id.input_email);
        mPassword = (EditText) findViewById(R.id.input_password);

        mProgressBar.setVisibility(View.GONE);
        setupFirebaseAuth();
        signupBtnLogic();
        loginBtnLogic();

    }

    private void signupBtnLogic() {
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean isStringNull(String string) {
        Log.d(TAG, "isStringNUll: check NULL string");
        if (string.equals("")){ return true; }
        return false;
    }

    private void loginBtnLogic() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();

                if (isStringNull(email) || isStringNull(password)) {
                    Toast.makeText(mContext, "Wrong login info!", Toast.LENGTH_SHORT).show();
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);

                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {
                                        try {
                                            Log.d(TAG, "Email is in database");
                                            firebaseMethods.goToFirstModePage(mAuth.getCurrentUser().getUid());

                                        } catch (NullPointerException e) {
                                            Log.e(TAG, "onComplete: NullPointerException");
                                        }
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(MainActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                    mProgressBar.setVisibility(View.GONE);
                                }
                            });
                }
            }
        });
    }

    // ============= Firebase Methods & Logic ===============

    private void setupFirebaseAuth() {
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
    }

    private void checkCurrentUser(FirebaseUser user) {
        Log.d(TAG, "checkCurrentUser: checking to see if user is signed in!");

        if (user == null) {
            Log.e(TAG, " User = NULL");
        } else {
            Log.d(TAG, "User is not null: " + user.getUid());
            firebaseMethods.goToFirstModePage(user.getUid());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        checkCurrentUser(currentUser);
    }


}
