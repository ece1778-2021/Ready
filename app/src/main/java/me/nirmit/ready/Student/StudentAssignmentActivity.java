package me.nirmit.ready.Student;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import me.nirmit.ready.R;

public class StudentAssignmentActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StudentAssgAdapter recyclerViewAdapter;
    private List<String> assgList;
    private TextView topBarTitle;
    private static final String LOG = StudentAssignmentActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_assg);

        topBarTitle = (TextView) findViewById(R.id.topBarTitle);
        topBarTitle.setText("Assignments");

        assgList = new ArrayList<>();

        /*TODO DELETE; Temp */
        assgList.add("Question 1: a = 1, b = 1, a + b = ?");
        assgList.add("Question 2: a = 2, b = 3, a + b = ?");

        recyclerView = findViewById(R.id.student_qt_rv);
        recyclerViewAdapter = new StudentAssgAdapter(assgList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerViewAdapter.setOnItemClickListener(new StudentAssgAdapter.ClickListener<String>(){
            @Override
            public void onItemClick(String data) {
                Log.d(LOG, data);
            }
        });
        recyclerView.setAdapter(recyclerViewAdapter);
    }

}
