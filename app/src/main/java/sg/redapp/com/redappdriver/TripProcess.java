package sg.redapp.com.redappdriver;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.view.View;

import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import de.hdodenhof.circleimageview.CircleImageView;
import sg.redapp.com.redappdriver.functions.SharedPreferenceStorage;

public class TripProcess extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener{
    CircleImageView profile;
    TextView name;
    ImageButton message, phone;
    Toolbar toolbar;
    Button cancel, confirmpickup, completeTrip;
    ImageButton messageUser, userPhone;
    LinearLayout user;

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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel:
                showCancelDialog();
                break;
            case R.id.cfmpickup:
                confirmpickup.setVisibility(View.GONE);
                cancel.setVisibility(View.GONE);
                completeTrip.setVisibility(View.VISIBLE);
                break;
            case R.id.completetrip:
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
}
