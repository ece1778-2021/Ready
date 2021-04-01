package me.nirmit.ready.Teacher;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.nirmit.ready.R;
import me.nirmit.ready.Util.FirebaseMethods;
import me.nirmit.ready.models.Test;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<Test> assessments;
    private Context mContext;

    // Firebase stuff
    private FirebaseAuth mAuth;
    private FirebaseMethods firebaseMethods;
    private FirebaseFirestore db;

    QuizAdapter(Context context, List<Test> assessments) {
        firebaseMethods = new FirebaseMethods(context);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        this.layoutInflater = LayoutInflater.from(context);
        this.assessments = assessments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.teacher_custom_quiz_view, parent, false);
        mContext = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        // bind the textview with data received
        String quiz = assessments.get(position).getTestname();
        holder.quizName.setText(quiz);
        String quizFirebaseId = assessments.get(position).getTest_id();
        holder.quizFirebaseId.setText(quizFirebaseId);

        // set cardview background color
        if(assessments.get(position).getType().equals("hw")) {
            holder.quizCard.setCardBackgroundColor(mContext.getResources().getColor(R.color.hw_color));
        } else {
            holder.quizCard.setCardBackgroundColor(mContext.getResources().getColor(R.color.quiz_color));
        }

        DocumentReference docRef = db.collection("tests")
                .document(assessments.get(position).getTest_id());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        Long status = (Long) document.get("status");
                        if (status == 0) {
                            holder.btnPublish.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Log.d("LOGGER", "No such document");
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return assessments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView quizName, quizFirebaseId;
        Button btnDelete, btnPublish;
        CardView quizCard;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            quizName = itemView.findViewById(R.id.quizName);
            quizFirebaseId = itemView.findViewById(R.id.quizFirebaseID);
            quizFirebaseId.setVisibility(View.GONE);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnPublish = itemView.findViewById(R.id.btnPublish);
            btnPublish.setVisibility(View.GONE);
            quizCard = itemView.findViewById(R.id.quizCard);

            quizCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("QUIZ CARD:", "Card for: " + quizName.getText());
                    Intent intent = new Intent(itemView.getContext(), TeacherQuizQuestionsActivity.class);
                    intent.putExtra("QUIZ_FIREBASE_ID", quizFirebaseId.getText().toString());
                    intent.putExtra("IS_QUIZ_PUBLISHED",
                            btnPublish.getVisibility() == View.VISIBLE ? "0" : "1");
                    for (int i = 0; i < assessments.size(); i++) {
                        if (assessments.get(i).getTest_id().equals(quizFirebaseId.getText().toString())) {
                            intent.putExtra("TEST_TYPE", assessments.get(i).getType());
                        }
                    }

                    itemView.getContext().startActivity(intent);
                }
            });

            btnPublish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("QUIZ CARD:", "Publishing Card for: " + quizName.getText());

                    Map<String, Object> updateStatus = new HashMap<>();
                    updateStatus.put("status", 1);
                    btnPublish.setVisibility(View.GONE);
                    db.collection("tests")
                            .document(quizFirebaseId.getText().toString())
                            .update(updateStatus);
                }
            });

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("QUIZ CARD:", "Deleting Card for: " + quizName.getText());
                    db.collection("tests")
                            .document(quizFirebaseId.getText().toString())
                            .delete();
                }
            });
        }
    }
}