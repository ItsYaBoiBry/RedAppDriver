package sg.redapp.com.redappdriver.HomeFragments;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import sg.redapp.com.redappdriver.DropOff;
import sg.redapp.com.redappdriver.R;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class Wallet extends Fragment {
    TextView credit, date, amount;
    LinearLayout transactionhistory;
    Button withdraw;
    private FirebaseAuth mAuth;
    ArrayList<ArrayList<String>> transactions;

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
        withdraw = view.findViewById(R.id.withdraw);
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
        withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(Objects.requireNonNull(getContext()));
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.withdraw_money);

                EditText withdraw_amount = dialog.findViewById(R.id.etwihdraw);
                Button withdraw = dialog.findViewById(R.id.withdraw);
                Button cancel = dialog.findViewById(R.id.cancel);

                withdraw.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        double withdrawing_amount = Double.parseDouble(withdraw_amount.getText().toString());
                        double current_amount = Double.parseDouble(credit.getText().toString());
                        if(withdrawing_amount > current_amount){
                            Toast.makeText(getContext(), "You cannot withdraw this amount", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }else{
                            double final_amount = current_amount - withdrawing_amount;
                            Date currentTime = Calendar.getInstance().getTime();
                            DatabaseReference history = FirebaseDatabase.getInstance().getReference("wallet").child(user.getUid()).child("history").child(String.valueOf(currentTime));
                            DatabaseReference draw_amount = history.child("amount");
                            DatabaseReference draw_reason = history.child("type");

                            draw_reason.setValue("Withdrawal");
                            draw_amount.setValue(withdrawing_amount);
                            amount.setValue(final_amount);
                            dialog.dismiss();
                        }

                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });


        DatabaseReference history = FirebaseDatabase.getInstance().getReference("wallet").child(user.getUid()).child("history");

        history.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                transactions = new ArrayList<>();
                transactionhistory.removeAllViews();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    ArrayList<String> transaction = new ArrayList<>();
                    transaction.add(String.valueOf(postSnapshot.getKey()));
                    transaction.add("$" + String.valueOf(postSnapshot.child("amount").getValue()));

                    Log.e("date", String.valueOf(postSnapshot.getKey()));
                    Log.e("amount", String.valueOf(postSnapshot.child("amount").getValue()));

                    transactions.add(transaction);
                }

                for (int i = 0; i < transactions.size(); i++) {
                    ArrayList<String> qn = transactions.get(i);
                    transactionhistory.addView(initView(qn.get(0), qn.get(1)));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }
    public View initView(String getDate, String getAmount) {
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
