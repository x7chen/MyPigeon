package com.pumelotech.dev.mypigeon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.pumelotech.dev.mypigeon.DataType.PigeonInfo;

public class PigeonEditActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pigeon_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_setting);
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
        final EditText pigeon_birthdate = (EditText) findViewById(R.id.pigeon_edit_birth_date);
        final EditText shed_id = (EditText) findViewById(R.id.pigeon_edit_shed);
        switch (id) {
            case R.id.menu_done:
                PigeonInfo pigeonInfo = new PigeonInfo();
                pigeonInfo.Name = pigeon_name.getText().toString();
                pigeonInfo.ID = pigeon_id.getText().toString();
                pigeonInfo.ShedID = shed_id.getText().toString();
                pigeonInfo.BirthDate = pigeon_birthdate.getText().toString();
                MyPigeonDAO myPigeonDAO = MyPigeonDAO.getInstance();
                if (myPigeonDAO != null) {
                    myPigeonDAO.insertPigeon(pigeonInfo);
                }
                MyApplication.getPigeonListAdapter().addPigeon(pigeonInfo);
                MyApplication.getPigeonListAdapter().notifyDataSetChanged();
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
