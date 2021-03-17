package me.nirmit.ready.Student;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import me.nirmit.ready.Login.MainActivity;
import me.nirmit.ready.R;
import me.nirmit.ready.Util.FirebaseMethods;
import me.nirmit.ready.models.Answer;

public class StudentSubmissionActivity extends AppCompatActivity {

    private static final String LOG = StudentSubmissionActivity.class.getSimpleName();
    private static final String TAG = "StudentSubmissionActivi";
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int GALLERY_REQUEST = 2;

    // Firebase stuff
    private FirebaseAuth mAuth;
    private FirebaseMethods firebaseMethods;
    private FirebaseFirestore db;
    private FirebaseUser userAcc;
    private StorageReference storageReference;

    private Button cameraBtn, albumBtn, updateBtn, saveBtn;
    private EditText ans;
    private TextView qText, ansText;
    private ImageView qImage, ansImage;
    private LinearLayout cameraBtnLayout, updateBtnLayout;
    private String testId, testType, questionId, imagePath, questionText, currentPhotoPath, ansImagePath, answerId;
    private Bitmap imageBitmap;
    private Uri imageUrl;


    private TextView topBarTitle;
    private ImageView ivBackArrow, signoutBtn;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_submit);

        // Top Bar setup
        topBarTitle = (TextView) findViewById(R.id.topBarTitle);
        topBarTitle.setText("Submit an Answer");
        signoutBtn = (ImageView) findViewById(R.id.signout);
        ivBackArrow = (ImageView) findViewById(R.id.backArrow);

        // Firebase stuff
        mAuth = FirebaseAuth.getInstance();
        userAcc = mAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseMethods = new FirebaseMethods(StudentSubmissionActivity.this);
        mContext = StudentSubmissionActivity.this;

        cameraBtn = findViewById(R.id.student_cam_btn);
        albumBtn = findViewById(R.id.student_album_btn);
        updateBtn = findViewById(R.id.student_update_btn);
        saveBtn = findViewById(R.id.student_save_btn);
        qText = findViewById(R.id.student_question_text);
        qImage = findViewById(R.id.stu_question_img);
        ansImage = findViewById(R.id.student_ans_img);
        ansText = findViewById(R.id.student_ans_text);

        ans = findViewById(R.id.student_ans);
        cameraBtnLayout = findViewById(R.id.student_btn_linearlayout);
        updateBtnLayout = findViewById(R.id.student_update_layout);

        testId =  getIntent().getStringExtra("PUBLISHED_TEST_FIREBASE_ID");
        testType = getIntent().getStringExtra("TEST_TYPE");
        questionId = getIntent().getStringExtra("question_id");
        imagePath = getIntent().getStringExtra("image");
        questionText = getIntent().getStringExtra("text_question");

        setupFirebaseAuth();
        getQuestion();
        getAnswer();
        cameraBtnLogic();
        albumBtnLogic();
        saveBtnLogic();
        updateBtnLogic();
        ivBackArrowLogic();
        signoutBtnLogic();

    }

    private void getQuestion() {
        Log.d(TAG, "Getting a question");
        if (imagePath != null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(
                    imagePath);
            storageReference.getBytes(1024*1024*7)
                    .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            qImage.setImageBitmap(bitmap);
                        }
                    });

            qImage.setVisibility(View.VISIBLE);
        } else { // text based question
            if (questionText.length() != 0) {
                qText.setText(questionText);
                qText.setVisibility(View.VISIBLE);
            }
        }
        if (testType.equals("hw")){
            ansText.setVisibility(View.GONE);
            ans.setVisibility(View.GONE);

        }
    }

    private void getAnswer() {
            db.collection("answers")
            .whereEqualTo("question_id", questionId)
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful() && task.getResult() != null) {  // no such answer on the database
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (Objects.equals(document.getString("user_id"), userAcc.getUid())) {
                                Answer answer = document.toObject(Answer.class);
                                // show image & answer
                                if (testType.equals("test")) {
                                    ans.setText(answer.getAnswer());
                                }
                                else {
                                    ans.setVisibility(View.GONE);
                                }
                                StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(
                                        answer.getImage_path());
                                ansImagePath = answer.getImage_path();
                                answerId = answer.getAnswer_id();
                                storageReference.getBytes(1024*1024*7)
                                        .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                            @Override
                                            public void onSuccess(byte[] bytes) {
                                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                                ansImage.setImageBitmap(bitmap);
                                                ansImage.setVisibility(View.VISIBLE);
                                                cameraBtnLayout.setVisibility(View.GONE);
                                                updateBtnLayout.setVisibility(View.VISIBLE);
                                            }
                                        });

                            }
                        }
                    }
                }
            });
    }

    private boolean validate() {
        boolean valid = true;
        if (testType.equals("test") && (
                ans.getText().toString() == null
                        ||ans.getText().toString().trim().equals(""))) {
            Toast.makeText(mContext, "Please enter an answer", Toast.LENGTH_LONG).show();
            valid = false;
            return valid;
        }

        if (imageUrl == null) {
            Toast.makeText(mContext, "Please upload an answer", Toast.LENGTH_LONG).show();
            valid = false;
            return valid;
        }
        return valid;
    }

    private void saveAnswer() {
        if (validate()) {
            if (answerId == null) {
                firebaseMethods.addAnswerFirestore(
                        testId,
                        questionId,
                        userAcc.getUid(),
                        imageUrl.toString(),
                        ans.getText().toString()
                );
                Toast.makeText(mContext, "Question is saved", Toast.LENGTH_SHORT).show();
                finish();

            }
            else {
                firebaseMethods.updateAnswerFirestore(
                        answerId,
                        imageUrl.toString(),
                        ans.getText().toString()
                );
                Toast.makeText(mContext, "Question is updated", Toast.LENGTH_SHORT).show();
                finish();

            }
        }
    }

    private void updateAnswer() {
        if (ansImagePath != null) {
            StorageReference photoRef = FirebaseStorage.getInstance().getReferenceFromUrl(ansImagePath);
            photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    imageBitmap = null;
                    updateBtnLayout.setVisibility(View.GONE);
                    cameraBtnLayout.setVisibility(View.VISIBLE);
                    ans.getText().clear();
                    Log.d(TAG, "Delete photo successfully");

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.d(TAG, "Fail to delete file");
                }
            });
        }
        else {
            Log.d(TAG, "Image url is null");
        }
    }

    // ========== Camera Function =========
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    // Launch Camera Function
    private void takePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    // Open album Function
    private void chooseFromAlbumIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        try {
            startActivityForResult(intent, GALLERY_REQUEST);
        } catch (ActivityNotFoundException e) {
            Log.d(TAG, String.valueOf(e));
        }
    }

    // Save bitmap data for the image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE_CAPTURE:
                    File file = new File(currentPhotoPath);
                    try {
                        imageBitmap = MediaStore.Images.Media.getBitmap(
                                getContentResolver(), Uri.fromFile(file));
                        ansImage.setImageBitmap(imageBitmap);
                        ansImage.setVisibility(View.VISIBLE);
                        uploadPhoto(imageBitmap);
                    } catch (IOException e) {
                        Log.i(TAG, "Exception: " + e);
                    }
                    break;

                case GALLERY_REQUEST:
                    Uri selectedImage = data.getData();
                    try {
                        imageBitmap = MediaStore.Images.Media.getBitmap(
                                StudentSubmissionActivity.this.getContentResolver(), selectedImage);
                        ansImage.setImageBitmap(imageBitmap);
                        ansImage.setVisibility(View.VISIBLE);
                        uploadPhoto(imageBitmap);
                    } catch (IOException e) {
                        Log.i("TAG", "Some exception " + e);
                    }
                    break;
            }
        }
    }

    // Upload a photo to storage
    private void uploadPhoto(Bitmap imageBitmap) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String currentTime = String.valueOf(timestamp.getTime());

        String userId = userAcc.getUid();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();

        final StorageReference ref = storageReference.child(userId).child(currentTime);
        ref.putBytes(data)
                .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            Log.d(TAG, "task fail: cannot upload photo");
                        }
                        return ref.getDownloadUrl();
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            imageUrl = task.getResult();
                            Log.d(TAG, "Upload photo successfully ");
                        } else {
                            String error = task.getException().getMessage();
                            Log.d(TAG, error);
                        }
                    }
                });

    }


    // ========== Listeners =========

    private void cameraBtnLogic() {
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Take a picture");
                takePictureIntent();
            }
        });
    }

    private void albumBtnLogic() {
        albumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Choose a picture from album");
                chooseFromAlbumIntent();
            }
        });
    }

    private void saveBtnLogic() {
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Save an answer");
                saveAnswer();
                cameraBtnLayout.setVisibility(View.GONE);
                updateBtnLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void updateBtnLogic() {
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick:Update an answer");
                updateAnswer();
                cameraBtnLayout.setVisibility(View.VISIBLE);
                updateBtnLayout.setVisibility(View.GONE);
            }
        });
    }


    private void signoutBtnLogic() {
        signoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Signing out the user");
                Toast.makeText(mContext, "Signing out the user", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(StudentSubmissionActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                mAuth.signOut();
                startActivity(intent);
            }
        });
    }

    private void ivBackArrowLogic() {
        ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(LOG, "onClick: closing the activity");
                finish();
            }
        });
    }

    private void setupFirebaseAuth() {
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

}

