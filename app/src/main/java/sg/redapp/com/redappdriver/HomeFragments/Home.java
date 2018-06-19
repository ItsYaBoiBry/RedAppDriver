package sg.redapp.com.redappdriver.HomeFragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;
import sg.redapp.com.redappdriver.R;
import sg.redapp.com.redappdriver.TripProcess;
import sg.redapp.com.redappdriver.profile.ViewProfile;

/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment {
    CircleImageView profile;
    TextView name, rating, credit, referral, backtomap;
    Button viewProfile;


    public Home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        name = view.findViewById(R.id.name);
        credit = view.findViewById(R.id.credit);
        rating = view.findViewById(R.id.rating);
        profile = view.findViewById(R.id.userimage);
        referral = view.findViewById(R.id.referral);
        backtomap = view.findViewById(R.id.backtomap);
        backtomap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), TripProcess.class));
            }
        });
//        backtomap.setVisibility(View.GONE);

        viewProfile = view.findViewById(R.id.viewProfile);
        viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ViewProfile.class));
            }
        });

        tempsetup();

        return view;
    }

    public void tempsetup() {
        referral.setText("AdSIF127");
        credit.setText("$" + String.valueOf(2000.00));
        rating.setText(String.valueOf(4.2));
        name.setText("Bryan Low");
        Glide.with(getContext())
                .load("https://images.pexels.com/photos/91227/pexels-photo-91227.jpeg?auto=compress&cs=tinysrgb&h=350")
                .into(profile);
    }

}
