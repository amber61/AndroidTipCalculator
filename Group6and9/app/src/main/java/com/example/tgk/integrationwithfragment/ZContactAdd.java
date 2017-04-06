package com.example.tgk.integrationwithfragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by yingmei on 14-Apr-16.
 */
public class ZContactAdd extends Fragment {

    OnAddClickListener mCallback;
    private String firstname;
    private String lastname;
    private String phone;
    private String email;
    private String note;

    // The container Activity must implement this interface so the frag can deliver messages
    public interface OnAddClickListener {
        public void onAddClick(String firstname, String lastname,String phone, String email,String note);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ZContactHelp.
     */
    // TODO: Rename and change types and number of parameters
    public static ZContactAdd newInstance() {
        ZContactAdd fragment = new ZContactAdd();
        //Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        //fragment.setArguments(args);
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

        // If activity recreated (such as from screen rotate), restore
        // the previous article selection set by onSaveInstanceState().
        // This is primarily necessary when in the two-pane layout.

        final View fragmentView = inflater.inflate(R.layout.zcontact_add, container, false);

        //create an instance of Add button and add it's listener
        Button addButton = (Button)fragmentView.findViewById(R.id.add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                EditText firstnameEditText = (EditText) fragmentView.findViewById(R.id.firstname);
                firstname=firstnameEditText.getText().toString();
                EditText lastnameEditText = (EditText) fragmentView.findViewById(R.id.lastname);
                lastname=lastnameEditText.getText().toString();
                EditText phoneEditText = (EditText) fragmentView.findViewById(R.id.phone);
                phone=phoneEditText.getText().toString();
                EditText emailEditText = (EditText) fragmentView.findViewById(R.id.email);
                email=emailEditText.getText().toString();
                EditText noteEditText = (EditText) fragmentView.findViewById(R.id.note);
                note=noteEditText.getText().toString();
                // Notify the parent activity of button clicked
                mCallback.onAddClick(firstname,lastname,phone,email,note);
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mCallback = (OnAddClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnAddClickListener");
        }
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

