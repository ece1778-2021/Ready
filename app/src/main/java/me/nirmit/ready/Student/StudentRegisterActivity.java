package me.nirmit.ready.Student;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import me.nirmit.ready.R;

public class StudentRegisterActivity extends AppCompatActivity {

    private TextView topBarTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_register);

        topBarTitle = (TextView) findViewById(R.id.topBarTitle);
        topBarTitle.setText("Register");
    }

}
