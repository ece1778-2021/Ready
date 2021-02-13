package me.nirmit.ready;

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
        holder.question.setText(assgQt);
        Log.d(LOG, "clicked");
        //TODO: Save final answer
        //TODO: Upload picture
        //TODO: Set picture visible

    }
    @Override
    public int getItemCount() {
        return assgList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView question;
        private ImageView image;
        private EditText answer;
        private Button uploadButton;


        public RecyclerViewHolder(View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.student_question);
            image = itemView.findViewById(R.id.student_image);
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

