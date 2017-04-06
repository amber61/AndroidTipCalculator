package com.example.tgk.integrationwithfragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
/**
 * List specific record page of the first mini app--Tip Record
 * 
 * @author Zhe Huang created on 4/18/2016.
 * @version 1.0.0
 * @see android.database.Cursor
 * @see android.os.Bundle
 * @see android.support.v4.app.FragmentTransaction
 * @see android.support.v4.app.Fragment
 * @see android.view.LayoutInflater
 * @see android.view.Menu
 * @see android.view.MenuInflater
 * @see android.view.View
 * @see android.view.ViewGroup
 * @see android.widget.Button
 * @see android.widget.TextView
 * @see android.widget.SimpleCursorAdapter
 * @see android.widget.Toast
 * @since 1.8.0_73 
 */
public class HTipRecordDetail extends Fragment
        implements HTipRecordAdd.OnFragmentInteractionListener{

    private HMainDbAdapter dbHelperRD;
    private SimpleCursorAdapter dataAdapter;
    Bundle args;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    static final String ARG_PARAM1 = "param1";
    static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    int mParam1;
    String mParam2;

    private HTipRecordAdd.OnFragmentInteractionListener mListener;

    public HTipRecordDetail() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HTipRecordAdd.
     */
    // TODO: Rename and change types and number of parameters
    public static HTipRecordDetail newInstance(String param1, String param2) {
        HTipRecordDetail fragment = new HTipRecordDetail();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mParam1 = savedInstanceState.getInt(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // If activity recreated (such as from screen rotate), restore
        // the previous article selection set by onSaveInstanceState().
        // This is primarily necessary when in the two-pane layout.
        if (savedInstanceState != null) {
            mParam1 = savedInstanceState.getInt(ARG_PARAM1);
        }
        final View rootView = inflater.inflate(R.layout.htipfragment_detail, container, false);
        Button back = (Button) rootView.findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                HTipRecordAllList newFragment = new HTipRecordAllList();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack so the user can navigate back
                transaction.replace(R.id.fragment_window, newFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
            }
        });

        Button delete = (Button) rootView.findViewById(R.id.delete_button);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelperRD = new HMainDbAdapter(getActivity());
                dbHelperRD.open();
                boolean flag = dbHelperRD.deleteTipRecordById((int)(args.getLong(ARG_PARAM1)));
                if(flag){
                    Toast.makeText(getActivity(), "Delete Successfully", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getActivity(), "Failure to delete", Toast.LENGTH_SHORT).show();
                }
                HTipRecordAllList newFragment = new HTipRecordAllList();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack so the user can navigate back
                transaction.replace(R.id.fragment_window, newFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed() {
        if (mListener != null) {
            onFragmentInteraction(mParam1);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();

        // During startup, check if there are arguments passed to the fragment.
        // onStart is a good place to do this because the layout has already been
        // applied to the fragment at this point so we can safely call the method
        // below that sets the article text.
        args = getArguments();
        if (args != null) {
            // Set article based on argument passed in
            onFragmentInteraction(args.getLong(ARG_PARAM1));
        } else if (mParam1 != -1) {
            // Set article based on saved instance state defined during onCreateView
            onFragmentInteraction(mParam1);
        }
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    @Override
    public void onFragmentInteraction(long id) {
        // TODO: concrete the interface in HTipRecordAdd

        dbHelperRD = new HMainDbAdapter(getActivity());
        dbHelperRD.open();
        Cursor rd = dbHelperRD.fetchTipRecordById((int) id);
        TextView record = (TextView) getActivity().findViewById(R.id.record_detail);
        if( rd != null && rd.moveToFirst() ) {
            record.setText("Record ID:   " + rd.getString(rd.getColumnIndex("_id")) + "\n" +
                    "Restaurant Name:   " + rd.getString(rd.getColumnIndex("restaurantName")) + "\n" +
                    "Expense Amount:   " + rd.getString(rd.getColumnIndex("expenseAmount")) + "\n" +
                    "Tip Percentage:   " + rd.getString(rd.getColumnIndex("tipPercentage")) + "\n" +
                    "Tip Amount:   " + rd.getString(rd.getColumnIndex("tipAmount")) + "\n" +
                    "Total Amount:   " + rd.getString(rd.getColumnIndex("totalAmount")) + "\n" +
                    "Tip Note:   " + rd.getString(rd.getColumnIndex("tipNote")) + "\n" +
                    "Tip Date:   " + rd.getString(rd.getColumnIndex("tipDate")));
            int visitTimes = dbHelperRD.fetchTipRecordByName(rd.getString(rd.getColumnIndex("restaurantName")));
            record.append("\n\n\nSummary: " + rd.getString(rd.getColumnIndex("restaurantName")) + " has been visited " + visitTimes + " times.");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.htipfragment_menu, menu);
    }
}
