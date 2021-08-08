package com.code_n_droid.dateme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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
import com.google.firebase.auth.FirebaseUser;

public class LogInActivity extends AppCompatActivity {

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth firebaseAuth;
    private EditText emailField, passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Instantiate
        MaterialButton signInButton = findViewById(R.id.sign_in_button);
        MaterialButton logIn = findViewById(R.id.logIn);
        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        TextView signUpText = findViewById(R.id.signUp);

        // GOTO signup activity
        signUpText.setOnClickListener(view -> {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Fire Base auth
        firebaseAuth = FirebaseAuth.getInstance();

        signInButton.setOnClickListener(view -> {
            Intent intent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(intent,100);
        });

        // Log In Button Click
        logIn.setOnClickListener(view -> {
            logInWithEmailAndPassword();
        });
    }

    private void logInWithEmailAndPassword() {
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        // CHECK
        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Field can not be empty!", Toast.LENGTH_SHORT).show();
            return;
        }else if(password.length() < 6){
            Toast.makeText(this, "Password length must be at least 6", Toast.LENGTH_SHORT).show();
        }

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        // SEND USER TO HOME ACTIVITY
                        goToHome(email);
                    }else{
                        Toast.makeText(this, "Log In failed!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void goToHome(String email) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra(AddDetailsActivity.EMAIL_KEY, email);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // google sign in
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null){
            goToHome(account.getEmail());
            return;
        }
        // FireBase auth
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null){
            // send user to home page
            goToHome(user.getEmail());
        }
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
            goToHome(account.getEmail());
        } catch (ApiException e) {
            Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
        }
    }
}