package com.example.mectow_mechanic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.mectow_mechanic.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login_mechanic extends AppCompatActivity {
    Button mech_signup, mech_loginbtn, mech_forgetbtn;
    EditText mech_emailtxt, mech_passwordtxt;
    public static String services="";
    private String mech_email, mech_password;
    boolean state = true;
    private FirebaseAuth auth;
    private DatabaseReference refer;
    private ProgressBar progressBar;
    Boolean checkmechanic=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_mechanic);
        FirebaseApp.initializeApp(login_mechanic.this);
        mech_forgetbtn = (Button) findViewById(R.id.mech_forgot_password);
        mech_forgetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login_mechanic.this, verification1.class);
                startActivity(intent);
            }
        });
        mech_loginbtn = findViewById(R.id.mech_login);
        mech_signup = findViewById(R.id.mech_signuphere);
        mech_emailtxt = findViewById(R.id.mech_email_login);
        mech_passwordtxt = findViewById(R.id.mech_pass_login);
        //progressBar = (ProgressBar) findViewById(R.id.progressbar);
        auth = FirebaseAuth.getInstance();
        refer = FirebaseDatabase.getInstance().getReference("Mechanic");
        mech_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login_mechanic.this,signup_mechanic.class);
                startActivity(intent);
            }
        });
        mech_loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valid_data();
            }


            private void valid_data() {
                mech_email = mech_emailtxt.getText().toString();
                mech_password = mech_passwordtxt.getText().toString();
                if (mech_email.isEmpty() || mech_password.isEmpty()) {
                    Toast.makeText(login_mechanic.this, "Please provide all fields", Toast.LENGTH_SHORT).show();
                } else {
                    //mech_loginuser();
                    checkuseracceptedornot(mech_email,mech_password);
                }
            }
            private void checkuseracceptedornot(String email,String password){
                refer.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            String useremail=dataSnapshot.child("email").getValue().toString();
                            String _password=dataSnapshot.child("password").getValue().toString();
                            if (useremail.equals(mech_email)&& _password.equals(mech_password)) {
                                services=dataSnapshot.child("service").getValue().toString();
                                if (dataSnapshot.child("status").getValue().toString().equals("false")) {
                                    state=false;
                                }
                                checkmechanic=true;
                            }
                        }
                        move_forward();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
//                final Handler handler = new Handler(Looper.getMainLooper());
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        //Do something after 100ms
//                        if (state){
//                            Toast.makeText(login_mechanic.this, "Mechanic is not verified yet", Toast.LENGTH_SHORT).show();
//                        }
//                        else {
//                            mech_loginuser();
//                        }
//                    }
//                }, 10000);

            }
        });
    }
    public void move_forward(){
        if (!state){
            Toast.makeText(login_mechanic.this, "Mechanic is not verified yet", Toast.LENGTH_SHORT).show();
        }
        else if (checkmechanic && state){
            mech_loginuser();
        }
        else if (!checkmechanic){
            Toast.makeText(this, "User doesn't Exist!", Toast.LENGTH_SHORT).show();
        }
    }
    private void mech_loginuser() {
        auth.signInWithEmailAndPassword(mech_email, mech_password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mech_upload_data();
                        } else {
                            Toast.makeText(login_mechanic.this, "Error:" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    private void mech_upload_data() {

                        startActivity(new Intent(login_mechanic.this, HomeNavigation_mech.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(login_mechanic.this, "Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void ShowHidePass(View view) {
        if(view.getId()==R.id.show_pass_btn) {

            if (mech_passwordtxt.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                ((ImageView) (view)).setImageResource(R.drawable.eye);

                //Show Password
                mech_passwordtxt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                ((ImageView) (view)).setImageResource(R.drawable.invisible);

                //Hide Password
                mech_passwordtxt.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        }
    }
}
