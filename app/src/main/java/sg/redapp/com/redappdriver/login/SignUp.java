package sg.redapp.com.redappdriver.login;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import sg.redapp.com.redappdriver.Classes.API;
import sg.redapp.com.redappdriver.MainActivity;
import sg.redapp.com.redappdriver.R;
import sg.redapp.com.redappdriver.WebView;

public class SignUp extends AppCompatActivity {
    Toolbar toolbar;
    EditText getFirstName, getLastName, getEmail, getPassword, getCfmPassword, getPhoneNumber, getCarPlateNumber;
    TextView getCountryCode, getTypeOfService, tnc, pp;
    CheckBox cbTowAccident, cbTowBreakdown, cbTyreManding, cbSpareTyreReplacement,cbBatteryJumpStart,cbBatteryReplacement, cbOthers;
    Button signup;
    private FirebaseAuth mAuth;
    String TAG = "Signup";
    LinearLayout loading;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser user;

    ArrayList arrayList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        SetupToolbar();
        loading = findViewById(R.id.loading);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        user = mAuth.getCurrentUser();

        getFirstName = findViewById(R.id.getFirstName);
        getLastName = findViewById(R.id.getLastName);
        getEmail = findViewById(R.id.getEmail);
        getPassword = findViewById(R.id.getPassword);
        getCfmPassword = findViewById(R.id.getCfmPassword);
        getPhoneNumber = findViewById(R.id.getPhoneNumber);
        getCountryCode = findViewById(R.id.getCountryCode);
        getTypeOfService = findViewById(R.id.getServiceType);
        getCarPlateNumber = findViewById(R.id.getCarPlateNumber);
        tnc = findViewById(R.id.tnc);
        pp = findViewById(R.id.pp);
        signup = findViewById(R.id.signup);
        HideLoad();

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
        getCountryCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = {"+65"};
                AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                builder.setItems(items, (dialog, item) -> {
                    getCountryCode.setText(items[item]);
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        getTypeOfService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                final CharSequence[] items = {"Tow (Accident)", "Tow (Breakdown)", "Tyre Mending", "Spare Tyre Replacement", "Battery Jump Start", "Battery Replacement", "Others (Unsure)"};
//                AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
////                builder.setView(R.id.type_of_service);
//                builder.setItems(items, (dialog, item) -> {
//                    getTypeOfService.setText(items[item]);
//                });
//                AlertDialog alert = builder.create();
//                alert.show();
                final Dialog dialog = new Dialog(SignUp.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.drop_down_type_of_service);
                Button okButton = dialog.findViewById(R.id.buttonOk);
                cbTowAccident = dialog.findViewById(R.id.serviceType_tow_accident);
                cbTowBreakdown = dialog.findViewById(R.id.serviceType_tow_breakdown);
                cbTyreManding = dialog.findViewById(R.id.serviceType_tyre_mending);
                cbSpareTyreReplacement = dialog.findViewById(R.id.serviceType_spare_tyre_replacement);
                cbBatteryJumpStart = dialog.findViewById(R.id.serviceType_battery_jump_start);
                cbBatteryReplacement = dialog.findViewById(R.id.serviceType_battery_replacement);
                cbOthers = dialog.findViewById(R.id.serviceType_other);
                dialog.show();
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference driverRef = firebaseDatabase.getReference().child("user").child("driver");
                        arrayList.clear();
                        if(cbTowAccident.isChecked()){
                            arrayList.add(cbTowAccident.getText().toString());
                        }
                        if(cbTowBreakdown.isChecked()){
                            arrayList.add(cbTowBreakdown.getText().toString());
                        }
                        if(cbTyreManding.isChecked()){
                            arrayList.add(cbTyreManding.getText().toString());
                        }
                        if(cbSpareTyreReplacement.isChecked()){
                            arrayList.add(cbSpareTyreReplacement.getText().toString());
                        }
                        if(cbBatteryJumpStart.isChecked()){
                            arrayList.add(cbBatteryJumpStart.getText().toString());
                        }
                        if(cbBatteryReplacement.isChecked()){
                            arrayList.add(cbBatteryReplacement.getText().toString());
                        }
                        if(cbOthers.isChecked()){
                            arrayList.add(cbOthers.getText().toString());
                        }
                        String completeString = "";
                        for(int i = 0; i<arrayList.size(); i++){
                            Log.i("arraylist",arrayList.size()+"");
                            completeString = completeString + arrayList.get(i);
                        }
                        getTypeOfService.setText(completeString);
                        dialog.dismiss();
                    }
                });
            }
        });
        pp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                API api = new API();
                startActivity(new Intent(SignUp.this, WebView.class).putExtra("message", api.getPP()).putExtra("title", "Privacy Policy"));
            }
        });
        tnc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                API api = new API();
                startActivity(new Intent(SignUp.this, WebView.class).putExtra("message", api.getTNC()).putExtra("title", "Terms and Conditions"));
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Validate()){
                    ShowLoad();
                    Register(getEmail.getText().toString()
                            , getPassword.getText().toString()
                            , getFirstName.getText().toString() + " " + getLastName.getText().toString()
                            , getCarPlateNumber.getText().toString()
                            , getCountryCode.getText().toString()
                            , getPhoneNumber.getText().toString()
                            , getTypeOfService.getText().toString());
                }else{
                    Toast.makeText(SignUp.this, "Please fill in all the blanks!", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    public void Register(String user_email
            , String user_password
            , String user_name, String carplate
            , String user_country_code
            , String user_phone_number
            , String user_type_of_service) {

        mAuth.createUserWithEmailAndPassword(user_email, user_password)
                .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            HideLoad();
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
//                                    updateUI(user);
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("user");
                            DatabaseReference userData = myRef.child("driver");
                            assert user != null;
                            DatabaseReference userid = userData.child(user.getUid());
                            DatabaseReference useremail = userid.child("email");
                            DatabaseReference username = userid.child("name");
                            DatabaseReference userCarPlate = userid.child("userCarPlate");
                            DatabaseReference userCountryCode = userid.child("country_code");
                            DatabaseReference userPhoneNumber = userid.child("phone_number");
                            DatabaseReference userTypeOfService = userid.child("type_of_service");
                            DatabaseReference userRole = userid.child("role");
                            DatabaseReference approve = userid.child("approve");
                            DatabaseReference onlineStatus = userid.child("online_status");
                            DatabaseReference inProgress = userid.child("in_progress");

//                            for(int i=0; i < arrayList.size(); i++){
//                                userTypeOfService.child(arrayList.get(i).toString());
//                            }

                            DatabaseReference setWallet = database.getReference("wallet");
                            DatabaseReference setWalletUserId = setWallet.child(user.getUid());
                            DatabaseReference setWalletAmount = setWalletUserId.child("value");
                            setWalletAmount.setValue(100.00);

                            useremail.setValue(user_email);
                            username.setValue(user_name);
                            userCountryCode.setValue(user_country_code);
                            userPhoneNumber.setValue(user_phone_number);
                            userTypeOfService.setValue(user_type_of_service);
                            userCarPlate.setValue(carplate);
                            userRole.setValue(TAG);
                            approve.setValue("pending");
                            onlineStatus.setValue(false);
                            inProgress.setValue(false);

                            approve.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String value = dataSnapshot.getValue(String.class);
                                    Log.e("email", value);
                                    assert value != null;
                                    if (value.equals("pending")) {
                                        startActivity(new Intent(SignUp.this, MainActivity.class));
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            });

                        } else {
                            HideLoad();
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUp.this, Objects.requireNonNull(task.getException()).getLocalizedMessage(),
                                    Toast.LENGTH_SHORT).show();
//                                    updateUI(null);
                        }

                    }
                });
    }

    public void HideLoad() {
        getFirstName.setEnabled(true);
        getLastName.setEnabled(true);
        getEmail.setEnabled(true);
        getCarPlateNumber.setEnabled(true);
        getPassword.setEnabled(true);
        getCfmPassword.setEnabled(true);
        getPhoneNumber.setEnabled(true);
        getCountryCode.setEnabled(true);
        getTypeOfService.setEnabled(true);
        tnc.setEnabled(true);
        pp.setEnabled(true);
        signup.setEnabled(true);
        loading.setVisibility(View.GONE);
    }

    public void ShowLoad() {
        getFirstName.setEnabled(false);
        getLastName.setEnabled(false);
        getEmail.setEnabled(false);
        getCarPlateNumber.setEnabled(false);
        getPassword.setEnabled(false);
        getCfmPassword.setEnabled(false);
        getPhoneNumber.setEnabled(false);
        getCountryCode.setEnabled(false);
        getTypeOfService.setEnabled(false);
        tnc.setEnabled(false);
        pp.setEnabled(false);
        signup.setEnabled(false);
        loading.setVisibility(View.VISIBLE);
    }

    public boolean Validate() {
        String[] getfield = new String[9];
        getfield[0] = "false";
        getfield[1] = "false";
        getfield[2] = "false";
        getfield[3] = "false";
        getfield[4] = "false";
        getfield[5] = "false";
        getfield[6] = "false";
        getfield[7] = "false";
        getfield[8] = "false";

        if (!getFirstName.getText().toString().equals("")) {
            getfield[0] = "true";
        }
        if (!getLastName.getText().toString().equals("")) {
            getfield[1] = "true";
        }
        if (!getEmail.getText().toString().equals("")) {
            getfield[2] = "true";
        }
        if (!getEmail.getText().toString().equals("")) {
            getfield[3] = "true";
        }
        if (!getPassword.getText().toString().equals("")) {
            getfield[4] = "true";
        }
        if (!getCfmPassword.getText().toString().equals("")) {
            getfield[5] = "true";
        }
        if (!getCountryCode.getText().toString().equals("")) {
            getfield[6] = "true";
        }
        if (!getPhoneNumber.getText().toString().equals("")) {
            getfield[7] = "true";
        }
        if (!getTypeOfService.getText().toString().equals("Type of Services")||!getTypeOfService.getText().toString().equals("")) {
            getfield[8] = "true";
        }
        return !getfield[0].equals("false") && !getfield[1].equals("false")&& !getfield[2].equals("false")&& !getfield[3].equals("false")&& !getfield[4].equals("false")&& !getfield[5].equals("false")&& !getfield[6].equals("false")&& !getfield[7].equals("false") && !getfield[8].equals("false");
    }
}
