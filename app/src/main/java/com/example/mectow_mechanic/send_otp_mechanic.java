package com.example.mectow_mechanic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class send_otp_mechanic extends AppCompatActivity {
    Button mech_getotpbtn;
    String ph_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_otp_mechanic);
        final TextView mech_inputMobile=findViewById(R.id.mech_inputmobile);
        final Button mech_buttonGetOTP=findViewById(R.id.mech_otpbutton);
        ph_num=signup_mechanic.mech_phonenumber.substring(1);
        mech_inputMobile.setText("+92"+ph_num);
        final ProgressBar mech_progressBar=findViewById(R.id.mech_progressbar);
        mech_buttonGetOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mech_inputMobile.getText().toString().trim().isEmpty()) {
                    Toast.makeText(send_otp_mechanic.this, "Enter Mobile", Toast.LENGTH_SHORT).show();
                    return;

                }
                mech_progressBar.setVisibility(View.VISIBLE);
                mech_buttonGetOTP.setVisibility(View.INVISIBLE);
                PhoneAuthProvider.getInstance().verifyPhoneNumber
                        (
                                "+92" +ph_num,
                                60,
                                TimeUnit.SECONDS,
                                send_otp_mechanic.this,
                                new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                                    @Override
                                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                        mech_progressBar.setVisibility(View.GONE);
                                        mech_buttonGetOTP.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onVerificationFailed(@NonNull FirebaseException e) {
                                        mech_progressBar.setVisibility(View.GONE);
                                        mech_buttonGetOTP.setVisibility(View.VISIBLE);
                                        Toast.makeText(send_otp_mechanic.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onCodeSent(@NonNull String mech_verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                        mech_progressBar.setVisibility(View.GONE);
                                        mech_buttonGetOTP.setVisibility(View.VISIBLE);
                                        Intent intent=new Intent(getApplicationContext(),verify_otpmechanic.class);
                                        intent.putExtra("mobile",mech_inputMobile.getText().toString());
                                        intent.putExtra("verificationId",mech_verificationId);
                                        startActivity(intent);

                                    }
                                }
                        );
            }
        });
    }

    @Override
    public void onBackPressed() {

    }
}