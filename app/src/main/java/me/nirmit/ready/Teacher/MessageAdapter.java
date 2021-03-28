package me.nirmit.ready.Teacher;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import me.nirmit.ready.R;
import me.nirmit.ready.Student.StudentSubmissionActivity;
import me.nirmit.ready.Util.FirebaseMethods;
import me.nirmit.ready.models.User;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.RecyclerViewHolder>{
    private static final String LOG = MessageAdapter.class.getSimpleName();
    private static final String TAG = "MessageAdapter";

    private List<User> nameList;
    private List<Double> markList;
    private List<Boolean> statusList;
    private String testType;
    private String testId;
    private Context mContext;

    // Firebase stuff
    private FirebaseAuth mAuth;
    private FirebaseMethods firebaseMethods;
    private FirebaseFirestore db;


    public MessageAdapter(Context context, List<User> nameList, List<Double> markList,
                          List<Boolean> statusList, String testId, String testType){
        firebaseMethods = new FirebaseMethods(context);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        this.nameList = nameList;
        this.markList = markList;
        this.statusList = statusList;
        this.testType = testType;
        this.testId = testId;

    }

    @Override
    public MessageAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.teacher_message_adapter, parent, false);
        mContext = parent.getContext();
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageAdapter.RecyclerViewHolder holder, final int position) {
        if (nameList.size() != 0) {
            final String name = nameList.get(position).getName();
            holder.studentName.setText(name);
        }

        if (statusList.size() != 0 && markList.size() != 0 && position < statusList.size()
                && position < markList.size()) {

            final Boolean studentStatus = statusList.get(position);
            final String studentMark = "Mark: " + markList.get(position) + "\n";

            String submitStatus;
            if (studentStatus) {
                submitStatus = "Submitted \n";
                if (testType.equals("test")) {
                    holder.mark.setText(studentMark);
                }
                else {
                    holder.mark.setVisibility(View.INVISIBLE);
                }
            }
            else {
                submitStatus = "Not Submitted \n";
                holder.mark.setVisibility(View.INVISIBLE);
            }
            final String newStatus = submitStatus;
            holder.status.setText(newStatus);
        }

        // Color code card views
        if (holder.status.getText().toString().equals("Not Submitted \n")) {
            holder.cardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.not_submitted));
        } else if (holder.status.getText().toString().equals("Submitted \n")) {
            holder.cardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.submitted));
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String userId = nameList.get(position).getUser_id();
                final boolean status = statusList.get(position);
                if (status) {
                    Intent intent = new Intent(mContext, HwReviewActivity.class);
                    intent.putExtra("PUBLISHED_TEST_FIREBASE_ID", testId);
                    intent.putExtra("TEST_TYPE", testType);
                    intent.putExtra("USER_ID", userId);
                }
                else {
                    Toast.makeText(mContext, "Student not submit the answers yet", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return nameList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView studentName;
        private TextView mark;
        private TextView status;
        private Button messgButton;
        private CardView cardView;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            studentName = itemView.findViewById(R.id.messg_student_name);
            mark = itemView.findViewById(R.id.student_mark);
            status = itemView.findViewById(R.id.assg_status);
            messgButton = itemView.findViewById(R.id.messg_button);
            cardView = itemView.findViewById(R.id.messg_student_card);

            messgButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), TeacherEditTextActivity.class);
                    intent.putExtra("mark", mark.getText().toString());
                    intent.putExtra("status", status.getText().toString());
                    view.getContext().startActivity(intent);
                }
            });

        }
    }
}