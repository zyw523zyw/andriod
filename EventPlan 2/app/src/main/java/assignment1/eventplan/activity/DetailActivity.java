package assignment1.eventplan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import assignment1.eventplan.ContactsPickerActivity;
import assignment1.eventplan.R;
import assignment1.eventplan.entity.EventEntity;
import assignment1.eventplan.utils.DbUtil;

/**
 * Created by ZZ on 2016/8/27.
 */
public class DetailActivity extends AppCompatActivity {

    TextView etEventTitle;
    private TextView venueEdit;
    private TextView etLat;
    private TextView etNote;
    private EventEntity eventEntity;

    private TextView startEdit, endEdit;
    private TextView etAtten;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        etEventTitle = (TextView) findViewById(R.id.etEventTitle);
        startEdit = (TextView) findViewById(R.id.startEditText);
        endEdit = (TextView) findViewById(R.id.endEditText);
        venueEdit = (TextView) findViewById(R.id.etVeune);
        etLat = (TextView) findViewById(R.id.etLat);
        etAtten = (TextView) findViewById(R.id.etAtten);
        etNote = (TextView) findViewById(R.id.etNote);

        int eventId = getIntent().getIntExtra("eventId", -1);
        if (eventId != -1) {
            eventEntity = DbUtil.getEventById(eventId);
            initData(eventEntity);
        }

    }

    private void initData(EventEntity eventEntity) {
        if (eventEntity != null) {
            etEventTitle.setText(eventEntity.getTitle());
            startEdit.setText(eventEntity.getStartDate()+" "+eventEntity.getStartTime());
            endEdit.setText(eventEntity.getEndDate()+" "+eventEntity.getEndTime());
            venueEdit.setText(eventEntity.getVeune());
            etLat.setText("("+eventEntity.getLocationX()+","+eventEntity.getLocationY()+")");
            etAtten.setText(eventEntity.getAttendees());
            etNote.setText(eventEntity.getNote());
        }
    }


}
