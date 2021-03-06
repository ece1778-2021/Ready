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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;

import me.nirmit.ready.Login.MainActivity;
import me.nirmit.ready.R;
import me.nirmit.ready.Util.FirebaseMethods;
import me.nirmit.ready.models.Answer;
import me.nirmit.ready.models.Question;

public class StudentAssignmentActivity extends AppCompatActivity {

    private static final String LOG = StudentAssignmentActivity.class.getSimpleName();
    private static final String TAG = "StudentAssignmentActivi";
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int GALLERY_REQUEST = 2;


    private RecyclerView recyclerView;
    private StudentAssgAdapter recyclerViewAdapter;
    private ArrayList<Question> testQuestions;
    private ArrayList<Answer> testAnswers;

    private TextView topBarTitle;
    private ProgressBar progressBar;
    private ImageView ivBackArrow, signoutBtn;
    private Button uploadButton;
    private Context mContext;
    private Bitmap imageBitmap;
    private String imageUrl;
    private String testId;
    private String testType;
    private String currentPhotoPath;


    // Firebase stuff
    private FirebaseAuth mAuth;
    private FirebaseMethods firebaseMethods;
    private FirebaseFirestore db;
    private FirebaseUser userAcc;
    private StorageReference storageReference;

    public StudentAssignmentActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_assg);

        // Top Bar setup
        topBarTitle = (TextView) findViewById(R.id.topBarTitle);
        topBarTitle.setText("Assignments");
        signoutBtn = (ImageView) findViewById(R.id.signout);
        ivBackArrow = (ImageView) findViewById(R.id.backArrow);

        testQuestions = new ArrayList<>();
        testAnswers = new ArrayList<>();


        mAuth = FirebaseAuth.getInstance();
        userAcc = mAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseMethods = new FirebaseMethods(StudentAssignmentActivity.this);
        mContext = StudentAssignmentActivity.this;
        progressBar = findViewById(R.id.student_assg_progressbar);
        progressBar.setVisibility(View.VISIBLE);
        testId = getIntent().getStringExtra("PUBLISHED_TEST_FIREBASE_ID");
        testType = getIntent().getStringExtra("TEST_TYPE");

        recyclerView = findViewById(R.id.student_qt_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        setupFirebaseAuth();
        ivBackArrowLogic();
        signoutBtnLogic();
        getTestQuestionsFirestore(testId, testType);

        progressBar.setVisibility(View.INVISIBLE);
    }


    // ========= Camera Methods =========

    private File createImageFile() throws IOException {
        // Create an image file name
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

    public void takePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
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
    public void chooseFromAlbumIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        try {
            startActivityForResult(intent, GALLERY_REQUEST);
        } catch (ActivityNotFoundException e) {
            Log.d(TAG, String.valueOf(e));
        }
    }

    private void setPic(ImageView imageView) {
        // Get the dimensions of the View
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(currentPhotoPath, bmOptions);

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.max(1, Math.min(photoW / targetW, photoH / targetH));

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
//        imageView.setImageBitmap(bitmap);
        uploadPhoto(bitmap);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE_CAPTURE:
//                    Bundle extras = data.getExtras();
//                    imageBitmap = (Bitmap) extras.get("data");
//                    uploadPhoto(imageBitmap);

                    File file = new File(currentPhotoPath);
                    try {
                        imageBitmap = MediaStore.Images.Media.getBitmap(
                                getContentResolver(), Uri.fromFile(file));
                        uploadPhoto(imageBitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;

                case GALLERY_REQUEST:
                    Uri selectedImage = data.getData();
                    try {
                        imageBitmap = MediaStore.Images.Media.getBitmap(StudentAssignmentActivity.this.getContentResolver(), selectedImage);
                        uploadPhoto(imageBitmap);
                    } catch (IOException e) {
                        Log.i("TAG", "Some exception " + e);
                    }
                    break;
            }
        }
    }


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
                            imageUrl = task.getResult().toString();
                            recyclerViewAdapter =
                                    new StudentAssgAdapter(mContext, testQuestions, testAnswers, testType, testId, imageUrl);
                            recyclerView.setAdapter(recyclerViewAdapter);
                            Log.d(TAG, "Upload photo successfully ");
                            Toast.makeText(mContext, "Upload success. Please save!", Toast.LENGTH_LONG).show();
                        } else {
                            String error = task.getException().getMessage();
                            Log.d(TAG, error);
                        }
                    }
                });

    }


    // ========== Listeners =========

    private void signoutBtnLogic() {
        signoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Signing out the user");
                Toast.makeText(mContext, "Signing out the user", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(StudentAssignmentActivity.this, MainActivity.class);
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


    // ============= Firebase Methods & Logic ===============

    private void setupFirebaseAuth() {
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }


    // get test questions
    public void getTestQuestionsFirestore(final String testId, final String testType) {

        CollectionReference all_tests = db.collection("questions");
        Query query = all_tests.whereEqualTo("test_id", testId);

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null) {
                    Log.e(TAG, "onEvent: Listen failed:" + error);
                    return;
                }

                testQuestions.clear();
                testAnswers.clear();

                for (DocumentSnapshot document : value) {
                    Question question = document.toObject(Question.class);
                    testQuestions.add(question);
                }

                // sort the tests based on the creation time
                Collections.sort(testQuestions, new Comparator<Question>() {
                    @Override
                    public int compare(Question p1, Question p2) {
                        return p1.getDate_created().compareTo(p2.getDate_created());
                    }
                });

                for (int i = 0; i < testQuestions.size(); i++) {
                    testAnswers.add(new Answer());
                }

                for (int i = 0; i < testQuestions.size(); i++) {
                    Log.d(TAG, testQuestions.get(i).getQuestion_id());
//                    Answer[] testAnswerArr = new Answer[testQuestions.size()];

                    getAnswersFirestore(testQuestions.get(i).getQuestion_id(), i);
                }
            }
        });
    }

    public void getAnswersFirestore(final String test_id, final int i) {

        db.collection("answers")
                .whereEqualTo("question_id", test_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {  // no such answer on the database
                            if (task.getResult().isEmpty()) {
//                                testAnswerArray[i]= new Answer();
                                testAnswers.set(i, new Answer());
                                Log.d(TAG, "ANSWER eMPTY");
                            } else {
                                boolean submitted = false;
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if (Objects.equals(document.getString("user_id"), userAcc.getUid())) {
                                        Answer answer = document.toObject(Answer.class);
                                        Log.d(TAG, answer.getQuestion_id());
//                                        testAnswerArray[i]= answer;
                                        testAnswers.set(i, answer);
                                        submitted = true;
                                        Log.d(TAG, "ANSWER not null ");
                                    }
                                }
                                if (!submitted) {  // no such answer submitted by a student
//                                    testAnswerArray[i]= new Answer();
                                    testAnswers.set(i, new Answer());
                                    Log.d(TAG, "ANSWER eMPTY for student");

                                }
                            }


                        }

                        recyclerViewAdapter =
                                new StudentAssgAdapter(mContext, testQuestions, testAnswers, testType, testId, imageUrl);
                        recyclerView.setAdapter(recyclerViewAdapter);
                    }


                });
    }


    }
