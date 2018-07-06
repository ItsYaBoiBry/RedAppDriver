package sg.redapp.com.redappdriver;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;
import sg.redapp.com.redappdriver.Classes.PassengerRequest;
import sg.redapp.com.redappdriver.Classes.User;

public class TripDetail extends AppCompatActivity {
    Toolbar toolbar;
    ImageButton messageUser, userPhone;
    CircleImageView profile_image;
    TextView userName, userCarPlate, typeOfService, pickupLocation, destinationLocation, tripAmount;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);
        profile_image = findViewById(R.id.profile_image);
        messageUser = findViewById(R.id.messageUser);
        userPhone = findViewById(R.id.userPhone);
        toolbar = findViewById(R.id.toolbar);
        userName = findViewById(R.id.userName);
        userCarPlate = findViewById(R.id.userCarPlate);
        typeOfService = findViewById(R.id.typeOfService);
        pickupLocation = findViewById(R.id.pickupLocation);
        destinationLocation = findViewById(R.id.destinationLocation);
        tripAmount = findViewById(R.id.tripAmount);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        userPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference trip = firebaseDatabase.getReference().child("trip").child(firebaseUser.getUid());
                trip.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        DatabaseReference customerPhone = firebaseDatabase.getReference().child("user").child("passenger").child(String.valueOf(dataSnapshot.child("passenger_uid").getValue())).child("phone");
                        customerPhone.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + String.valueOf(dataSnapshot.getValue())));
                                if (ActivityCompat.checkSelfPermission(TripDetail.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return;
                                }
                                startActivity(intent);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });


        DatabaseReference customerRequestRef = firebaseDatabase.getReference().child("passengerRequest");
        customerRequestRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String uid = firebaseUser.getUid();
                String userKey = dataSnapshot.getKey();
//
                if(userKey.equals(uid)){
                    PassengerRequest passengerRequest = dataSnapshot.getValue(PassengerRequest.class);
                    String destinationName  = passengerRequest.getDestinationName();
                    String name  = passengerRequest.getName();
                    double pickupLatitude  = passengerRequest.getPickupLatitude();
                    double pickupLongitude  = passengerRequest.getPickupLongitude();
                    String  pickupName  = passengerRequest.getPickupName();
                    double price  = passengerRequest.getPrice();
                    String  serviceType  = passengerRequest.getServiceType();
                    String vehicleNumber  = passengerRequest.getVehicleNumber();
                    Log.d("passenger request", "" + serviceType);

                    userName.setText(name);
                    userCarPlate.setText(vehicleNumber);
                    typeOfService.setText(serviceType);
                    pickupLocation.setText(pickupName);
                    destinationLocation.setText(destinationName);
                    tripAmount.setText(price+"");


                    final DatabaseReference passengerRef = FirebaseDatabase.getInstance().getReference("/user").child("passenger");
                    passengerRef.child(passengerRequest.getPassengeruid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User currUserProfile = dataSnapshot.getValue(User.class);
                            if(dataSnapshot.hasChild("profileImageUrl")){
                                if(!currUserProfile.getProfileImageUrl().equalsIgnoreCase("No Image")){
                                    Log.i("status called","set image");
                                    Glide.with(getApplication()).load(currUserProfile.getProfileImageUrl()).into(profile_image);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

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


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        messageUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TripDetail.this,Messages.class));
            }
        });

    }
}
