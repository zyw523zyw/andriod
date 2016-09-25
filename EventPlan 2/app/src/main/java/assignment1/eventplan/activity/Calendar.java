package assignment1.eventplan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import assignment1.eventplan.GVCalendar;
import assignment1.eventplan.GVCalendarItem;
import assignment1.eventplan.R;

public class Calendar extends AppCompatActivity {

    private GVCalendar gvCalendar;
    private Button btnNext;
    private Button btnPre;
    private TextView tvDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        //load the UI
        gvCalendar = (GVCalendar) findViewById(R.id.gvCalendar);
        gvCalendar.initCalendar();
        btnPre = (Button) findViewById(R.id.btn_pre);
        btnNext = (Button) findViewById(R.id.btn_next);
        tvDate = (TextView) findViewById(R.id.tv_date);
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
                    intent.putExtra("date", date);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(Calendar.this, "no plan", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
