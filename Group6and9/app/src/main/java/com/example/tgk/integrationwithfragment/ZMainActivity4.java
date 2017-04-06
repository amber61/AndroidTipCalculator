package com.example.tgk.integrationwithfragment;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


public class ZMainActivity4 extends AppCompatActivity implements ZContactList.OnContactSelectedListener, ZContactList.OnAddContactClickListener,
        ZContactAdd.OnAddClickListener,ZContactDetail.OnDeleteClickListener{
    private ZContactDbAdapter dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zactivity_main_four);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        dbHelper = new ZContactDbAdapter(this);
        dbHelper.open();

        // Check whether the activity is using the layout version with
        // the fragment_container FrameLayout. If so, we must add the first fragment
        if (findViewById(R.id.fragment_window) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create an instance of ZContactList Fragment
            ZContactList contactListFragment = new ZContactList();

            // In case this activity was started with special instructions from an Intent,
            // pass the Intent's extras to the fragment as arguments
            contactListFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_window, contactListFragment).commit();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        switch (id){
            case R.id.action_one:
                go(HMainActivity.class);
                break;
            case R.id.action_two:
                go(MainActivity2.class);
                break;
            case R.id.action_three:
                go(MainActivity3.class);
                break;
            case R.id.action_four:
                go(ZMainActivity4.class);
                break;
            case R.id.menu_contact_help:
                onHelpClick();
                break;
            case R.id.menu_contact_list:
                go(ZMainActivity4.class);
                break;
            case R.id.menu_contact_add:
                onAddContactClick();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    private void go(Class c){
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }

    public void onHelpClick(){
        ZContactHelp helpFragment = new ZContactHelp();
        helpFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_window, helpFragment).commit();
    }

    @Override
    public void onAddContactClick(){
        // Create fragment
        ZContactAdd addContactFrag = new ZContactAdd();
        Bundle args = new Bundle();

        addContactFrag.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_window, addContactFrag);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }


    @Override
    public void onContactSelected(long id){

        // Create fragment and give it an argument for the selected contact
        //ZContactDetail contactDetailFrag = ZContactDetail.newInstance(id);
        ZContactDetail contactDetailFrag = new ZContactDetail();
        Bundle args = new Bundle();
        args.putLong(ZContactDetail.ARG_ID, id);
        contactDetailFrag.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_window, contactDetailFrag);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    @Override
    public void onAddClick(String firstname, String lastname,String phone, String email,String note){
        dbHelper.createContact(firstname, lastname, phone, email, note);
        go(ZMainActivity4.class);
    }

    @Override
    public void onDeleteClick(long id){
        dbHelper.deleteContactById(id);
        go(ZMainActivity4.class);

    }
}
