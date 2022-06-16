package com.example.mectow_mechanic;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.example.mectow_mechanic.ui.home.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
@GlideModule

public class MessagingActivity extends Activity {
    TextView username;
    ImageButton btn_send;
    EditText txt_send;
    FirebaseAuth auth;
    DatabaseReference reference , ref;
    String userid;
    RecyclerView recyclerView;
    String customer_uid;
    MessageAdapter messageAdapter;
    CircleImageView profile_image;
    List<Chat> mChat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        profile_image=(CircleImageView)findViewById(R.id.profile_image);
        username = findViewById(R.id.username);
        btn_send = findViewById(R.id.btn_send);
        txt_send = findViewById(R.id.text_send);
        auth = FirebaseAuth.getInstance();
        Toolbar toolbar=findViewById(R.id.toolbar);
        if (getIntent().getExtras().getString("cid")!=null){
            customer_uid=getIntent().getExtras().getString("cid");
        }
        recyclerView =findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        ref =FirebaseDatabase.getInstance().getReference("Customers").child(customer_uid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                username.setText( dataSnapshot.child("name").getValue().toString());
                if(dataSnapshot.child("profileimage").exists()){
                    Picasso.get().load(dataSnapshot.child("profileimage").getValue().toString()).into(profile_image);
                }
                readMessage(auth.getUid(),customer_uid);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void readMessage(final String uid, final String recieverUID) {

        mChat=new ArrayList<>();
        reference=FirebaseDatabase.getInstance().getReference("Chats").child(HomeFragment.customerrequestuid);
        reference.keepSynced(true);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    Chat chat=snapshot.getValue(Chat.class);
                    if (chat.getReciver().equals(uid)&&chat.getSender().equals(recieverUID) ||
                            chat.getReciver().equals(recieverUID) && chat.getSender().equals(uid))
                    {
                        mChat.add(chat);
                    }
                    messageAdapter = new MessageAdapter(getApplicationContext(),mChat);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void sendMessage(String uid, String recieverUID, String message) {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Chats").child(HomeFragment.customerrequestuid);
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("sender",uid);
        hashMap.put("reciver",recieverUID);
        hashMap.put("message",message);
        reference.push().setValue(hashMap);
    }

    public void Submit(View view) {
        String message = txt_send.getText().toString();
        if (!message.equals("")){
            sendMessage(auth.getUid(),customer_uid, message);
        }
        else
        {
            Toast.makeText(getApplicationContext(), "You Can't Send Empty Message", Toast.LENGTH_SHORT).show();
        }
        txt_send.setText("");
    }
}
