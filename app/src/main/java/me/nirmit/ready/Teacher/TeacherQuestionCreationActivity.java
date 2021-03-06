package me.nirmit.ready.Teacher;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import me.nirmit.ready.Login.MainActivity;
import me.nirmit.ready.R;
import me.nirmit.ready.Util.FirebaseMethods;

public class TeacherQuestionCreationActivity extends AppCompatActivity {

    private static final String TAG = "TeacherQuestionCreation";
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int GALLERY_REQUEST = 2;

    private TextView topBarTitle;
    private EditText etTopic, etQuestion, etAnswer;
    private Button btnTypeQuestion, btnUploadQuestion, btnSave, btnCamera, btnAlbum, btnDle;
    private Uri imageUrl;
    private RelativeLayout relativeLayoutQuestion, relativeLayoutBtn;
    private ImageView ivQuestion, ivBackArrow, signoutBtn;
    private Context mContext;
    private Bitmap imageBitmap;
    private String currentPhotoPath;


    // Firebase stuff
    private FirebaseAuth mAuth;
    private FirebaseMethods firebaseMethods;
    private FirebaseFirestore db;
    private FirebaseUser userAcc;
    private StorageReference storageReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_question_creation);

        // Top bar setup
        topBarTitle = (TextView) findViewById(R.id.topBarTitle);
        topBarTitle.setText("Question Setup");
        ivBackArrow = (ImageView) findViewById(R.id.backArrow);
        signoutBtn = (ImageView) findViewById(R.id.signout);

        mAuth = FirebaseAuth.getInstance();
        userAcc = mAuth.getCurrentUser();
        firebaseMethods = new FirebaseMethods(TeacherQuestionCreationActivity.this);
        storageReference = FirebaseStorage.getInstance().getReference();
        mContext = TeacherQuestionCreationActivity.this;
        etTopic = (EditText) findViewById(R.id.topic);
        etQuestion = (EditText) findViewById(R.id.textQuestion);
        etAnswer = (EditText) findViewById(R.id.answer);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnTypeQuestion = (Button) findViewById(R.id.btnTypeQuestion);
        btnUploadQuestion = (Button) findViewById(R.id.btnUploadQuestion);
        btnCamera = (Button) findViewById(R.id.cameraBtn);
        btnAlbum = (Button) findViewById(R.id.albumBtn);
        btnDle = (Button) findViewById(R.id.img_delete_btn);
        ivQuestion = (ImageView) findViewById(R.id.ivQuestion);
        relativeLayoutQuestion = (RelativeLayout) findViewById(R.id.questionRelLayout);
        relativeLayoutBtn = (RelativeLayout) findViewById(R.id.btnRelLayout);


        setupFirebaseAuth();
        btnTypeQuestionLogic();
        btnUploadQuestionLogic();
        btnSaveLogic();
        ivBackArrowLogic();
        signoutBtnLogic();
        btnCameraLogic();
        btnAlbumLogic();
        btnDeleteLogic();
    }

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
                Log.d(TAG, "Uri" + photoURI.toString());
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

    private void displayImage() {
        ivQuestion.setImageBitmap(imageBitmap);
        relativeLayoutBtn.setVisibility(View.GONE);
        relativeLayoutQuestion.setVisibility(View.VISIBLE);
        etQuestion.setVisibility(View.GONE);
        ivQuestion.setVisibility(View.VISIBLE);
        btnDle.setVisibility(View.VISIBLE);

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
                        displayImage();
                        uploadPhoto(imageBitmap);
                    } catch (IOException e) {
                        Log.i(TAG, "Exception: " + e);
                    }
                    break;

                case GALLERY_REQUEST:
                    Uri selectedImage = data.getData();
                    try {
                        imageBitmap = MediaStore.Images.Media.getBitmap(TeacherQuestionCreationActivity.this.getContentResolver(), selectedImage);
                        displayImage();
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

    // check if inputs are correct
    public boolean validate() {
        boolean valid = true;
        String topic = etTopic.getText().toString();
        if (topic.isEmpty() || topic.trim().equals("")) {
            etTopic.setError("Enter topic name!");
            valid = false;
        }
        String question = etQuestion.getText().toString();
        if ((question.isEmpty() || question.trim().equals("")) && imageUrl == null) {
            Toast.makeText(TeacherQuestionCreationActivity.this, "Enter text question or upload an image!",
                    Toast.LENGTH_SHORT).show();
            valid = false;
        }

        return valid;
    }

    private void deletePhoto() {
        StorageReference photoRef = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl.toString());
        photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                imageUrl = null;
                imageBitmap = null;
                ivQuestion.setVisibility(View.GONE);
                btnDle.setVisibility(View.GONE);
                Log.d(TAG, "Delete file successfuly");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d(TAG, "Fail to delete file");
            }
        });
    }

    // ========= Listeners ===========

    private void btnSaveLogic() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Saving question");

                if (validate()) {
                    firebaseMethods.addQuestionInfoFirestore(
                            getIntent().getStringExtra("QUIZ_FIREBASE_ID"),
                            etTopic.getText().toString(),
                            etAnswer.getText().toString(),
                            imageUrl == null? null:imageUrl.toString(),
                            etQuestion.getText().toString());

                    // Go back to the questions page
                    Intent intent = new Intent(mContext, TeacherQuizQuestionsActivity.class);
                    intent.putExtra("QUIZ_FIREBASE_ID", getIntent().getStringExtra("QUIZ_FIREBASE_ID"));
                    startActivity(intent);
                }

            }
        });
    }

    public void btnDeleteLogic() {
        btnDle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletePhoto();
            }
        });
    }

    public void btnCameraLogic() {
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePictureIntent();
            }
        });
    }

    public void btnAlbumLogic() {
        btnAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseFromAlbumIntent();
            }
        });
    }

    private void btnUploadQuestionLogic() {
        btnUploadQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Uploading Question");
                if (imageBitmap != null) {
                    Log.d(TAG, "image not null");
                    ivQuestion.setImageBitmap(imageBitmap);
                    ivQuestion.setVisibility(View.VISIBLE);
                    etQuestion.setVisibility(View.GONE);
                    btnDle.setVisibility(View.VISIBLE);

                }
                else {
                    relativeLayoutBtn.setVisibility(View.VISIBLE);
                    etQuestion.setVisibility(View.GONE);
                }
            }
        });
        
    }

    private void btnTypeQuestionLogic() {
        btnTypeQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Typeing question");
                relativeLayoutBtn.setVisibility(View.GONE);
                relativeLayoutQuestion.setVisibility(View.VISIBLE);
                etQuestion.setVisibility(View.VISIBLE);
                ivQuestion.setVisibility(View.GONE);
                btnDle.setVisibility(View.GONE);
            }
        });
    }

    private void ivBackArrowLogic() {
        ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: closing the activity");
                if (imageUrl != null) {
                    deletePhoto();
                }
                finish();
            }
        });
    }

    private void signoutBtnLogic() {
        signoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Signing out the user");
                Toast.makeText(mContext, "Signing out the user", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(TeacherQuestionCreationActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                mAuth.signOut();
                startActivity(intent);
            }
        });
    }


    // ============= Firebase Methods & Logic ===============

    private void setupFirebaseAuth() {
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

}
