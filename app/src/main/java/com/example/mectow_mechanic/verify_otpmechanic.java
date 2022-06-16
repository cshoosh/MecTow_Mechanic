package com.example.mectow_mechanic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class verify_otpmechanic extends AppCompatActivity {
    private EditText mech_inputotpcode1,mech_inputotpcode2,mech_inputotpcode3,mech_inputotpcode4,mech_inputotpcode5,mech_inputotpcode6;
    private String mech_verificationId,mech_mobilenumber,mech_phonenumber;
    ProgressBar progressBar;
    DatabaseReference reff,reference;
    FirebaseAuth auth;
    public String mech_type,userid;
    String mech_name, mech_email,_phonenumber, mech_password, mech_confirm_password, mech_userid;
    user User;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otpmechanic);
        mech_mobilenumber=getIntent().getExtras().getString("mobile");
        mech_phonenumber=mech_mobilenumber;
        mech_inputotpcode1=findViewById(R.id.mech_inputotpcode1);
        mech_inputotpcode2=findViewById(R.id.mech_inputotpcode2);
        mech_inputotpcode3=findViewById(R.id.mech_inputotpcode3);
        mech_inputotpcode4=findViewById(R.id.mech_inputotpcode4);
        mech_inputotpcode5=findViewById(R.id.mech_inputotpcode5);
        mech_inputotpcode6=findViewById(R.id.mech_inputotpcode6);
        setupOTPInput();
        progressBar=findViewById(R.id.progressbar1);
        final Button mech_verifybutton=findViewById(R.id.mech_verifybutton);
        mech_verificationId=getIntent().getStringExtra("verificationId");
        mech_verifybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mech_inputotpcode1.getText().toString().trim().isEmpty()
                        || mech_inputotpcode2.getText().toString().trim().isEmpty()
                        || mech_inputotpcode3.getText().toString().trim().isEmpty()
                        || mech_inputotpcode4.getText().toString().trim().isEmpty()
                        || mech_inputotpcode5.getText().toString().trim().isEmpty()
                        || mech_inputotpcode6.getText().toString().trim().isEmpty()
                ){
                    Toast.makeText(verify_otpmechanic.this, "Please enter valid code", Toast.LENGTH_SHORT).show();
                    return;
                }
                String code= mech_inputotpcode1.getText().toString() +
                        mech_inputotpcode2.getText().toString() +
                        mech_inputotpcode3.getText().toString() +
                        mech_inputotpcode4.getText().toString() +
                        mech_inputotpcode5.getText().toString() +
                        mech_inputotpcode6.getText().toString() ;
                if (mech_verificationId != null){
                    progressBar.setVisibility(View.VISIBLE);
                    mech_verifybutton.setVisibility(View.INVISIBLE);
                    PhoneAuthCredential phoneAuthCredential= PhoneAuthProvider.getCredential(
                            mech_verificationId,
                            code
                    );
                    FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    mech_verifybutton.setVisibility(View.VISIBLE);
                                    if (task.isSuccessful()){
                                        Toast.makeText(verify_otpmechanic.this, "Verified Phone!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(),verification.class);
                                        startActivity(intent);
                                    }
                                    else {
                                        Toast.makeText(verify_otpmechanic.this, "The verification code was entered is invalid", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    findViewById(R.id.mech_resenderotp).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                    "+92" + getIntent().getStringExtra("mobile"),
                                    60,
                                    TimeUnit.SECONDS,
                                    verify_otpmechanic.this,
                                    new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

                                        @Override
                                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                            Toast.makeText(verify_otpmechanic.this, "successful", Toast.LENGTH_SHORT).show();

                                        }

                                        @Override
                                        public void onVerificationFailed(@NonNull FirebaseException e) {

                                            Toast.makeText(verify_otpmechanic.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onCodeSent(@NonNull String newverificationID, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                            mech_verificationId=newverificationID;
                                            Toast.makeText(verify_otpmechanic.this, "OTP sent", Toast.LENGTH_SHORT).show();

                                        }
                                    }

                            );
                        }
                    });
                }

            }
        });

    }
    private void setupOTPInput(){
        mech_inputotpcode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    mech_inputotpcode2.requestFocus();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mech_inputotpcode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    mech_inputotpcode3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mech_inputotpcode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    mech_inputotpcode4.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mech_inputotpcode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    mech_inputotpcode5.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mech_inputotpcode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    mech_inputotpcode6.requestFocus();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }
    @Override
    public void onBackPressed() {

    }
}
