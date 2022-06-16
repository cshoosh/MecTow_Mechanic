package com.example.mectow_mechanic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forget_pass_mechanic extends AppCompatActivity {
    private EditText mech_forget_email;
    private Button mech_reset_passbtn;
    private String mech_email;
    private ProgressBar mech_progressBar;
    FirebaseAuth mech_auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass_mechanic);
        mech_forget_email=(EditText)findViewById(R.id.mech_forgotemail);
        mech_reset_passbtn=(Button)findViewById(R.id.mech_reset);
        mech_progressBar=(ProgressBar) findViewById(R.id.mech_progressbar);
        mech_auth=FirebaseAuth.getInstance();
        mech_reset_passbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mech_email=mech_forget_email.getText().toString();
                if (mech_email.isEmpty()){
                    Toast.makeText(forget_pass_mechanic.this, "Please provide your email!", Toast.LENGTH_SHORT).show();
                }
                else{
                    forget_password();
                }
            }

            private void forget_password() {
                FirebaseAuth auth=FirebaseAuth.getInstance();
                auth.sendPasswordResetEmail(mech_email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(forget_pass_mechanic.this, "check your email!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(forget_pass_mechanic.this,login_mechanic.class));
                                    finish();
                                }
                                else {
                                    Toast.makeText(forget_pass_mechanic.this, "Error: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    public void GoBackToLogin(View view) {
        Intent intent = new Intent(forget_pass_mechanic.this,login_mechanic.class);
        startActivity(intent);
    }
}
