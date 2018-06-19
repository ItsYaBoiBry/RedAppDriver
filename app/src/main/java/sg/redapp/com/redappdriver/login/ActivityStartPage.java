package sg.redapp.com.redappdriver.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import sg.redapp.com.redappdriver.R;

public class ActivityStartPage extends AppCompatActivity{
    Button toSignin, toSignup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);
        toSignin = findViewById(R.id.toSignIn);
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
    }


}
