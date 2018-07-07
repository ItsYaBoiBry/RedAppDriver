package sg.redapp.com.redappdriver.HomeFragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import sg.redapp.com.redappdriver.R;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class History extends Fragment {
    TextView fromAddress, fromTime, toAddress, toTime, id,price,type, date;
    ArrayList<ArrayList<String>> transactions;
    LinearLayout todaylist, previouslist;

    public History() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        transactions = new ArrayList<>();
        todaylist = view.findViewById(R.id.todaylist);
        previouslist = view.findViewById(R.id.previouslist);


        for (int i =0; i < transactions.size();i++){
            ArrayList<String> transaction = transactions.get(i);
            String getDate = transaction.get(7);
            if(getDate.equals("2018-19-09")){
                todaylist.addView(initView(transaction));
            }else{
                previouslist.addView(initView(transaction));
            }
        }
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference history = FirebaseDatabase.getInstance().getReference("history");

        history.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                transactions = new ArrayList<>();
                todaylist.removeAllViews();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if(String.valueOf(postSnapshot.getKey()).equals("null")){
                        Log.e("HISTORY", "HISTORY KEY IS NULL");
                        Log.e("HISTORY ID NULL",String.valueOf(postSnapshot.getKey()));
                    }else{
                        Log.e("HISTORY ID",String.valueOf(postSnapshot.getKey()));
                        if(String.valueOf(postSnapshot.child("driver_uid").getValue()).equals(user.getUid())){
                            ArrayList<String> transactionDetails = new ArrayList<>();
                            transactionDetails.add(String.valueOf(postSnapshot.child("pickup_name").getValue()));
                            transactionDetails.add(String.valueOf(postSnapshot.child("transaction_time_start").getValue()));
                            transactionDetails.add(String.valueOf(postSnapshot.child("destination_name").getValue()));
                            transactionDetails.add(String.valueOf(postSnapshot.child("transaction_time_complete").getValue()));
                            transactionDetails.add(String.valueOf(postSnapshot.getKey()));
                            transactionDetails.add(String.valueOf(postSnapshot.child("price").getValue()));
                            transactionDetails.add(String.valueOf(postSnapshot.child("service_type").getValue()));
                            transactionDetails.add("0000-00-00");
                            transactions.add(transactionDetails);
                        }
                    }


                }

                for (int i = 0; i < transactions.size(); i++) {
                    ArrayList<String> qn = transactions.get(i);
                    todaylist.addView(initView(qn));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    private View initView(ArrayList<String> details){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View view = inflater.inflate(R.layout.transaction_item, null);
        fromAddress = view.findViewById(R.id.fromAddress);
        fromTime = view.findViewById(R.id.fromTime);
        toAddress = view.findViewById(R.id.toAddress);
        toTime = view.findViewById(R.id.toTime);
        id = view.findViewById(R.id.id);
        price = view.findViewById(R.id.price);
        type = view.findViewById(R.id.type);

        fromAddress.setText(details.get(0));
        fromTime.setText(details.get(1));
        toAddress.setText(details.get(2));
        toTime.setText(details.get(3));
        id.setText(details.get(4));
        price.setText(String.format("$%s", details.get(5)));
        type.setText(details.get(6));

        return view;
    }


}
