package com.pumelotech.dev.mypigeon;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, PigeonListFragment.OnFragmentInteractionListener {

    static final String DebugTag = "MyPigeon";
    private MainPageFragment mainPageFragment;
    private PigeonListFragment pigeonListFragment;
    private RecordFragment recordFragment;
    private ManageFragment manageFragment;
    private HelpFragment helpFragment;
    private PacketParser mPacketParser;
    ApplicationContextHelper applicationContextHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // 设置默认的Fragment
        setDefaultFragment();

        mPacketParser = PacketParser.getInstance(this);
        MyPigeonDAO.getInstance(this);
    }
    private void setDefaultFragment() {
        setMainPageFragment();
    }

    private void setMainPageFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        mainPageFragment = new MainPageFragment();
        transaction.replace(R.id.main_fragment_container, mainPageFragment);
        transaction.commit();
    }

    private void setPigeonListFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        pigeonListFragment = new PigeonListFragment();
        transaction.replace(R.id.main_fragment_container, pigeonListFragment);
        transaction.commit();
    }
    private void setRecordFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        recordFragment = new RecordFragment();
        transaction.replace(R.id.main_fragment_container, recordFragment);
        transaction.commit();
    }
    private void setInputFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        manageFragment = new ManageFragment();
        transaction.replace(R.id.main_fragment_container, manageFragment);
        transaction.commit();
    }

    private void setHelpFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        helpFragment = new HelpFragment();
        transaction.replace(R.id.main_fragment_container, helpFragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    boolean Toggle;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if(id==R.id.menu_fly){

            Toggle = !Toggle;
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.buttombar);
            if(Toggle)
            linearLayout.setVisibility(View.GONE);
            else
                linearLayout.setVisibility(View.VISIBLE);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_pigeon) {
            setMainPageFragment();
        } else if (id == R.id.nav_fly) {
            setPigeonListFragment();
        } else if (id == R.id.nav_record) {
            setRecordFragment();
        } else if (id == R.id.nav_input) {
            setInputFragment();
        } else if (id == R.id.nav_help) {
            setHelpFragment();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
