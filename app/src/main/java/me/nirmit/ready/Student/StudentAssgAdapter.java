package me.nirmit.ready.Student;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import me.nirmit.ready.R;
import me.nirmit.ready.Util.FirebaseMethods;
import me.nirmit.ready.models.Answer;
import me.nirmit.ready.models.Question;

public class StudentAssgAdapter extends RecyclerView.Adapter<StudentAssgAdapter.RecyclerViewHolder>{

    private static final String TAG = "StudentAssgAdapter";

    private LayoutInflater layoutInflater;
    private List<Question> questions;
    private List<Answer> answers;
    private String testType;
    private String testId;
    private String imageUrl;
    private Context mcontext;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int GALLERY_REQUEST = 2;

    // Firebase stuff
    private FirebaseAuth mAuth;
    private FirebaseMethods firebaseMethods;
    private FirebaseFirestore db;
    private FirebaseUser userAcc;

    public StudentAssgAdapter(Context context, List<Question> questions, List<Answer> answers, String testType,
                              String testId, String imageUrl){
        firebaseMethods = new FirebaseMethods(context);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userAcc = mAuth.getCurrentUser();

        this.layoutInflater = LayoutInflater.from(context);
        this.questions = questions;
        this.answers = answers;
        this.testType = testType;
        this.mcontext = context;
        this.testId = testId;
        this.imageUrl = imageUrl;
    }

    @Override
    public StudentAssgAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_assg_adapter, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final StudentAssgAdapter.RecyclerViewHolder holder, final int position) {

        final String topicText = questions.get(position).getTopic();
        holder.topic.setText(topicText);

        // Display question (text or an image)
        if (questions.get(position).getImage_path() != null) {
            holder.question.setText("Question");
            holder.teacher_image.setVisibility(View.VISIBLE);

            StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(
                    questions.get(position).getImage_path());
            storageReference.getBytes(1024*1024*7)
                    .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            holder.teacher_image.setImageBitmap(bitmap);
                            holder.teacherLinearLayout.setVisibility(View.VISIBLE);

                        }
                    });

        } else { // text based question
            final String assgQt = questions.get(position).getQuestion_text();
            if (assgQt.length() != 0) {
                holder.question.setText(assgQt);
            }
        }

        if (answers.size() != 0 && position < answers.size()
            && answers.get(position).getImage_path()!= null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(
                    answers.get(position).getImage_path());
            storageReference.getBytes(1024*1024*7)
                    .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            holder.student_image.setImageBitmap(bitmap);
                            holder.studentLinearLayout.setVisibility(View.VISIBLE);
                            if (testType.equals("test")) {
                                final String answerText = "Answer: " + answers.get(position).getAnswer();
                                holder.finalAns.setText(answerText);
                                holder.buttonLinearLayout.setVisibility(View.GONE);
                                holder.answerLinearLayout.setVisibility(View.VISIBLE);
                            }

                        }
                    });
        }


        // Display answer box if type == test else not.
        if (testType.equals("hw")) {
            holder.answer.setVisibility(View.GONE);
        }

        holder.uploadButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Upload button at position " + position + " is clicked");
                photoAlertDialog();  // launch alert

            }
        });

        holder.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageUrl != null) {
                    StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl);
                    storageReference.getBytes(1024*1024*7)
                            .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] bytes) {
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                    holder.student_image.setImageBitmap(bitmap);
                                    holder.studentLinearLayout.setVisibility(View.VISIBLE);

                                    if (testType.equals("test") && (
                                            holder.answer.getText().toString() == null ||
                                            holder.answer.getText().toString().trim().equals(""))){
                                        Toast.makeText(mcontext, "Please enter an answer", Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                    firebaseMethods.addAnswerFirestore(
                                            testId,
                                            questions.get(position).getQuestion_id(),
                                            userAcc.getUid(),
                                            imageUrl,
                                            holder.answer.getText().toString()
                                            );
                                    Toast.makeText(mcontext, "Answer is submitted", Toast.LENGTH_LONG).show();
                                    if (testType.equals("test")) {
                                        final String answerText = "Answer: " + holder.answer.getText().toString();
                                        holder.finalAns.setText(answerText);
                                        holder.buttonLinearLayout.setVisibility(View.GONE);
                                        holder.answerLinearLayout.setVisibility(View.VISIBLE);
                                    }
                                }
                            });
                }
                else {
                    Toast.makeText(mcontext, "Please upload a photo", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView question, topic;
        private ImageView student_image;
        private EditText answer;
        private TextView finalAns;
        private Button uploadButton, saveButton;
        private ImageView teacher_image;
        private LinearLayout teacherLinearLayout, studentLinearLayout, buttonLinearLayout, answerLinearLayout;


        public RecyclerViewHolder(View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.student_question);
            topic = itemView.findViewById(R.id.student_topic);
            student_image = itemView.findViewById(R.id.student_image);
            teacher_image = itemView.findViewById(R.id.teacher_image);
            answer = itemView.findViewById(R.id.student_answer_text);
            finalAns = itemView.findViewById(R.id.stu_final_answer);
            uploadButton = itemView.findViewById(R.id.student_upload_button);
            saveButton = itemView.findViewById(R.id.assg_save_button);
            teacherLinearLayout = itemView.findViewById(R.id.teacherRelativeView);
            studentLinearLayout = itemView.findViewById(R.id.studentRelativeView);
            buttonLinearLayout = itemView.findViewById(R.id.answerBtnLinearLayout);
            answerLinearLayout = itemView.findViewById(R.id.ansLinearLayout);
        }
    }

    public void photoAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
        builder.setMessage("Please upload a photo");
        builder.setCancelable(true);

        builder.setPositiveButton(
                "Camera",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((StudentAssignmentActivity)mcontext).takePictureIntent();
                    }
                });

        builder.setNegativeButton(
                "Choose from Album",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((StudentAssignmentActivity)mcontext).chooseFromAlbumIntent();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }



}

