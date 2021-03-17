package me.nirmit.ready.Student;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import me.nirmit.ready.R;
import me.nirmit.ready.Util.FirebaseMethods;
import me.nirmit.ready.models.Test;

public class StudentAssgListAdapter extends RecyclerView.Adapter<StudentAssgListAdapter.RecyclerViewHolder>{

    private static final String TAG = "StudentAssgListAdapter";

    private LayoutInflater layoutInflater;
    private List<Test> assessments;
    private Context mContext;

    // Firebase stuff
    private FirebaseAuth mAuth;
    private FirebaseMethods firebaseMethods;
    private FirebaseFirestore db;


    public StudentAssgListAdapter(Context context, List<Test> assessments){
        firebaseMethods = new FirebaseMethods(context);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        this.layoutInflater = LayoutInflater.from(context);
        this.assessments = assessments;
    }

    @Override
    public StudentAssgListAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.student_assg_list_adapter, parent, false);
        mContext = parent.getContext();
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StudentAssgListAdapter.RecyclerViewHolder holder, final int position) {

        String assignment = assessments.get(position).getTestname();
        holder.title.setText(assignment);

        String testFirebaseId = assessments.get(position).getTest_id();
        holder.firebaseTestId.setText(testFirebaseId);

        String dueDate = assessments.get(position).getDeadline_date();
        String dueTime = assessments.get(position).getDeadline_time();
        String due = "Due: " + dueDate + " " + dueTime;
        holder.dueText.setText(due);

        if(assessments.get(position).getType().equals("hw")) {
            holder.cardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.hw_color));
        } else {
            holder.cardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.quiz_color));
        }


    }


    @Override
    public int getItemCount() {
        return assessments.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView title, firebaseTestId, dueText;
        private CardView cardView;

        public RecyclerViewHolder(final View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.student_question);
            firebaseTestId = itemView.findViewById(R.id.firebaseTestId);
            firebaseTestId.setVisibility(View.GONE);
            dueText = itemView.findViewById(R.id.due_date_text);
            cardView = itemView.findViewById(R.id.student_assg_list_card);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(itemView.getContext(), StudentAssignmentActivity.class);
                    intent.putExtra("PUBLISHED_TEST_FIREBASE_ID", firebaseTestId.getText().toString());
                    // pass down the test type (test or hw)
                    for (int i = 0; i < assessments.size(); i++) {
                        if (assessments.get(i).getTest_id().equals(firebaseTestId.getText().toString())) {
                            intent.putExtra("TEST_TYPE", assessments.get(i).getType());
                        }
                    }
                    itemView.getContext().startActivity(intent);
                }
            });

        }
    }

}
