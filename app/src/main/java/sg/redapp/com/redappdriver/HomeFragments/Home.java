package sg.redapp.com.redappdriver.HomeFragments;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import sg.redapp.com.redappdriver.Classes.User;
import sg.redapp.com.redappdriver.R;
import sg.redapp.com.redappdriver.TripProcess;
import sg.redapp.com.redappdriver.functions.SharedPreferenceStorage;
import sg.redapp.com.redappdriver.profile.ViewProfile;

import static android.media.MediaExtractor.MetricsConstants.FORMAT;

/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment {
    CircleImageView profile;
    TextView name, rating, credit, referral, backtomap;
    Button viewProfile;
    SharedPreferenceStorage storage;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    public Home() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storage = new SharedPreferenceStorage(getContext());
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        DatabaseReference customerRequestRef = firebaseDatabase.getReference().child("passengerRequest").child("3bmvIyIkzaRxwI7Xtpl4eadGHlf1");
        customerRequestRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String passengerKey = dataSnapshot.getKey();
                DatabaseReference location  = customerRequestRef.child(passengerKey).child("l").child("0");
                Log.d("pasenger request", "" + dataSnapshot.getValue() );
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        name = view.findViewById(R.id.name);
        credit = view.findViewById(R.id.credit);
        rating = view.findViewById(R.id.rating);
        profile = view.findViewById(R.id.userimage);
        referral = view.findViewById(R.id.referral);
        backtomap = view.findViewById(R.id.backtomap);
        backtomap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), TripProcess.class));
            }
        });
        checkTrip();

        viewProfile = view.findViewById(R.id.viewProfile);
        viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ViewProfile.class));
            }
        });

        tempsetup();

        return view;
    }

    public void tempsetup() {
        referral.setText("AdSIF127");
        credit.setText("$" + String.valueOf(2000.00));
        rating.setText(String.valueOf(4.2));
        user = firebaseAuth.getCurrentUser();
        DatabaseReference userName = firebaseDatabase.getReference().child("user").child("driver");
//        name.setText(""+ userName);
        userName.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User userList = dataSnapshot.getValue(User.class);
                String userKey = dataSnapshot.getKey();
                if(userKey == user.getUid()){
                    Log.d("user", "onChildAdded: User " + userList.getName());
                    name.setText(userList.getName());
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Glide.with(getContext())
                .load("https://images.pexels.com/photos/91227/pexels-photo-91227.jpeg?auto=compress&cs=tinysrgb&h=350")
                .into(profile);
//        showDialog();
    }

    public void showDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.new_request_dialog_layout);
        TextView timer = dialog.findViewById(R.id.timer);
        Button reject = dialog.findViewById(R.id.reject);
        Button accept = dialog.findViewById(R.id.accept);
        new CountDownTimer(5000, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {

                timer.setText(String.valueOf(millisUntilFinished / 1000));
            }

            public void onFinish() {
                dialog.dismiss();
            }
        }.start();

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storage.StoreString("trip_status", "2");
                startActivity(new Intent(getContext(), TripProcess.class));
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    @Override
    public void onResume() {
        super.onResume();
        checkTrip();
        user = firebaseAuth.getCurrentUser();
        DatabaseReference userName = firebaseDatabase.getReference().child("user").child("driver");
//        name.setText(""+ userName);
        userName.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User userList = dataSnapshot.getValue(User.class);
                String userKey = dataSnapshot.getKey();
                if(userKey == user.getUid()){
                    Log.d("user", "onChildAdded: User " + userList.getName());
                    name.setText(userList.getName());
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Glide.with(getContext())
                .load("https://images.pexels.com/photos/91227/pexels-photo-91227.jpeg?auto=compress&cs=tinysrgb&h=350")
                .into(profile);
//        showDialog();
    }

    @Override
    public void onStart() {
        super.onStart();
        checkTrip();
    }

    public void checkTrip() {
        Log.e("STATUS ID:",storage.getString("trip_status"));
        if (storage.getString("trip_status").equals("1")) {
            backtomap.setVisibility(View.GONE);
        } else if (storage.getString("trip_status").equals("2")) {
            backtomap.setVisibility(View.VISIBLE);
        } else if (storage.getString("trip_status").equals("")) {
            backtomap.setVisibility(View.GONE);
            Log.e("STATUS", "Unable to get status");
        } else {
            backtomap.setVisibility(View.GONE);
            Log.e("STATUS", "Invalid status");
        }
    }
}
