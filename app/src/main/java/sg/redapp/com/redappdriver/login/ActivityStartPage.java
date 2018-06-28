package sg.redapp.com.redappdriver.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import sg.redapp.com.redappdriver.MainActivity;
import sg.redapp.com.redappdriver.R;

public class ActivityStartPage extends AppCompatActivity{
    Button toSignin, toSignup;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);
        toSignin = findViewById(R.id.toSignIn);
        firebaseAuth = FirebaseAuth.getInstance();
        toSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityStartPage.this, Login.class));
            }
        });
        toSignup = findViewById(R.id.toSignup);
        toSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityStartPage.this, SignUp.class));
            }
        });
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.e("SIGNED IN", "onAuthStateChanged:signed_in:" + user.getUid());
                    startActivity(new Intent(ActivityStartPage.this, Login.class));

                } else {
                    // User is signed out
                    Log.e("SIGNED OUT", "onAuthStateChanged:signed_out");
                    startActivity(new Intent(ActivityStartPage.this, SignUp.class));

                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (firebaseAuth.getCurrentUser() != null) {
            Log.e("LOGIN DETAILS UID", firebaseAuth.getCurrentUser().getUid());
            Log.e("LOGIN DETAILS EMAIl", firebaseAuth.getCurrentUser().getEmail());
            Intent i = new Intent(this, MainActivity.class).putExtra("uid", firebaseAuth.getCurrentUser().getUid());
            startActivity(i);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            firebaseAuth.removeAuthStateListener(mAuthListener);
        }
    }


}