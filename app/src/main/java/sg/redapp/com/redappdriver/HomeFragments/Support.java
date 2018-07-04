package sg.redapp.com.redappdriver.HomeFragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import java.util.Calendar;
import java.util.Date;

import android.os.CountDownTimer;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import sg.redapp.com.redappdriver.R;
import sg.redapp.com.redappdriver.login.SignUp;

/**
 * A simple {@link Fragment} subclass.
 */
public class Support extends Fragment {
    EditText feedbackMessage;
    TextView feedbackTitle;
    Button submitFeedback, callFeedback;
    String DRIVER_TAG = "driver";
    private FirebaseAuth mAuth;
    LinearLayout loading;

    public Support() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_support, container, false);
        feedbackMessage = view.findViewById(R.id.feedbackMessage);
        feedbackTitle = view.findViewById(R.id.feedbackTitle);
        submitFeedback = view.findViewById(R.id.submitFeedback);
        callFeedback = view.findViewById(R.id.callFeedback);
        loading = view.findViewById(R.id.loading);
        HideLoad();
        feedbackTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = {"I have been Over-charged", "Incurred Charges", "The Service Provider was rude", "The Service Provider was unprofessional", "Driver requested cash payment", "Others"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setItems(items, (dialog, item) -> {
                    feedbackTitle.setText(items[item]);
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        callFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "+6562624620"));
                startActivity(intent);
            }
        });
        submitFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Validate()) {
                    ShowLoad();
                    SubmitFeedback(feedbackTitle.getText().toString(), feedbackMessage.getText().toString());
                }
            }
        });
        return view;
    }

    public void SubmitFeedback(String getTitle, String getMessage) {
        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        Log.e("USER ID", user.getUid());
        Date currentTime = Calendar.getInstance().getTime();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("feedback");
        DatabaseReference userid = myRef.child(user.getUid() + " (" + currentTime + ")");
        DatabaseReference title = userid.child("title");
        DatabaseReference message = userid.child("message");
        DatabaseReference userRole = userid.child("role");
        DatabaseReference date = userid.child("date");

        date.setValue(String.valueOf(currentTime));
        Log.e("date entered", String.valueOf(currentTime));
        userRole.setValue("driver");
        title.setValue(getTitle);
        message.setValue(getMessage);
        new CountDownTimer(3000, 1000) { // adjust the milli seconds here
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                date.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String value = dataSnapshot.getValue(String.class);
                        Log.e("date recieved", value);
                        assert value != null;
                        if (value.equals(String.valueOf(currentTime))) {
                            HideLoad();
                            Toast.makeText(getContext(), "Feedback has been submitted!",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            HideLoad();
                            Toast.makeText(getContext(), "Unable to submit feedback!",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        }.start();
    }

    public boolean Validate() {
        String[] getfield = new String[2];
        getfield[0] = "false";
        getfield[1] = "false";
        if (!feedbackTitle.getText().toString().equals("Select Topic")) {
            getfield[0] = "true";
        } else {
            Toast.makeText(getContext(), "Please select a title", Toast.LENGTH_LONG).show();
        }
        if (!feedbackMessage.getText().toString().equals("")) {
            getfield[1] = "true";
        } else {
            Toast.makeText(getContext(), "Please enter a feedback", Toast.LENGTH_LONG).show();
        }
        Log.e("title validate", getfield[0]);
        Log.e("message validate", getfield[1]);
        return !getfield[0].equals("false") && !getfield[1].equals("false");
    }

    public void HideLoad() {
        feedbackMessage.setEnabled(true);
        feedbackTitle.setEnabled(true);
        submitFeedback.setEnabled(true);
        callFeedback.setEnabled(true);
        loading.setVisibility(View.GONE);
    }

    public void ShowLoad() {
        feedbackMessage.setEnabled(false);
        feedbackTitle.setEnabled(false);
        submitFeedback.setEnabled(false);
        callFeedback.setEnabled(false);
        loading.setVisibility(View.VISIBLE);
    }


}
