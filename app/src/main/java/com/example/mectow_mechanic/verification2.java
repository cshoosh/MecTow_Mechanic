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
import android.widget.EditText;
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

public class verification2 extends AppCompatActivity {

    FirebaseUser user;
    public static String currentUid;
    private static final String Mechanic = "mechanic";
    private StorageReference storageReference;
    private FirebaseDatabase database;
    private String uid;
    private Uri imageuri;
    String licenseurl;
    private DatabaseReference reference;
    EditText experience_num,experience_place;
    ImageView license;
    Button upload_data,nextpage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification2);

        experience_num=findViewById(R.id.no_experience);
        experience_place=findViewById(R.id.experience_shop_name);
        license=findViewById(R.id.licence_pic);
        upload_data=findViewById(R.id.updata_info);
        nextpage=findViewById(R.id.next_pg);
        storageReference= FirebaseStorage.getInstance().getReference();
        database = FirebaseDatabase.getInstance();
        user= FirebaseAuth.getInstance().getCurrentUser();
        uid=currentUid;
        reference = FirebaseDatabase.getInstance().getReference("Mechanic");
        reference = database.getReference("Mechanic").child(uid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("certificate").exists() ){
                    Picasso.get().load(snapshot.child("certificate").getValue().toString()).into(license);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        license.setOnLongClickListener(new View.OnLongClickListener() {
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
        upload_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageuri != null){
                    SaveImageUrl(imageuri);
                }
            }
        });
        nextpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(verification2.this,verification_complete.class);
                startActivity(intent);
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

                        licenseurl = uri.toString();
                        DatabaseReference reff = FirebaseDatabase.getInstance()
                                .getReference("Mechanic")
                                .child(uid);
                        Log.i("AUTH_USER_ID", uid);
                        HashMap<String, Object> saveimage = new HashMap<>();
                        saveimage.put("certificate", licenseurl);
                        saveimage.put("experience",experience_num.getText().toString());
                        saveimage.put("experience place",experience_place.getText().toString());


                        reff.updateChildren(saveimage).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(verification2.this, "Data Uploaded Successfully!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }
        });
    }
    private String getFileExtension(Uri mUri) {
        ContentResolver cr=verification2.this.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            imageuri = data.getData();
            license.setImageURI(imageuri);

        }

    }
}