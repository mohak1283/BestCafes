package com.mohak.android.bestcafes;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private String mVerificationId;
    private ProgressBar mProgressBar;
    Button send_otp_btn_signup;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    FirebaseAuth mFirebaseauth;
    String phonenumber;
    private static final String LOG_TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        send_otp_btn_signup = findViewById(R.id.send_otp_btn_signup);
        editText = findViewById(R.id.textInputEditText);
        mProgressBar = (ProgressBar) findViewById(R.id.main_progress);
        mFirebaseauth = FirebaseAuth.getInstance();

        mProgressBar.setVisibility(View.INVISIBLE);

        send_otp_btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //if (checkBox2.isChecked()) {

                    ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                    assert connectivityManager != null;
                    if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                        //we are connected to a network
                        //  connected = true;


                        phonenumber = editText.getText().toString();

                        phonenumber = "+91" + phonenumber;
                        mProgressBar.setVisibility(View.VISIBLE);
                        PhoneAuthProvider.getInstance().verifyPhoneNumber(phonenumber, 60, TimeUnit.SECONDS, MainActivity.this, mCallbacks);

                        //      checkPermissions();



                    } else {
                        //connected = false;
                        Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
               // } else {
                   // Toast.makeText(MainActivity.this, "You need to be 13 years or older to use SeKreative!", Toast.LENGTH_SHORT).show();
                //}

            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                Log.e(LOG_TAG, "Entered method onVerificationCompleted");


            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                mProgressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Error occurred! Please try again", Toast.LENGTH_SHORT).show();
                Log.e(LOG_TAG, "Entered method onVerificationFailed", e);


            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.

                mProgressBar.setVisibility(View.INVISIBLE);

                Log.e(LOG_TAG, "Entered method onCodeSent");

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                Log.e(LOG_TAG, "Verification Id is " + mVerificationId);
                mResendToken = token;

                Intent intent = new Intent(MainActivity.this, OtpActivitySignUp.class);
                intent.putExtra("phoneNumber", phonenumber);
                intent.putExtra("verificationId", mVerificationId);
                startActivity(intent);
                finish();
            }


        };
    }
}
