package com.example.tgk.integrationwithfragment;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
/**
 * List all records page of the first mini app--Tip Record
 * 
 * @author Zhe Huang created on 4/18/2016.
 * @version 1.0.0
 * @see android.content.Context
 * @see android.database.Cursor
 * @see android.os.AsyncTask
 * @see android.os.Bundle
 * @see android.support.v4.app.FragmentTransaction
 * @see android.support.v4.app.ListFragment
 * @see android.view.LayoutInflater
 * @see android.view.Menu
 * @see android.view.MenuInflater
 * @see android.view.View
 * @see android.view.ViewGroup
 * @see android.widget.Button
 * @see android.widget.ListView
 * @see android.widget.SimpleCursorAdapter
 * @since 1.8.0_73 
 */
public class HTipRecordAllList extends ListFragment
                implements HTipRecordAdd.OnFragmentInteractionListener{

    private HMainDbAdapter dbHelper;
    private SimpleCursorAdapter dataAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    static final String ARG_PARAM1 = "param1";
    static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    int mParam1;
    String mParam2;

    private HTipRecordAdd.OnFragmentInteractionListener mListener;

    public HTipRecordAllList() {
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
    public static HTipRecordAllList newInstance(String param1, String param2) {
        HTipRecordAllList fragment = new HTipRecordAllList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
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
        final View rootView = inflater.inflate(R.layout.htipfragment_alllist, container, false);

        displayListView();

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed() {
        if (mListener != null) {
            onFragmentInteraction(mParam1);
        }
    }

    private void displayListView() {
        // The desired columns to be bound
        String[] from = new String[] {
                HMainDbAdapter.RESTAURANT_NAME,
                HMainDbAdapter.EXPENSE_AMOUTN,
                HMainDbAdapter.TIP_PERCENTAGE,
                HMainDbAdapter.TOTAL_AMOUNT
        };

        // the XML defined views which the data will be bound to
        int[] to = new int[] {
                R.id.nameview,
                R.id.expenseview,
                R.id.percentview,
                R.id.totalview,
        };

        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        //cursor is null for now, but will be swapped by the following AsyncTask onPostExecute method
        dataAdapter = new SimpleCursorAdapter(
                getActivity(), R.layout.htipfragment_alllistinfo,
                null,      //notice the cursor is null for now
                from,
                to,0);

        //This Java statement (beginning with "new" and ending with "}.execute();") executes an new instance
        // of an anonymous class that extends AsyncTask.  The new instance is-a AsyncTask.
        // Executes an AsyncTask to acquire the cursor on a background thread
        //in onPostExecute, the real cursor will replace the null cursor
        new AsyncTask<Void, Void, Cursor>() {
            @Override
            public Cursor doInBackground(Void... v) {
                dbHelper = new HMainDbAdapter(getActivity());
                dbHelper.open();

                return dbHelper.fetchAllRecords();
            }
            @Override
            public void onPostExecute(Cursor c){
                dataAdapter.swapCursor(c);
            }
        }.execute();

        // Assign adapter to ListView
        setListAdapter(dataAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HTipRecordAdd.OnFragmentInteractionListener) {
            mListener = (HTipRecordAdd.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        HTipRecordAdd newFragment = new HTipRecordAdd();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_window view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_window, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
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
        HTipRecordDetail newFragment = new HTipRecordDetail();
        Bundle args = new Bundle();
        args.putLong(HTipRecordDetail.ARG_PARAM1, id);
        newFragment.setArguments(args);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_window, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.htipfragment_menu, menu);
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        // Notify the parent activity of selected item
        onFragmentInteraction(id);

        // Set the item as checked to be highlighted when in two-pane layout
        getListView().setItemChecked(position, true);
    }
}
