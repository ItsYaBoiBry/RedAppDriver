package sg.redapp.com.redappdriver;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import sg.redapp.com.redappdriver.Classes.AvailableDriver;
import sg.redapp.com.redappdriver.HomeFragments.FAQ;
import sg.redapp.com.redappdriver.HomeFragments.History;
import sg.redapp.com.redappdriver.HomeFragments.Home;
import sg.redapp.com.redappdriver.HomeFragments.Settings;
import sg.redapp.com.redappdriver.HomeFragments.Support;
import sg.redapp.com.redappdriver.HomeFragments.Wallet;
import sg.redapp.com.redappdriver.login.ActivityStartPage;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    DrawerLayout drawerLayout;
    FragmentTransaction ft;
    Toolbar toolbar;
    TextView toolbartitle;
    TextView onlineStatus;
    SwitchCompat setOnline;

    // TODO: Task 1 - Declare Firebase variables
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private DatabaseReference availableDriverListRef;

    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private String customerId ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // TODO: Task 2: Get Firebase database instance and reference
        checkpermission();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("user");
        DatabaseReference userData = userRef.child("driver");
        assert user != null;
        DatabaseReference userId = userData.child(user.getUid());
        DatabaseReference onlineStatus = userId.child("online_status");
//        getAssignedCustomer();

        onlineStatus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean value = dataSnapshot.getValue(Boolean.class);
                if (value) {
                    setOnline.setChecked(true);

                } else {
                    setOnline.setChecked(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        availableDriverListRef = firebaseDatabase.getReference("availableDriver");
        configureNavigationDrawer();
        configureToolbar();
        configureSwitch();
        toolbartitle = findViewById(R.id.toolbartitle);
        setTitle(getString(R.string.app_logo_name));
        Intent intent = getIntent();
        String uid = intent.getStringExtra("uid");
        Log.d("uid", "" + uid);
        if (savedInstanceState == null) {
            replacefragment(new Home());
            setTitle(getString(R.string.navbar_home));
        }
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }


    @Override
    public void onLocationChanged(Location location) {
        //the detected location is given by the variable location in the signature

        Toast.makeText(this, "Lat : " + location.getLatitude() + " Lng : " +
                location.getLongitude(), Toast.LENGTH_SHORT).show();
        Log.d("tag", "onConnected lan long: " + location.getLatitude());

        DatabaseReference availableDriverRef = FirebaseDatabase.getInstance().getReference("availableDriver");
        GeoFire geofire = new GeoFire(availableDriverListRef);
        geofire.setLocation(user.getUid(), new GeoLocation(location.getLatitude(), location.getLongitude()), new GeoFire.CompletionListener() {
            @Override
            public void onComplete(String key, DatabaseError error) {
                Log.d("location", "changed: "+ location.getLatitude());
            }
        });
        Log.d("", "onLocationChanged: latitude" +  location.getLatitude() + "longitude:" + location.getLongitude());
        Toast.makeText(MainActivity.this,"latitude " +  location.getLatitude() + "longitude:" + location.getLongitude() ,Toast.LENGTH_SHORT).show();

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
        Intent intent = getIntent();
        String uid = intent.getStringExtra("uid");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("availableDriver");
        GeoFire geofire = new GeoFire(ref);
//        geofire.removeLocation(uid);
    }

    private void configureSwitch() {
        onlineStatus = findViewById(R.id.onlineStatus);
        setOnline = findViewById(R.id.setOnline);
        setOnline.setChecked(false);
        if (setOnline.isChecked()) {
            onlineStatus.setText("online\u2022 ");
            onlineStatus.setTextColor(getResources().getColor(R.color.swampgreen));
            Intent intent = getIntent();
            String uid = intent.getStringExtra("uid");
//            availableDriverListRef.child(uid).setValue(availableDriver);
//            DatabaseReference availableDriverRef = FirebaseDatabase.getInstance().getReference("availableDriver");
//            availableDriverRef.child(uid).child("latitude").setValue(mLocation.getLatitude());
//            availableDriverRef.child(uid).child("longitude").setValue(mLocation.getLongitude());
            DatabaseReference availableDriverRef = FirebaseDatabase.getInstance().getReference("availableDriver");

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("user");
            DatabaseReference userData = myRef.child("driver");
            assert user != null;
            DatabaseReference userid = userData.child(user.getUid());
            DatabaseReference onlineStatus = userid.child("online_status");
            onlineStatus.setValue(true);

            Log.e("Online Status: ", "Online");
        } else {
            onlineStatus.setText("offline\u2022 ");
            onlineStatus.setTextColor(getResources().getColor(R.color.darkgrey));
            Log.e("Online Status: ", "Offline\u2022 ");
            user = firebaseAuth.getCurrentUser();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("user");
            DatabaseReference userData = myRef.child("driver");
            assert user != null;
            DatabaseReference userid = userData.child(user.getUid());
            DatabaseReference onlineStatus = userid.child("online_status");
            onlineStatus.setValue(false);
        }
        setOnline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (setOnline.isChecked()) {
                    onlineStatus.setText("online\u2022 ");
                    onlineStatus.setTextColor(getResources().getColor(R.color.swampgreen));
                    Log.e("Online Status: ", "Online");
                    startLocationService();
                    Intent intent = getIntent();
                    String uid = intent.getStringExtra("uid");
                    AvailableDriver availableDriver = new AvailableDriver(mLocation.getLatitude(), mLocation.getLongitude());
                    user = firebaseAuth.getCurrentUser();
                    DatabaseReference availableDriverRef = FirebaseDatabase.getInstance().getReference("availableDriver");
                    GeoFire geofire = new GeoFire(availableDriverListRef);
                    geofire.setLocation(user.getUid(), new GeoLocation(mLocation.getLatitude(), mLocation.getLongitude()), new GeoFire.CompletionListener() {
                        @Override
                        public void onComplete(String key, DatabaseError error) {

                        }
                    });
//                    availableDriverRef.child(uid).child("latitude").setValue(mLocation.getLatitude());
//                    availableDriverRef.child(uid).child("longitude").setValue(mLocation.getLongitude());

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("user");
                    DatabaseReference userData = myRef.child("driver");
                    assert user != null;
                    DatabaseReference userid = userData.child(user.getUid());
                    DatabaseReference onlineStatus = userid.child("online_status");
                    onlineStatus.setValue(true);


                } else {
                    onlineStatus.setText("offline\u2022 ");
                    onlineStatus.setTextColor(getResources().getColor(R.color.darkgrey));
                    Log.e("Online Status: ", "Offline");
                    Intent intent = getIntent();
                    String uid = intent.getStringExtra("uid");
                    availableDriverListRef.child(uid).removeValue();
                    user = firebaseAuth.getCurrentUser();
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("user");
                    DatabaseReference userData = myRef.child("driver");
                    assert user != null;
                    DatabaseReference userid = userData.child(user.getUid());
                    DatabaseReference onlineStatus = userid.child("online_status");
                    onlineStatus.setValue(false);
                }
            }
        });
    }

    private void configureNavigationDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navView = findViewById(R.id.left_drawer);

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                switch (itemId) {
                    case R.id.home:
                        replacefragment(new Home());
                        setTitle("Home");
                        CloseDrawer();
                        return true;
                    case R.id.wallet:
                        replacefragment(new Wallet());
                        setTitle("Wallet");
                        CloseDrawer();
                        return true;
                    case R.id.job_history:
                        replacefragment(new History());
                        setTitle("Job History");
                        CloseDrawer();
                        return true;
                    case R.id.settings:
                        replacefragment(new Settings());
                        setTitle("Settings");
                        CloseDrawer();
                        return true;
                    case R.id.faq:
                        replacefragment(new FAQ());
                        setTitle("FAQ");
                        CloseDrawer();
                        return true;
                    case R.id.support:
                        replacefragment(new Support());
                        setTitle("Support");
                        CloseDrawer();
                        return true;
                    case R.id.logout:

                        user = firebaseAuth.getCurrentUser();
                        assert user != null;
                        availableDriverListRef.child(user.getUid()).removeValue();
                        firebaseAuth.signOut();
                        startActivity(new Intent(MainActivity.this, ActivityStartPage.class));

                        finish();
                        return true;
                }
                return false;
            }
        });
