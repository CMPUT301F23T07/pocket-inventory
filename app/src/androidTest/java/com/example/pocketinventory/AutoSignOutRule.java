package com.example.pocketinventory;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class AutoSignOutRule implements TestRule {
    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                try {
                    base.evaluate(); // Execute the test
                } finally {
                    signOut(); // Ensure sign out happens after test execution
                }
            }
        };
    }

    private void signOut() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signOut(); // Sign out from Firebase Auth
        System.out.println("Signed out successfully");
    }
}
