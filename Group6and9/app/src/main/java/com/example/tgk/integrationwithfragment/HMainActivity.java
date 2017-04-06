package com.example.tgk.integrationwithfragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

/**
 * MainActivity of the first mini app--Tip Record
 * 
 * @author Zhe Huang created on 4/18/2016.
 * @version 1.0.0
 * @see android.content.Intent
 * @see android.support.v4.app.Fragment
 * @see android.support.v4.app.FragmentTransaction
 * @see android.support.v7.app.AppCompatActivity
 * @see android.os.Bundle
 * @see android.support.v7.widget.Toolbar
 * @see android.view.Menu
 * @see android.view.MenuItem
 * @since 1.8.0_73 
 */
public class HMainActivity extends AppCompatActivity  implements HTipRecordAdd.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.htipactivity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // Check whether the activity is using the layout version with
        // the fragment_container FrameLayout. If so, we must add the first fragment
        if (findViewById(R.id.fragment_window) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create an instance of ExampleFragment
            HTipRecordAdd firstFragment = new HTipRecordAdd();

            // In case this activity was started with special instructions from an Intent,
            // pass the Intent's extras to the fragment as arguments
            firstFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_window' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_window, firstFragment).commit();
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
        switch (id) {
            case R.id.action_one:
                //go(HMainActivity.class);
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
            case R.id.fragment_menu_item:
                doHelp();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void go(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }

    public void doHelp() {
        HMainHelp newFragment = new HMainHelp();
        Bundle args = new Bundle();
        newFragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_window, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(long id) {
        HTipRecordAllList newFragment = new HTipRecordAllList();
        Bundle args = new Bundle();
        args.putLong(HTipRecordAllList.ARG_PARAM1, id);
        newFragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_window view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_window, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub

        Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment_window);
        if (f instanceof HTipRecordAdd == false)
            super.onBackPressed();
    }
}
