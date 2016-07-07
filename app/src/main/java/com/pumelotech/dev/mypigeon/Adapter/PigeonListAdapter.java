package com.pumelotech.dev.mypigeon.Adapter;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.pumelotech.dev.mypigeon.DataType.PigeonInfo;
import com.pumelotech.dev.mypigeon.MainActivity;
import com.pumelotech.dev.mypigeon.PigeonEditActivity;
import com.pumelotech.dev.mypigeon.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/10/9.
 */
public class PigeonListAdapter extends BaseAdapter {
    private ArrayList<PigeonInfo> mPigeon;
    private LayoutInflater mInflator;
    private Context mContext;

    public PigeonListAdapter(Context context) {
        super();
        mContext = context;
        mPigeon = new ArrayList<PigeonInfo>();
//        mInflator = fragment.getLayoutInflater();
        mInflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addPigeon(PigeonInfo pigeon) {
        if (!mPigeon.contains(pigeon)) {
            mPigeon.add(pigeon);
        }
    }

    public PigeonInfo getPigeon(int position) {
        return mPigeon.get(position);
    }

    public void clear() {
        mPigeon.clear();
    }

    @Override
    public int getCount() {
        return mPigeon.size();
    }

    @Override
    public Object getItem(int i) {
        return mPigeon.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        // General ListView optimization code.
        if (view == null) {
            //此处的绘制（inflate）最耗时，这也是viewholder存在的必要
            view = mInflator.inflate(R.layout.listitem_pigeon, null);
            viewHolder = new ViewHolder();
            viewHolder.PigeonID = (TextView) view.findViewById(R.id.pigeon_item_id);
            viewHolder.PigeonName = (TextView) view.findViewById(R.id.pigeon_item_name);
            viewHolder.PigeonStatus = (TextView) view.findViewById(R.id.pigeon_item_status);
            viewHolder.editButton = (Button) view.findViewById(R.id.pigeon_item_edit_button);
            viewHolder.flyButton = (Button) view.findViewById(R.id.pigeon_item_fly_button);
            Log.i(MainActivity.DebugTag, "getView:" + i);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        final PigeonInfo pigeon = mPigeon.get(i);
        final String pigeonName = pigeon.Name;
        if (pigeonName != null && pigeonName.length() > 0)
            viewHolder.PigeonName.setText(pigeonName);
        else
            viewHolder.PigeonName.setText(R.string.no_name);

        viewHolder.PigeonID.setText("ID:" + pigeon.ID);

        final String pigeonStatus = pigeon.Status;
        if (pigeonStatus != null && pigeonStatus.equals("FLY")) {
            viewHolder.PigeonStatus.setText("飞行中");
            viewHolder.flyButton.setEnabled(false);
        } else {
            viewHolder.PigeonStatus.setText("在棚");
            viewHolder.flyButton.setEnabled(true);
        }
        final Context context = mContext;
        viewHolder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, PigeonEditActivity.class));
            }
        });
        final ViewHolder viewHolder1 = viewHolder;
        viewHolder.flyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pigeon.Status = "FLY";
                viewHolder1.PigeonStatus.setText("飞行中");
                viewHolder1.flyButton.setEnabled(false);
            }
        });
        if ((pigeon.FlyEnable != null) && pigeon.FlyEnable.equals("Enable")) {
            viewHolder.flyButton.setVisibility(View.VISIBLE);
        } else {
            viewHolder.flyButton.setVisibility(View.INVISIBLE);
        }

        return view;
    }

    static class ViewHolder {
        TextView PigeonName;
        TextView PigeonID;
        TextView PigeonStatus;
        Button editButton;
        Button flyButton;
    }
}

