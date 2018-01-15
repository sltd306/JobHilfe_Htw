package com.example.luongtiendat.jobhilfe;

import android.icu.text.DateFormat;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.HashMap;
import java.util.Random;

public class AuftragErstellenActivity extends AppCompatActivity {

    private static final int MAX_LENGTH = 10;
    public TextInputLayout mTitel,mStellenBeschreibung,mArbeitZeit,mArbeitOrt,mVergutung,mBeginn;
    private Button mAuftragBtn;

    private Toolbar mToolbar;

    private FirebaseAuth mAuth;

    private DatabaseReference mAuftragDatabase;
    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auftrag_erstellen);

    mTitel = (TextInputLayout)findViewById(R.id.auftrag_titel);
    mStellenBeschreibung = (TextInputLayout)findViewById(R.id.auftrag_stellen_beschreibung);
    mArbeitZeit = (TextInputLayout)findViewById(R.id.auftrag_arbeit_zeit);
    mArbeitOrt = (TextInputLayout)findViewById(R.id.auftrag_arbeit_ort);
    mVergutung = (TextInputLayout)findViewById(R.id.auftrag_vergutung);
    mBeginn = (TextInputLayout)findViewById(R.id.auftrag_begin_arbeit);
    mAuftragBtn = (Button)findViewById(R.id.auftrag_creat_btn);

    mToolbar = (Toolbar) findViewById(R.id.auftrag_erstellen_appbar);
    setSupportActionBar(mToolbar);
    getSupportActionBar().setTitle("Auftrag Erstellen");
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuftragBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            // Hier kann Fehler auftretten, weil FireBase path darf nich , .  usw
            // Lösung bis jetzt final @SuppressWarnings("VisibleForTests") =)
            final @SuppressWarnings("VisibleForTests")String titel = mTitel.getEditText().getText().toString();
            final @SuppressWarnings("VisibleForTests")String stellenBeschreibung = mStellenBeschreibung.getEditText().getText().toString();
            final @SuppressWarnings("VisibleForTests")String arbeitZeit = mArbeitZeit.getEditText().getText().toString();
            final @SuppressWarnings("VisibleForTests")String arbeitOrt = mArbeitOrt.getEditText().getText().toString();
            final @SuppressWarnings("VisibleForTests")String vergutung = mVergutung.getEditText().getText().toString();
            final @SuppressWarnings("VisibleForTests")String beginn = mBeginn.getEditText().getText().toString();

            if(!TextUtils.isEmpty(titel) || !TextUtils.isEmpty(stellenBeschreibung) ||
                    !TextUtils.isEmpty(arbeitZeit) || !TextUtils.isEmpty(arbeitOrt)
                    ||  !TextUtils.isEmpty(vergutung) || !TextUtils.isEmpty(beginn)){
                auftragErstellen(titel,stellenBeschreibung,arbeitZeit,arbeitOrt,vergutung,beginn);
                finish();

            }
        }
    });



}

    private void auftragErstellen(String titel, String stellenBeschreibung, String arbeitZeit, String arbeitOrt, String vergutung, String beginn) {

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid= mCurrentUser.getUid();
        //final @SuppressWarnings("VisibleForTests")String status = "0";

        mAuftragDatabase = FirebaseDatabase.getInstance().getReference().child("Auftrags");

        final String currentDate = DateFormat.getDateTimeInstance().format(new Date());

        HashMap<String,String> auftragMap = new HashMap<>();
        auftragMap.put("titel",titel);
        auftragMap.put("stellen_beschreibung",stellenBeschreibung);
        auftragMap.put("arbeit_zeit",arbeitZeit);
        auftragMap.put("arbeit_ort",arbeitOrt);
        auftragMap.put("vergutung",vergutung);
        auftragMap.put("beginn",beginn);
        auftragMap.put("userId",current_uid);
        auftragMap.put("mUser","default");
        auftragMap.put("status","0");
        auftragMap.put("datum",currentDate);

        String auftrag_id = random();



        mAuftragDatabase.child("auftrag-"+auftrag_id).setValue(auftragMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(AuftragErstellenActivity.this,"Auftrag wird erstellt",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(AuftragErstellenActivity.this,"Auftrag fehler",Toast.LENGTH_LONG).show();
                }

            }
        });


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
