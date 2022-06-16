package com.example.mectow_mechanic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class verification_complete extends AppCompatActivity {
    Button completebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_complete);
        completebtn=findViewById(R.id.complete_btn_verification);
        completebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(verification_complete.this,login_mechanic.class);
                startActivity(intent);
            }
        });
    }
}