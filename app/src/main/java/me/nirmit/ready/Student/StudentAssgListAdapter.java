package me.nirmit.ready.Student;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import me.nirmit.ready.R;

public class StudentAssgListAdapter extends RecyclerView.Adapter<StudentAssgListAdapter.RecyclerViewHolder>{
    private List<String> assgList;
    private ClickListener<String> clickListener;


    public StudentAssgListAdapter(List<String> assgList){
        this.assgList = assgList;
    }
    @Override
    public StudentAssgListAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_assg_list_adapter, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StudentAssgListAdapter.RecyclerViewHolder holder, final int position) {
        final String assignment = assgList.get(position);
        holder.title.setText(assignment);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onItemClick(assignment);  //check
            }
        });


    }
    @Override
    public int getItemCount() {
        return assgList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private CardView cardView;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.student_question);
            cardView = itemView.findViewById(R.id.student_assg_list_card);

        }
    }

    public void setOnItemClickListener(ClickListener<String> clickListener) {
        this.clickListener = clickListener;
    }

    interface ClickListener<T> {
        void onItemClick(T data);
    }
}

