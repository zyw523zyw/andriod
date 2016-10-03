package assignment1.eventplan.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import assignment1.eventplan.Constants;
import assignment1.eventplan.R;
import assignment1.eventplan.adapter.EventListAdapter;
import assignment1.eventplan.db.dao.EventPlanDao;
import assignment1.eventplan.db.master.EventPlanProvider;
import assignment1.eventplan.entity.EventPlan;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EventListAdapter adapter;

    SwipeMenuCreator creator = new SwipeMenuCreator() {

        @Override
        public void create(SwipeMenu menu) {
            // create "openDatabase" item
            SwipeMenuItem openItem = new SwipeMenuItem(
                    getApplicationContext());
            // set item background
            openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                    0xCE)));
            // set item width
            openItem.setWidth(dip2px(90));
            // set item title
            openItem.setTitle("Edit");
            // set item title fontsize
            openItem.setTitleSize(18);
            // set item title font color
            openItem.setTitleColor(Color.WHITE);
            // add to menu
            menu.addMenuItem(openItem);

            // create "delete" item
            SwipeMenuItem deleteItem = new SwipeMenuItem(
                    getApplicationContext());
            // set item background
            deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
            // set item width
            deleteItem.setWidth(dip2px(90));
            // set a icon
            deleteItem.setIcon(R.drawable.ic_delete);
            // add to menu
            menu.addMenuItem(deleteItem);
        }
    };

    public int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.add_button:
                startActivityForResult(new Intent(MainActivity.this, AddOrUpdateEventActivity.class), Constants.ADD_EVENT);
                break;
            case R.id.calendar_button:
                startActivityForResult(new Intent(MainActivity.this, CalendarActivity.class), Constants.SEARCH_EVENT_BY_DATE);
                break;
            case R.id.all_button:
                refreshData();
                break;
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        refreshData();
    }

    private void initViews() {
        SwipeMenuListView lv = (SwipeMenuListView) findViewById(R.id.lv);
        // set creator
        lv.setMenuCreator(creator);
        lv.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        lv.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:// edit
                        Intent intent = new Intent(MainActivity.this, AddOrUpdateEventActivity.class);
                        intent.putExtra(EventPlanDao.KEY, adapter.getItem(position));
                        startActivityForResult(intent, Constants.UPDATE_EVENT);
                        break;
                    case 1:// delete
                        EventPlan plan = adapter.getItem(position);
                        if (null != plan) {
                            EventPlanProvider.get().deleteEvent(plan);
                        }
                        adapter.notifyDataSetChanged();
                        break;
                }
                // false : closeIfNeed the menu; true : not closeIfNeed the menu
                return false;
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("eventId", adapter.getItem(position).getId());
                startActivity(intent);
            }
        });

        lv.setAdapter(adapter = new EventListAdapter(this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.ADD_EVENT) {
                boolean isAdd = data.getBooleanExtra("isAdd", false);
                if (isAdd) {
                    adapter.notifyDataSetChanged();
                } else {
                    refreshData();
                }
            } else if (requestCode == Constants.SEARCH_EVENT_BY_DATE) {
                String dateStr = data.getStringExtra("date");
                adapter.replaceData(EventPlanProvider.get().findAllPlanByDate(dateStr));
                adapter.notifyDataSetChanged();
            } else {
                refreshData();
            }
        }
    }

    private void refreshData() {
        adapter.replaceData(EventPlanProvider.get().getAllPlans());
        adapter.notifyDataSetChanged();
    }


}

