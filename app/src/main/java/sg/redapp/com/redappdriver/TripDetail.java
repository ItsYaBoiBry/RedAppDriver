package sg.redapp.com.redappdriver;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import sg.redapp.com.redappdriver.Classes.PassengerRequest;

public class TripDetail extends AppCompatActivity {
    Toolbar toolbar;
    ImageButton messageUser, userPhone;
    TextView userName, userCarPlate,typeOfService, pickupLocation, destinationLocation, tripAmount;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);
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
