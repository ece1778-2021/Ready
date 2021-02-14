package me.nirmit.ready.Teacher;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import me.nirmit.ready.R;

public class TeacherAddQuizActivity extends AppCompatActivity {

    private static final String TAG = "TeacherAddQuizActivity";
    
    RecyclerView recyclerView;
    QuizAdapter quizAdapter;
    ArrayList<String> quizNames;
    private TextView topBarTitle;
    private Button btnAddQuiz;
    private ImageView ivBackArrow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_add_quiz);

        topBarTitle = (TextView) findViewById(R.id.topBarTitle);
        topBarTitle.setText("Assessments");
        btnAddQuiz = (Button) findViewById(R.id.btnAddQuiz);
        ivBackArrow = (ImageView) findViewById(R.id.backArrow);

        quizNames = new ArrayList<>();
        quizNames.add("Test 1");
        quizNames.add("Test 2");
        quizNames.add("Test 3");

        recyclerView = findViewById(R.id.rcvQuizzes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        quizAdapter = new QuizAdapter(this,  quizNames);
        recyclerView.setAdapter(quizAdapter);

        btnAddQuizLogic();
        ivBackArrowLogic();
    }

    private void btnAddQuizLogic() {
        btnAddQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddQuizDialog();
            }
        });
    }

    private void ivBackArrowLogic() {
        ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: closing the activity");
                finish();
            }
        });
    }


    /**
     * =============== POPUP DIALOG ================
     */
    private void showAddQuizDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.custom_quiz_dialog, null);

        final EditText quizName = view.findViewById(R.id.assessmentName);
        final CheckBox isHW = view.findViewById(R.id.checkboxHW);
        final CheckBox isUnitTest = view.findViewById(R.id.checkBoxUnitTest);
        Button acceptButton = view.findViewById(R.id.acceptButton);
        Button cancelButton = view.findViewById(R.id.cancelButton);
        final TextView selectTime = view.findViewById(R.id.selectTime);
        final TextView selectDate = view.findViewById(R.id.selectDate);

        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();
        alertDialog.show();

        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        TeacherAddQuizActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                month = month + 1;
                                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                                String date = month + "/" + day + "/" + year;
                                selectDate.setText(date);
                            }},
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        selectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int hours = cal.get(Calendar.HOUR_OF_DAY);
                int mins = cal.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        TeacherAddQuizActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                Calendar c = Calendar.getInstance();
                                c.set(Calendar.HOUR_OF_DAY, i);
                                c.set(Calendar.MINUTE, i1);
                                c.setTimeZone(TimeZone.getDefault());
                                SimpleDateFormat format = new SimpleDateFormat("k:mm a");
                                String time = format.format(c.getTime());
                                selectTime.setText(time);
                            }
                        }, hours, mins, false);
                timePickerDialog.show();
            }
        });

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: accept button");

                if (quizName.getText().toString().isEmpty()) {
                    Toast.makeText(TeacherAddQuizActivity.this, "Enter assessment name",
                            Toast.LENGTH_SHORT).show();
                } else if (isHW.isChecked() && isUnitTest.isChecked()) {
                    Toast.makeText(TeacherAddQuizActivity.this, "Select ONE assessment " +
                            "type only", Toast.LENGTH_SHORT).show();
                } else if (!isHW.isChecked() && !isUnitTest.isChecked()) {
                    Toast.makeText(TeacherAddQuizActivity.this, "Select assessment type ",
                            Toast.LENGTH_SHORT).show();
                } else {
                    quizNames.add(String.valueOf(quizName.getText()));
                    alertDialog.cancel();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: cancel button" );
                alertDialog.cancel();
            }
        });
    }

}
