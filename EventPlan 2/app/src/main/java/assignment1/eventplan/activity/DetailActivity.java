package assignment1.eventplan.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import assignment1.eventplan.R;
import assignment1.eventplan.db.master.EventPlanProvider;
import assignment1.eventplan.entity.EventPlan;
import assignment1.eventplan.utils.DateUtil;

/**
 * Created by YumiZhang on 2016/8/27.
 */
public class DetailActivity extends AppCompatActivity implements View.OnClickListener {
    private long eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (initData(null == savedInstanceState ? getIntent().getExtras() : savedInstanceState)) {
            setContentView(R.layout.activity_detail);
            initViews();
        } else {
            Toast.makeText(this, " miss event!!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("eventId", eventId);
    }

    private void initViews() {
        TextView etEventTitle = (TextView) findViewById(R.id.etEventTitle);
        TextView startEdit = (TextView) findViewById(R.id.startEditText);
        TextView endEdit = (TextView) findViewById(R.id.endEditText);
        TextView venueEdit = (TextView) findViewById(R.id.etVeune);
        TextView etAtten = (TextView) findViewById(R.id.etAtten);
        TextView etNote = (TextView) findViewById(R.id.etNote);

        EventPlan eventEntity = EventPlanProvider.get().getEventById(eventId);
        if (null != eventEntity) {
            etEventTitle.setText(eventEntity.getTitle());
            startEdit.setText(DateUtil.format(eventEntity.getStartDateTime()));
            endEdit.setText(DateUtil.format(eventEntity.getEndDateTime()));
            venueEdit.setText(eventEntity.getAddress() + " " + eventEntity.getAddress());
            etAtten.setText(eventEntity.getAttendees());
            etNote.setText(eventEntity.getNote());
        } else {
            Toast.makeText(this, " miss event!!", Toast.LENGTH_SHORT).show();
            finish();
        }

    }


    private boolean initData(Bundle bundle) {
        if (null == bundle)
            return false;
        return (eventId = bundle.getLong("eventId", -1)) > 0;
    }


    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.mapview_button:
                break;
        }

    }
}
