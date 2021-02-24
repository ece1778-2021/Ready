package me.nirmit.ready.Teacher;

import android.content.Context;
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

import java.util.List;

import me.nirmit.ready.R;
import me.nirmit.ready.Util.FirebaseMethods;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<String> questions, questionFirebaseIds;
    private int quetionNumber = 0;

    // Firebase stuff
    private FirebaseAuth mAuth;
    private FirebaseMethods firebaseMethods;
    private FirebaseFirestore db;

    QuestionAdapter(Context context, List<String> questions, List<String> questionFirebaseIds) {
        firebaseMethods = new FirebaseMethods(context);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        this.layoutInflater = LayoutInflater.from(context);
        this.quetionNumber = 0;
        this.questions = questions;
        this.questionFirebaseIds = questionFirebaseIds;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.teacher_custom_quiz_questions_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // bind the textview with data received
        String question = questions.get(position);
        holder.questionName.setText("Q" + (++quetionNumber) +" " + question);
        String questionFirebaseId = questionFirebaseIds.get(position);
        holder.questionFirebaseId.setText(questionFirebaseId);

    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView questionName, questionFirebaseId;
        Button btnDeleteQuestion;
        CardView questionCard;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            questionName = itemView.findViewById(R.id.questionName);
            btnDeleteQuestion = itemView.findViewById(R.id.btnDeleteQuestion);
            questionCard = itemView.findViewById(R.id.questionCard);
            questionFirebaseId = itemView.findViewById(R.id.questionFirebaseID);
            questionFirebaseId.setVisibility(View.GONE);

            questionCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("QUESTION CARD:", "Card for: " + questionName.getText());
//                    Toast.makeText(itemView.getContext(),
//                            "Question Card for: " + questionName.getText(), Toast.LENGTH_LONG).show();
                }
            });

            btnDeleteQuestion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("QUESTION CARD:", "Deleting Card for: " + questionName.getText());
                    db.collection("questions")
                            .document(questionFirebaseId.getText().toString())
                            .delete();
                }
            });
        }
    }

}
