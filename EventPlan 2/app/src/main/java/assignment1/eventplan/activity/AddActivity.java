package assignment1.eventplan.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import assignment1.eventplan.Contact;
import assignment1.eventplan.ContactsPickerActivity;
import assignment1.eventplan.R;
import assignment1.eventplan.entity.EventEntity;
import assignment1.eventplan.utils.DbUtil;


public class AddActivity extends AppCompatActivity {

    EditText etEventTitle;
    private TextView startEdit, endEdit;
    private TextView etAtten;
    private TextView endTimeEdit;
    private TextView startTimeEdit;

    Button btOk;
    private EditText venueEdit;
    private EditText etLat;
    private EditText etLog;
    private EditText etNote;

    final int CONTACT_PICK_REQUEST = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_add);
        etEventTitle = (EditText) findViewById(R.id.etEventTitle);
        startEdit = (TextView) findViewById(R.id.startEditText);
        startTimeEdit = (TextView) findViewById(R.id.startTimeEditText);
        endEdit = (TextView) findViewById(R.id.endEditText);
        endTimeEdit = (TextView) findViewById(R.id.endTimeEditText);
        venueEdit = (EditText) findViewById(R.id.etVeune);
        etLat = (EditText) findViewById(R.id.etLat);
        etLog = (EditText) findViewById(R.id.etLog);
        btOk = (Button) findViewById(R.id.btOk);
        startEdit.setOnTouchListener(new MyDateTouchListener());
        endEdit.setOnTouchListener(new MyDateTouchListener());
        startTimeEdit.setOnTouchListener(new MyTimeTouchListener());
        endTimeEdit.setOnTouchListener(new MyTimeTouchListener());
        etAtten = (TextView) findViewById(R.id.etAtten);
        etNote = (EditText) findViewById(R.id.etNote);
        //btOk.setOnClickListener(new MyClickListener());
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEvent();
            }
        });

        etAtten.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(AddActivity.this, ContactsPickerActivity.class);
                startActivityForResult(intent, CONTACT_PICK_REQUEST);
            }
        });
    }

    private void addEvent() {
        String title = etEventTitle.getText().toString().trim();
        String startDate = startEdit.getText().toString().trim();
        String endDate = endEdit.getText().toString().trim();
        String startTime = startTimeEdit.getText().toString().trim();
        String endTime = endTimeEdit.getText().toString().trim();
        String veune = venueEdit.getText().toString().trim();
        String locationX = etLat.getText().toString().trim();
        String locationY = etLog.getText().toString().trim();
        String attendees = etAtten.getText().toString().trim();
        String note = etNote.getText().toString().trim();
        EventEntity eventEntity = new EventEntity();
        eventEntity.setTitle(title);
        eventEntity.setStartDate(startDate);
        eventEntity.setEndDate(endDate);
        eventEntity.setVeune(veune);
        eventEntity.setStartTime(startTime);
        eventEntity.setEndTime(endTime);
        eventEntity.setLocationX(locationX);
        eventEntity.setLocationY(locationY);
        eventEntity.setAttendees(attendees);
        eventEntity.setNote(note);
        DbUtil.addEvent(eventEntity);
        Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK, null);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CONTACT_PICK_REQUEST && resultCode == RESULT_OK) {

            ArrayList<Contact> selectedContacts = data.getParcelableArrayListExtra("SelectedContacts");

            String display = "";
            for (int i = 0; i < selectedContacts.size(); i++) {

                display += (i + 1) + ". " + selectedContacts.get(i).toString() + "\n";

            }
            etAtten.setText(display);
        }

    }

    class MyDateTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // TODO Auto-generated method stub
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddActivity.this);
                View view = View.inflate(AddActivity.this, R.layout.activity_datepicker, null);

                final DatePicker datepicker = (DatePicker) view.findViewById(R.id.myDatePicker);
                //设置对话框的布局文件
                builder.setView(view);

                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(System.currentTimeMillis());
                datepicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH), null);
                //判断事件的对象
                if (v.getId() == R.id.startEditText) {
                    final int inType = startEdit.getInputType();
                    startEdit.setInputType(InputType.TYPE_NULL);
                    startEdit.onTouchEvent(event);
                    startEdit.setInputType(inType);
                    //设置对话框属性
                    builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub
                                    StringBuffer sb = new StringBuffer();
                                    sb.append(String.format("%d-%02d-%02d",
                                            datepicker.getYear(),
                                            datepicker.getMonth() + 1,
                                            datepicker.getDayOfMonth()));
                                    startEdit.setText(sb);
                                    dialog.cancel();
                                }
                            }
                    );
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                }
                if (v.getId() == R.id.endEditText) {
                    final int inType = endEdit.getInputType();
                    endEdit.setInputType(InputType.TYPE_NULL);
                    endEdit.onTouchEvent(event);
                    endEdit.setInputType(inType);
                    //设置对话框属性

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub
                                    StringBuffer sb = new StringBuffer();
                                    sb.append(String.format("%d-%02d-%02d",
                                            datepicker.getYear(),
                                            datepicker.getMonth() + 1,
                                            datepicker.getDayOfMonth()));
                                    endEdit.setText(sb);
                                    dialog.cancel();
                                }
                            }
                    );
                }
                //创建对话框，并显示
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            return true;
        }

    }

    class MyTimeTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // TODO Auto-generated method stub
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddActivity.this);
                View view = View.inflate(AddActivity.this, R.layout.activity_timepicker, null);

                final TimePicker timepicker = (TimePicker) view.findViewById(R.id.myTimePicker);
                //设置对话框的布局文件
                builder.setView(view);

                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(System.currentTimeMillis());
                timepicker.setIs24HourView(true);
                timepicker.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
                timepicker.setCurrentHour(cal.get(Calendar.MINUTE));
                //判断事件的对象
                if (v.getId() == R.id.startTimeEditText) {
                    final int inType = startTimeEdit.getInputType();
                    startTimeEdit.setInputType(InputType.TYPE_NULL);
                    startTimeEdit.onTouchEvent(event);
                    startTimeEdit.setInputType(inType);
                    //设置对话框属性
                    builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub
                                    StringBuffer sb = new StringBuffer();
                                    sb.append(timepicker.getCurrentHour())
                                            .append(":")
                                            .append(timepicker.getCurrentMinute());
                                    startTimeEdit.setText(sb);
                                    dialog.cancel();
                                }
                            }
                    );
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                }
                if (v.getId() == R.id.endTimeEditText) {
                    final int inType = endTimeEdit.getInputType();
                    endTimeEdit.setInputType(InputType.TYPE_NULL);
                    endTimeEdit.onTouchEvent(event);
                    endTimeEdit.setInputType(inType);
                    //设置对话框属性

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub
                                    StringBuffer sb = new StringBuffer();
                                    sb.append(timepicker.getCurrentHour())
                                            .append(":").append(timepicker.getCurrentMinute());
                                    endTimeEdit.setText(sb);
                                    dialog.cancel();
                                }
                            }
                    );
                }
                //创建对话框，并显示
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            return true;
        }

    }


}
