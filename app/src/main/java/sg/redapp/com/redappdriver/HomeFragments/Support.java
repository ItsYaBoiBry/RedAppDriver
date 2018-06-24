package sg.redapp.com.redappdriver.HomeFragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import sg.redapp.com.redappdriver.R;
import sg.redapp.com.redappdriver.login.SignUp;

/**
 * A simple {@link Fragment} subclass.
 */
public class Support extends Fragment {
    EditText feedbackMessage;
    TextView feedbackTitle;
    Button submitFeedback, callFeedback;
    public Support(){

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

        feedbackTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = {"I have been Over-charged","Incurred Charges","The Service Provider was rude","The Service Provider was unprofessional","Driver requested cash payment","Others"};
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
                Intent intent=new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+"+6562624620"));
                startActivity(intent);
            }
        });
        return view;
    }

}
