package sg.redapp.com.redappdriver.HomeFragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebViewClient;
import android.widget.Button;

import sg.redapp.com.redappdriver.R;
import sg.redapp.com.redappdriver.WebView;

/**
 * A simple {@link Fragment} subclass.
 */
public class Settings extends Fragment {
    Button tnc, pp;

    public Settings() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        tnc = view.findViewById(R.id.tnc);
        pp = view.findViewById(R.id.pp);

        tnc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), WebView.class).putExtra("title","Terms and Conditions").putExtra("message","This is the Terms and Conditions"));
            }
        });
        pp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), WebView.class).putExtra("title","Privacy Policy").putExtra("message","This is the Privacy Policy"));
            }
        });
        return view;
    }

}
