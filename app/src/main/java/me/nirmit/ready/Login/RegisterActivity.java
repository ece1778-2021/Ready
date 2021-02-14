package me.nirmit.ready.Login;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import de.hdodenhof.circleimageview.CircleImageView;
import me.nirmit.ready.R;
import me.nirmit.ready.Student.StudentMainActivity;
import me.nirmit.ready.Teacher.TeacherAddQuizActivity;

public class RegisterActivity extends AppCompatActivity {

    private static final String LOG = "RegisterActivity";
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private Context mContext;
    private TextView topBarTitle;
    private EditText email;
    private EditText password;
    private EditText conPassword;
    private EditText name;
    private EditText studentID;
    private EditText phone;
    private CircleImageView pic;
    private Button signUp;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private LinearLayout linearLayout;
    private FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        topBarTitle = (TextView) findViewById(R.id.topBarTitle);
        topBarTitle.setText("Register");

        mContext = RegisterActivity.this;
        linearLayout = findViewById(R.id.student_content);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        conPassword = findViewById(R.id.pw2);
        name = findViewById(R.id.name);
        studentID = findViewById(R.id.id);
        phone = findViewById(R.id.phone);
        pic = findViewById(R.id.profile_pic);
        signUp = findViewById(R.id.signup_button);
        radioGroup = findViewById(R.id.radio_group);
        fab = findViewById(R.id.profile_fab);

        takePicLogic();
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
        if (radioButton.getText().toString().equals("Student")) {
            linearLayout.setVisibility(View.VISIBLE);
        }
        else {
            linearLayout.setVisibility(View.GONE);
        }
        Log.d(LOG, radioButton.getText().toString());
    }

    private void takePicLogic() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });
    }

    // Launch Camera Function
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            Log.d(LOG, "Error: Unable to take picture");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
        }
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
                else Log.d(LOG, "invalid user info");
            }
        });
    }





}
