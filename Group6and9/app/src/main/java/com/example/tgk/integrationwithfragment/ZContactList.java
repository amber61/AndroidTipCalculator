package com.example.tgk.integrationwithfragment;

import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


/**
 * Created by yingmei on 15-Apr-16.
 */
public class ZContactList extends Fragment {

    private ZContactDbAdapter dbHelper;
    private SimpleCursorAdapter dataAdapter;
    OnContactSelectedListener mCallback;
    OnAddContactClickListener mCallbackAdd;

    // The container Activity must implement this interface so the frag can deliver messages
    public interface OnAddContactClickListener {
        void onAddContactClick();
    }

    // The container Activity must implement this interface so the frag can deliver messages
    public interface OnContactSelectedListener {
        void onContactSelected(long id);
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ZContactList.
     */
    // TODO: Rename and change types and number of parameters
    public static ZContactList newInstance() {
        ZContactList fragment = new ZContactList();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View fragmentView = inflater.inflate(R.layout.zcontact_list, container, false);

        Button addContactButton = (Button)fragmentView.findViewById(R.id.addcontact);
        addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // Notify the parent activity of button clicked
                mCallbackAdd.onAddContactClick();
            }
        });

        //Generate ListView from SQLite Database
     /*
     * Sets up a ListView with a SimpleCursorAdapter and a Cursor with all the rows
     * from the database table.  Also sets up the handler for when an item is selected.
     */

        // The desired columns to be bound
        String[] columns = new String[] {
                ZContactDbAdapter.KEY_FIRSTNAME,
                ZContactDbAdapter.KEY_LASTNAME,
                ZContactDbAdapter.KEY_PHONE
        };

        // the XML defined views which the data will be bound to
        int[] to = new int[] {
                R.id.firstname,
                R.id.lastname,
                R.id.phone
        };

        //create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        //cursor is null for now, but will be swapped by the following AsyncTask onPostExecute method
        dataAdapter = new SimpleCursorAdapter(getActivity(), R.layout.zcontact_summary,
                null,      //notice the cursor is null for now
                columns,
                to,0);

        //This Java statement (beginning with "new" and ending with "}.execute();") executes an new instance
        // of an anonymous class that extends AsyncTask.  The new instance is-a AsyncTask.
        // Executes an AsyncTask to acquire the cursor on a background thread
        //in onPostExecute, the real cursor will replace the null cursor
        new AsyncTask<Void, Void, Cursor>() {
            @Override
            public Cursor doInBackground(Void... v) {
                dbHelper = new ZContactDbAdapter(getActivity());
                dbHelper.open();

//                int count = dbHelper.countContactsByName("");
//                if(count == 0) {
//                    //Add some data for testing
//                    dbHelper.insertSomeContacts();
//                }

                Cursor cursor = dbHelper.fetchAllContacts();
                return cursor;
            }
            @Override
            public void onPostExecute(Cursor c){
                dataAdapter.swapCursor(c);
            }
        }.execute();


        ListView listView = (ListView) fragmentView.findViewById(R.id.listView1);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                mCallback.onContactSelected(id);

            }
        });

        EditText contactFilter = (EditText) fragmentView.findViewById(R.id.contactFilter);
        contactFilter.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                dataAdapter.getFilter().filter(s.toString());
            }
        });

        dataAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                return dbHelper.fetchContactsByName(constraint.toString());
            }
        });

        // Inflate the layout for this fragment
        return fragmentView;
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mCallback = (OnContactSelectedListener) activity;
            mCallbackAdd = (OnAddContactClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnContactSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
        mCallbackAdd = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.zmenu_contact, menu);
    }
}