package sg.redapp.com.redappdriver;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class Messages extends AppCompatActivity {
    LinearLayout messages;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        messages = findViewById(R.id.messages);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ArrayList<ArrayList<String>> questions = new ArrayList<>();
        ArrayList<String> question1 = new ArrayList<>();
        question1.add("Aqil Tahir");
        question1.add("Hello");
        question1.add("7.30pm");
        question1.add("user");
        questions.add(question1);
        ArrayList<String> question2 = new ArrayList<>();
        question2.add("Aqil Tahir");
        question2.add("Hello");
        question2.add("7.30pm");
        question2.add("user");
        questions.add(question2);
        ArrayList<String> question3 = new ArrayList<>();
        question3.add("Aqil Tahir");
        question3.add("Hello");
        question3.add("7.30pm");
        question3.add("driver");
        questions.add(question3);

        for (int i = 0; i < questions.size();i++){
            ArrayList<String> qn = questions.get(i);
            if(qn.get(3).equals("user")){
                Log.e("type",qn.get(3));
                messages.addView(initUserMessage(qn.get(0),qn.get(1),qn.get(2)));
            }else if(qn.get(3).equals("driver")){
                Log.e("type",qn.get(3));
                messages.addView(initDriverMessage(qn.get(0),qn.get(1),qn.get(2)));
            }else{
                Log.e("type",qn.get(3));
                messages.addView(initDriverMessage(qn.get(0),qn.get(1),qn.get(2)));
            }

        }
    }
    public View initUserMessage(String getName, String getMessage, String getTime){
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View view = inflater.inflate(R.layout.user_message, null);
        TextView name = view.findViewById(R.id.name);
        TextView time = view.findViewById(R.id.time);
        TextView message = view.findViewById(R.id.message);

        message.setText(getMessage);
        time.setText(getTime);
        name.setText(getName);

        return view;
    }
    public View initDriverMessage(String getName, String getMessage, String getTime){
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View view = inflater.inflate(R.layout.driver_message, null);
        TextView name = view.findViewById(R.id.name);
        TextView time = view.findViewById(R.id.time);
        TextView message = view.findViewById(R.id.message);

        message.setText(getMessage);
        time.setText(getTime);
        name.setText(getName);

        return view;
    }
}
