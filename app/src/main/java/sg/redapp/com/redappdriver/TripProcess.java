package sg.redapp.com.redappdriver;

import android.support.v4.app.FragmentActivity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import de.hdodenhof.circleimageview.CircleImageView;

public class TripProcess extends FragmentActivity implements OnMapReadyCallback{
    CircleImageView profile;
    TextView name;
    ImageButton message, phone;
    Toolbar toolbar;
    Button cancel, confirmpickup;

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

        name = findViewById(R.id.name);
        name.setText("Bryan Low");
        message = findViewById(R.id.messageUser);
        phone = findViewById(R.id.userPhone);
        profile = findViewById(R.id.userimage);
        cancel = findViewById(R.id.cancel);
        confirmpickup = findViewById(R.id.cfmpickup);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
