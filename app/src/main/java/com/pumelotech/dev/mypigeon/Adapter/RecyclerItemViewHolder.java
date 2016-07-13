package com.pumelotech.dev.mypigeon.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pumelotech.dev.mypigeon.DataType.PigeonInfo;
import com.pumelotech.dev.mypigeon.DataType.RecordInfo;
import com.pumelotech.dev.mypigeon.MyApplication;
import com.pumelotech.dev.mypigeon.PigeonEditActivity;
import com.pumelotech.dev.mypigeon.R;
import com.pumelotech.dev.mypigeon.RecordActivity;
import com.pumelotech.dev.mypigeon.MyPigeonDAO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RecyclerItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    TextView PigeonName;
    TextView PigeonID;
    TextView PigeonStatus;
    TextView FlyTimes;
    TextView TotalDistance;
    TextView TotalTime;
    TextView BirthDate;
    Button editButton;
    Button flyButton;

    public RecyclerItemViewHolder(final View parent) {
        super(parent);
        PigeonName = (TextView) parent.findViewById(R.id.pigeon_item_name);
        PigeonID = (TextView) parent.findViewById(R.id.pigeon_item_id);
        PigeonStatus = (TextView) parent.findViewById(R.id.pigeon_item_status);
        FlyTimes = (TextView) parent.findViewById(R.id.pigeon_item_fly_times);
        TotalTime = (TextView) parent.findViewById(R.id.pigeon_item_total_time);
        TotalDistance = (TextView) parent.findViewById(R.id.pigeon_item_total_distance);
        BirthDate= (TextView) parent.findViewById(R.id.pigeon_item_birth_date);
        editButton = (Button) parent.findViewById(R.id.pigeon_item_edit_button);
        flyButton = (Button) parent.findViewById(R.id.pigeon_item_fly_button);
        parent.setOnClickListener(this);
        parent.setOnLongClickListener(this);
        final Context context = MyApplication.pigeonListActivity;
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PigeonInfo pigeon = MyApplication.mPigeonList.get(getLayoutPosition() - 1);
                Intent intent = new Intent(context, PigeonEditActivity.class);
                intent.putExtra("Name",pigeon.Name);
                intent.putExtra("ID",pigeon.ID);
                intent.putExtra("BirthDate",pigeon.BirthDate);
                intent.putExtra("ShedID",pigeon.ShedID);
                context.startActivity(intent);
            }
        });
        flyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PigeonInfo pigeon = MyApplication.mPigeonList.get(getLayoutPosition() - 1);
                MyPigeonDAO myPigeonDAO = MyPigeonDAO.getInstance();
                if(pigeon.Status!=null&&pigeon.Status.equals("FLY")){
                    pigeon.Status = "REST";
                    PigeonStatus.setText("在棚");

                    if (myPigeonDAO != null) {
                        int index = myPigeonDAO.getActiveRecordIndex(pigeon.ID);
                        RecordInfo record = myPigeonDAO.getRecord(index);
                        Calendar calendar = Calendar.getInstance();
                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH);
                        int day = calendar.get(Calendar.DAY_OF_MONTH);
                        int hour = calendar.get(Calendar.HOUR_OF_DAY);
                        int minute = calendar.get(Calendar.MINUTE);
                        int second = calendar.get(Calendar.SECOND);
                        record.PigeonID = pigeon.ID;
                        record.ArriveTime = year + "年" + (month + 1) + "月" + day + "日" + hour + "时" + minute + "分" + second + "秒";
                        record.ArriveShedID = "C000003";
                        record.Status = "REST";
                        myPigeonDAO.updateRecord(index,record);
                    }
                }else {
                    pigeon.Status = "FLY";
                    PigeonStatus.setText("飞行中");
//                    flyButton.setEnabled(false);
                    if (myPigeonDAO != null) {
                        myPigeonDAO.updatePigeon(myPigeonDAO.getPigeonIndex(pigeon.ID), pigeon);
                        RecordInfo record = new RecordInfo();
                        Calendar calendar = Calendar.getInstance();
                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH);
                        int day = calendar.get(Calendar.DAY_OF_MONTH);
                        int hour = calendar.get(Calendar.HOUR_OF_DAY);
                        int minute = calendar.get(Calendar.MINUTE);
                        int second = calendar.get(Calendar.SECOND);
                        record.PigeonID = pigeon.ID;
                        record.StartTime = year + "年" + (month + 1) + "月" + day + "日" + hour + "时" + minute + "分" + second + "秒";
                        record.StartShedID = "C000001";
                        record.Status = "FLY";
                        myPigeonDAO.insertRecord(record);
                    }
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        if (MyApplication.PigeonListItemClickable) {
            final PigeonInfo pigeon = MyApplication.mPigeonList.get(getLayoutPosition() - 1);
            if (pigeon == null) return;
            Intent intent = new Intent(MyApplication.pigeonListActivity, RecordActivity.class);
            intent.putExtra("Name", pigeon.Name);
            intent.putExtra("ID",pigeon.ID);
            MyApplication.pigeonListActivity.startActivity(intent);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }
}
