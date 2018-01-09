package com.example.luongtiendat.jobhilfe;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
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
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class UserSettingsActivity extends AppCompatActivity {

    private CircleImageView mDisplayImage;

    private TextView mDisplayName, mEmail,mPhone,mAuftragCount,mBewertungCount;

    private Button mUpdateProfile,mChangeFoto;

    private static final int gallery_pick =1;
    private static final int MAX_LENGTH = 10;

    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;

    private StorageReference mStorageProfileImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mDisplayImage = (CircleImageView)findViewById(R.id.profile_image_avatar);
        mDisplayName = (TextView)findViewById(R.id.profile_displayname);
        mEmail = (TextView)findViewById(R.id.profile_email_id);
        mPhone = (TextView)findViewById(R.id.profile_phone_nummer);
        mAuftragCount = (TextView)findViewById(R.id.profile_auftrag_count);
        mBewertungCount = (TextView)findViewById(R.id.profile_bewertung_count);
        mUpdateProfile = (Button)findViewById(R.id.profile_update_btn);
        mChangeFoto = (Button)findViewById(R.id.profile_image_btn);

        final String user_id = getIntent().getStringExtra("user_id");

        //Firebase
        mStorageProfileImage = FirebaseStorage.getInstance().getReference();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String email = dataSnapshot.child("email").getValue().toString();
                String phone = dataSnapshot.child("phone").getValue().toString();
                String auftrag_count = dataSnapshot.child("auftrag_count").getValue().toString();
                String bewertung_count = dataSnapshot.child("bewertung_count").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                //String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();

                mDisplayName.setText(name);
                mEmail.setText(email);
                mPhone.setText(phone);
                mAuftragCount.setText(auftrag_count);
                mBewertungCount.setText(bewertung_count);

                if(!image.equals("default")){
                    Picasso.with(UserSettingsActivity.this).load(image).placeholder(R.drawable.default_avatar).into(mDisplayImage);
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if (mCurrentUser.getUid().equals(user_id)){
            mUpdateProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name_value = mDisplayName.getText().toString();
                    String phone_value = mPhone.getText().toString();
                    String email_value = mEmail.getText().toString();

                    Intent profileEditIntent = new Intent(UserSettingsActivity.this,UserProfileEditActivity.class);
                    profileEditIntent.putExtra("name_value",name_value);
                    profileEditIntent.putExtra("phone_value",phone_value);
                    profileEditIntent.putExtra("email_value",email_value);
                    startActivity(profileEditIntent);
                }
            });

            mChangeFoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent galleryIntent = new Intent();
                    galleryIntent.setType("image/+");
                    galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                    startActivityForResult(Intent.createChooser(galleryIntent,"SELECT IMAGE"),gallery_pick);
                }
            });
        }else {
            mUpdateProfile.setVisibility(View.INVISIBLE);
            mChangeFoto.setVisibility(View.INVISIBLE);
        }




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == gallery_pick && resultCode == RESULT_OK){

            Uri imageUrl = data.getData();

            CropImage.activity(imageUrl)
                    .setAspectRatio(1,1)
                    .start(this);


            //Toast.makeText(SettingActivity.this,imageUrl,Toast.LENGTH_LONG).show();

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                //Dialog hier hinzufügen better =)

                Uri resultUri = result.getUri();

                File thumb_filePath = new File(resultUri.getPath());

                String current_User_id = mCurrentUser.getUid();

                Bitmap thumb_bitmap = new Compressor(this)
                        .setMaxWidth(200)
                        .setMaxHeight(200)
                        .setQuality(76)
                        .compressToBitmap(thumb_filePath);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                final byte[] thumb_byte = baos.toByteArray();

                StorageReference filepath = mStorageProfileImage.child("Profile_image").child(current_User_id+".jpg");
                final StorageReference thump_filepath = mStorageProfileImage.child("Profile_image").child("thumbs").child(current_User_id+"jpg");

                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){

                            //Toast.makeText(SettingActivity.this,"Working",Toast.LENGTH_LONG).show();

                            final @SuppressWarnings("VisibleForTests")String download_url = task.getResult().getDownloadUrl().toString(); //Achtung fehler

                            UploadTask uploadTask = thump_filepath.putBytes(thumb_byte);
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                    @SuppressWarnings("VisibleForTests")String thumb_downloadUrl = task.getResult().getDownloadUrl().toString();

                                    if(task.isSuccessful()){

                                        Map update_hashMap = new HashMap<>();
                                        update_hashMap.put("image",download_url);
                                        update_hashMap.put("thumb_image",thumb_downloadUrl);

                                        mUserDatabase.updateChildren(update_hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){

                                                    Toast.makeText(UserSettingsActivity.this,"Success Uploading",Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                    }
                                    else{
                                        Toast.makeText(UserSettingsActivity.this,"Error",Toast.LENGTH_LONG).show();

                                    }
                                }
                            });


                        }else{
                            Toast.makeText(UserSettingsActivity.this,"Error",Toast.LENGTH_LONG).show();

                        }

                    }
                });

            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

            }
        }
    }

    // random String =)
    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(MAX_LENGTH);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }
}

