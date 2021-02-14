package me.nirmit.ready.Student;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import me.nirmit.ready.R;

public class StudentMainActivity extends AppCompatActivity {
    private static final String LOG = StudentMainActivity.class.getSimpleName();

    private RecyclerView recyclerView;
    private StudentAssgListAdapter recyclerViewAdapter;
    private List<String> assgList;
    private TextView topBarTitle;
    private ProgressBar progressBar;
    private ImageView ivBackArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

        topBarTitle = (TextView) findViewById(R.id.topBarTitle);
        topBarTitle.setText("Main Activity");

        assgList = new ArrayList<>();

        progressBar = findViewById(R.id.student_main_progressbar);
        progressBar.setVisibility(View.VISIBLE);
        ivBackArrow = (ImageView) findViewById(R.id.backArrow);

        ivBackArrowLogic();

        /*TO Delete*/
        assgList.add("Test");
        assgList.add("Test2");

        recyclerView = findViewById(R.id.student_card);
        recyclerViewAdapter = new StudentAssgListAdapter(assgList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerViewAdapter.setOnItemClickListener(new StudentAssgListAdapter.ClickListener<String>(){
            @Override
            public void onItemClick(String data) {
                Log.d(LOG, data);
                Intent intent = new Intent(StudentMainActivity.this, StudentAssignmentActivity.class);
                //TODO: putextra
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(recyclerViewAdapter);
        progressBar.setVisibility(View.INVISIBLE);

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

}