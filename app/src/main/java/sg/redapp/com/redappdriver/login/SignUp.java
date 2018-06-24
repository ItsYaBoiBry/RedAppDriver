package sg.redapp.com.redappdriver.login;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import sg.redapp.com.redappdriver.Classes.API;
import sg.redapp.com.redappdriver.R;
import sg.redapp.com.redappdriver.WebView;

public class SignUp extends AppCompatActivity {
    Toolbar toolbar;
    EditText getFirstName, getLastName, getEmail, getPassword, getCfmPassword, getPhoneNumber;
    TextView getCountryCode, getTypeOfService, tnc, pp;
    Button signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        SetupToolbar();
        getFirstName = findViewById(R.id.getFirstName);
        getLastName = findViewById(R.id.getLastName);
        getEmail = findViewById(R.id.getEmail);
        getPassword = findViewById(R.id.getPassword);
        getCfmPassword = findViewById(R.id.getCfmPassword);
        getPhoneNumber = findViewById(R.id.getPhoneNumber);
        getCountryCode = findViewById(R.id.getCountryCode);
        getTypeOfService = findViewById(R.id.getServiceType);
        tnc = findViewById(R.id.tnc);
        pp = findViewById(R.id.pp);
        signup = findViewById(R.id.signup);
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
                final CharSequence[] items = {"Tow (Accident)","Tow (Breakdown)","Tyre Mending","Spare Tyre Replacement","Battery Jump Start","Battery Replacement","Others (Unsure)"};
                AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                builder.setItems(items, (dialog, item) -> {
                    getTypeOfService.setText(items[item]);
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        pp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                API api = new API();
                startActivity(new Intent(SignUp.this, WebView.class).putExtra("message", api.getPP()).putExtra("title","Privacy Policy"));
            }
        });
        tnc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                API api = new API();
                startActivity(new Intent(SignUp.this, WebView.class).putExtra("message", api.getTNC()).putExtra("title","Terms and Conditions"));
            }
        });
    }
}
