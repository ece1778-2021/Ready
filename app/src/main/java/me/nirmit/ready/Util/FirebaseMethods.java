package me.nirmit.ready.Util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

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
}
