package sg.redapp.com.redappdriver;

import android.*;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import sg.redapp.com.redappdriver.Classes.PassengerRequest;
import sg.redapp.com.redappdriver.Classes.User;
import sg.redapp.com.redappdriver.functions.HttpRequest;
import sg.redapp.com.redappdriver.functions.SharedPreferenceStorage;

public class TripProcess extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    CircleImageView profile;
    TextView name, destination;
    ImageButton message, phone;
    Toolbar toolbar;
    ImageButton navigate;
    Button cancel, confirmpickup, completeTrip;
    ImageButton messageUser, userPhone;
    LinearLayout user;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private GoogleApiClient mGoogleApiClient;
    public Location mLocation;

    private GoogleMap mMap;
    String tripid;

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


        navigate = findViewById(R.id.navigate);
        user = findViewById(R.id.user);
        name = findViewById(R.id.name);
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

        DatabaseReference customerRequestRef = firebaseDatabase.getReference().child("trip");
        customerRequestRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String uid = firebaseUser.getUid();
                Log.e("FIREBASE USER UID", uid);
                String userKey = dataSnapshot.getKey();

                if (userKey.equals(uid)) {
                    String destinationName = String.valueOf(dataSnapshot.child("destinationName").getValue());
                    String names = String.valueOf(dataSnapshot.child("name").getValue());



//                    double pickupLatitude = Double.parseDouble(dataSnapshot.child("pickupLatitude").getValue().toString());
//                    double pickupLongitude = Double.parseDouble(dataSnapshot.child("pickupLongitude").getValue().toString());;
//                    String pickupName = String.valueOf(dataSnapshot.child("pickupName").getValue());
//
//                    double price = Double.parseDouble(dataSnapshot.child("price").getValue().toString());
//                    String vehicleModel = String.valueOf(dataSnapshot.child("vehicleModel").getValue());
//                    String vehicleNumber = String.valueOf(dataSnapshot.child("vehicleNumber").getValue());

                    final DatabaseReference passengerRef = FirebaseDatabase.getInstance().getReference("/user").child("passenger");
                    passengerRef.child(dataSnapshot.child("passenger_uid").getValue().toString());
                    if(String.valueOf(passengerRef.getKey()).equals("null")){
                        Log.e("TRIP PROCESS", dataSnapshot.child("passenger_uid").getValue().toString());
                    }else{
                        passengerRef.addListenerForSingleValueEvent(new ValueEventListener(){
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                User currUserProfile = dataSnapshot.getValue(User.class);
                                assert currUserProfile != null;
                                name.setText(String.valueOf(currUserProfile.getName()));
                                if (dataSnapshot.hasChild("profileImageUrl")) {
                                    if (!currUserProfile.getProfileImageUrl().equalsIgnoreCase("No Image")) {
                                        Log.i("status called", "set image");
                                        Glide.with(getApplication()).load(currUserProfile.getProfileImageUrl()).into(profile);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        String serviceType = String.valueOf(dataSnapshot.child("serviceType").getValue());
                        Log.d("pasenger request", "" + serviceType);
                        destination.setText(destinationName);
                    }

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
                                if (ActivityCompat.checkSelfPermission(TripProcess.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TripProcess.this, TripDetail.class));
            }
        });


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

        DatabaseReference trip = firebaseDatabase.getReference().child("trip").child(firebaseUser.getUid());


        trip.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("LATITUDE:", dataSnapshot.child("pickupLatitude").getValue() + "");
                Log.e("LONGITUDE:", dataSnapshot.child("pickupLongtitude").getValue() + "");
                Log.e("DESTINATION:", dataSnapshot.child("destinationName").getValue() + "");
                Log.e("ORIGIN:", dataSnapshot.child("pickupName").getValue() + "");
//                String pickupLatitude = (String)dataSnapshot.child("pickupLatitude").getValue();
//                String pickupLongitude =(String)dataSnapshot.child("pickupLongitude").getValue();
//                Log.e("latitude", pickupLatitude + "");
//                Log.e("longitude", pickupLongitude + "");
                if(String.valueOf(dataSnapshot.child("destinationName").getValue() + "").equals("null")){
                   Log.e("TRIP PROCESS", "DESTINATION NULL");
                }else{
                    LatLng location = GetLocationFromAddress(TripProcess.this, String.valueOf(dataSnapshot.child("destinationName").getValue() + ""));
                    if(location!=null){
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 20.0f));
                        mMap.addMarker(new MarkerOptions().position(location).title(String.valueOf(dataSnapshot.child("destinationName").getValue())));
                        mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(String.valueOf(dataSnapshot.child("pickupLatitude").getValue())), Double.parseDouble(String.valueOf(dataSnapshot.child("pickupLongtitude").getValue())))).title(String.valueOf(dataSnapshot.child("pickupName").getValue())));
                    }else{
                        Log.e("TRIP PROCESS", "UNABLE TO FIND LOCATION");
                    }

                }


