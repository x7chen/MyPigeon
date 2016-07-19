package com.pumelotech.dev.mypigeon.Adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
        BirthDate = (TextView) parent.findViewById(R.id.pigeon_item_birth_date);
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
                intent.putExtra("pigeon",pigeon);
                context.startActivity(intent);
            }
        });
        flyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PigeonInfo pigeon = MyApplication.mPigeonList.get(getLayoutPosition() - 1);
                MyPigeonDAO myPigeonDAO = MyPigeonDAO.getInstance();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                if (pigeon.Status != null && pigeon.Status.equals("FLY")) {
                    pigeon.Status = "REST";
                    PigeonStatus.setText("在棚");
                    flyButton.setText("放飞");
                    if (myPigeonDAO != null) {
                        int index = myPigeonDAO.getActiveRecordIndex(pigeon.ID);
                        RecordInfo record = myPigeonDAO.getRecord(index);
                        Date start_time = new Date();
                        Date arrive_time = new Date();
                        record.PigeonID = pigeon.ID;
                        record.ArriveTime = format.format(arrive_time);
                        try {
                            start_time = format.parse(record.StartTime);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        long between_minutes = (arrive_time.getTime() - start_time.getTime())/60000;
                        record.ElapsedMinutes = (int)between_minutes;
                        record.DistanceMeter = 100*1000;
                        record.ArriveShedID = "C000003";
                        record.Status = "REST";
                        pigeon.FlyTimes = pigeon.FlyTimes + 1;
                        pigeon.TotalDistance = pigeon.TotalDistance+record.DistanceMeter;
                        pigeon.TotalMinutes = pigeon.TotalMinutes+record.ElapsedMinutes;
                        myPigeonDAO.updatePigeon(myPigeonDAO.getPigeonIndex(pigeon.ID),pigeon);
                        myPigeonDAO.updateRecord(index, record);

//                        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//                        Ringtone r = RingtoneManager.getRingtone(MyApplication.context, notification);
//                        r.play();
                    }
                } else {
                    pigeon.Status = "FLY";
                    PigeonStatus.setText("飞行中");
//                    flyButton.setEnabled(false);
                    flyButton.setText("到达");
                    if (myPigeonDAO != null) {
                        myPigeonDAO.updatePigeon(myPigeonDAO.getPigeonIndex(pigeon.ID), pigeon);
                        RecordInfo record = new RecordInfo();
                        record.PigeonID = pigeon.ID;
                        record.StartTime = format.format(new Date());
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
            intent.putExtra("pigeon", pigeon);
            MyApplication.pigeonListActivity.startActivity(intent);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }
}
