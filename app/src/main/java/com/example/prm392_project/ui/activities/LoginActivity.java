package com.example.prm392_project.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392_project.R;
import com.example.prm392_project.data.external.interfaces.ApiCallback;
import com.example.prm392_project.data.external.response.BaseResp;
import com.example.prm392_project.data.external.response.LoginResp;
import com.example.prm392_project.data.external.services.AuthSvc;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {
    public static final String TAG = LoginActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 9001;

    private EditText etEmail, etPassword;
    private Button btnLogin, btnRegister, btnGoogleLogin;
    private ProgressBar progressBar;
    private AuthSvc authSvc;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Configure Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Initialize UI components
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        btnGoogleLogin = findViewById(R.id.btnGoogleLogin);
        progressBar = findViewById(R.id.progressBar);
        authSvc = new AuthSvc(LoginActivity.this);

        // Set click listeners
        btnLogin.setOnClickListener(v -> loginUser());
        btnRegister.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
        btnGoogleLogin.setOnClickListener(v -> loginGoogle());
    }

    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        progressBar.setVisibility(View.VISIBLE);

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter all details", Toast.LENGTH_SHORT).show();
            return;
        }

        authSvc.login(email, password, new ApiCallback<BaseResp<LoginResp>>() {
            @Override
            public void onSuccess(BaseResp<LoginResp> response) {
                progressBar.setVisibility(View.GONE);
                LoginResp data = response.getData();
                if (data != null) {
                    Toast.makeText(LoginActivity.this, "Login successful UserId: " + data.getUserId(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, response.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String message) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "Login error: " + message);
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loginGoogle() {
        progressBar.setVisibility(View.VISIBLE);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    firebaseAuthWithGoogle(account);
                } else {
                    progressBar.setVisibility(View.GONE);
                    Log.w(TAG, "Google Sign-In account is null");
                    Toast.makeText(this, "Google Sign-In failed: No account", Toast.LENGTH_SHORT).show();
                }
            } catch (ApiException e) {
                progressBar.setVisibility(View.GONE);
                Log.w(TAG, "Google Sign-In failed with status code: " + e.getStatusCode(), e);
                Toast.makeText(this, "Google Sign-In failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        String googleIdToken = account.getIdToken();
        if (googleIdToken == null) {
            progressBar.setVisibility(View.GONE);
            Log.w(TAG, "Google ID Token is null");
            Toast.makeText(this, "Failed to get Google ID Token", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, "Google ID Token: " + googleIdToken);

        // Sign in with Firebase using the Google token
        AuthCredential credential = GoogleAuthProvider.getCredential(googleIdToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Firebase Auth sign-in successful");
                        // Get Firebase ID token after successful sign-in
                        mAuth.getCurrentUser().getIdToken(false)
                                .addOnCompleteListener(tokenTask -> {
                                    if (tokenTask.isSuccessful()) {
                                        String firebaseIdToken = tokenTask.getResult().getToken();
                                        Log.d(TAG, "Firebase ID Token: " + firebaseIdToken);

                                        // Send Firebase token to server
                                        authSvc.loginFirebase(firebaseIdToken, new ApiCallback<BaseResp<LoginResp>>() {
                                            @Override
                                            public void onSuccess(BaseResp<LoginResp> response) {
                                                LoginResp data = response.getData();
                                                progressBar.setVisibility(View.GONE);
                                                if (data != null) {
                                                    Toast.makeText(LoginActivity.this, "Google Login successful UserId: " + data.getUserId(), Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                                    intent.putExtra("idToken", firebaseIdToken);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    Toast.makeText(LoginActivity.this, response.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onError(String message) {
                                                progressBar.setVisibility(View.GONE);
                                                Log.e(TAG, "Google Login error: " + message);
                                                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    } else {
                                        progressBar.setVisibility(View.GONE);
                                        Log.w(TAG, "Failed to get Firebase ID Token", tokenTask.getException());
                                        Toast.makeText(LoginActivity.this, "Failed to get Firebase ID Token", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Log.w(TAG, "Firebase Auth sign-in failed", task.getException());
                        Toast.makeText(LoginActivity.this, "Firebase authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}