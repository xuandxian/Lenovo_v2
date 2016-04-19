package com.overtech.lenovo.widget.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.overtech.lenovo.R;
import com.overtech.lenovo.activity.MainActivity;
import com.overtech.lenovo.activity.business.tasklist.TaskDetailActivity;

/**
 * Created by Overtech on 16/4/18.
 * 用于预约的对话框管理
 */
public class WorkorderAppointDialog extends DialogFragment {
    private DatePicker mDatePicker;
    private TimePicker mTimePicker;
    public static final int MAIN_ACTIVITY = 0;
    public static final int DETAIL_ACTIVITY = 1;
    private static int type;
    private static int position;

    public static WorkorderAppointDialog newInstance(int t,int p) {
        type = t;
        position=p;
        Bundle arg = new Bundle();
        WorkorderAppointDialog fragment = new WorkorderAppointDialog();
        fragment.setArguments(arg);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.fragment_dialog_workorder_appoint, null);
        mDatePicker= (DatePicker) v.findViewById(R.id.dialog_appoint_datepicker);
        mTimePicker= (TimePicker) v.findViewById(R.id.dialog_appoint_timepicker);
        mTimePicker.setIs24HourView(true);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
                .setView(v)
                .setTitle("请选择您的上门时间")
                .setNegativeButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int year=mDatePicker.getYear();
                        int month=mDatePicker.getMonth()+1;//月份是从0月开始
                        int day=mDatePicker.getDayOfMonth();
                        int hour=mTimePicker.getCurrentHour();
                        int minute=mTimePicker.getCurrentMinute();

                        if (type == WorkorderAppointDialog.MAIN_ACTIVITY) {
                            ((MainActivity) getActivity()).taskListFragment.doAppointNegativeClick(position,year+"-"+month+"-"+day+" "+hour+":"+minute+":"+"00");
                        } else {
                            ((TaskDetailActivity) getActivity()).taskInfoFrag.doAppointNegativeClick(position,year+"-"+month+"-"+day+" "+hour+":"+minute+":"+"00");
                        }
                    }
                })
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (type == WorkorderAppointDialog.MAIN_ACTIVITY) {
                            ((MainActivity) getActivity()).taskListFragment.doAppointPositiveClick(position);
                        } else {
                            ((TaskDetailActivity) getActivity()).taskInfoFrag.doAppointPositiveClick(position);
                        }
                    }
                });

        return builder.create();
    }
}
