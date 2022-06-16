package com.example.mectow_mechanic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class verification extends AppCompatActivity {
    Button verification_next;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        verification_next=findViewById(R.id.next_to_verification);
        verification_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(verification.this,verification1.class);
                startActivity(intent);
            }
        });
    }
}