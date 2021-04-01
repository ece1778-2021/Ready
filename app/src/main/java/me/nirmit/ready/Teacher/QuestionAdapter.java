package me.nirmit.ready.Teacher;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.List;

import me.nirmit.ready.R;
import me.nirmit.ready.Util.FirebaseMethods;
import me.nirmit.ready.models.Question;
import me.nirmit.ready.models.Test;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {

    private static final String TAG = "QuestionAdapter";

    private LayoutInflater layoutInflater;
    private List<Question> questions;
    private boolean isQuizPublished;
    private int quetionNumber = 0;
    private Context mContext;

    // Firebase stuff
    private FirebaseAuth mAuth;
    private FirebaseMethods firebaseMethods;
    private FirebaseFirestore db;

    QuestionAdapter(Context context, List<Question> questions, boolean isQuizPublished) {
        firebaseMethods = new FirebaseMethods(context);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        this.layoutInflater = LayoutInflater.from(context);
        this.quetionNumber = 0;
        this.questions = questions;
        this.isQuizPublished = isQuizPublished;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.teacher_custom_quiz_questions_view, parent, false);
        mContext = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // bind the textview with data received
        String questionTopic = questions.get(position).getTopic();
        holder.questionName.setText("Q" + (++quetionNumber) +" " + questionTopic);
        String questionFirebaseId = questions.get(position).getQuestion_id();
        holder.questionFirebaseId.setText(questionFirebaseId);

        holder.questionCard.setCardBackgroundColor(mContext.getResources().getColor(R.color.question_color));

        if (isQuizPublished) {
            holder.btnDeleteQuestion.setVisibility(View.GONE);
        }

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

                    LayoutInflater layoutInflater = LayoutInflater.from(view.getContext());
                    View popup_view = layoutInflater.inflate(R.layout.custom_question_dialog, null);

                    TextView questionBox = popup_view.findViewById(R.id.questionBox);
                    TextView answerBox = popup_view.findViewById(R.id.answerBox);
                    Button closePopupBtn = popup_view.findViewById(R.id.closeBtn);
                    final ProgressBar progressBar = popup_view.findViewById(R.id.question_loading);
                    final ImageView questionImage = popup_view.findViewById(R.id.questionImage);
                    progressBar.setVisibility(View.VISIBLE);

                    final AlertDialog alertDialog = new AlertDialog.Builder(view.getContext())
                            .setView(popup_view)
                            .create();
                    alertDialog.show();

                    // from the questions get the one that match the current box
                    for (int i = 0; i < questions.size(); i++) {
                        Question q = questions.get(i);
                        if (q.getQuestion_id().equals
                                (questionFirebaseId.getText().toString())) {
                            if (q.getImage_path() != null) {
                                StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(q.getImage_path());
                                storageReference.getBytes(1024*1024*7)
                                        .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                            @Override
                                            public void onSuccess(byte[] bytes) {
                                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                                questionImage.setImageBitmap(bitmap);
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        });
                            } else {
                                questionBox.setText(q.getQuestion_text());
                                progressBar.setVisibility(View.GONE);
                            }
                            answerBox.setText(q.getAnswer());
                        }
                    }


                    closePopupBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.d(TAG, "onClick: cancel button" );
                            alertDialog.cancel();
                        }
                    });
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
