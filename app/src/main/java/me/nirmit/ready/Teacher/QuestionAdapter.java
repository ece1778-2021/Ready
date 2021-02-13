package me.nirmit.ready.Teacher;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import me.nirmit.ready.R;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<String> questions;

    QuestionAdapter(Context context, List<String> questions) {
        this.layoutInflater = LayoutInflater.from(context);
        this.questions = questions;
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
        holder.questionName.setText(question);

    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView questionName;
        Button btnDeleteQuestion;
        CardView questionCard;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            questionName = itemView.findViewById(R.id.questionName);
            btnDeleteQuestion = itemView.findViewById(R.id.btnDeleteQuestion);
            questionCard = itemView.findViewById(R.id.questionCard);

            questionCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("QUESTION CARD:", "Card for: " + questionName.getText());
                    Toast.makeText(itemView.getContext(), "Question Card for: " + questionName.getText(), Toast.LENGTH_LONG).show();
                }
            });

            btnDeleteQuestion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("QUESTION CARD:", "Deleting Card for: " + questionName.getText());
                }
            });
        }
    }

}
