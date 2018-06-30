package sg.redapp.com.redappdriver;

import android.*;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.util.Log;
import android.view.View;

import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;
import sg.redapp.com.redappdriver.Classes.PassengerRequest;
import sg.redapp.com.redappdriver.functions.SharedPreferenceStorage;

public class TripProcess extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    CircleImageView profile;
    TextView name, destination;
    ImageButton message, phone;
    Toolbar toolbar;
    Button cancel, confirmpickup, completeTrip;
    ImageButton messageUser, userPhone;
    LinearLayout user;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private GoogleApiClient mGoogleApiClient;
    public Location mLocation;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_process);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        user = findViewById(R.id.user);
        name = findViewById(R.id.name);
        name.setText("Bryan Low");
        message = findViewById(R.id.messageUser);
        phone = findViewById(R.id.userPhone);
        profile = findViewById(R.id.userimage);
        cancel = findViewById(R.id.cancel);
        confirmpickup = findViewById(R.id.cfmpickup);
        completeTrip = findViewById(R.id.completetrip);
        completeTrip.setVisibility(View.GONE);
        messageUser = findViewById(R.id.messageUser);
        userPhone = findViewById(R.id.userPhone);
        destination = findViewById(R.id.textViewDestination);

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
                    String vehicleModel  = passengerRequest.getVehicleModel();
                    String vehicleNumber  = passengerRequest.getVehicleNumber();
                    Log.d("pasenger request", "" + serviceType);
                    destination.setText(destinationName);
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

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TripProcess.this, TripDetail.class));
            }
        });


        Glide.with(this)
                .load("https://images.pexels.com/photos/91227/pexels-photo-91227.jpeg?auto=compress&cs=tinysrgb&h=350")
                .into(profile);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference(12);
        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
    public void showCancelDialog(){
        final Dialog dialog = new Dialog(TripProcess.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.trip_cancel_dialog);
        ImageButton reject = dialog.findViewById(R.id.reject);
        Button accept = dialog.findViewById(R.id.accept);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference tripRef = firebaseDatabase.getReference().child("trip").child(firebaseUser.getUid());
                tripRef.removeValue();
                startLocationService();
                DatabaseReference availableDriverRef = firebaseDatabase.getReference().child("availableDriver");
                GeoFire geofire = new GeoFire(availableDriverRef);
                geofire.setLocation(firebaseUser.getUid(), new GeoLocation(mLocation.getLatitude(), mLocation.getLongitude()), new GeoFire.CompletionListener() {
                    @Override
                    public void onComplete(String key, DatabaseError error) {
                        Log.d("location", "changed: "+ mLocation.getLatitude());
                    }
                });
                dialog.dismiss();
            }
        });
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public void confirmPickup(){
        DatabaseReference trip = firebaseDatabase.getReference().child("trip").child(firebaseUser.getUid());
        trip.child("status").setValue("confirm pickup");
    }
    public void completedTrip(){
        DatabaseReference trip = firebaseDatabase.getReference().child("trip").child(firebaseUser.getUid());
        trip.child("status").setValue("complete pickup");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel:
                showCancelDialog();
                break;
            case R.id.cfmpickup:
                confirmPickup();
                confirmpickup.setVisibility(View.GONE);
                cancel.setVisibility(View.GONE);
                completeTrip.setVisibility(View.VISIBLE);
                confirmPickup();
                break;
            case R.id.completetrip:
                completedTrip();
                SharedPreferenceStorage storage = new SharedPreferenceStorage(TripProcess.this);
                storage.StoreString("trip_status","1");
                startActivity(new Intent(TripProcess.this,DropOff.class));
                finish();
                break;
            case R.id.messageUser:
                startActivity(new Intent(TripProcess.this,Messages.class));
                break;

        }
    }

    @Override
    public void onLocationChanged(Location location) {
        //the detected location is given by the variable location in the signature

        Toast.makeText(this, "Lat : " + location.getLatitude() + " Lng : " +
                location.getLongitude(), Toast.LENGTH_SHORT).show();
        Log.d("tag", "onConnected lan long: " + location.getLatitude());

        DatabaseReference availableDriverRef = FirebaseDatabase.getInstance().getReference("availableDriver");
        GeoFire geofire = new GeoFire(availableDriverRef);
        geofire.setLocation(firebaseUser.getUid(), new GeoLocation(location.getLatitude(), location.getLongitude()), new GeoFire.CompletionListener() {
            @Override
            public void onComplete(String key, DatabaseError error) {
                Log.d("location", "changed: "+ location.getLatitude());
            }
        });
        Log.d("", "onLocationChanged: latitude" +  location.getLatitude() + "longitude:" + location.getLongitude());
        Toast.makeText(TripProcess.this,"latitude " +  location.getLatitude() + "longitude:" + location.getLongitude() ,Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {


    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }

    }

    public void startLocationService() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest
                .PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setSmallestDisplacement(100);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);


        if (mLocation != null) {
            Toast.makeText(this, "Lat : " + mLocation.getLatitude() +
                            " Lng : " + mLocation.getLongitude(),
                    Toast.LENGTH_SHORT).show();
            Log.d("tag", "onConnected lan long: " + mLocation.getLatitude());
        } else {
            Toast.makeText(this, "Location not Detected",
                    Toast.LENGTH_SHORT).show();
            Log.d("tag", "Location not Detected ");
        }
    }

}
