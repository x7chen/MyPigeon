package com.pumelotech.dev.mypigeon;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pumelotech.dev.mypigeon.DataType.RecordInfo;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/10/9.
 */
public class RecordListAdapter extends BaseAdapter {
    private ArrayList<RecordInfo> mRecord;
    private LayoutInflater mInflator;

    public RecordListAdapter(Context context) {
        super();
        mRecord = new ArrayList<RecordInfo>();
        mInflator = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addRecord(RecordInfo recordInfo) {
        if(!mRecord.contains(recordInfo)) {
            mRecord.add(recordInfo);
        }
    }

    public RecordInfo getRecord(int position) {
        return mRecord.get(position);
    }

    public void clear() {
        mRecord.clear();
    }

    @Override
    public int getCount() {
        return mRecord.size();
    }

    @Override
    public Object getItem(int i) {
        return mRecord.get(i);
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
            view = mInflator.inflate(R.layout.listitem_record, null);
            viewHolder = new ViewHolder();
            viewHolder.Year = (TextView) view.findViewById(R.id.recorditem_year);
            viewHolder.Date = (TextView) view.findViewById(R.id.recorditem_month);
            viewHolder.Time = (TextView) view.findViewById(R.id.recorditem_time);
            viewHolder.Count = (TextView) view.findViewById(R.id.recorditem_count);
            viewHolder.Address = (TextView) view.findViewById(R.id.recorditem_address);
            viewHolder.Other = (TextView) view.findViewById(R.id.recorditem_other);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        RecordInfo recordInfo = mRecord.get(i);
//        viewHolder.Year.setText(recordInfo.getYear());
//        viewHolder.Date.setText(recordInfo.getDate());
//        viewHolder.Time.setText(recordInfo.getTime());
//        viewHolder.Count.setText(recordInfo.getCount());
        return view;
    }
    static class ViewHolder {
        TextView Year;
        TextView Date;
        TextView Count;
        TextView Address;
        TextView Time;
        TextView Other;
    }
}


