package sg.redapp.com.redappdriver.profile;

import android.app.AlertDialog;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;
import sg.redapp.com.redappdriver.Classes.User;
import sg.redapp.com.redappdriver.R;
import sg.redapp.com.redappdriver.login.SignUp;

public class ViewProfile extends AppCompatActivity {
    Toolbar toolbar;
    EditText firstName, lastName, email,phone;
    TextView countryCode, serviceType, name, rating, vehicleNum;
    Button save, edit;
    CircleImageView profileImage, editImage;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        SetupToolbar();

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        countryCode = findViewById(R.id.countryCode);
        serviceType = findViewById(R.id.serviceType);
        name = findViewById(R.id.name);
        rating = findViewById(R.id.rating);
        vehicleNum = findViewById(R.id.vehicleNum);
        save = findViewById(R.id.save);
        edit = findViewById(R.id.edit);
        profileImage = findViewById(R.id.userimage);
        editImage = findViewById(R.id.editImage);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
    }

    public void SetupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        Init();
        tempSetup();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Save();
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Edit();
            }
        });
        countryCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = {"+65"};
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewProfile.this);
                builder.setItems(items, (dialog, item) -> {
                    countryCode.setText(items[item]);
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        serviceType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = {"Tow (Accident)","Tow (Breakdown)","Tyre Mending","Spare Tyre Replacement","Battery Jump Start","Battery Replacement","Others (Unsure)"};
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewProfile.this);
                builder.setItems(items, (dialog, item) -> {
                    serviceType.setText(items[item]);
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    public void tempSetup(){
        DatabaseReference driverRef = firebaseDatabase.getReference().child("user").child("driver");
//        name.setText(""+ userName);
        driverRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User userList = dataSnapshot.getValue(User.class);
                String userKey = dataSnapshot.getKey();
                if(userKey == user.getUid()){
                    Log.d("user", "onChildAdded: User " + userList.getName());
                    firstName.setText(userList.getName());
                    lastName.setText("Low");
                    email.setText(userList.getEmail());
                    countryCode.setText("+65");
                    phone.setText(userList.getPhone_number());
                    serviceType.setText(userList.getType_of_service());
                    name.setText(String.format("%s %s", firstName.getText().toString(), lastName.getText().toString()));
                    rating.setText("4.2");
                    vehicleNum.setText("SFK423J");
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
        Glide.with(ViewProfile.this).load("https://images.pexels.com/photos/91227/pexels-photo-91227.jpeg?auto=compress&cs=tinysrgb&h=350").into(profileImage);
    }
    public void Edit(){
        editImage.setVisibility(View.VISIBLE);
        edit.setVisibility(View.GONE);
        save.setVisibility(View.VISIBLE);
        firstName.setEnabled(true);
        lastName.setEnabled(true);
        email.setEnabled(true);
        countryCode.setEnabled(true);
        countryCode.setClickable(true);
        phone.setEnabled(true);
        serviceType.setClickable(true);
        serviceType.setEnabled(true);
    }
    public void Save(){
        editImage.setVisibility(View.GONE);
        edit.setVisibility(View.VISIBLE);
        save.setVisibility(View.GONE);
        firstName.setEnabled(false);
        lastName.setEnabled(false);
        email.setEnabled(false);
        countryCode.setEnabled(false);
        countryCode.setClickable(false);
        phone.setEnabled(false);
        serviceType.setClickable(false);
        serviceType.setEnabled(false);
        name.setText(String.format("%s %s", firstName.getText().toString(), lastName.getText().toString()));
        String updateName = name.getText().toString();
        String updateEmail = email.getText().toString();
        String updatePhoneNumber = phone.getText().toString();
        String updateTypeOfService = serviceType.getText().toString();
        DatabaseReference driverRef = firebaseDatabase.getReference().child("user").child("driver");
        User updateUser = new User(updateEmail,updateName,updatePhoneNumber,updateTypeOfService);
        driverRef.child(user.getUid()).setValue(updateUser);
    }
    public void Init(){
        editImage.setVisibility(View.GONE);
        edit.setVisibility(View.VISIBLE);
        save.setVisibility(View.GONE);
        firstName.setEnabled(false);
        lastName.setEnabled(false);
        email.setEnabled(false);
        countryCode.setEnabled(false);
        countryCode.setClickable(false);
        phone.setEnabled(false);
        serviceType.setClickable(false);
        serviceType.setEnabled(false);
    }
}
