package me.nirmit.ready.Util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import me.nirmit.ready.Student.StudentMainActivity;
import me.nirmit.ready.Teacher.TeacherAddQuizActivity;
import me.nirmit.ready.models.Test;

public class FirebaseMethods {

    private static final String TAG = "FirebaseMethods";

    // Firebase variables
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    // Other variablies
    private String userID;
    private Context mContext;

    public FirebaseMethods(Context context) {
        FirebaseApp.initializeApp(context);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        mContext = context;

        if (mAuth.getCurrentUser() != null) {
            userID = mAuth.getCurrentUser().getUid();
        }
    }

    private String getTimestamp(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.CANADA);
        sdf.setTimeZone(TimeZone.getTimeZone("Canada/Eastern"));
        return sdf.format(new Date());
    }

    public void registerNewEmail(final String email, final String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            userID = user.getUid();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(mContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void addNewUserFirestore(String name, String email, String user_id,
                                    String id, String phoneNumber, String mode) {
        Log.d(TAG, "Adding new user to the Firestore backend");

        Map<String, Object> new_user = new HashMap<>();
        new_user.put("name", name);
        new_user.put("email", email);
        new_user.put("user_id", user_id);
        new_user.put("id", id);
        new_user.put("phoneNumber", phoneNumber);
        new_user.put("mode", mode);

        // Add a new document with a generated ID
        db.collection("users")
                .document(user_id)
                .set(new_user);
    }

    public void addTestFirestore(String deadline_date, String deadline_time, String testname, String type,
                                 int status, String course_id, String teacher_id) {
        Log.d(TAG, "Adding new assessment to the Firestore backend");

        Map<String, Object> new_test = new HashMap<>();
        new_test.put("date_created", getTimestamp());
        new_test.put("deadline_date", deadline_date);
        new_test.put("deadline_time", deadline_time);
        new_test.put("testname", testname);
        new_test.put("type", type);
        new_test.put("status", status);
        new_test.put("course_id", course_id);
        new_test.put("teacher_id", teacher_id);

        DocumentReference addedDocRef = db.collection("tests").document();
        new_test.put("test_id", addedDocRef.getId());
        addedDocRef.set(new_test);
    }

    public void goToFirstModePage(String userId) {

        db.collection("users").document(userId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String mode = documentSnapshot.getString("mode");
                if (mode.equals("Teacher")) {
                    Log.d(TAG, "onSuccess: Mode - " + mode );
                    Intent intent = new Intent(mContext, TeacherAddQuizActivity.class);
                    mContext.startActivity(intent);
                } else {
                    Log.d(TAG, "onSuccess: Mode - " + mode);
                    Intent intent = new Intent(mContext, StudentMainActivity.class);
                    mContext.startActivity(intent);
                }
            }
        });
    }

}