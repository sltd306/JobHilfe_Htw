package com.example.luongtiendat.jobhilfe;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.luongtiendat.jobhilfe.Model.Notifications;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotifiticationFragment extends Fragment {

    private RecyclerView mNotificationList;

    private DatabaseReference mNotificationDatabase;
    private DatabaseReference mUsersDatabase;

    private FirebaseAuth mAuth;

    private String mCurrent_user_id;

    private View mMainView;


    public NotifiticationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainView = inflater.inflate(R.layout.fragment_notifitication, container, false);

        mNotificationList = (RecyclerView) mMainView.findViewById(R.id.notification_list);
        mNotificationList.setHasFixedSize(true);
        mNotificationList.setLayoutManager(new LinearLayoutManager(getContext()));

        mAuth = FirebaseAuth.getInstance();
        mCurrent_user_id = mAuth.getCurrentUser().getUid();

        mNotificationDatabase = FirebaseDatabase.getInstance().getReference().child("notifications").child(mCurrent_user_id);
        mNotificationDatabase.keepSynced(true);

        return mMainView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Notifications,NotificationViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Notifications, NotificationViewHolder>(
                Notifications.class,
                R.layout.notification_singer_layout,
                NotificationViewHolder.class,
                mNotificationDatabase

        ) {
            @Override
            protected void populateViewHolder(NotificationViewHolder viewHolder, Notifications model, int position) {
                viewHolder.setName(model.getFrom());
                viewHolder.setText(model.getType());
            }
        };
        mNotificationList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public NotificationViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setName(String name){

            TextView notificationNameView = (TextView) mView.findViewById(R.id.notification_name);
            notificationNameView.setText(name);
        }
        public void setText(String text){

            TextView notificationTextView = (TextView) mView.findViewById(R.id.notification_text);
            notificationTextView.setText(text);

        }
    }
}
