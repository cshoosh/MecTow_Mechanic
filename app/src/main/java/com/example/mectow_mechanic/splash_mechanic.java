package com.example.mectow_mechanic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class splash_mechanic extends AppCompatActivity {
    ImageView Splashlogo,Logobeat,Logoheader,Logobottom;
    TextView textLogo;
    CharSequence charSequence;
    int index;
    private DatabaseReference refer;
    FirebaseAuth auth;
    long delay=50;
    Handler handler=new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_mechanic);
        Splashlogo=findViewById(R.id.splashlogo);
        Logobeat=findViewById(R.id.logobeat);
        Logoheader=findViewById(R.id.logoheader);
        Logobottom=findViewById(R.id.logobottom);
        textLogo=findViewById(R.id.textlogo);
        auth = FirebaseAuth.getInstance();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Animation animation1= AnimationUtils.loadAnimation(this,R.anim.top_anim);
        Splashlogo.setAnimation(animation1);

        ObjectAnimator objectAnimator=ObjectAnimator.ofPropertyValuesHolder(Logoheader,
                PropertyValuesHolder.ofFloat("scaleX",1.2f),
                PropertyValuesHolder.ofFloat("scaleY",1.2f));

        objectAnimator.setDuration(1000);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        objectAnimator.start();
        animatText("It's Ready to Get Back on It's Track");
        Animation animation2= AnimationUtils.loadAnimation(this,R.anim.bottom_anim);
        Logobottom.setAnimation(animation2);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(splash_mechanic.this,login_mechanic.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }
        },4000);

    }
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            textLogo.setText(charSequence.subSequence(0,index++));
            if (index<=charSequence.length()){
                handler.postDelayed(runnable,delay);
            }
        }
    };
    public void animatText(CharSequence cs){
        charSequence=cs;
        index=0;
        textLogo.setText("");
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable,delay);
    }

}
