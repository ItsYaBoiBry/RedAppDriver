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

        ArrayList<String> transactionDetails = new ArrayList<>();
        transactionDetails.add("Blk 279 tampines street 22");
        transactionDetails.add("9.40 PM");
        transactionDetails.add("Blk 279 tampines Avenue 7");
        transactionDetails.add("10.45 PM");
        transactionDetails.add("#49094");
        transactionDetails.add("$264.50");
        transactionDetails.add("Tow (Accident)");
        transactionDetails.add("2018-19-09");

        ArrayList<String> transactionDetails2 = new ArrayList<>();
        transactionDetails2.add("Blk 279 tampines street 22");
        transactionDetails2.add("9.40 PM");
        transactionDetails2.add("Blk 279 tampines Avenue 7");
        transactionDetails2.add("10.45 PM");
        transactionDetails2.add("#49094");
        transactionDetails2.add("$264.50");
        transactionDetails2.add("Tow (Accident)");
        transactionDetails2.add("2018-19-04");

        ArrayList<String> transactionDetails3 = new ArrayList<>();
        transactionDetails3.add("Blk 279 tampines street 22");
        transactionDetails3.add("9.40 PM");
        transactionDetails3.add("Blk 279 tampines Avenue 7");
        transactionDetails3.add("10.45 PM");
        transactionDetails3.add("#49094");
        transactionDetails3.add("$264.50");
        transactionDetails3.add("Tow (Accident)");
        transactionDetails3.add("2018-19-08");

        transactions.add(transactionDetails);
        transactions.add(transactionDetails2);
        transactions.add(transactionDetails3);


        for (int i =0; i < transactions.size();i++){
            ArrayList<String> transaction = transactions.get(i);
            String getDate = transaction.get(7);
            if(getDate.equals("2018-19-09")){
                todaylist.addView(initView(transaction));
            }else{
                previouslist.addView(initView(transaction));
            }
        }
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
        price.setText(details.get(5));
        type.setText(details.get(6));

        return view;
    }


}
