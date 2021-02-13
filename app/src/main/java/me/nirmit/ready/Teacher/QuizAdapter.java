package me.nirmit.ready.Teacher;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import me.nirmit.ready.R;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<String> quizzes;

    QuizAdapter(Context context, List<String> quizzes) {
        this.layoutInflater = LayoutInflater.from(context);
        this.quizzes = quizzes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.teacher_custom_quiz_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // bind the textview with data received
        String quiz = quizzes.get(position);
        holder.quizName.setText(quiz);

    }

    @Override
    public int getItemCount() {
        return quizzes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView quizName;
        Button btnDelete, btnPublish;
        CardView quizCard;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            quizName = itemView.findViewById(R.id.quizName);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnPublish = itemView.findViewById(R.id.btnPublish);
            quizCard = itemView.findViewById(R.id.quizCard);

            quizCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("QUIZ CARD:", "Card for: " + quizName.getText());
                    Toast.makeText(itemView.getContext(), "hehehe", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(itemView.getContext(), TeacherQuizQuestionsActivity.class);
                    itemView.getContext().startActivity(intent);
                }
            });

            btnPublish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("QUIZ CARD:", "Publishing Card for: " + quizName.getText());
                }
            });

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("QUIZ CARD:", "Deleting Card for: " + quizName.getText());
                }
            });
        }
    }

}
