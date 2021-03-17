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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
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
    private String testType;
    private String testId;
    private Context mcontext;


    // Firebase stuff
    private FirebaseAuth mAuth;
    private FirebaseMethods firebaseMethods;
    private FirebaseFirestore db;
    private FirebaseUser userAcc;

    public StudentAssgAdapter(Context context, List<Question> questions, String testType,
                              String testId){
        firebaseMethods = new FirebaseMethods(context);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userAcc = mAuth.getCurrentUser();

        this.layoutInflater = LayoutInflater.from(context);
        this.questions = questions;
        this.testType = testType;
        this.mcontext = context;
        this.testId = testId;
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
        holder.studentCard.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  Question question = questions.get(position);
                  Intent intent = new Intent(mcontext, StudentSubmissionActivity.class);
                  intent.putExtra("PUBLISHED_TEST_FIREBASE_ID", testId);
                  intent.putExtra("TEST_TYPE", testType);
                  intent.putExtra("image", question.getImage_path());
                  intent.putExtra("text_question", question.getQuestion_text());
                  intent.putExtra("question_id", question.getQuestion_id());
                  mcontext.startActivity(intent);
              }
        });
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView topic;
        private CardView studentCard;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            topic = itemView.findViewById(R.id.student_topic);
            studentCard = itemView.findViewById(R.id.student_qt_card);

        }
    }


}