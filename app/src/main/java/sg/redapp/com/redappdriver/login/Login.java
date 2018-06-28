package sg.redapp.com.redappdriver.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

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
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferenceStorage storage = new SharedPreferenceStorage(Login.this);
        storage.StoreString("trip_status","1");
        firebaseAuth = FirebaseAuth.getInstance();
        getEmail = findViewById(R.id.email);
        getPassword = findViewById(R.id.password);
        forgotPassword = findViewById(R.id.forgotpassword);
        getEmail.setText("bryanlow987@gmail.com");
        getPassword.setText("Bryan987");
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
                String email = getEmail.getText().toString().trim();
                String pass = getPassword.getText().toString();
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    getEmail.setError("Email format is incorrect");
//
                } else {
//                    final ProgressDialog progressDialog = ProgressDialog.show(Login.this, "Please wait...", "Processing...", true);
                    (firebaseAuth.signInWithEmailAndPassword(email, pass)).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                Toast.makeText(Login.this, "Login successful", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(Login.this, MainActivity.class);
                                i.putExtra("uid", firebaseAuth.getCurrentUser().getUid());
                                startActivity(i);
                            } else {
                                Log.e("Error", task.getException().toString());
                                Toast.makeText(Login.this, "Sorry, your email and/or password is invalid.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
//                startActivity(new Intent(Login.this, MainActivity.class));
//                finish();
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
