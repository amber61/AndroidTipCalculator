package com.example.tgk.integrationwithfragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
/**
 * Add records page of the first mini app--Tip Record
 * 
 * @author Zhe Huang created on 4/18/2016.
 * @version 1.0.0
 * @see android.content.Context
 * @see android.os.Bundle
 * @see android.support.v4.app.Fragment
 * @see android.view.LayoutInflater
 * @see android.view.Menu
 * @see android.view.MenuInflater
 * @see android.view.View
 * @see android.view.ViewGroup
 * @see android.widget.Button
 * @see android.widget.EditText
 * @see android.widget.RadioGroup
 * @see android.widget.TextView
 * @see android.widget.Toast
 * @since 1.8.0_73 
 */
/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HTipRecordAdd.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HTipRecordAdd#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HTipRecordAdd extends Fragment {

    private HMainDbAdapter dbHelper;
    Double tipPercentage=0.0;
    Double tipAmount=0.0;
    Double expenseAmount=0.0;
    Double totalAmount=0.0;
    TextView tipText=null;
    TextView totalText=null;
    EditText expenseText=null;
    String nameMessage=null;
    String noteMessage=null;
    String dateCreated=null;
    EditText noteText=null;
    EditText nameText=null;
    RadioGroup radioBtn=null;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HTipRecordAdd() {
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
    public static HTipRecordAdd newInstance(String param1, String param2) {
        HTipRecordAdd fragment = new HTipRecordAdd();
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
        final View rootView = inflater.inflate(R.layout.htipfragment_add, container, false);
        Button save = (Button) rootView.findViewById(R.id.save_button);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameMessage = nameText.getText().toString();
                noteMessage = noteText.getText().toString();
                dbHelper = new HMainDbAdapter(getActivity());
                dbHelper.open();
                dbHelper.createTipRecords(nameMessage, expenseAmount, tipPercentage, tipAmount, totalAmount, noteMessage, dateCreated);
                onButtonPressed();
            }
        });
        Button clear = (Button)rootView.findViewById(R.id.clear_button);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameText.setText("");
                expenseAmount=0.0;
                expenseText.setText("");
                noteText.setText("");
                radioBtn.clearCheck();
                tipAmount=0.0;
                tipText.setText("");
                totalAmount=0.0;
                totalText.setText("");
            }
        });

        nameText = (EditText) rootView.findViewById(R.id.name_text);// get restaurant text
        expenseText = (EditText) rootView.findViewById(R.id.expense_text);// get expense text
        tipText = (TextView) rootView.findViewById(R.id.tip_text);// get tip text
        totalText = (TextView) rootView.findViewById(R.id.total_text);// get total text
        // get tip Percentage and calculate tip value and total value

        radioBtn = (RadioGroup)rootView.findViewById(R.id.radio);
        radioBtn.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.radio1:
                        tipPercentage = 0.1;
                        break;
                    case R.id.radio2:
                        tipPercentage = 0.15;
                        break;
                    case R.id.radio3:
                        tipPercentage = 0.2;
                        break;
                }
                String expenseMessage = expenseText.getText().toString();
                try{expenseAmount = Double.valueOf(expenseMessage);}
                catch(NumberFormatException e){return;}
                tipAmount = expenseAmount * tipPercentage;
                DecimalFormat f = new DecimalFormat("##.00");
                tipAmount = Double.valueOf(f.format(tipAmount));
                totalAmount = expenseAmount + tipAmount;
                tipText.setText(Double.toString(tipAmount));
                totalText.setText(Double.toString(totalAmount));
            }
        });
        noteText = (EditText) rootView.findViewById(R.id.note_text);//get note text
        // get current date value
        TextView dateText = (TextView) rootView.findViewById(R.id.date_text);
        SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = new java.util.Date();
        dateCreated = ymd.format(date);
        dateText.setText(dateCreated);

        return rootView;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onFragmentInteraction(mParam1);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;

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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(long id);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.htipfragment_menu, menu);
    }
}
