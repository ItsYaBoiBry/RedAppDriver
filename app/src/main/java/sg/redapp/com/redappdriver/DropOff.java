package sg.redapp.com.redappdriver;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class DropOff extends AppCompatActivity implements View.OnClickListener{
    TextView report;
    Button done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drop_off);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.report:
                showFeedbackDialog();
                break;
            case R.id.done:
                finish();
                break;
        }
    }
    public void showFeedbackDialog(){
        final Dialog dialog = new Dialog(DropOff.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.trip_report_feedback);
        ImageButton reject = dialog.findViewById(R.id.reject);
        EditText message = dialog.findViewById(R.id.message);
        Button accept = dialog.findViewById(R.id.accept);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("FEEDBACK MESSAGE:",message.getText().toString());
                dialog.dismiss();
            }
        });
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {
                    dialog.dismiss();
            }
        });
        dialog.show();

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Log.e("Back Press", "On Back Pressed");
    }
}
