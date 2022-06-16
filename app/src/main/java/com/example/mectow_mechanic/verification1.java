package com.example.mectow_mechanic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class verification1 extends AppCompatActivity {
    ImageView nic_pic_front,nic_pic_back;
    FirebaseUser user;
    public static String currentUid;
    private static final String Mechanic = "mechanic";
    private StorageReference storageReference;
    private FirebaseDatabase database;
    private String uid;
    private Uri imageuri,imageuri2;
    String nicfronturl,nicbackurl;
    Button nic_picbtn,nextbtn;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification1);
        nic_pic_front=findViewById(R.id.nic_front);
        nic_pic_back=findViewById(R.id.nic_back);
        nic_picbtn=findViewById(R.id.update_nicpics);
        nextbtn=findViewById(R.id.next);
        storageReference= FirebaseStorage.getInstance().getReference();
        database = FirebaseDatabase.getInstance();
        user=FirebaseAuth.getInstance().getCurrentUser();
        uid=currentUid;
        reference = FirebaseDatabase.getInstance().getReference("Mechanic");
        reference = database.getReference("Mechanic").child(uid);
        reference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               if(snapshot.child("nicfront").exists() ){
                   Picasso.get().load(snapshot.child("nicfront").getValue().toString()).into(nic_pic_front);

               }
               else if (snapshot.child("nicback").exists()){
                   Picasso.get().load(snapshot.child("nicback").getValue().toString()).into(nic_pic_back);
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
        nic_pic_front.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // upload pic code here.....
                Intent galleryintent=new Intent();
                galleryintent.setAction(Intent.ACTION_GET_CONTENT);
                galleryintent.setType("image/*");
                startActivityForResult(galleryintent,1);

                return false;
            }
        });
        nic_pic_back.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // upload pic code here.....
                Intent galleryintent2=new Intent();
                galleryintent2.setAction(Intent.ACTION_GET_CONTENT);
                galleryintent2.setType("image/*");
                startActivityForResult(galleryintent2,2);

                return false;
            }
        });
        nic_picbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageuri != null){
                    SaveImageUrl(imageuri);
                }
                else if(imageuri2!=null){
                    SaveImageUrl(imageuri2);
                }
            }
        });


    }
    private void SaveImageUrl(Uri uri) {
        StorageReference fileref = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        fileref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        nicfronturl = uri.toString();
                        nicbackurl = uri.toString();
                        DatabaseReference reff = FirebaseDatabase.getInstance()
                                .getReference("Mechanic")
                                .child(uid);
                        Log.i("AUTH_USER_ID", uid);
                        HashMap<String, Object> saveimage = new HashMap<>();
                        saveimage.put("nicfront", nicfronturl);
                        saveimage.put("nicback", nicbackurl);
                        reff.updateChildren(saveimage).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(verification1.this, "NIC Front and Back Picture Updated", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                nextbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(verification1.this,verification2.class);
                        startActivity(intent);
                    }
                });
            }
        });
    }
    private String getFileExtension(Uri mUri) {
        ContentResolver cr=verification1.this.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            imageuri = data.getData();
            nic_pic_front.setImageURI(imageuri);

        }
        else if(requestCode == 2 && resultCode == RESULT_OK && data != null){
            imageuri2 = data.getData();
            nic_pic_back.setImageURI(imageuri2);
        }
    }
}