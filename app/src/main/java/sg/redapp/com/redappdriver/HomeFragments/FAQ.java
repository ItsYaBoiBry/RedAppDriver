package sg.redapp.com.redappdriver.HomeFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import sg.redapp.com.redappdriver.R;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


public class FAQ extends Fragment {
    TextView title,message;
    LinearLayout faqlist;
    public FAQ() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_faq, container, false);
        faqlist = view.findViewById(R.id.faqlist);
        ArrayList<ArrayList<String>> questions = new ArrayList<>();
        ArrayList<String> question1 = new ArrayList<>();
        question1.add("How do we go about accepting a request?");
        question1.add("Hello, you can start accepting requests once you go ‘live’. Go ‘live’ simply by tapping on the switch icon at the top right of the home screen. You are live once the button turns green.");
        questions.add(question1);
        ArrayList<String> question2 = new ArrayList<>();
        question2.add("How do we go about accepting a request?");
        question2.add("Hello, you can start accepting requests once you go ‘live’. Go ‘live’ simply by tapping on the switch icon at the top right of the home screen. You are live once the button turns green.");
        questions.add(question2);
        ArrayList<String> question3 = new ArrayList<>();
        question3.add("How do we go about accepting a request?");
        question3.add("Hello, you can start accepting requests once you go ‘live’. Go ‘live’ simply by tapping on the switch icon at the top right of the home screen. You are live once the button turns green.");
        questions.add(question3);

        for (int i = 0; i < questions.size();i++){
            ArrayList<String> qn = questions.get(i);
            faqlist.addView(initView(qn.get(0),qn.get(1)));
        }

        return view;
    }
    public View initView(String getTitle, String getDetail){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View view = inflater.inflate(R.layout.faq_item, null);
        title = view.findViewById(R.id.title);
        message = view.findViewById(R.id.details);
        title.setText(getTitle);
        message.setText(getDetail);

        return view;
    }

}
