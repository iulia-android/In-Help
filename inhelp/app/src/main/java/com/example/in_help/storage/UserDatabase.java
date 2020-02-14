package com.example.in_help.storage;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.in_help.category.CategoriesActivity;
import com.example.in_help.user.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;

public class UserDatabase {

    private FirebaseAuth auth = FirebaseAuth.getInstance();

    public void login(final Context context, final String userEmail, final String pass) {
        auth.signInWithEmailAndPassword(userEmail, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("LoginActivity", "signInWithEmail:success");
                            Toast.makeText(context, "Authentication succeded.",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, CategoriesActivity.class);
                            context.startActivity(intent);
                        } else {
                            Log.w("LoginActivity", "signInWithEmail:failure", task.getException());
                            Toast.makeText(context, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void signup(final Context context, String emailString, String passwordString){
        auth.createUserWithEmailAndPassword(emailString, passwordString)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("SignUpActivity", "createUserWithEmail:success");
                            Toast.makeText(context, "Sign up succeded.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.w("SignUpActivity", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(context, "Sign up failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public FirebaseAuth getAuth() {
        return auth;
    }

    public void signOut(Context context){
        auth.signOut();
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
}
