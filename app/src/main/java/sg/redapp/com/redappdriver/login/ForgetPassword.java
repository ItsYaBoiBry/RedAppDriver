package sg.redapp.com.redappdriver.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import sg.redapp.com.redappdriver.R;

public class ForgetPassword extends AppCompatActivity {
    Toolbar toolbar;
    EditText getEmail;
    Button submitEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        SetupToolbar();
        getEmail = findViewById(R.id.email);
        submitEmail = findViewById(R.id.submitemail);
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
