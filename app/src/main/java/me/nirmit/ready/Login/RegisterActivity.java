package me.nirmit.ready.Login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import me.nirmit.ready.R;
import me.nirmit.ready.Student.StudentMainActivity;
import me.nirmit.ready.Teacher.TeacherAddQuizActivity;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private Context mContext;
    private TextView topBarTitle;
    private EditText email;
    private EditText password;
    private EditText conPassword;
    private EditText name;
    private EditText ID;
    private EditText phone;
    private Button signUp;
    private RadioGroup radioGroup;
    private RadioButton radioButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        topBarTitle = (TextView) findViewById(R.id.topBarTitle);
        topBarTitle.setText("Register");

        mContext = RegisterActivity.this;
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        conPassword = findViewById(R.id.pw2);
        name = findViewById(R.id.name);
        ID = findViewById(R.id.id);
        phone = findViewById(R.id.phone);
        signUp = findViewById(R.id.signup_button);
        radioGroup = findViewById(R.id.radio_group);
        signUpLogic();

    }

    public boolean validate() {
        boolean valid = true;
        if (radioButton == null) {
            valid = false;
        }

        //TODO: validate all information is entered and valid
        return valid;
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
                if (validate()) {
                    String mode = radioButton.getText().toString();
                    if (mode.equals("Teacher")) {
                        Intent intent = new Intent(mContext, TeacherAddQuizActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Intent intent = new Intent(mContext, StudentMainActivity.class);
                        startActivity(intent);
                    }
                }
                else Log.d(TAG, "invalid user info");
            }
        });
    }

}
