package com.example.luongtiendat.jobhilfe;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class EditAuftragActivity extends AppCompatActivity {

    TextInputLayout mTitel,mStellenbeschreibung,mArbeitZeit,mArbeitOrt,mVergutung;
    Button mAuftragEdit_btn;
    Toolbar mToolBar;

    private DatabaseReference mAuftragDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_auftrag);
        mTitel = (TextInputLayout)findViewById(R.id.auftrag_titel_edit);
        mStellenbeschreibung = (TextInputLayout) findViewById(R.id.auftrag_stellen_beschreibung_edit);
        mArbeitZeit = (TextInputLayout)findViewById(R.id.auftrag_arbeit_zeit_edit);
        mArbeitOrt = (TextInputLayout)findViewById(R.id.auftrag_arbeit_ort_edit);
        mVergutung = (TextInputLayout)findViewById(R.id.auftrag_vergutung_edit);

        mAuftragEdit_btn=(Button)findViewById(R.id.auftrag_edit_btn);

        mToolBar = (Toolbar)findViewById(R.id.auftrag_bewerben_edit_appbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Auftrag bearbeiten");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final String auftrag_id = getIntent().getStringExtra("auftrag_id");
        final String titel = getIntent().getStringExtra("titel");
        final String arbeit_zeit = getIntent().getStringExtra("arbeit_zeit");
        final String arbeit_ort = getIntent().getStringExtra("arbeit_ort");
        final String stellen_beschreibung = getIntent().getStringExtra("stellen_beschreibung");
        final String vergutung = getIntent().getStringExtra("vergutung");

        mTitel.getEditText().setText(titel);
        mStellenbeschreibung.getEditText().setText(stellen_beschreibung);
        mArbeitZeit.getEditText().setText(arbeit_zeit);
        mArbeitOrt.getEditText().setText(arbeit_ort);
        mVergutung.getEditText().setText(vergutung);



        mAuftragDatabase = FirebaseDatabase.getInstance().getReference().child("Auftrags").child(auftrag_id);


        mAuftragEdit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final @SuppressWarnings("VisibleForTests")String titel_value = mTitel.getEditText().getText().toString();
                final @SuppressWarnings("VisibleForTests")String arbeit_ort_value = mArbeitOrt.getEditText().getText().toString();
                final @SuppressWarnings("VisibleForTests")String arbeit_zeit_value = mArbeitZeit.getEditText().getText().toString();
                final @SuppressWarnings("VisibleForTests")String stellen_beschreibung_value = mStellenbeschreibung.getEditText().getText().toString();
                final @SuppressWarnings("VisibleForTests")String vergutung_value = mVergutung.getEditText().getText().toString();

                /*
                HashMap<String,String> editAuftrag = new HashMap<>();
                editAuftrag.put("titel",titel_value);
                editAuftrag.put("arbeit_ort",arbeit_ort_value);
                editAuftrag.put("arbeit_zeit",arbeit_zeit_value);
                editAuftrag.put("stellen_beschreibung",stellen_beschreibung_value);
                editAuftrag.put("vergutung",vergutung_value);
                */

                final ProgressDialog mDialog = new ProgressDialog(EditAuftragActivity.this);
                mDialog.setMessage("Pleas wait while we save the changes ...");
                mDialog.show();

                mAuftragDatabase.child("titel").setValue(titel_value).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
                mAuftragDatabase.child("arbeit_ort").setValue(arbeit_ort_value).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
                mAuftragDatabase.child("arbeit_zeit").setValue(arbeit_zeit_value).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
                mAuftragDatabase.child("stellen_beschreibung").setValue(stellen_beschreibung_value).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
                mAuftragDatabase.child("vergutung").setValue(vergutung_value).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            mDialog.dismiss();
                            Toast.makeText(EditAuftragActivity.this, "Update Successfully! ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });

    }
}
