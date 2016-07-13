package com.pumelotech.dev.mypigeon;

import android.app.*;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.pumelotech.dev.mypigeon.DataType.PigeonInfo;

public class PigeonEditActivity extends AppCompatActivity {

    EditText pigeon_name;
    EditText pigeon_id;
    EditText pigeon_birth_date;
    EditText shed_id;
    Boolean is_modify = false;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pigeon_edit);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_pigeon_edit);
        mToolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        mToolbar.setTitle("编辑");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        pigeon_name = (EditText) findViewById(R.id.pigeon_edit_name);
        pigeon_id = (EditText) findViewById(R.id.pigeon_edit_id);
        pigeon_birth_date = (EditText) findViewById(R.id.pigeon_edit_birth_date);
        shed_id = (EditText) findViewById(R.id.pigeon_edit_shed);

        Intent intent = getIntent();
        String name = intent.getStringExtra("Name");
        String id = intent.getStringExtra("ID");
        String birth_date = intent.getStringExtra("BirthDate");
        String shed = intent.getStringExtra("ShedID");
        if (id != null) {
            pigeon_name.setText(name);
            pigeon_id.setText(id);
            pigeon_birth_date.setText(birth_date);
            shed_id.setText(shed);
            is_modify = true;
        } else {
            is_modify = false;
        }
        pigeon_birth_date.setClickable(true);
        pigeon_birth_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.DatePickerDialog dialog = new DatePickerDialog(PigeonEditActivity.this, onDateSetListener, 2016, 7, 1);
                dialog.show();
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            pigeon_birth_date.setText(year + "年" + monthOfYear + "月" + dayOfMonth + "日");
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.done, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_done:
                PigeonInfo pigeonInfo = new PigeonInfo();
                pigeonInfo.Name = pigeon_name.getText().toString();
                pigeonInfo.ID = pigeon_id.getText().toString();
                if (pigeonInfo.ID.matches("^\\d{16}$")) {
                    pigeonInfo.ShedID = shed_id.getText().toString();
                } else {
                    Toast.makeText(this, "ID输入有误,必须为16位数字", Toast.LENGTH_SHORT).show();
                    break;
                }
                pigeonInfo.BirthDate = pigeon_birth_date.getText().toString();

                if (is_modify) {
                    MyPigeonDAO myPigeonDAO = MyPigeonDAO.getInstance();
                    if (myPigeonDAO != null) {
                        myPigeonDAO.updatePigeon(myPigeonDAO.getPigeonIndex(pigeonInfo.ID),pigeonInfo);
                    }
                    if (MyApplication.mPigeonList == null) {
                        Log.i(MyApplication.DebugTag, "MyApplication.mPigeonList = null");
                    } else {
                        for(PigeonInfo pigeon:MyApplication.mPigeonList){
                            if(pigeon.ID.equals(pigeonInfo.ID)){
                                MyApplication.mPigeonList.set(MyApplication.mPigeonList.indexOf(pigeon),pigeonInfo);
                            }
                        }
                        MyApplication.pigeonRecyclerAdapter.notifyDataSetChanged();
                    }
                } else {
                    MyPigeonDAO myPigeonDAO = MyPigeonDAO.getInstance();
                    if (myPigeonDAO != null) {
                        myPigeonDAO.insertPigeon(pigeonInfo);
                    }
                    if (MyApplication.mPigeonList == null) {
                        Log.i(MyApplication.DebugTag, "MyApplication.mPigeonList = null");
                    } else {
                        MyApplication.mPigeonList.add(pigeonInfo);
                        MyApplication.pigeonRecyclerAdapter.notifyDataSetChanged();
                    }
                }
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "PigeonEdit Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.pumelotech.dev.mypigeon/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "PigeonEdit Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.pumelotech.dev.mypigeon/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
