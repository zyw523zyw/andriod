package assignment1.eventplan;

/**
 * Created by yumizhang on 16/8/24.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import assignment1.eventplan.db.master.EventPlanProvider;

public class GVCalendar extends GridView {
    Context context;

    //a calendar of today
    private Calendar calToday;
    //this GridView
    GVCalendarAdapter adapter;
    //items of GridView
    private List<GVCalendarItem> items = new ArrayList<GVCalendarItem>();

    public List<GVCalendarItem> getItems() {
        return items;
    }

    public void setItems(List<GVCalendarItem> items) {
        this.items = items;
    }

    //the showing year, month and day
    private int year;
    //this month is between 1 and 12
    private int month;
    private int day;

    private OnCalenderItemCilckListener listener;

    public String getTitle() {
        //you can custom the format
        return year + "/" + month;
    }

    //this will mark the calendar if finish load
    private boolean isLoadDone;

    public boolean isLoadDone() {
        return isLoadDone;
    }

    public GVCalendar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    public GVCalendar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listener != null) {
                    listener.onItemClick(parent, view, position, id, year + "-" + String.format(Locale.getDefault(), "%02d", month) + "-" + items.get(position).getDateString());
                }
            }
        });
    }

    public GVCalendar(Context context) {
        super(context);
        this.context = context;
    }

    public void setListener(OnCalenderItemCilckListener listener) {
        this.listener = listener;
    }

    public interface OnCalenderItemCilckListener {
        void onItemClick(AdapterView<?> parent, View view, int position, long id, String date);
    }

    /**
     * use this method to init the calendar
     */
    public void initCalendar() {
        initCurrentTime();
        initCalendarItems(year, month, day);
        this.adapter = new GVCalendarAdapter(context, items);
        this.setAdapter(adapter);
    }

    /**
     * use this method to refresh
     */
    public void refreshCalendar() {
        this.adapter.notifyDataSetChanged();
    }

    private void initCurrentTime() {
        calToday = Calendar.getInstance();
        year = calToday.get(Calendar.YEAR);
        month = calToday.get(Calendar.MONTH) + 1;
        day = calToday.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * refresh the calendar
     *
     * @param year  the year on showing
     * @param month the month on showing
     * @param day   the day will mark if is today
     */
    private void initCalendarItems(int year, int month, int day) {
        this.items.clear();
        this.isLoadDone = false;

        int days = getDays(year, month);
        int predays;
        if (month == 1) {
            predays = getDays(year - 1, 12);
        } else {
            predays = getDays(year, month - 1);
        }
        int firstDayOfWeek = dayForWeek(year + "-" + month + "-01");
        //first day not Sunday? fill a few days from previous month
        if (firstDayOfWeek != 1) {
            for (int i = predays - firstDayOfWeek + 2; i <= predays; i++) {
                GVCalendarItem cb = new GVCalendarItem();
                cb.setDateString(i + "");
                cb.setLastOrNextMonth(true);
                items.add(cb);
            }
        }
        //days of the showing month
        for (int i = 1; i <= days; i++) {
            GVCalendarItem cb = new GVCalendarItem();
            cb.setDateString(i + "");
            cb.setLastOrNextMonth(false);
            if (i == day && calToday.get(Calendar.YEAR) == year && calToday.get(Calendar.MONTH) + 1 == month) {
                cb.setToday(true);
            }
            //if has plan
            String date = year + String.format("-%02d-%02d", month, i);
            if (EventPlanProvider.hasPlan(date + " 00:00:01")) {
                cb.setHasPlan(true);
            } else {
                cb.setHasPlan(false);
            }
            //set week
            cb.setDayOfWeek(dayForWeek(date));
            //add the item to list
            items.add(cb);
        }
        //Fill a few days from next month
        if ((firstDayOfWeek - 1 + days) % 7 != 0) {
            for (int i = 1; i <= 7 - (firstDayOfWeek - 1 + days) % 7; i++) {
                GVCalendarItem cb = new GVCalendarItem();
                cb.setDateString(i + "");
                cb.setLastOrNextMonth(true);
                items.add(cb);
            }
        }
        //call notifyDataSetChanged
        if (adapter == null) {
            adapter = new GVCalendarAdapter(context, items);
        }
        adapter.notifyDataSetChanged();
        this.isLoadDone = true;
    }

    /**
     * how many days of this month
     *
     * @param year
     * @param month
     * @return days
     */
    private static int getDays(int year, int month) {
        int days = 0;
        boolean isLeapYear = ((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0);
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                days = 31;
                break;
            case 2:
                days = isLeapYear ? 29 : 28;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                days = 30;
                break;
        }
        return days;
    }

    /**
     * get the day of week
     *
     * @param pTime the time format with yyyy-MM-dd
     * @return dayForWeek
     */
    @SuppressLint("SimpleDateFormat")
    private static int dayForWeek(String pTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(pTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return c.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * to the next month
     */
    public void NextMonth() {
        if (month == 12) {
            year += 1;
            month = 1;
        } else month = month + 1;
        initCalendarItems(year, month, day);
    }

    /**
     * to the last month
     */
    public void PreMonth() {
        if (month == 1) {
            year -= 1;
            month = 12;
        } else month = month - 1;
        initCalendarItems(year, month, day);
    }
}
