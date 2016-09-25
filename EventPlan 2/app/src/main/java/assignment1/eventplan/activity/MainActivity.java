package assignment1.eventplan.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.List;

import assignment1.eventplan.R;
import assignment1.eventplan.adapter.EventListAdapter;
import assignment1.eventplan.entity.EventEntity;
import assignment1.eventplan.utils.DbUtil;

public class MainActivity extends AppCompatActivity {

    Button Addbtn, CalendarViewbtn;

    SwipeMenuListView lv;

    List<EventEntity> list;

    private EventListAdapter adapter;

    SwipeMenuCreator creator = new SwipeMenuCreator() {

        @Override
        public void create(SwipeMenu menu) {
            // create "open" item
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
    private Button allBtn;

    public int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Addbtn = (Button) findViewById(R.id.Addbtn);
        allBtn = (Button) findViewById(R.id.Allbtn);
        CalendarViewbtn = (Button) findViewById(R.id.CalendarViewbtn);
        lv = (SwipeMenuListView) findViewById(R.id.lv);

        Addbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivityForResult(intent, 0x11);
            }
        });

        allBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshData();
            }
        });
        CalendarViewbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Calendar.class);
                startActivityForResult(intent, 0x12);
            }
        });

        // set creator
        lv.setMenuCreator(creator);

        lv.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

        lv.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // edit
                        Intent intent = new Intent(MainActivity.this, EditActivity.class);
                        intent.putExtra("eventId", list.get(position).getId());
                        startActivityForResult(intent, 0x13);
                        break;
                    case 1:
                        // delete
                        DbUtil.deleteEvent(list.get(position));
                        refreshData();
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("eventId", list.get(position).getId());
                startActivity(intent);
            }
        });

        list = new ArrayList<>();
        adapter = new EventListAdapter(this, list);
        lv.setAdapter(adapter);

        refreshData();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 0x12) {
                String date = data.getStringExtra("date");
                list = DbUtil.getEventByDate(date);
                adapter.setList(list);
                adapter.notifyDataSetChanged();
            } else {
                refreshData();
            }
        }
    }

    private void refreshData() {
        list = DbUtil.getAllEvent();
        adapter.setList(list);
        adapter.notifyDataSetChanged();
    }

}

