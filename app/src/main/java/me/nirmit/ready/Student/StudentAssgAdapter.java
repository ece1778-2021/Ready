package me.nirmit.ready.Student;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import me.nirmit.ready.R;

public class StudentAssgAdapter extends RecyclerView.Adapter<StudentAssgAdapter.RecyclerViewHolder>{
    private List<String> assgList;
    private ClickListener<String> clickListener;
    private static final String LOG = StudentAssgAdapter.class.getSimpleName();



    public StudentAssgAdapter(List<String> assgList){
        this.assgList = assgList;
    }
    @Override
    public StudentAssgAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_assg_adapter, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StudentAssgAdapter.RecyclerViewHolder holder, final int position) {
        final String assgQt = assgList.get(position);
        if (assgQt.length() != 0) {
            holder.question.setText(assgQt);
        }

        //TODO:
        // If teacher upload image instead of entering a question
        // holder.teacher_image.setVisibility(View.VISIBLE);
        //If type == assignment:
        // holder.answer.setVisibility(View.INVISIBLE);

        //TODO: Save final answer
        //TODO: Upload picture
        //TODO: Set picture visible
        holder.uploadButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //TODO: upload picture function
                Log.d(LOG, "Upload button at position " + position + " is clicked");
            }
        });
        Log.d(LOG, "clicked");
    }
    @Override
    public int getItemCount() {
        return assgList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView question;
        private ImageView student_image;
        private EditText answer;
        private Button uploadButton;
        private ImageView teacher_image;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.student_question);
            student_image = itemView.findViewById(R.id.student_image);
            teacher_image = itemView.findViewById(R.id.teacher_image);
            answer = itemView.findViewById(R.id.student_answer_text);
            uploadButton = itemView.findViewById(R.id.student_upload_button);
        }
    }

    public void setOnItemClickListener(ClickListener<String> movieClickListener) {
        this.clickListener = movieClickListener;
    }

    interface ClickListener<T> {
        void onItemClick(T data);
    }
}

