package com.pumelotech.dev.mypigeon;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.pumelotech.dev.mypigeon.DataType.PigeonInfo;

public class PigeonEditActivity extends AppCompatActivity {


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

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.done, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        final EditText pigeon_name = (EditText) findViewById(R.id.pigeon_edit_name);
        final EditText pigeon_id = (EditText) findViewById(R.id.pigeon_edit_id);
        final EditText pigeon_birth_date = (EditText) findViewById(R.id.pigeon_edit_birth_date);
        final EditText shed_id = (EditText) findViewById(R.id.pigeon_edit_shed);
        switch (id) {
            case R.id.menu_done:
                PigeonInfo pigeonInfo = new PigeonInfo();
                pigeonInfo.Name = pigeon_name.getText().toString();
                pigeonInfo.ID = pigeon_id.getText().toString();
                pigeonInfo.ShedID = shed_id.getText().toString();
                pigeonInfo.BirthDate = pigeon_birth_date.getText().toString();
                MyPigeonDAO myPigeonDAO = MyPigeonDAO.getInstance();
                if (myPigeonDAO != null) {
                    myPigeonDAO.insertPigeon(pigeonInfo);
                }
                if (MyApplication.mPigeonList == null) {
                    Log.i(MyApplication.DebugTag, "MyApplication.mPigeonList = null");
                } else {
                    MyApplication.mPigeonList.add(pigeonInfo);
                    MyApplication.getPigeonRecyclerAdapter().notifyDataSetChanged();
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
