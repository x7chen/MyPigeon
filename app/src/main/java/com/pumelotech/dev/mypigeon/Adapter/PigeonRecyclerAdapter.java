package com.pumelotech.dev.mypigeon.Adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pumelotech.dev.mypigeon.DataType.PigeonInfo;
import com.pumelotech.dev.mypigeon.MyApplication;
import com.pumelotech.dev.mypigeon.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/10/9.
 */
public class PigeonRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<PigeonInfo> mPigeonList;
    private LayoutInflater mInflator;
    private Context mContext;
    private static final int TYPE_HEADER = 2;
    private static final int TYPE_ITEM = 1;

    public PigeonRecyclerAdapter(List<PigeonInfo> list) {
        super();
        mPigeonList = (ArrayList<PigeonInfo>) list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        if (viewType == TYPE_ITEM) {
            final View view = LayoutInflater.from(context).inflate(R.layout.listitem_pigeon, parent, false);
            return new RecyclerItemViewHolder(view);
        } else if (viewType == TYPE_HEADER) {
            final View view = LayoutInflater.from(context).inflate(R.layout.recycler_header, parent, false);
            return new RecyclerHeaderViewHolder(view);
        }
        throw new RuntimeException("There is no type that matches the type " + viewType + " + make sure your using types correctly");

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RecyclerItemViewHolder viewHolder;
        if (!isPositionHeader(position)) {
            viewHolder = (RecyclerItemViewHolder) holder;
        } else {
            return;
        }
        if (holder == null) {
            return;
        }
        final PigeonInfo pigeon = mPigeonList.get(position - 1);
        final String pigeonName = pigeon.Name;
        viewHolder.PigeonName.setText(pigeonName);
        viewHolder.PigeonID.setText("ID:" + pigeon.ID);
        viewHolder.FlyTimes.setText(Integer.toString(pigeon.FlyTimes));
        viewHolder.TotalTime.setText(pigeon.TotalMinutes / 60 + "时" + pigeon.TotalMinutes % 60 + "分");
        viewHolder.TotalDistance.setText(pigeon.TotalDistance/1000 + "Km");
        viewHolder.BirthDate.setText(pigeon.BirthDate);
        final String pigeonStatus = pigeon.Status;
        if (pigeonStatus.equals("FLY")) {
            viewHolder.PigeonStatus.setText("飞行中");
            viewHolder.flyButton.setText("到达");
            //viewHolder.flyButton.setEnabled(false);
        } else {
            viewHolder.PigeonStatus.setText("在棚");
            //viewHolder.flyButton.setEnabled(true);
        }

        if (MyApplication.FlyEnable) {
            viewHolder.flyButton.setVisibility(View.VISIBLE);
        } else {
            viewHolder.flyButton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    //our old getItemCount()
    public int getBasicItemCount() {
        return mPigeonList == null ? 0 : mPigeonList.size();
    }

    //our new getItemCount() that includes header View
    @Override
    public int getItemCount() {
        return getBasicItemCount() + 1; // header
    }

    //added a method that returns viewType for a given position
    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    //added a method to check if given position is a header
    private boolean isPositionHeader(int position) {
        return position == 0;
    }
}

