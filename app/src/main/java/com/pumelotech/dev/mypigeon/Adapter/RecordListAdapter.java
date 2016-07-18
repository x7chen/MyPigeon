package com.pumelotech.dev.mypigeon.Adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pumelotech.dev.mypigeon.DataType.RecordInfo;
import com.pumelotech.dev.mypigeon.R;

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
        mInflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addRecord(RecordInfo record) {
        if (!mRecord.contains(record)) {
            mRecord.add(record);
        }
    }
    public void setRecord(RecordInfo record) {
        for (RecordInfo recordInfo : mRecord) {
            if ("FLY".equals(recordInfo.Status)) {
                int record_index = mRecord.indexOf(recordInfo);
                mRecord.set(record_index, record);
            }
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
            viewHolder.StartDate = (TextView) view.findViewById(R.id.record_item_start_date);
            viewHolder.StartTime= (TextView) view.findViewById(R.id.record_item_start_time);
            viewHolder.StartAddress= (TextView) view.findViewById(R.id.record_item_start_address);
            viewHolder.ArriveDate= (TextView) view.findViewById(R.id.record_item_arrive_date);
            viewHolder.ArriveTime= (TextView) view.findViewById(R.id.record_item_arrive_time);
            viewHolder.ArriveAddress= (TextView) view.findViewById(R.id.record_item_arrive_address);
            viewHolder.Distance= (TextView) view.findViewById(R.id.record_item_distance);
            viewHolder.ElapsedTime= (TextView) view.findViewById(R.id.record_item_elapsed_time);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        RecordInfo recordInfo = mRecord.get(i);
        if (recordInfo.StartTime != null && recordInfo.StartTime.length() > 10) {
            viewHolder.StartDate.setText(recordInfo.StartTime.substring(0, 10));
            viewHolder.StartTime.setText(recordInfo.StartTime.substring(10));
            viewHolder.StartAddress.setText(recordInfo.StartShedID);
        } else {
            viewHolder.StartDate.setText("--");
            viewHolder.StartTime.setText("--");
            viewHolder.StartAddress.setText("--");

        }
        if (recordInfo.ArriveTime != null && recordInfo.ArriveTime.length() > 10) {
            viewHolder.ArriveDate.setText(recordInfo.ArriveTime.substring(0, 10));
            viewHolder.ArriveTime.setText(recordInfo.ArriveTime.substring(10));
            viewHolder.ArriveAddress.setText(recordInfo.ArriveShedID);
            viewHolder.Distance.setText(recordInfo.DistanceMeter/1000+"Km");
            viewHolder.ElapsedTime.setText(recordInfo.ElapsedMinutes/60+"时"+recordInfo.ElapsedMinutes%60+"分");
        } else {
            viewHolder.ArriveDate.setText("--");
            viewHolder.ArriveTime.setText("--");
            viewHolder.ArriveAddress.setText("--");
            viewHolder.Distance.setText("飞行中");
            viewHolder.ElapsedTime.setText("--");
        }

        return view;
    }

    static class ViewHolder {
        TextView StartDate;
        TextView StartTime;
        TextView StartAddress;
        TextView ArriveDate;
        TextView ArriveTime;
        TextView ArriveAddress;
        TextView Distance;
        TextView ElapsedTime;
    }
}


