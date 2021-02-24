package me.nirmit.ready.Teacher;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import me.nirmit.ready.R;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.RecyclerViewHolder>{
    private static final String LOG = MessageAdapter.class.getSimpleName();
    private List<String> nameList;
    private List<Double> markList;
    private List<Boolean> statusList;
    private MessageAdapter.ClickListener<String, String> clickListener;


    public MessageAdapter(List<String> nameList, List<Double> markList, List<Boolean> statusList){
        this.nameList = nameList;
        this.markList = markList;
        this.statusList = statusList;
    }
    @Override
    public MessageAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.teacher_message_adapter, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageAdapter.RecyclerViewHolder holder, final int position) {
        final String name = nameList.get(position);
        final String studentMark = "Mark: " + markList.get(position) + "\n";
        final Boolean studentStatus = statusList.get(position);
        String submitStatus = "Status: ";
        if (studentStatus) submitStatus += "Submitted \n";
        else submitStatus += "Not Submitted \n";
        final String newStatus = submitStatus;
        holder.studentName.setText(name);
        holder.mark.setText(studentMark);
        holder.status.setText(newStatus);
        holder.messgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onItemClick(studentMark, newStatus);

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

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            studentName = itemView.findViewById(R.id.messg_student_name);
            mark = itemView.findViewById(R.id.student_mark);
            status = itemView.findViewById(R.id.assg_status);
            messgButton = itemView.findViewById(R.id.messg_button);

        }
    }

    public void setOnItemClickListener(MessageAdapter.ClickListener<String, String> clickListener) {
        this.clickListener = clickListener;
    }

    interface ClickListener<T, K> {
        void onItemClick(T data, K status);
    }

}
