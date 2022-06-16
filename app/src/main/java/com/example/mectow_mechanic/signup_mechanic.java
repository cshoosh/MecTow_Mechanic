package com.example.mectow_mechanic;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class signup_mechanic extends AppCompatActivity {
    public static String mech_name, mech_email, mech_phonenumber, mech_password, mech_confirm_password, mech_userid;
    private EditText mech_nametxt, mech_emailtxt, mech_phonetxt, mech_passtxt, mech_confirmpasstxt;
    private Button mech_loginherebtn, mech_registrationbtn1;
    String Category;
    private FirebaseAuth auth;
    Boolean checkuser = false;
    private DatabaseReference reference, reff;
    Spinner spinner;
    private FirebaseAuth mAuth;
    ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_mechanic);
        auth = FirebaseAuth.getInstance();
        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();
        mech_nametxt = findViewById(R.id.mech_name);
        mech_emailtxt = findViewById(R.id.mech_email);
        mech_phonetxt = findViewById(R.id.mech_phonenumber);
        mech_passtxt = findViewById(R.id.mech_password);
        spinner = (Spinner) findViewById(R.id.mech_signup_spinner);
        List<String> list = new ArrayList<String>();
        list.add("Choose your category");
        list.add("mechanic");
        list.add("cartow");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setPrompt("Select Category");
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mech_registrationbtn1 = findViewById(R.id.mech_registration);
        mech_loginherebtn = findViewById(R.id.mech_loginhere);
        mech_loginherebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signup_mechanic.this, login_mechanic.class);
                startActivity(intent);
            }
        });
        mech_confirmpasstxt = findViewById(R.id.mech_confirm_password);
        mech_registrationbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate_data();
            }
        });
    }


    private void open_main() {
        startActivity(new Intent(this, send_otp_mechanic.class));
        finish();
    }

    private void validate_data() {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String phonepattern = "[0-9]{11}";
        Category = spinner.getSelectedItem().toString();
        mech_name = mech_nametxt.getText().toString();
        mech_email = mech_emailtxt.getText().toString();
        mech_phonenumber = mech_phonetxt.getText().toString();
        mech_password = mech_passtxt.getText().toString();
        mech_confirm_password = mech_confirmpasstxt.getText().toString();
        if (mech_name.isEmpty()) {
            mech_nametxt.setError("required");
            mech_nametxt.requestFocus();
        } else if (mech_name.length() >= 15) {
            mech_nametxt.setError("Username too long");
            mech_nametxt.requestFocus();
        } else if (mech_email.isEmpty()) {
            mech_emailtxt.setError("required");
            mech_emailtxt.requestFocus();
        } else if (!mech_email.matches(emailPattern)) {
            mech_emailtxt.setError("Invalid email address");
            mech_emailtxt.requestFocus();
        } else if (mech_phonenumber.isEmpty()) {
            mech_phonetxt.setError("required");
            mech_phonetxt.requestFocus();
        } else if (!mech_phonenumber.matches(phonepattern)) {
            mech_phonetxt.setError("Pattern is incorrect");
            mech_phonetxt.requestFocus();
        } else if (mech_password.isEmpty()) {
            mech_passtxt.setError("required");
            mech_passtxt.requestFocus();
        } else if (!mech_password.matches(mech_confirm_password)) {
            mech_passtxt.setError("Not matched with Confirm password");
            mech_passtxt.requestFocus();
        } else if (mech_confirm_password.isEmpty()) {
            mech_confirmpasstxt.setError("required");
            mech_confirmpasstxt.requestFocus();
        } else if (Category.equals("Choose your category")) {
            Toast.makeText(this, "Choose your Category first!", Toast.LENGTH_SHORT).show();
        } else {
            create_user();
        }
    }

    private void create_user() {
        auth.createUserWithEmailAndPassword(mech_email, mech_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override

            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String userId = task.getResult().getUser().getUid();
                    verification1.currentUid = userId;
                    verification2.currentUid=userId;
                    Log.i("AUTH_USER_ID", userId);
                    upload_data(userId);
                } else {
                    Toast.makeText(signup_mechanic.this, "Error:" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(signup_mechanic.this, "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void upload_data(String uid) {
        reff = reference.child("Mechanic");
        String text = spinner.getSelectedItem().toString();
        HashMap<String, String> user = new HashMap<>();
        user.put("key", uid);
        user.put("name", mech_name);
        user.put("email", mech_email);
        user.put("phone_number", mech_phonenumber);
        user.put("password", mech_password);
        user.put("status", "false");
        user.put("service", text);
        user.put("confirm_password", mech_confirm_password);
        user.put("rating","5.0");
        reff.child(uid).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Toast.makeText(signup_mechanic.this, "user created", Toast.LENGTH_SHORT).show();
                    open_main();
                } else {
                    Toast.makeText(signup_mechanic.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(signup_mechanic.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void GoBackToLogin(View view) {
        Intent intent = new Intent(signup_mechanic.this, login_mechanic.class);
        startActivity(intent);
    }

    public void ShowHidePass(View view) {
        if (mech_passtxt.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
            ((ImageView) (view)).setImageResource(R.drawable.eye);

            //Show Password
            mech_passtxt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            ((ImageView) (view)).setImageResource(R.drawable.invisible);

            //Hide Password
            mech_passtxt.setTransformationMethod(PasswordTransformationMethod.getInstance());

        }
    }

    public void ShowHideCPass(View view) {
        if (mech_confirmpasstxt.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
            ((ImageView) (view)).setImageResource(R.drawable.eye);

            //Show Password
            mech_confirmpasstxt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            ((ImageView) (view)).setImageResource(R.drawable.invisible);

            //Hide Password
            mech_confirmpasstxt.setTransformationMethod(PasswordTransformationMethod.getInstance());

        }
    }
    @Override
    public void onBackPressed() {

    }
}