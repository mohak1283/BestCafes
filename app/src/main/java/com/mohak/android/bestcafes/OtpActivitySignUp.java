package com.mohak.android.bestcafes;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class OtpActivitySignUp extends AppCompatActivity {

    private Button verifyButton;
    private EditText otpView;
    private String mVerificationId;
    FirebaseAuth mFirebaseauth;
    String verificationCode;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    String phn;

    private ProgressBar mProgressBar;

    //firebase

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    long phoneLong;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_sign_up);

        mFirebaseauth = FirebaseAuth.getInstance();

        mProgressBar = (ProgressBar) findViewById(R.id.verify_progress);

        mProgressBar.setVisibility(View.INVISIBLE);



        otpView = (EditText) findViewById(R.id.otp_view);



        Intent intent = getIntent();
        verifyButton = findViewById(R.id.verify_btn);

        String phonenumber = intent.getStringExtra("phoneNumber");

        mVerificationId = intent.getStringExtra("verificationId");


        verifyButton.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {

                verificationCode = otpView.getText().toString();

                if(!(TextUtils.isEmpty(verificationCode))){

                    mProgressBar.setVisibility(View.VISIBLE);

                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verificationCode);

                    boolean connected = false;

                    ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

                    assert connectivityManager != null;

                    if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||

                            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

                        //we are connected to a network

                        connected = true;

                        signInWithPhoneAuthCredential(credential);

                    } else {
                        connected = false;

                        Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    otpView.setError("Please enter OTP");
                }


            }

        });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        mFirebaseauth.signInWithCredential(credential)

                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override

                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            mProgressBar.setVisibility(View.INVISIBLE);

                         Intent intent = new Intent(OtpActivitySignUp.this , HomeActivity.class);
                         startActivity(intent);


                        } else {

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {

                                // The verification code entered was invalid

                                Toast.makeText(OtpActivitySignUp.this, "Invalid Code entered", Toast.LENGTH_SHORT).show();

                                mProgressBar.setVisibility(View.INVISIBLE);

                            }
                        }
                    }
                });
    }
}
