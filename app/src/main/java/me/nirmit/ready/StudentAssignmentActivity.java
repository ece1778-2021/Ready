package me.nirmit.ready;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StudentAssignmentActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StudentAssgAdapter recyclerViewAdapter;
    private List<String> assgList;
    private static final String LOG = StudentAssignmentActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_assg);
    }

}
