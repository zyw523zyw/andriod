package assignment1.eventplan.widgets;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatDialog;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;

import assignment1.eventplan.R;

/**
 * Created by yumizhang on 16/9/27.
 */
public final class DateTimePicker implements DialogInterface.OnClickListener {

    public static final int CANCEL = -1;

    public interface Callback {
        /**
         * //TODO 时间回调,回调后需要判断是否是取消操作
         *
         * @param timeMillis {@link #CANCEL}
         */
        void onTimeCallback(long timeMillis);
    }

    private DatePickerDialogCompat datePicker;
    private TimePickerDialogCompat timePicker;
    private Calendar calendar;

    private DateTimePicker.Callback callback;

    public DateTimePicker(Context context) {
        calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        datePicker = new DatePickerDialogCompat(context).setListener(this);
        timePicker = new TimePickerDialogCompat(context).setListener(this);

        final DialogInterface.OnCancelListener cancelListener = new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                if (null != callback) {// TODO: 2016/9/27 当取消之后,认为是放弃操作
                    callback.onTimeCallback(CANCEL);
                }
            }
        };
        datePicker.setOnCancelListener(cancelListener);
        timePicker.setOnCancelListener(cancelListener);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        DatePickerDialogCompat datePicker = this.datePicker;
        if (dialog == datePicker) {
            calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
            timePicker.show(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
            return;
        }
        calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
        calendar.set(Calendar.MINUTE, timePicker.getMinute());
        if (null != callback) {
            callback.onTimeCallback(calendar.getTimeInMillis());
        }
    }


    public void setTimeMillis(long millis) {
        calendar.setTimeInMillis(millis);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    public void show() {
        datePicker.show(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }


    private static class DatePickerDialogCompat extends AppCompatDialog implements View.OnClickListener {

        DatePicker picker;
        private Dialog.OnClickListener listener;

        DatePickerDialogCompat(Context context) {
            super(context);
            supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.view_date_picker);
            picker = (DatePicker) findViewById(R.id.date_picker);
            findViewById(R.id.submit_button).setOnClickListener(this);
        }


        void show(int year, int monthOfYear, int dayOfMonth) {
            picker.init(year, monthOfYear, dayOfMonth, null);
            show();
        }


        @Override
        public void onClick(View v) {
            if (null != listener) {
                listener.onClick(this, DialogInterface.BUTTON_POSITIVE);
            }
            dismiss();
        }

        DatePickerDialogCompat setListener(DialogInterface.OnClickListener listener) {
            this.listener = listener;
            return this;
        }

        int getYear() {
            return picker.getYear();
        }

        int getMonth() {
            return picker.getMonth();
        }

        int getDayOfMonth() {
            return picker.getDayOfMonth();
        }
    }

    private static class TimePickerDialogCompat extends AppCompatDialog implements View.OnClickListener {

        TimePicker picker;
        private Dialog.OnClickListener listener;

        TimePickerDialogCompat(Context context) {
            super(context);
            supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.view_time_picker);
            picker = (TimePicker) findViewById(R.id.time_picker);
            picker.setIs24HourView(true);
            findViewById(R.id.submit_button).setOnClickListener(this);
        }


        void show(int hour, int minute) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                picker.setHour(hour);
                picker.setMinute(minute);
            } else {
                picker.setCurrentHour(hour);
                picker.setCurrentMinute(minute);
            }
            show();
        }


        @Override
        public void onClick(View v) {
            if (null != listener) {
                listener.onClick(this, DialogInterface.BUTTON_POSITIVE);
            }
            dismiss();
        }

        TimePickerDialogCompat setListener(DialogInterface.OnClickListener listener) {
            this.listener = listener;
            return this;
        }

        int getHour() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return picker.getHour();
            } else {
                return picker.getCurrentHour();
            }
        }

        int getMinute() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return picker.getMinute();
            } else {
                return picker.getCurrentMinute();
            }
        }

    }

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }
}
