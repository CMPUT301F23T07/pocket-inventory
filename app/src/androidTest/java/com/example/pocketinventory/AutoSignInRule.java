package com.example.pocketinventory;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.concurrent.CountDownLatch;

public class AutoSignInRule implements TestRule {
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                CountDownLatch latch = new CountDownLatch(1);
                signIn(latch);
                latch.await();
                base.evaluate();
            }
        };
    }

    private void signIn(CountDownLatch latch) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword("test@test.com", "testtest")
                .addOnCompleteListener(task -> {
                    latch.countDown();
                    if (task.isSuccessful()) {
                        System.out.println("Sign in successful");
                    } else {
                        System.out.println("Sign in failed");
                    }
                });
    }




}