//                mMap.addPolyline(new PolylineOptions()
//                        .add(location, new LatLng(Double.parseDouble(String.valueOf(dataSnapshot.child("pickupLatitude").getValue())), Double.parseDouble(String.valueOf(dataSnapshot.child("pickupLongtitude").getValue()))))
//                        .width(5)
//                        .color(Color.RED));
//                getWaypoints getWaypoints = new getWaypoints();
//                getWaypoints.execute(String.valueOf(dataSnapshot.child("pickupName").getValue()).replace(" ", "+"), String.valueOf(dataSnapshot.child("destinationName").getValue()).replace(" ", "+"));


                navigate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final CharSequence[] items = {"Pickup\n" + String.valueOf(dataSnapshot.child("pickupName").getValue()) + "\n", "DropOff\n" + String.valueOf(dataSnapshot.child("destinationName").getValue()) + "\n"};
                        AlertDialog.Builder builder = new AlertDialog.Builder(TripProcess.this);
//                builder.setView(R.id.type_of_service);
                        builder.setItems(items, (dialog, item) -> {
                            if (items[item].toString().equals("Pickup\n" + String.valueOf(dataSnapshot.child("pickupName").getValue()) + "\n")) {
                                // Create a Uri from an intent string. Use the result to create an Intent.
                                String url = "https://www.google.com/maps/dir/?api=1&destination=" + String.valueOf(dataSnapshot.child("pickupName").getValue()) + "&travelmode=driving";
                                // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
                                Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                // Make the Intent explicit by setting the Google Maps package
                                mapIntent.setPackage("com.google.android.apps.maps");
                                // Attempt to start an activity that can handle the Intent
                                startActivity(mapIntent);
                            } else if (items[item].toString().equals("DropOff\n" + String.valueOf(dataSnapshot.child("destinationName").getValue()) + "\n")) {
                                // Create a Uri from an intent string. Use the result to create an Intent.
                                String url = "https://www.google.com/maps/dir/?api=1&destination=" + String.valueOf(dataSnapshot.child("destinationName").getValue()) + "&travelmode=driving";
                                // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
                                Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                // Make the Intent explicit by setting the Google Maps package
                                mapIntent.setPackage("com.google.android.apps.maps");
                                // Attempt to start an activity that can handle the Intent
                                startActivity(mapIntent);
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        // Add a marker in Sydney, Australia, and move the camera.
    }

    public void showCancelDialog() {
        final Dialog dialog = new Dialog(TripProcess.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.trip_cancel_dialog);
        ImageButton reject = dialog.findViewById(R.id.reject);
        Button accept = dialog.findViewById(R.id.accept);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startLocationService();
                DatabaseReference availableDriverRef = firebaseDatabase.getReference().child("availableDriver");
                GeoFire geofire = new GeoFire(availableDriverRef);
                geofire.setLocation(firebaseUser.getUid(), new GeoLocation(mLocation.getLatitude(), mLocation.getLongitude()), new GeoFire.CompletionListener() {
                    @Override
                    public void onComplete(String key, DatabaseError error) {
                        Log.d("location", "changed: " + mLocation.getLatitude());
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


    public void confirmPickup() {
        DatabaseReference trip = firebaseDatabase.getReference().child("trip").child(firebaseUser.getUid());
        trip.child("status").setValue("confirm");
    }

    public void completedTrip() {
        DatabaseReference trip = firebaseDatabase.getReference().child("trip").child(firebaseUser.getUid());
        DatabaseReference tripId = firebaseDatabase.getReference().child("trip").child(firebaseUser.getUid()).child("trip_id");
        trip.child("status").setValue("completed");

        tripId.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DatabaseReference history = firebaseDatabase.getReference().child("history").child(String.valueOf(snapshot.getValue()));
                DatabaseReference passengerRequest = firebaseDatabase.getReference().child("passengerRequest").child(firebaseUser.getUid());

                trip.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.e("DESTINATION PROCESS",String.valueOf(dataSnapshot.child("destinationName").getValue()));
                        history.child("destination_name").setValue(String.valueOf(dataSnapshot.child("destinationName").getValue()));
                        history.child("pickup_name").setValue(String.valueOf(dataSnapshot.child("pickupName").getValue()));
                        history.child("service_type").setValue(String.valueOf(dataSnapshot.child("serviceType").getValue()));
                        history.child("passenger_name").setValue(String.valueOf(dataSnapshot.child("name").getValue()));
                        history.child("passenger_uid").setValue(String.valueOf(dataSnapshot.child("passenger_uid").getValue()));

                        history.child("driver_uid").setValue(firebaseUser.getUid());
                        history.child("vehicle_model").setValue(String.valueOf(dataSnapshot.child("vehicleModel").getValue()));
                        history.child("vehicle_number").setValue(String.valueOf(dataSnapshot.child("vehicleNumber").getValue()));
                        history.child("price").setValue(String.valueOf(dataSnapshot.child("price").getValue()));

                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
                        String strDate = mdformat.format(calendar.getTime());
                        history.child("transaction_time_start").setValue(String.valueOf(dataSnapshot.child("tripStarted").getValue()));
                        history.child("transaction_time_complete").setValue(String.valueOf(strDate));
                        history.child("rating").setValue(0);


//                        completeTrip.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//
//                            }
//                        });
                        SharedPreferenceStorage storages = new SharedPreferenceStorage(TripProcess.this);
                        storages.StoreString("trip_status", "1");
                        Log.e("TRIP_ID", String.valueOf(tripid));

                        startActivity(new Intent(TripProcess.this, DropOff.class).putExtra("history_id", tripid));
                        passengerRequest.removeValue();
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                tripid = String.valueOf(snapshot.getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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


                break;
            case R.id.messageUser:
                startActivity(new Intent(TripProcess.this, Messages.class));
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
                Log.d("location", "changed: " + location.getLatitude());
            }
        });

        Log.d("", "onLocationChanged: latitude" + location.getLatitude() + "longitude:" + location.getLongitude());
        Toast.makeText(TripProcess.this, "latitude " + location.getLatitude() + "longitude:" + location.getLongitude(), Toast.LENGTH_SHORT).show();
        LatLng currentlocation = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(currentlocation).title(""));
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

//    public class getWaypoints extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected String doInBackground(String... strings) {
//            HttpRequest request = new HttpRequest();
//            return request.GetRequest("https://maps.googleapis.com/maps/api/directions/json?origin=" + strings[0] + "&destination=" + strings[1] + "&key=AIzaSyB9-Ffz2pS0ekVlFR29VJnY6DtYDYXik60");
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            try {
//                JSONObject getCoordinates = new JSONObject(s);
//                Log.e("REQUEST COORDINATES", getCoordinates + "");
//                JSONArray routes = getCoordinates.getJSONArray("routes");
//                JSONObject getLegs = routes.getJSONObject(0);
//                JSONArray legs = getLegs.getJSONArray("legs");
//                JSONObject gettingSteps = legs.getJSONObject(0);
//                JSONArray steps = gettingSteps.getJSONArray("steps");
//                Log.e("STEPS", String.valueOf(steps));
//                for (int i = 0; i < steps.length(); i++) {
//                    JSONObject step = steps.getJSONObject(i);
//                    JSONObject startLocation = step.getJSONObject("start_location");
//                    JSONObject endLocation = step.getJSONObject("end_location");
//                    LatLng startLatLng = new LatLng(Double.parseDouble(String.valueOf(startLocation.getString("lat"))), Double.parseDouble(String.valueOf(startLocation.getString("lng"))));
//                    LatLng endLatLng = new LatLng(Double.parseDouble(String.valueOf(endLocation.getString("lat"))), Double.parseDouble(String.valueOf(endLocation.getString("lng"))));
//                    mMap.addPolyline(new PolylineOptions()
//                            .add(startLatLng, endLatLng)
//                            .width(5)
//                            .color(Color.RED));
//                    Log.e("START LOCATIONS", String.valueOf(startLocation.getString("lat")) + " " + String.valueOf(startLocation.getString("lng")));
//                    Log.e("END LOCATIONS", String.valueOf(endLocation.getString("lat")) + " " + String.valueOf(endLocation.getString("lng")));
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    public LatLng GetLocationFromAddress(Context context, String strAddress) {
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng currentLocation = null;
        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            currentLocation = new LatLng((double) (location.getLatitude()),
                    (double) (location.getLongitude()));
            Log.e("current Locale", String.valueOf(currentLocation));


        } catch (IOException e) {
            e.printStackTrace();
        }
        return currentLocation;
    }

}
