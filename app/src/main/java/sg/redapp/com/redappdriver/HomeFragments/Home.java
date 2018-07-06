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
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import sg.redapp.com.redappdriver.Classes.PassengerRequest;
import sg.redapp.com.redappdriver.Classes.User;
import sg.redapp.com.redappdriver.MainActivity;
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

        DatabaseReference customerRequestRef = firebaseDatabase.getReference().child("passengerRequest");
        customerRequestRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String uid = user.getUid();
                String userKey = dataSnapshot.getKey();

                if(userKey.equals(uid)){
//                    showDialog();
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

        backtomap.setVisibility(View.INVISIBLE);

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
        DatabaseReference amount = FirebaseDatabase.getInstance().getReference("wallet").child(user.getUid()).child("value");
        amount.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Double value = dataSnapshot.getValue(Double.class);
                credit.setText(String.format("$%s", String.valueOf(value)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //rating.setText(String.valueOf(4.2));
        user = firebaseAuth.getCurrentUser();
        DatabaseReference userName = firebaseDatabase.getReference().child("user").child("driver");
//        name.setText(""+ userName);
        userName.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User userList = dataSnapshot.getValue(User.class);
                String userKey = dataSnapshot.getKey();
                assert userKey != null;
                if(userKey.equals(user.getUid())){
                    assert userList != null;
                    Log.d("user", "onChildAdded: User " + userList.getName());
                    name.setText(userList.getName());
                    Log.d("Gunjan","CAR PLATE VALUE "+ userList.getUserCarPlate()+"");
                    rating.setText(userList.getUserCarPlate());
                    Glide.with(getActivity()).load(userList.getProfileImageUrl()).into(profile);

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
                    Glide.with(getActivity()).load(userList.getProfileImageUrl()).into(profile);
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
