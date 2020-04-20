package com.amavr.femory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.amavr.tools.XMem;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.gson.Gson;

/// активити вызывается из главной формы
public class AuthActivity extends AppCompatActivity {

    final static int SOME_REQUEST_CODE = 123;
    final static String TAG = "XDBG.auth";

    GoogleSignInClient mGoogleClient;
    GoogleSignInAccount mGoogleAccount;
    FirebaseAuth mAuth;
    SignInButton btnSign;
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        XMem.create(getApplication());

        btnSign = (SignInButton) findViewById(R.id.btnSign);
        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /// https://developers.google.com/identity/sign-in/android/sign-in
                Intent intent = mGoogleClient.getSignInIntent();
                startActivityForResult(intent, SOME_REQUEST_CODE);
            }
        });

        /// проверка авторизации происходит по пользователю FB
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser fbu = mAuth.getCurrentUser();
        Log.d(TAG, String.format("Firebase user: %s", fbu));

//        btnSign.setVisibility(fbu == null ? View.VISIBLE : View.INVISIBLE);

        /// создание необходимых объектов для авторизация через гугл
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("323447805848-iep77ef5lqm62v5059p6516jq9a1tq0n.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleClient = GoogleSignIn.getClient(this, gso);
        /// потребуется для выхода из учетной записи гугл
        XMem.getInstance().setGoogleClient(mGoogleClient);

//        ///
//        if (fbu == null) {
//            Log.d(TAG, String.format("FB user: %s", fbu));
//        }
//        else{
//            backToMain();
//        }
    }

    private void backToMain() {
        setResult(200);
        finish();
    }

    protected void onActivityResult(final int requestCode, final int resultCode,
                                    final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SOME_REQUEST_CODE) {
            String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            Log.d(TAG, String.format("Google account: %s", accountName));
//            Log.d(TAG, String.format("Google account: %s", gson.toJson(data)));
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                mGoogleAccount = task.getResult(ApiException.class);
                Log.d(TAG, String.format("Selected account=%s", mGoogleAccount.getAccount().name));

                firebaseAuthWithGoogle(mGoogleAccount);

                // Signed in successfully, show authenticated UI.
                backToMain();
            } catch (ApiException e) {
                // The ApiException status code indicates the detailed failure reason.
                // Please refer to the GoogleSignInStatusCodes class reference for more information.
                String msg = String.format("error: %s", gson.toJson(e));
                Log.w(TAG, msg);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(TAG, "Google account ID: " + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
//        Log.d(TAG, "Auth credential: " + gson.toJson(credential));

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "Firebase signInWithCredential: success");
                            FirebaseUser user = mAuth.getCurrentUser();
//                            Log.d(TAG, String.format("Firebase user: %s", gson.toJson(user)));
                            XMem.getInstance().setFirebaseUser(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            String msg = String.format("Auth failure: %s", task.getException());
                            Log.w(TAG, "FB authfailure: " + msg);
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(0);
        Log.d(TAG, "On back");
    }
}
