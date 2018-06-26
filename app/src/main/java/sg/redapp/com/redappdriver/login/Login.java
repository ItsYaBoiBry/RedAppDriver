package sg.redapp.com.redappdriver.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.nio.charset.MalformedInputException;
import java.util.Set;

import sg.redapp.com.redappdriver.MainActivity;
import sg.redapp.com.redappdriver.R;
import sg.redapp.com.redappdriver.functions.SharedPreferenceStorage;

public class Login extends AppCompatActivity {
    Button login;
    EditText getEmail, getPassword;
    TextView forgotPassword;
    Toolbar toolbar;
    SharedPreferences sharedPreferences;
    Context context = Login.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferenceStorage storage = new SharedPreferenceStorage(Login.this);
        storage.StoreString("trip_status","1");

        getEmail = findViewById(R.id.email);
        getPassword = findViewById(R.id.password);
        forgotPassword = findViewById(R.id.forgotpassword);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,ForgetPassword.class));
            }
        });

        SetupToolbar();
        login = findViewById(R.id.signin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, MainActivity.class));
                finish();
            }
        });
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
}
