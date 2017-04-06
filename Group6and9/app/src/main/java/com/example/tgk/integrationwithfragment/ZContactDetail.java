package com.example.tgk.integrationwithfragment;


import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.TextView;


/**
 * Created by yingmei on 15-Apr-16.
 */
public class ZContactDetail extends Fragment {

    final static String ARG_ID = "id";
    long mCurrentId = -1;
    private ZContactDbAdapter dbHelper;
    OnDeleteClickListener mCallback;



    // The container Activity must implement this interface so the frag can deliver messages
    public interface OnDeleteClickListener {
        /** Called by Contact Detail Fragment when a Delete button is clicked */
        void onDeleteClick(long id);
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param id Parameter 1 a long value.
     * @return A new instance of fragment ZContactHelp.
     */
    public static ZContactDetail newInstance(long id) {
        ZContactDetail  fragment = new ZContactDetail();
        Bundle args = new Bundle();
        args.putLong(ARG_ID, id);
        fragment.setArguments(args);
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

        if (savedInstanceState != null) {
            mCurrentId = savedInstanceState.getLong(ARG_ID);
        }

        final View fragmentView = inflater.inflate(R.layout.zcontact_detail, container, false);

        Button deleteButton = (Button)fragmentView.findViewById(R.id.delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // Notify the parent activity of button clicked
                mCallback.onDeleteClick(mCurrentId);
            }
        });

        dbHelper = new ZContactDbAdapter(getActivity());
        dbHelper.open();

        // Inflate the layout for this fragment
        return fragmentView;
    }


    @Override
    public void onStart() {
        super.onStart();

        // During startup, check if there are arguments passed to the fragment.
        // onStart is a good place to do this because the layout has already been
        // applied to the fragment at this point so we can safely call the method
        // below that sets the article text.
        Bundle args = getArguments();

        if (args != null) {
            // Set detail based on argument passed in

            mCurrentId=args.getLong(ARG_ID);
            displayContactDetail(mCurrentId);
        } else if (mCurrentId != -1) {
            // Set detail based on saved instance state defined during onCreateView
            displayContactDetail(mCurrentId);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mCallback = (OnDeleteClickListener) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnDeleteClickListener");
        }
    }

    public void displayContactDetail(long id) {
        Cursor cursor = dbHelper.fetchContactById(id);

        TextView firstnameTextView = (TextView) getActivity().findViewById(R.id.firstname);
        firstnameTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow("firstname")));

        TextView lastnameTextView = (TextView) getActivity().findViewById(R.id.lastname);
        lastnameTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow("lastname")));

        TextView phoneTextView = (TextView) getActivity().findViewById(R.id.phone);
        phoneTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow("phone")));

        TextView emailTextView = (TextView) getActivity().findViewById(R.id.email);
        emailTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow("email")));

        TextView noteTextView = (TextView) getActivity().findViewById(R.id.note);
        noteTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow("note")));

        TextView createdateTextView = (TextView) getActivity().findViewById(R.id.createdate);
        createdateTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow("createdate")));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current article selection in case we need to recreate the fragment
        outState.putLong(ARG_ID, mCurrentId);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.zmenu_contact, menu);
    }

}