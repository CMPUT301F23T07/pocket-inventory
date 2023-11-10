package com.example.pocketinventory;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

/**
 * This class is the login activity of the app. It allows returning users to sign into their
 * accounts.
 * It also allows new users to go to the signup page and create an account.
 */
public class LoginActivity extends AppCompatActivity {

    private EditText loginEmail, loginPassword;
    private TextView signupRedirectText;
    private Button loginButton;
    private FirebaseAuth auth;


    /**
     * Called when the Login activity is starting or being reinitialized
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, HomePageActivity.class));
            finish();
        }

        loginEmail = findViewById(R.id.login_email);
        loginPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        signupRedirectText = findViewById(R.id.signUpRedirectText);

        // Set imeOptions for loginEmail and loginPassword
        loginEmail.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        loginPassword.setImeOptions(EditorInfo.IME_ACTION_DONE);

        // Add an OnEditorActionListener to loginEmail and loginPassword fields
        loginEmail.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    loginPassword.requestFocus(); // Move focus to the next EditText (loginPassword)
                    return true;
                }
                return false;
            }
        });

        loginPassword.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginButton.performClick();
                    return true;
                }
                return false;
            }
        });
        auth = FirebaseAuth.getInstance();

        // This method allows the returning users to login to their account
        loginButton.setOnClickListener(v -> {

            String email = loginEmail.getText().toString();
            String pass = loginPassword.getText().toString();

            if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                if (!pass.isEmpty()) {
                    auth.signInWithEmailAndPassword(email, pass)
                            .addOnSuccessListener(authResult -> {
                                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, HomePageActivity.class));
                                finish();
                            }).addOnFailureListener(e -> Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show());
                } else {
                    loginPassword.setError("This field is empty");
                }
            } else if (email.isEmpty()) {
                loginEmail.setError("This field is empty");
            } else {
                loginEmail.setError("Please enter correct email");
            }
        });

        // This method redirects the user to signup page
        signupRedirectText.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, SignUpActivity.class)));


    }
}
