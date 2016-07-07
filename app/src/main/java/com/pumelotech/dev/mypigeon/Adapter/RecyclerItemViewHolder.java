package com.pumelotech.dev.mypigeon.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pumelotech.dev.mypigeon.DataType.PigeonInfo;
import com.pumelotech.dev.mypigeon.MyApplication;
import com.pumelotech.dev.mypigeon.PigeonEditActivity;
import com.pumelotech.dev.mypigeon.R;
import com.pumelotech.dev.mypigeon.RecordActivity;

public class RecyclerItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    TextView PigeonName;
    TextView PigeonID;
    TextView PigeonStatus;
    Button editButton;
    Button flyButton;

    public RecyclerItemViewHolder(View parent) {
        super(parent);
        PigeonName = (TextView) parent.findViewById(R.id.pigeon_item_name);
        PigeonID = (TextView) parent.findViewById(R.id.pigeon_item_id);
        PigeonStatus = (TextView) parent.findViewById(R.id.pigeon_item_status);
        editButton = (Button) parent.findViewById(R.id.pigeon_item_edit_button);
        flyButton = (Button) parent.findViewById(R.id.pigeon_item_fly_button);
        parent.setOnClickListener(this);
        parent.setOnLongClickListener(this);
        final Context context = MyApplication.pigeonListActivity;
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, PigeonEditActivity.class));
            }
        });
        flyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PigeonInfo pigeon = MyApplication.mPigeonList.get(getLayoutPosition()-1);
                pigeon.Status = "FLY";
                PigeonStatus.setText("飞行中");
                flyButton.setEnabled(false);
                Log.i(MyApplication.DebugTag, "getPosition():" + getPosition());
                Log.i(MyApplication.DebugTag, "getAdapterPosition():" + getAdapterPosition());
                Log.i(MyApplication.DebugTag, "getItemId():" + getItemId());
                Log.i(MyApplication.DebugTag, "getLayoutPosition():" + getLayoutPosition());
                Log.i(MyApplication.DebugTag, "getOldPosition():" + getOldPosition());
            }
        });


    }


    @Override
    public void onClick(View v) {
        final PigeonInfo pigeon = MyApplication.mPigeonList.get(getLayoutPosition()-1);
        if (pigeon == null) return;
        Intent intent = new Intent(MyApplication.pigeonListActivity, RecordActivity.class);
        intent.putExtra("Name", pigeon.Name);
        MyApplication.pigeonListActivity.startActivity(intent);
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }
}
