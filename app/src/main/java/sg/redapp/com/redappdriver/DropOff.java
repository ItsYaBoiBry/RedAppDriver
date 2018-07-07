package sg.redapp.com.redappdriver;

import android.app.Dialog;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DropOff extends AppCompatActivity implements View.OnClickListener{
    TextView report;
    Button done;
    TextView price, pickupLocation, pickupTime, dropLocation, dropTime, transaction_id, serviceType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drop_off);

        price = findViewById(R.id.price);
        pickupLocation = findViewById(R.id.pickupLocation);
        pickupTime = findViewById(R.id.pickupTime);
        dropLocation = findViewById(R.id.dropLocation);
        dropTime = findViewById(R.id.dropTime);
        transaction_id = findViewById(R.id.transaction_id);
        serviceType = findViewById(R.id.serviceType);

        done = findViewById(R.id.done);

        Intent intent = getIntent();
        String tripid = intent.getStringExtra("history_id");
        Log.e("TRIP ID DROPOFF", String.valueOf(tripid));
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference tripdetails = database.getReference().child("history").child(String.valueOf(tripid));
        tripdetails.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("TO:",String.valueOf(dataSnapshot.child("destination_name").getValue()));
                Log.e("FROM:",String.valueOf(dataSnapshot.child("pickup_name").getValue()));
                Log.e("ID:",String.valueOf(dataSnapshot.getKey()));
                Log.e("PRICE:",String.valueOf(dataSnapshot.child("price").getValue()));
                Log.e("SERVICE TYPE:",String.valueOf(dataSnapshot.child("service_type").getValue()));
                Log.e("FROM TIME:",String.valueOf(dataSnapshot.child("transaction_time_start").getValue()));
                Log.e("TO TIME:",String.valueOf(dataSnapshot.child("transaction_time_complete").getValue()));
                if(String.valueOf(dataSnapshot.child("").getValue()).equals("null")){
                    Log.e("DROPOFF DATA", "NULL");
                    finish();
                }else{
                    Log.wtf("DROPOFF DATA", "NOT NULL");
                    if(String.valueOf(dataSnapshot.child("price").getValue()).equals("null")){
                        Log.e("PRICE DATA", "NULL");
                        finish();
                    }else{
                        price.setText(String.format("$%s", String.valueOf(dataSnapshot.child("price").getValue())));
                        pickupLocation.setText(String.valueOf(dataSnapshot.child("pickup_name").getValue()));
                        pickupTime.setText(String.valueOf(dataSnapshot.child("transaction_time_start").getValue()));
                        dropLocation.setText(String.valueOf(dataSnapshot.child("destination_name").getValue()));
                        dropTime.setText(String.valueOf(dataSnapshot.child("transaction_time_complete").getValue()));
                        transaction_id.setText(String.valueOf(dataSnapshot.getKey()));
                        serviceType.setText(String.valueOf(dataSnapshot.child("service_type").getValue()));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                DatabaseReference trip = firebaseDatabase.getReference().child("trip").child(firebaseUser.getUid());
                trip.child("status").setValue("pending");
//                if(trip.getKey()!=null){
//                    trip.removeValue();
//                }
                finish();

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.report:
                showFeedbackDialog();
                break;

        }
    }

    public void showFeedbackDialog(){
        final Dialog dialog = new Dialog(DropOff.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.trip_report_feedback);
        ImageButton reject = dialog.findViewById(R.id.reject);
        EditText message = dialog.findViewById(R.id.message);
        Button accept = dialog.findViewById(R.id.accept);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("FEEDBACK MESSAGE:",message.getText().toString());
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
    public void onBackPressed() {
//        super.onBackPressed();
        Log.e("Back Press", "On Back Pressed");
    }
}
