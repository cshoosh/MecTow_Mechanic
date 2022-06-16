package com.example.mectow_mechanic;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mectow_mechanic.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Random;

import static com.example.mectow_mechanic.ui.home.HomeFragment.customerrequestuid;

public class Activity_rate extends AppCompatActivity {
    String discountname ;
    String discount , amountCharged, randomid;
    TextView Cname, Kname, catagory,charges;
    RatingBar ratingBar;
    DecimalFormat format;
    FirebaseAuth auth;
    double totalcharges;
    String customerid;
    String requestid;
    String mechanicid;
    String id;
    DatabaseReference reference;
    public static int mech_totalcharges=0;
    double rate, f_rate;
    Button rec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        //initialize
        Cname = findViewById(R.id.name_text);
        Kname = findViewById(R.id.name_rate_field);
        catagory = findViewById(R.id.category_field);
        charges = findViewById(R.id.user_rating);
        //ratingBar = findViewById(R.id.simpleRatingBar);
        totalcharges = HomeFragment.totalcharges;
        auth = FirebaseAuth.getInstance();
        rec=findViewById(R.id.recammount);
        customerid = HomeFragment.customer_uid;
        requestid = customerrequestuid;
        mechanicid = auth.getUid();



        reference = FirebaseDatabase.getInstance().getReference("Complaint").child(requestid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                catagory.setText(dataSnapshot.child("Category").getValue().toString());
                if (login_mechanic.services.equals("mechanic")) {
                    charges.setText(dataSnapshot.child("charges").getValue().toString());
                }
                else if(login_mechanic.services.equals("cartow")){
                    charges.setText(dataSnapshot.child("totalcharges").getValue().toString());
                }
                    reference = FirebaseDatabase.getInstance().getReference("Customers").child(customerid);
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Cname.setText(dataSnapshot.child("name").getValue().toString());
                            Kname.setText(dataSnapshot.child("name").getValue().toString());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mech_totalcharges=Integer.parseInt(charges.getText().toString());
                    recv_charges((mech_totalcharges));

                Toast.makeText(getApplicationContext(), "received", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(Activity_rate.this,complaint_activity.class);
                startActivity(intent);
            }
        });

    }


    public  void recv_charges(int charged){

        reference = FirebaseDatabase.getInstance().getReference("Complaint").child(mechanicid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                reference = FirebaseDatabase.getInstance().getReference("Mechanic");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        HashMap<String , Object> charges = new HashMap<>();
                        charges.put("payment",charged);
//                        int sum = 0;
//                           int number = Integer.valueOf(dataSnapshot.child(mechanicid).child("sum").getValue().toString());
//                           sum = Integer.valueOf(charged) + number;
//                           charges.put("sum",Integer.valueOf(sum));
                        //charges.put("sum",String.valueOf(Double.parseDouble(charged)+Double.parseDouble(dataSnapshot.child(mechanicid).child("sum").getValue().toString())));
                        FirebaseDatabase.getInstance().getReference("Mechanic").child(mechanicid).updateChildren(charges);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


    }
}

//   }
//}