//        navView.setNavigationItemSelectedListener(menuItem -> {
//            int itemId = menuItem.getItemId();
//            return false;
//        });

    }

    private void configureToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionbar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            // Android home
            case android.R.id.home:
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    CloseDrawer();
                    return true;
                } else {
                    OpenDrawer();
                    return true;
                }
                // manage other entries if you have it ...
        }
        return false;
    }

    public void replacefragment(Fragment fragment) {
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, fragment);
        // or ft.add(R.id.your_placeholder, new FooFragment());
        // Complete the changes added above
        ft.commit();
    }

    public void CloseDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    public void OpenDrawer() {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void setTitle(String title) {
        toolbartitle.setText(title);
    }

    public void checkpermission() {
        int permissionCheck_Coarse = ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionCheck_Fine = ContextCompat.checkSelfPermission(
                MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck_Coarse == PermissionChecker.PERMISSION_GRANTED
                || permissionCheck_Fine == PermissionChecker.PERMISSION_GRANTED) {


        } else {
            mLocation = null;
            Toast.makeText(MainActivity.this,
                    "Permission not granted to retrieve location info",
                    Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 2);
        }
    }

    public void startLocationService() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
//    public void getAssignedCustomer(){
//
//        DatabaseReference assignedCustomerRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child();
//        String driverId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//    }
}