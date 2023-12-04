package com.example.pocketinventory.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pocketinventory.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * This class is the signup activity of the app. It allows new users of the app to create an account
 * on the app.
 * It also allows returning users to go to the login page and login.
 */
public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText signupEmail, signupPassword, signupConfirm;
    private Button signupButton;
    private TextView loginRedirectText;

    /**
     * Called when the Sign up activity is starting or being reinitialized
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        auth = FirebaseAuth.getInstance();
        signupEmail = findViewById(R.id.signup_email);
        signupPassword = findViewById(R.id.signup_password);
        signupConfirm = findViewById(R.id.confirm_signup_password);
        signupButton = findViewById(R.id.signup_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);

        // Set imeOptions for signupEmail and signupPassword fields
        signupEmail.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        signupPassword.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        signupConfirm.setImeOptions(EditorInfo.IME_ACTION_DONE);

        // Add an OnEditorActionListener to loginEmail and loginPassword fields
        signupEmail.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    signupPassword.requestFocus(); // Move focus to the next EditText (signupPassword)
                    return true;
                }
                return false;
            }
        });

        signupPassword.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    signupConfirm.requestFocus(); // Move focus to the next EditText (signupConfirm)
                    return true;
                }
                return false;
            }
        });

        signupConfirm.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    signupButton.performClick();
                    return true;
                }
                return false;
            }
        });

        // This method allows the user to create their account and sign in
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = signupEmail.getText().toString().trim();
                String pass = signupPassword.getText().toString().trim();
                String passConfirm = signupConfirm.getText().toString().trim();

                if (user.isEmpty()){
                    signupEmail.setError("Email cannot be empty");
                }
                if (pass.isEmpty()){
                    signupPassword.setError("Password cannot be empty");
                }
                if (!pass.equals(passConfirm)){
                    signupConfirm.setError("Passwords do not match");
                }else{
                    auth.createUserWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SignUpActivity.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignUpActivity.this, HomePageActivity.class));
                            } else {
                                Toast.makeText(SignUpActivity.this, "SignUp Failed: Invalid Password or Email", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });

        // This method redirects the user to login page
        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });

    }
}