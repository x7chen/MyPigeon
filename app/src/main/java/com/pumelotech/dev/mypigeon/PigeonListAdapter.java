package com.pumelotech.dev.mypigeon;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/10/9.
 */
public class PigeonListAdapter extends BaseAdapter {
    private ArrayList<PigeonInfo> mPigeon;
    private LayoutInflater mInflator;

    public PigeonListAdapter(Context context) {
        super();
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
            view = mInflator.inflate(R.layout.listitem_pigeon, null);
            viewHolder = new ViewHolder();
            viewHolder.PigeonID = (TextView) view.findViewById(R.id.pigeon_id);
            viewHolder.PigeonName = (TextView) view.findViewById(R.id.pigeon_name);
            viewHolder.Icon = (ImageButton) view.findViewById(R.id.imageButton);
            viewHolder.Check = (CheckBox) view.findViewById(R.id.checkBox);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        PigeonInfo pigeon = mPigeon.get(i);
        final String pigeonName = pigeon.getName();
        if (pigeonName != null && pigeonName.length() > 0)
            viewHolder.PigeonName.setText(pigeonName);
        else
            viewHolder.PigeonName.setText(R.string.no_name);
        viewHolder.PigeonID.setText(pigeon.getID());
        final String pigeonStatus = pigeon.getStatus();
        if (pigeonStatus != null && pigeonStatus.equals("FLY")) {
            viewHolder.Icon.setEnabled(true);
            viewHolder.Check.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.Icon.setEnabled(false);
            viewHolder.Check.setVisibility(View.VISIBLE);
        }
        return view;
    }

    static class ViewHolder {
        TextView PigeonName;
        TextView PigeonID;
        ImageButton Icon;
        CheckBox Check;
    }
}

