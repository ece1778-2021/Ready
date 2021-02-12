package me.nirmit.ready;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class StudentMainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StudentAssgListAdapter recyclerViewAdapter;
    private List<String> assgList;
    private static final String LOG = StudentMainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

        assgList = new ArrayList<>();
        recyclerView = (RecyclerView)findViewById(R.id.student_assg_list_card);
        recyclerViewAdapter = new StudentAssgListAdapter(assgList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerViewAdapter.setOnItemClickListener(new StudentAssgListAdapter.ClickListener<String>(){
            @Override
            public void onItemClick(String data) {
                Log.d(LOG, data);
            }
        });
        recyclerView.setAdapter(recyclerViewAdapter);
    }

}
