package com.example.luongtiendat.jobhilfe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.luongtiendat.jobhilfe.Model.Bewertung;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BewertungListeActivity extends AppCompatActivity {

    private RecyclerView mBewertungList;

    private FirebaseAuth mAuth;

    private DatabaseReference mBewertungDatabase;
    private String mCurrent_User;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bewertung_liste);

        Toolbar toolbar = (Toolbar) findViewById(R.id.bewertung_list_appbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Bewertung Liste");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mBewertungList = (RecyclerView)findViewById(R.id.bewertung_list);
        mBewertungList.setHasFixedSize(true);
        mBewertungList.setLayoutManager(new LinearLayoutManager(this));

        mAuth = FirebaseAuth.getInstance();
        mCurrent_User = mAuth.getCurrentUser().getUid();

        mBewertungDatabase = FirebaseDatabase.getInstance().getReference().child("Bewertungs").child(mCurrent_User);


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Bewertung,BewertungViewHolder> viewHolderFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Bewertung, BewertungViewHolder>(
                Bewertung.class,
                R.layout.singer_bewertung_layout,
                BewertungViewHolder.class,
                mBewertungDatabase
        ) {
            @Override
            protected void populateViewHolder(BewertungViewHolder viewHolder, Bewertung model, int position) {

                viewHolder.setName(model.getFrom());
                viewHolder.setRate(model.getRate());
                viewHolder.setText(model.getText());

            }
        };
        mBewertungList.setAdapter(viewHolderFirebaseRecyclerAdapter);

    }

    public static class BewertungViewHolder extends RecyclerView.ViewHolder{
        View mView;
        RatingBar mRatingBar;

        public BewertungViewHolder(View itemView) {
            super(itemView);

            mView =itemView;
            mRatingBar = itemView.findViewById(R.id.bewertung_rate);
        }
        public  void setName (String name){
            TextView bewertungNameView = mView.findViewById(R.id.bewertung_name);
            bewertungNameView.setText(name);
        }
        public  void setRate (String rate){
            Float rate_float =  Float.parseFloat(rate);
            mRatingBar.setRating(rate_float);
        }
        public  void setText (String text){
            TextView bewertungTextView = mView.findViewById(R.id.bewertung_text);
            bewertungTextView.setText(text);
        }

    }
}
