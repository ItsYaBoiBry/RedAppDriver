package sg.redapp.com.redappdriver.HomeFragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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
public class Wallet extends Fragment {
TextView credit, date, amount;
LinearLayout transactionhistory;
    private FirebaseAuth mAuth;

    public Wallet() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wallet, container, false);
        credit = view.findViewById(R.id.credit);
        transactionhistory = view.findViewById(R.id.transactionhistory);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        DatabaseReference amount = FirebaseDatabase.getInstance().getReference("wallet").child(user.getUid()).child("value");
        amount.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Double value = dataSnapshot.getValue(Double.class);
                credit.setText(String.valueOf(value));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        ArrayList<ArrayList<String>> transactions = new ArrayList<>();
        ArrayList<String> transaction1 = new ArrayList<>();
        transaction1.add("17-12-12121");
        transaction1.add("$10.00");
        transactions.add(transaction1);
        ArrayList<String> transaction2 = new ArrayList<>();
        transaction2.add("17-17-1212");
        transaction2.add("$432.00");
        transactions.add(transaction2);
        ArrayList<String> transaction3 = new ArrayList<>();
        transaction3.add("17-53-12121");
        transaction3.add("$13.04");
        transactions.add(transaction3);

        for (int i = 0; i < transactions.size();i++){
            ArrayList<String> qn = transactions.get(i);
            transactionhistory.addView(initView(qn.get(0),qn.get(1)));
        }

        return view;
    }

    public View initView(String getDate, String getAmount){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View view = inflater.inflate(R.layout.wallet_transaction_item, null);
        date = view.findViewById(R.id.date);
        amount = view.findViewById(R.id.amount);
        date.setText(getDate);
        amount.setText(getAmount);

        return view;
    }

}
