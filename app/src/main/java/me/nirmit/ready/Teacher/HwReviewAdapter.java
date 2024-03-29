package me.nirmit.ready.Teacher;

import android.content.Context;
import android.content.Intent;
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
import me.nirmit.ready.models.Answer;
import me.nirmit.ready.models.Question;

public class HwReviewAdapter extends RecyclerView.Adapter<HwReviewAdapter.RecyclerViewHolder>{
    private static final String LOG = HwReviewAdapter.class.getSimpleName();
    private static final String TAG = "HwReviewAdapter";

    private List<Question> questions;
    private List<Answer> answers;
    private String testType;
    private Context mContext;

    // Firebase stuff
    private FirebaseAuth mAuth;
    private FirebaseMethods firebaseMethods;
    private FirebaseFirestore db;


    public HwReviewAdapter(Context context, List<Question> questions, List<Answer> answers, String testType){
        firebaseMethods = new FirebaseMethods(context);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        this.questions = questions;
        this.answers = answers;
        this.testType = testType;

    }

    @Override
    public HwReviewAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.teacher_review_adapter, parent, false);
        mContext = parent.getContext();
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HwReviewAdapter.RecyclerViewHolder holder, final int position) {
        final String topic = questions.get(position).getTopic();
        holder.topic.setText(topic);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String questionImage = questions.get(position).getImage_path();
                final String questionText = questions.get(position).getQuestion_text();
                final String questionAns = questions.get(position).getAnswer();

                final String studentImage = answers.get(position).getImage_path();
                final String studentAns = answers.get(position).getAnswer();

                Intent intent = new Intent(mContext, HwQuestionActivity.class);
                intent.putExtra("questionImage", questionImage);
                intent.putExtra("questionText", questionText);
                intent.putExtra("questionAns", questionAns);
                intent.putExtra("studentImage", studentImage);
                intent.putExtra("studentAns", studentAns);
                intent.putExtra("TEST_TYPE", testType);

                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView topic;
        private CardView cardView;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            topic = itemView.findViewById(R.id.hw_question_topic);
            cardView = itemView.findViewById(R.id.review_question_card);

        }
    }
}