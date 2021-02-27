package me.nirmit.ready.Student;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import me.nirmit.ready.R;
import me.nirmit.ready.Teacher.TeacherAddQuizActivity;
import me.nirmit.ready.Util.FirebaseMethods;
import me.nirmit.ready.models.Question;
import me.nirmit.ready.models.Test;

public class StudentAssgAdapter extends RecyclerView.Adapter<StudentAssgAdapter.RecyclerViewHolder>{

    private static final String TAG = "StudentAssgAdapter";

    private LayoutInflater layoutInflater;
    private List<Question> questions;
    private String testType;

    // Firebase stuff
    private FirebaseAuth mAuth;
    private FirebaseMethods firebaseMethods;
    private FirebaseFirestore db;


    public StudentAssgAdapter(Context context, List<Question> questions, String testType){
        firebaseMethods = new FirebaseMethods(context);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        this.layoutInflater = LayoutInflater.from(context);
        this.questions = questions;
        this.testType = testType;
    }

    @Override
    public StudentAssgAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_assg_adapter, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final StudentAssgAdapter.RecyclerViewHolder holder, final int position) {

        // Display question (text or an image)
        if (questions.get(position).getImage_path() != null) {
            holder.question.setText("Question");
            holder.teacher_image.setVisibility(View.VISIBLE);

            StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(
                    questions.get(position).getImage_path());
            storageReference.getBytes(1024*1024)
                    .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            holder.teacher_image.setImageBitmap(bitmap);
                        }
                    });

        } else { // text based question
            final String assgQt = questions.get(position).getQuestion_text();
            if (assgQt.length() != 0) {
                holder.question.setText(assgQt);
            }
        }

        // Display answer box if type == test else not.
        if (testType.equals("hw")) {
            holder.answer.setVisibility(View.GONE);
        }

        //TODO: Save final answer
        //TODO: Upload picture
        //TODO: Set picture visible
        holder.uploadButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //TODO: upload picture function
                Log.d(TAG, "Upload button at position " + position + " is clicked");
            }
        });
    }

    @Override
    public int getItemCount() {
        return questions.size();
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

}

