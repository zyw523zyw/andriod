package assignment1.eventplan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import assignment1.eventplan.GVCalendar;
import assignment1.eventplan.GVCalendarItem;
import assignment1.eventplan.R;

public class CalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        //load the UI
        final GVCalendar gvCalendar = (GVCalendar) findViewById(R.id.gvCalendar);
        gvCalendar.initCalendar();
        final Button btnPre = (Button) findViewById(R.id.btn_pre);
        final Button btnNext = (Button) findViewById(R.id.btn_next);
        final TextView tvDate = (TextView) findViewById(R.id.tv_date);
        tvDate.setText(gvCalendar.getTitle());

        //listener of buttons
        btnPre.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                gvCalendar.PreMonth();
                tvDate.setText(gvCalendar.getTitle());
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                gvCalendar.NextMonth();
                tvDate.setText(gvCalendar.getTitle());
            }
        });

        //on item click, just a test
        gvCalendar.setListener(new GVCalendar.OnCalenderItemCilckListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id, String date) {
                GVCalendarItem item = (GVCalendarItem) parent.getItemAtPosition(position);
              /*  item.setHasPlan(!item.isHasPlan());
                item.setPlanString("todo");
                gvCalendar.refreshCalendar();*/
                if (item.isHasPlan()) {
                    Intent intent = new Intent();
                    intent.putExtra("date", date + " 00:00:01");
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Snackbar.make(getWindow().getDecorView(), "no plan", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

}
