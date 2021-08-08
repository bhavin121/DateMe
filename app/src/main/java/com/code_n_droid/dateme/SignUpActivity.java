package com.code_n_droid.dateme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private EditText email, password, confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Instantiate
        email = findViewById(R.id.emailField);
        password = findViewById(R.id.passwordField);
        confirmPassword = findViewById(R.id.confirmPasswordField);
        MaterialButton signUp = findViewById(R.id.signUp);
        MaterialButton signUpWithGoogle = findViewById(R.id.sign_up_button);

        // Handle log in text click
        findViewById(R.id.logIn).setOnClickListener(view -> {
            onBackPressed();
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        // sign up click handling
        signUp.setOnClickListener(view -> {
             signUpWithEmailAndPassword();
        });

        signUpWithGoogle.setOnClickListener(view -> {
            Intent intent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(intent,100);
        });
    }

    private void signUpWithEmailAndPassword() {
        String sEmail = email.getText().toString();
        String sPassword = password.getText().toString();
        String sConfirmPassword = confirmPassword.getText().toString();

        // check
        if(sEmail.isEmpty() || sPassword.isEmpty() || sConfirmPassword.isEmpty()){
            Toast.makeText(this, "Fields can not be empty!", Toast.LENGTH_SHORT).show();
            return;
        }else if(!sPassword.equals(sConfirmPassword)){
            Toast.makeText(this, "Confirm password doesn't match with password", Toast.LENGTH_SHORT).show();
            return;
        }else if(sPassword.length() < 6){
            Toast.makeText(this, "Password length must be at least 6", Toast.LENGTH_SHORT).show();
        }

        firebaseAuth.createUserWithEmailAndPassword(sEmail, sPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            // send user to AddDetailsActivity
                            Toast.makeText(SignUpActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            sendToUploadActivity(sEmail, sPassword);
                        }else{
                            Log.d("Login", task.getException().getMessage());
                            Toast.makeText(SignUpActivity.this, "Sign Up Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void sendToUploadActivity(String sEmail, String sPassword) {
        Intent intent = new Intent(SignUpActivity.this, UploadPhotos.class);
        intent.putExtra(AddDetailsActivity.EMAIL_KEY, sEmail);
        intent.putExtra(AddDetailsActivity.PASSWORD_KEY, sPassword);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleResult(task);
        }
    }

    private void handleResult(Task<GoogleSignInAccount> task) {
        try{
            GoogleSignInAccount account = task.getResult(ApiException.class);
            sendToUploadActivity(account.getEmail(), account.getId());
        } catch (ApiException e) {
            Toast.makeText(this, "Sign Up Failed", Toast.LENGTH_SHORT).show();
        }
    }
}