package com.example.luongtiendat.jobhilfe;

import android.support.design.widget.TextInputLayout;
import android.support.media.ExifInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;


public class BewertungActivity extends AppCompatActivity {

    private TextInputLayout mBewertung;

    private Button mBewertungBtn;

    private RatingBar mRatingBar;

    private Toolbar mToolBar;

    private FirebaseUser mCurrent_User;
    private DatabaseReference mUserDatabase,mRootRef;

    private TextView mTexview;
    private Float mRating;
    private String m1,m2,m3;
    private String mUserId,mCurrentID;
    private String mStatus,mName;

    private DatabaseReference mNotificationDatabase;
    private String newNotificationId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bewertung);

        final String arbeit_geber = getIntent().getStringExtra("arbeitgeber");
        final String arbeit_nehmer = getIntent().getStringExtra("arbeinehmer");
        final String auftrag_id = getIntent().getStringExtra("auftragid");
        final String arbeit_geber_name = getIntent().getStringExtra("arbeitgeber_name");
        final String arbeit_nehmer_name = getIntent().getStringExtra("arbeitnehmer_name");


       mToolBar = (Toolbar) findViewById(R.id.bewerten_layout_appbar);
       setSupportActionBar(mToolBar);
       getSupportActionBar().setTitle("Bewertung");
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);

       mBewertungBtn = (Button)findViewById(R.id.bewerten_btn);
       mBewertung = (TextInputLayout)findViewById(R.id.bewerten_text);
       mRatingBar = (RatingBar)findViewById(R.id.ratingBar);
       mTexview = (TextView)findViewById(R.id.textView);
       mRootRef = FirebaseDatabase.getInstance().getReference();
       mNotificationDatabase = FirebaseDatabase.getInstance().getReference().child("notifications");


       mCurrent_User = FirebaseAuth.getInstance().getCurrentUser();
       mUserId = mCurrent_User.getUid();
       mCurrentID = mCurrent_User.getUid();

        mRootRef.child("Users").child(mCurrentID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                mName = name;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

       if (mUserId.equals(arbeit_geber)){
           mUserId = arbeit_nehmer;
           mTexview.setText("Bitte Geben Sie Bewertung für :  " + arbeit_nehmer_name);
           mStatus= "4";
       }else {
           mUserId = arbeit_geber;
           mTexview.setText("Bitte Geben Sie Bewertung für :  " + arbeit_geber_name);
           mStatus= "5";
       }

        //Toast.makeText(BewertungActivity.this,mCurrent_uid,Toast.LENGTH_LONG).show();

        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                mRating = ratingBar.getRating();

                m1= new Float(mRating).toString();

            }
        });

       mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(mUserId);


       mUserDatabase.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               String bewertung_count = dataSnapshot.child("bewertung_count").getValue().toString();
               String bewertung_value = dataSnapshot.child("bewertung_value").getValue().toString();

               m2 = bewertung_count;
               m3 = bewertung_value;

           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });

       mBewertungBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               String bewertungText = mBewertung.getEditText().getText().toString();
               if (bewertungText.isEmpty()){
                   bewertungText = "default";
               }

               if (m2.equals("default")){
                   DatabaseReference newNotificationref = mRootRef.child("notifications").child(mUserId).push();
                   newNotificationId = newNotificationref.getKey();

                   HashMap<String, String> notificationData = new HashMap<>();
                   notificationData.put("from", mName);
                   notificationData.put("type", "Eine Bewertung wurde abgegeben");

                   Map bewertungMap = new HashMap();
                   bewertungMap.put("Users/" + mUserId + "/bewertung_count","1");
                   bewertungMap.put("Users/" + mUserId + "/bewertung_value",m1);
                   bewertungMap.put("Auftrags/" + auftrag_id + "/status",mStatus);
                   bewertungMap.put("Bewertungs/" + mUserId + "/" + auftrag_id + "/text",bewertungText );
                   bewertungMap.put("Bewertungs/" + mUserId + "/" + auftrag_id + "/rate",m1 );
                   bewertungMap.put("Bewertungs/" + mUserId + "/" + auftrag_id + "/from",mName );
                   bewertungMap.put("notifications/" + mUserId + "/" + newNotificationId, notificationData);

                   mRootRef.updateChildren(bewertungMap, new DatabaseReference.CompletionListener() {
                       @Override
                       public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                           if (databaseError == null){
                               Toast.makeText(BewertungActivity.this,"Bewertung wurde abgegeben!",Toast.LENGTH_LONG).show();
                           }
                           else {

                           }
                       }
                   });

               }else {
                   Integer bewertung_count_int = Integer.parseInt(m2);
                   Float bewertung_value_float = Float.parseFloat(m3);

                   bewertung_value_float = (bewertung_value_float * bewertung_count_int + mRating) / ( bewertung_count_int +1);

                   bewertung_count_int = bewertung_count_int +1;


                   m2 = Integer.toString(bewertung_count_int);

                   DatabaseReference newNotificationref = mRootRef.child("notifications").child(mUserId).push();
                   newNotificationId = newNotificationref.getKey();

                   HashMap<String, String> notificationData = new HashMap<>();
                   notificationData.put("from", mName);
                   notificationData.put("type", "Eine Bewertung wurde abgegeben");

                   Map bewertungMap = new HashMap();
                   bewertungMap.put("Users/" + mUserId + "/bewertung_count",m2);
                   bewertungMap.put("Users/" + mUserId + "/bewertung_value",bewertung_value_float);
                   bewertungMap.put("Auftrags/" + auftrag_id + "/status",mStatus);
                   bewertungMap.put("Bewertungs/" + mUserId + "/" + auftrag_id + "/text",bewertungText );
                   bewertungMap.put("Bewertungs/" + mUserId + "/" + auftrag_id + "/rate",m1 );
                   bewertungMap.put("Bewertungs/" + mUserId + "/" + auftrag_id + "/from",mName );
                   bewertungMap.put("notifications/" + mUserId + "/" + newNotificationId, notificationData);

                   mRootRef.updateChildren(bewertungMap, new DatabaseReference.CompletionListener() {
                       @Override
                       public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                           if (databaseError == null){
                               Toast.makeText(BewertungActivity.this,"Bewertung wurde abgegeben!",Toast.LENGTH_LONG).show();
                           }
                           else {

                           }
                       }
                   });

               }

               mBewertungBtn.setEnabled(false);
               mBewertungBtn.setVisibility(View.INVISIBLE);

           }

       });



    }


}
