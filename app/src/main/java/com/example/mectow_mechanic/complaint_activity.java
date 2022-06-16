package com.example.mectow_mechanic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mectow_mechanic.ui.home.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.HashMap;

public class complaint_activity extends AppCompatActivity {
    TextView complain1, complain2, complain3, complain4;
    EditText othdetail;
    String id, rate, R_id;
    DecimalFormat format;
    DatabaseReference reference;
    FirebaseAuth auth;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_complaint_activity);


//        Intent intent = getIntent();
//        id = intent.getStringExtra("ID");
//        rate = intent.getStringExtra("Rating");
       // R_id = intent.getStringExtra("randomid");

        auth = FirebaseAuth.getInstance();

        complain1 = findViewById(R.id.complain1);
        complain2 = findViewById(R.id.complain2);
        complain3 = findViewById(R.id.complain3);
        complain4 = findViewById(R.id.complain4);
        othdetail = findViewById(R.id.otherdetail);

        complain1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(complain1.getCurrentTextColor() == getResources().getColor(R.color.cadetBlue)) {
                    complain1.setTextColor(getResources().getColor(R.color.colorAccent));
                    complain4.setTextColor(getResources().getColor(R.color.cadetBlue));
                    othdetail.setVisibility(View.INVISIBLE);
                }
                else{
                    complain1.setTextColor(getResources().getColor(R.color.cadetBlue));
                }
            }
        });
        complain2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(complain2.getCurrentTextColor() == getResources().getColor(R.color.cadetBlue)) {
                    complain2.setTextColor(getResources().getColor(R.color.colorAccent));
                    complain4.setTextColor(getResources().getColor(R.color.cadetBlue));
                    othdetail.setVisibility(View.INVISIBLE);
                }
                else{
                    complain2.setTextColor(getResources().getColor(R.color.cadetBlue));
                }
            }
        });
        complain3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (complain3.getCurrentTextColor() == getResources().getColor(R.color.cadetBlue)) {
                    complain3.setTextColor(getResources().getColor(R.color.colorAccent));
                    complain4.setTextColor(getResources().getColor(R.color.cadetBlue));
                    othdetail.setVisibility(View.INVISIBLE);
                }
                else{
                    complain3.setTextColor(getResources().getColor(R.color.cadetBlue));
                }
            }
        });
        complain4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                complain4.setTextColor(getResources().getColor(R.color.colorAccent));
                complain1.setTextColor(getResources().getColor(R.color.cadetBlue));
                complain2.setTextColor(getResources().getColor(R.color.cadetBlue));
                complain3.setTextColor(getResources().getColor(R.color.cadetBlue));

                othdetail.setVisibility(View.VISIBLE);
            }
        });
    }

    public void SendComplain(View view) {

        if(complain1.getCurrentTextColor() == getResources().getColor(R.color.colorAccent) ||
                complain2.getCurrentTextColor() == getResources().getColor(R.color.colorAccent) ||
                complain3.getCurrentTextColor() == getResources().getColor(R.color.colorAccent) ||
                complain4.getCurrentTextColor() == getResources().getColor(R.color.colorAccent)) {

            reference = FirebaseDatabase.getInstance().getReference("Customers");
            HashMap<String, Object> rating = new HashMap<>();
            rating.put("rating", rate);
            reference.child(id).updateChildren(rating);

            Toast.makeText(getApplicationContext(), "Thank you for the feedback to Kaam Wala", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getApplicationContext(), HomeFragment.class);
            startActivity(intent);
            finishAffinity();
        }

        if (complain1.getCurrentTextColor() == getResources().getColor(R.color.colorAccent)){
            reference = FirebaseDatabase.getInstance().getReference("Complain");
            HashMap<String, Object> complain1 = new HashMap<>();
            complain1.put("Karigar Complain 1", "Customer was not behaving friendly");
            reference.child(R_id).updateChildren(complain1);
        }
        if(complain2.getCurrentTextColor() == getResources().getColor(R.color.colorAccent))
        {
            reference = FirebaseDatabase.getInstance().getReference("Complain");
            HashMap<String, Object> complain1 = new HashMap<>();
            complain1.put("Karigar Complain 2", "Customer argued me about the fare");
            reference.child(R_id).updateChildren(complain1);
        }
        if(complain3.getCurrentTextColor() == getResources().getColor(R.color.colorAccent))
        {
            reference = FirebaseDatabase.getInstance().getReference("Complain");
            HashMap<String, Object> complain1 = new HashMap<>();
            complain1.put("Karigar Complain 3", "Customer complaint was not same as requested complaint");
            reference.child(R_id).updateChildren(complain1);
        }
        if(complain4.getCurrentTextColor() == getResources().getColor(R.color.colorAccent))
        {
            reference = FirebaseDatabase.getInstance().getReference("Complain");
            HashMap<String, Object> complain1 = new HashMap<>();
            complain1.put("Karigar Complain 4", othdetail.getText().toString());
            reference.child(R_id).updateChildren(complain1);
        }
        if(complain1.getCurrentTextColor() != getResources().getColor(R.color.colorAccent) &&
                complain2.getCurrentTextColor() != getResources().getColor(R.color.colorAccent) &&
                complain3.getCurrentTextColor() != getResources().getColor(R.color.colorAccent) &&
                complain4.getCurrentTextColor() != getResources().getColor(R.color.colorAccent)){
            Toast.makeText(getApplicationContext(), "Select the Complain", Toast.LENGTH_SHORT).show();
        }
    }

}