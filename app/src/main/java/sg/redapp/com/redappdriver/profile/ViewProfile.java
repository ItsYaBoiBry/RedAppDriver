package sg.redapp.com.redappdriver.profile;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;
import sg.redapp.com.redappdriver.R;
import sg.redapp.com.redappdriver.login.SignUp;

public class ViewProfile extends AppCompatActivity {
    Toolbar toolbar;
    EditText firstName, lastName, email,phone;
    TextView countryCode, serviceType, name, rating, vehicleNum;
    Button save, edit;
    CircleImageView profileImage, editImage;

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
        firstName.setText("Bryan");
        lastName.setText("Low");
        email.setText("bryanlowsk@gmail.com");
        countryCode.setText("+65");
        phone.setText("94511958");
        serviceType.setText("Tow (Accident)");
        name.setText(String.format("%s %s", firstName.getText().toString(), lastName.getText().toString()));
        rating.setText("4.2");
        vehicleNum.setText("SFK423J");
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
