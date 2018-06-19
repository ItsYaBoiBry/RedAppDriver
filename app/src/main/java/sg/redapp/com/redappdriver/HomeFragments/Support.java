package sg.redapp.com.redappdriver.HomeFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sg.redapp.com.redappdriver.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Support extends Fragment {

    public Support(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_support, container, false);

        return view;
    }

}
