package com.example.luongtiendat.jobhilfe;

import android.content.Context;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luongtiendat.jobhilfe.Model.OfferUser;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
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
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class AuftragBewerbenActivity extends AppCompatActivity {

    //TESTEN GIT
    //NOCH TESTEN

    private static final int MAX_LENGTH = 10;
    private TextView mTitel,mAuftragReferen,mArbeitGeber,mArbeitOrt,mArbeitZeit,mStellenBeschreibung,
            mBeginnArbeit,mVergutung;

    private TextView mUserPhone,mUseremail;
    private Toolbar mToolBar;
    private Button mBewerbung;
    private Button mCancenRequest;

    private DatabaseReference mAuftragDatabase,mBewerbungRequestDatabase,mBewerbungDatabase;
    private DatabaseReference mUserDatabase;
    private DatabaseReference mUserOfferBewerbung;
    private DatabaseReference mOfferUser;
    private DatabaseReference mOfferUserDetail;
    private DatabaseReference mRootRef;
    private DatabaseReference mNotificationDatabase;
    private FirebaseUser mCurrent_User;


    private String mBewerben_stats;
    private String mUserId;
    private String mUserId02 ;
    private String newAuftragRequestId;
    private String mOfferUsername,mOfferUserimage;
    private String newNotificationId,newBewerbungId;

    private RecyclerView mOffenBewerbung;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auftrag_bewerben);

        mAuftragReferen = (TextView)findViewById(R.id.auftrag_id);
        mTitel = (TextView)findViewById(R.id.auftrag_titel);
        mArbeitOrt = (TextView)findViewById(R.id.auftrag_arbeit_ort);
        mArbeitZeit = (TextView)findViewById(R.id.auftrag_arbeit_zeit);
        mStellenBeschreibung = (TextView)findViewById(R.id.auftrag_stellen_beschreibung);
        mBeginnArbeit = (TextView)findViewById(R.id.auftrag_begin_arbeit);
        mVergutung = (TextView)findViewById(R.id.auftrag_vergutung);

        mUserPhone = (TextView)findViewById(R.id.auftrag_kontakt_phone);
        mUseremail = (TextView)findViewById(R.id.auftrag_kontakt_email);
        mArbeitGeber = (TextView)findViewById(R.id.auftrag_arbeit_geber);

        mOffenBewerbung = (RecyclerView)findViewById(R.id.auftrag_offen_bewerbung);
        mOffenBewerbung.setHasFixedSize(true);
        mOffenBewerbung.setLayoutManager(new LinearLayoutManager(this));

        mToolBar = (Toolbar)findViewById(R.id.auftrag_bewerben_appbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Auftrag Bewerbung");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mBewerbung = (Button)findViewById(R.id.auftrag_bewerben_btn);
        mCancenRequest = (Button)findViewById(R.id.auftrag_denin_bewerben_btn);

        mCancenRequest.setVisibility(View.INVISIBLE);
        mCancenRequest.setEnabled(false);

        final String auftrag_id = getIntent().getStringExtra("auftrag_id");
        mAuftragDatabase = FirebaseDatabase.getInstance().getReference().child("Auftrags").child(auftrag_id);
        mBewerbungRequestDatabase = FirebaseDatabase.getInstance().getReference().child("Bewerben_reg");
        mBewerbungDatabase = FirebaseDatabase.getInstance().getReference().child("Request_accept");
        mUserOfferBewerbung = FirebaseDatabase.getInstance().getReference().child("OfferUser").child(auftrag_id);
        mOfferUser = FirebaseDatabase.getInstance().getReference().child("OfferUser");
        mNotificationDatabase = FirebaseDatabase.getInstance().getReference().child("notifications");
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mCurrent_User = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference auftrag_id_ref = mAuftragDatabase.child(auftrag_id).push();
        newAuftragRequestId = auftrag_id_ref.getKey();

        mBewerben_stats ="not_bewerben";

        DatabaseReference newNotificationref = mRootRef.child("notifications").child(mCurrent_User.getUid()).push();
        newNotificationId = newNotificationref.getKey();

        mAuftragDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String titel = dataSnapshot.child("titel").getValue().toString();
                String arbeit_ort = dataSnapshot.child("arbeit_ort").getValue().toString();
                String arbeit_zeit = dataSnapshot.child("arbeit_zeit").getValue().toString();
                String arbeit_beginn = dataSnapshot.child("beginn").getValue().toString();
                String stellen_bes = dataSnapshot.child("stellen_beschreibung").getValue().toString();
                String vergutung = dataSnapshot.child("vergutung").getValue().toString();
                final String userId = dataSnapshot.child("userId").getValue().toString();
                final String mUser = dataSnapshot.child("mUser").getValue().toString();

                mUserId = userId;
                mUserId02 = mUser;

                mTitel.setText(titel);
                mArbeitOrt.setText(arbeit_ort);
                mArbeitZeit.setText(arbeit_zeit);
                mStellenBeschreibung.setText(stellen_bes);
                mBeginnArbeit.setText(arbeit_beginn);
                mVergutung.setText(vergutung);
                mAuftragReferen.setText(auftrag_id);

                mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

                mUserDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String display_name = dataSnapshot.child("name").getValue().toString();
                        String display_phone = dataSnapshot.child("phone").getValue().toString();
                        String display_email = dataSnapshot.child("email").getValue().toString();

                        mArbeitGeber.setText(display_name);
                        mUserPhone.setText(display_phone);
                        mUseremail.setText(display_email);

                        /// Mein Auftrag

                        if(mCurrent_User.getUid().equals(userId)){
                            mBewerbungRequestDatabase.child(mCurrent_User.getUid()).child(mUserId02).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChild(auftrag_id)){
                                        String req_type = dataSnapshot.child(auftrag_id).child("request_type").getValue().toString();

                                        if(req_type.equals("received_request")){
                                            mBewerben_stats = "req_received";
                                            mBewerbung.setText("Accept Bewerbung Request");

                                            mCancenRequest.setVisibility(View.VISIBLE);
                                            mCancenRequest.setEnabled(true);
                                        }
                                    }else {
                                        mBewerbungDatabase.child(mCurrent_User.getUid()).child(mUserId02).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.hasChild(auftrag_id)){
                                                   // String req_type = dataSnapshot.child(auftrag_id).child("request_type").getValue().toString(); //
                                                    mBewerben_stats = "request_accept";
                                                    mBewerbung.setText("Cancel This Request");

                                                    mCancenRequest.setVisibility(View.INVISIBLE);
                                                    mCancenRequest.setEnabled(false);

                                                }else
                                                {
                                                    mBewerbung.setText("Auftrag Bearbeiten");
                                                    mBewerben_stats = "edit_auftrag";
                                                    mCancenRequest.setVisibility(View.INVISIBLE);
                                                    mCancenRequest.setEnabled(false);

                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }else {
                            ///  nicht Mein Auftrag

                            mBewerbungRequestDatabase.child(auftrag_id).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChild(mUser)){
                                        String req_type = dataSnapshot.child("request_type").getValue().toString();

                                        if (req_type.equals("sent_requeset")){
                                            mBewerben_stats = "req_sent";
                                            mBewerbung.setText("Cancel Bewerbung Request");

                                            mCancenRequest.setVisibility(View.INVISIBLE);
                                            mCancenRequest.setEnabled(false);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        FirebaseRecyclerAdapter<OfferUser,UserViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<OfferUser, UserViewHolder>(
                OfferUser.class,
                R.layout.offen_singer_bewerbung_layout,
                UserViewHolder.class,
                mUserOfferBewerbung
        ) {
            @Override
            protected void populateViewHolder(UserViewHolder viewHolder, OfferUser model, int position) {
                viewHolder.setUserImage(model.getImage(),getApplication());
                viewHolder.setName(model.getName());
                String name = model.getName();
                // Toast.makeText(AuftragBewerbenActivity.this,name,Toast.LENGTH_SHORT).show();
                final String user_offer_requeset_id = getRef(position).getKey();

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent offerUserProfileIntent = new Intent(AuftragBewerbenActivity.this,UserSettingsActivity.class);
                        offerUserProfileIntent.putExtra("user_id",user_offer_requeset_id);
                        startActivity(offerUserProfileIntent);
                    }
                });


            }
        };

        mOffenBewerbung.setAdapter(firebaseRecyclerAdapter);


        mBewerbung.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                mBewerbung.setEnabled(false);

                // ------------------------ Noch nicht bewerben

                if(mBewerben_stats.equals("not_bewerben")){


                    HashMap<String, String> notificationData = new HashMap<>();
                    notificationData.put("from", mCurrent_User.getUid());
                    notificationData.put("type", "request");

                    Map requestMap = new HashMap();
                    requestMap.put("Bewerben_reg/" + mCurrent_User.getUid() + "/" + mUserId + "/"
                            + auftrag_id  + "/request_type","sent_requeset");
                    requestMap.put("Bewerben_reg/" + mUserId + "/" + mCurrent_User.getUid() + "/"
                            + auftrag_id + "/request_type","received_request");
                    requestMap.put("notifications/" + mUserId + "/" + newNotificationId, notificationData);
                    requestMap.put("Auftrags/" + auftrag_id + "/" +"mUser",mCurrent_User.getUid());

                    mRootRef.updateChildren(requestMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if(databaseError != null){
                                mAuftragDatabase.child("mUser").setValue(mCurrent_User.getUid()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                    }
                                });

                                Toast.makeText(AuftragBewerbenActivity.this, "There was some error in sending request", Toast.LENGTH_SHORT).show();

                            } else {

                                mBewerben_stats = "req_sent";
                                mBewerbung.setText("Cancel Friend Request");

                            }

                            mBewerbung.setEnabled(true);
                        }
                    });



                }

                // ------------------------ Schon beworben
                if(mBewerben_stats.equals("req_sent")){
                    mBewerbungRequestDatabase.child(mCurrent_User.getUid()).child(mUserId).child(auftrag_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mBewerbungRequestDatabase.child(mUserId).child(mCurrent_User.getUid()).child(auftrag_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Map requestMap = new HashMap();
                                    requestMap.put("Auftrags/" + auftrag_id + "/" +"mUser","default");
                                    mRootRef.updateChildren(requestMap, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                            if (databaseError != null){
                                                String error = databaseError.getMessage();

                                                Toast.makeText(AuftragBewerbenActivity.this, error, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                    mBewerben_stats = "not_bewerben";
                                    mBewerbung.setText("Send Bewerbung Request");
                                    mBewerbung.setEnabled(true);
                                    mCancenRequest.setVisibility(View.INVISIBLE);
                                    mCancenRequest.setEnabled(false);
                                }
                            });
                        }
                    });
                }

                // ------------------------ die Bewerbung annimmt

                if(mBewerben_stats.equals("req_received")){
                    final String currentDate = DateFormat.getDateTimeInstance().format(new Date());

                    Map kontaktMap = new HashMap();
                    kontaktMap.put("Kontakt/" + mCurrent_User.getUid() + "/" + mUserId02 + "/date",currentDate);
                    kontaktMap.put("Kontakt/" + mUserId02 + "/" + mCurrent_User.getUid() + "/date",currentDate);

                    kontaktMap.put("Bewerben_reg/" + mCurrent_User.getUid() + "/" + mUserId02 + "/" + auftrag_id,null);
                    kontaktMap.put("Bewerben_reg/" + mUserId02 + "/" + mUserId02 + "/" + mCurrent_User.getUid(),null);

                    kontaktMap.put("Request_accept/" + mCurrent_User.getUid() + "/" + mUserId02 + "/" + auftrag_id,currentDate);
                    kontaktMap.put("Request_accept/" + mUserId02 + "/" + mCurrent_User.getUid() + "/" + auftrag_id,currentDate);

                    mRootRef.updateChildren(kontaktMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                            if (databaseError == null){
                                mBewerben_stats = "auftrag_beworben";
                                mBewerbung.setText("Denin this Bewerbung");
                                mBewerbung.setEnabled(true);

                                mCancenRequest.setVisibility(View.INVISIBLE);
                                mCancenRequest.setEnabled(false);

                            }else {
                                String error = databaseError.getMessage();

                                Toast.makeText(AuftragBewerbenActivity.this, error, Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }


                // -------------Auftrag ablehnen

                if (mBewerben_stats.equals("auftrag_beworben")){
                    Map unkonkaktMap = new HashMap();
                    unkonkaktMap.put("Kontakt/" +  mCurrent_User.getUid() + "/" + mUserId02 ,null);
                    unkonkaktMap.put("Kontakt/" +  mUserId02 + "/" + mCurrent_User.getUid(),null);

                    unkonkaktMap.put("Request_accept/" + mCurrent_User.getUid() + "/" + mUserId02 + "/" + auftrag_id,null);
                    unkonkaktMap.put("Request_accept/" + mUserId02 + "/" + mCurrent_User.getUid() + "/" + auftrag_id,null);

                    unkonkaktMap.put("Auftrags/" + auftrag_id + "/" +"mUser","default");

                    mRootRef.updateChildren(unkonkaktMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError == null){
                                mBewerben_stats = "not_bewerben";
                                mBewerbung.setText("Send Bewerbung Request");

                                mCancenRequest.setVisibility(View.INVISIBLE);
                                mCancenRequest.setEnabled(false);
                            }else{
                                String error = databaseError.getMessage();

                                Toast.makeText(AuftragBewerbenActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                            mBewerbung.setEnabled(true);
                        }
                    });

                }

                // ------------- Auftrag edit
                if(mBewerben_stats.equals("edit_auftrag")){
                    String titel = mTitel.getText().toString();
                    String arbeit_zeit = mArbeitZeit.getText().toString();
                    String arbeit_ort = mArbeitOrt.getText().toString();
                    String stellen_beschreibung = mStellenBeschreibung.getText().toString();
                    String vergutung = mVergutung.getText().toString();

                    Intent auftragEditIntent = new Intent(AuftragBewerbenActivity.this,EditAuftragActivity.class);
                    auftragEditIntent.putExtra("auftrag_id",auftrag_id);
                    auftragEditIntent.putExtra("titel",titel);
                    auftragEditIntent.putExtra("arbeit_zeit",arbeit_zeit);
                    auftragEditIntent.putExtra("arbeit_ort",arbeit_ort);
                    auftragEditIntent.putExtra("stellen_beschreibung",stellen_beschreibung);
                    auftragEditIntent.putExtra("vergutung",vergutung);
                    startActivity(auftragEditIntent);

                    mBewerbung.setEnabled(true);

                }

            }
        });


    }

    public static class UserViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public UserViewHolder(View itemView) {
            super(itemView);

            mView =itemView;
        }

        public  void setName (String name){
            TextView user_offer_request_name = mView.findViewById(R.id.user_offer_name);
            user_offer_request_name.setText(name);
        }
        public  void setUserImage (String image, Context ctx){
            CircleImageView user_offer_request_image = (CircleImageView)mView.findViewById(R.id.user_offer_image);
            Picasso.with(ctx).load(image).placeholder(R.drawable.default_avatar).into(user_offer_request_image);

        }
    }

    // random String =)
    public static String rendom() {
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
