package com.example.luongtiendat.jobhilfe;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luongtiendat.jobhilfe.Model.Auftrag;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AuftragListeActivity extends AppCompatActivity {

    private RecyclerView mMeinAuftragList;

    private DatabaseReference mMeinAuftragDatabase;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auftrag_liste);

        Toolbar toolbar = (Toolbar) findViewById(R.id.all_auftrag_liste_appbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Alle Auftrag Liste");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mMeinAuftragList = (RecyclerView)findViewById(R.id.auftrag_list);
        mMeinAuftragList.setHasFixedSize(true);
        mMeinAuftragList.setLayoutManager(new LinearLayoutManager(this));

        mMeinAuftragDatabase = FirebaseDatabase.getInstance().getReference().child("Auftrags");
        mAuth = FirebaseAuth.getInstance();


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Auftrag,AuftragViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Auftrag, AuftragViewHolder>(
                Auftrag.class,
                R.layout.auftrag_singer_layout,
                AuftragViewHolder.class,
                mMeinAuftragDatabase
        ) {
            @Override
            protected void populateViewHolder(final AuftragViewHolder viewHolder, final Auftrag model, int position) {

                viewHolder.setTitel(model.getTitel());
                viewHolder.setArbeitOrt(model.getArbeit_ort());

                final String auftrag_id = getRef(position).getKey();

                mMeinAuftragDatabase.child(auftrag_id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String userId = dataSnapshot.child("userId").getValue().toString();

                        if (mAuth.getCurrentUser().getUid().equals(userId)){
                            viewHolder.setBackGroundColor(255); // tim them mau
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Toast.makeText(AuftragListeActivity.this,auftrag_id,Toast.LENGTH_LONG).show();
                        Intent auftragBewerbenIntent = new Intent(AuftragListeActivity.this,AuftragBewerbenActivity.class);
                        auftragBewerbenIntent.putExtra("auftrag_id",auftrag_id);
                        startActivity(auftragBewerbenIntent);
                    }
                });


            }
        };
        mMeinAuftragList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class AuftragViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public AuftragViewHolder(View itemView) {
            super(itemView);

            mView =itemView;
        }

        public  void setTitel (String titel){
            TextView auftragTitelView = mView.findViewById(R.id.auftrag_view_id);
            auftragTitelView.setText(titel);
        }

        public  void setDatum (String datum){
            TextView auftragDatumView = mView.findViewById(R.id.auftrag_view_datum);
            auftragDatumView.setText(datum);
        }
        public  void setArbeitOrt (String arbeit_ort){
            TextView auftragArbeitOrtView = mView.findViewById(R.id.auftrag_view_arbeit_ort);
            auftragArbeitOrtView.setText(arbeit_ort);
        }
        public  void setBackGroundColor (int color){
            RelativeLayout relativeLayout = mView.findViewById(R.id.auftrag_view_layout);
            relativeLayout.setBackgroundColor(color);
        }
    }
}
