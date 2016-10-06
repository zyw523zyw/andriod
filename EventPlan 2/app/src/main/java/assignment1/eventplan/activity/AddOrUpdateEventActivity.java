package assignment1.eventplan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import assignment1.eventplan.db.dao.EventPlanDao;
import assignment1.eventplan.db.master.EventPlanProvider;
import assignment1.eventplan.entity.Contact;
import assignment1.eventplan.R;
import assignment1.eventplan.entity.EventPlan;
import assignment1.eventplan.utils.DateUtil;
import assignment1.eventplan.widgets.DateTimePicker;


public class AddOrUpdateEventActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {


    private static final int CONTACT_PICK_REQUEST = 1000;
    TextView startDateText;
    TextView endDateText;
    TextView atteneesText;

    TextInputLayout titleInput;
    TextInputLayout vueneInput;
    TextInputLayout noteInput;


    Button submitButton;

    private EventPlan plan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData(savedInstanceState);
        setContentView(R.layout.activity_edit_add);
        initViews();
        updateViews();
    }


    private void initViews() {
        submitButton = (Button) findViewById(R.id.submit_button);
        submitButton.setEnabled(false);

        startDateText = (TextView) findViewById(R.id.start_date_text);
        endDateText = (TextView) findViewById(R.id.end_date_text);
        atteneesText = (TextView) findViewById(R.id.attendees_text);

        titleInput = (TextInputLayout) findViewById(R.id.title_input);
        vueneInput = (TextInputLayout) findViewById(R.id.address_input);
        noteInput = (TextInputLayout) findViewById(R.id.note_input);

        titleInput.getEditText().addTextChangedListener(this);
        vueneInput.getEditText().addTextChangedListener(this);
        noteInput.getEditText().addTextChangedListener(this);
    }


    private void updateViews() {
        EventPlan plan = this.plan;
        if (null == plan)
            return;
        startDateText.setText(DateUtil.format(plan.getStartDateTime()));
        startDateText.setTag(plan.getStartDateTime());
        endDateText.setText(DateUtil.format(plan.getEndDateTime()));
        endDateText.setTag(plan.getEndDateTime());
        titleInput.getEditText().setText(plan.getTitle());
        vueneInput.getEditText().setText(plan.getAddress());
        noteInput.getEditText().setText(plan.getNote());
        String attendees = plan.getAttendees();
        if (TextUtils.isEmpty(attendees)) {
            atteneesText.setText("Attendees \n");
        } else {
            atteneesText.setText(String.format("Attendees \n%s", plan.getAttendees()));
        }
    }

    private void initData(Bundle savedInstanceState) {
        Bundle bundle = null == savedInstanceState ? getIntent().getExtras() : savedInstanceState;
        if (null == bundle) {
            return;
        }
        plan = bundle.getParcelable(EventPlanDao.KEY);
        selectedContacts = bundle.getParcelableArrayList("SelectedContacts");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EventPlanDao.KEY, plan);
        if (null != selectedContacts) {
            outState.putParcelableArrayList("SelectedContacts", selectedContacts);
        }
    }


    @Override
    public void afterTextChanged(Editable editable) {
        checkSubmitEnable();
    }

    private void checkSubmitEnable() {
        //// TODO: check it can or cannot submit the data
        submitButton.setEnabled(checkSubmitEnable(titleInput) && //set up title
                !TextUtils.isEmpty(startDateText.getText().toString()) && //set up start date
                !TextUtils.isEmpty(endDateText.getText().toString())); //set up end date
    }

    private boolean checkSubmitEnable(TextInputLayout inputLayout) {
        if (!TextUtils.isEmpty(inputLayout.getEditText().getText().toString())) {
            inputLayout.setError(null);
        } else {
            inputLayout.setError(inputLayout.getHint() + " can`t not be null!");
            return false;
        }
        return true;
    }

    ArrayList<Contact> selectedContacts;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CONTACT_PICK_REQUEST && resultCode == RESULT_OK) {
            selectedContacts = data.getParcelableArrayListExtra("SelectedContacts");
            String attendees = TextUtils.join("\n", selectedContacts);
            atteneesText.setText(String.format("Attendees \n%s", attendees));
            atteneesText.setTag(attendees);
        }

    }

    @Override
    public void onClick(View view) {
        final int id = view.getId();
        switch (id) {
            case R.id.close_button:
                finish();
                break;
            case R.id.start_date_container:
                showDateTimePicker(startDateText);
                break;
            case R.id.end_date_container:
                showDateTimePicker(endDateText);
                break;
            case R.id.attendees_text:
                Intent intent = new Intent(AddOrUpdateEventActivity.this, ContactsPickerActivity.class);
                startActivityForResult(intent, CONTACT_PICK_REQUEST);
                break;
            case R.id.submit_button:
                insertOrUpdate();
                break;
        }
    }


    private void insertOrUpdate() {
        String attendees = (String) atteneesText.getTag();
        if (null == plan) {
            plan = new EventPlan();
        }
        final boolean isAdd = plan.getId() == 0;
        plan.setAttendees(attendees);
        plan.setStartDateTime((long) startDateText.getTag());
        plan.setEndDateTime((long) endDateText.getTag());
        plan.setTitle(titleInput.getEditText().getText().toString().trim());
        plan.setAddress(vueneInput.getEditText().getText().toString().trim());
        plan.setNote(noteInput.getEditText().getText().toString().trim());
        if (null != selectedContacts) {
            plan.setContacts(selectedContacts);
        }
        plan.setDate(DateUtil.resetDateTime(plan.getStartDateTime()));
        EventPlanProvider.get().insertOrUpdate(plan);
        Toast.makeText(this, isAdd ? "Added!" : "Updated!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.putExtra("isAdd", isAdd);
        intent.putExtra(EventPlanDao.KEY, plan);
        setResult(RESULT_OK, intent);
        finish();
    }

    DateTimePicker picker;

    /**
     * @param text current effect
     */
    private void showDateTimePicker(final TextView text) {
        if (null == picker) {
            picker = new DateTimePicker(this);
        }
        picker.setCallback(new DateTimePicker.Callback() {
            @Override
            public void onTimeCallback(long timeMillis) {
                if (timeMillis != DateTimePicker.CANCEL) {
                    if (isTimeValid(text, timeMillis)) {
                        text.setText(DateUtil.format(timeMillis));
                        text.setTag(timeMillis);//cache timeMillis
                        checkSubmitEnable();
                    } else {
                        Snackbar.make(text, "You cannot choose the time,please choose again!", Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
        });

        picker.setTimeMillis(getCacheTimeMillis(text));
        picker.show();
    }

    /**
     * @param textView   current effect
     * @param timeMillis update millis
     * @return true, will update current textView
     */
    private boolean isTimeValid(TextView textView, long timeMillis) {
        if (textView == startDateText) {
            Object tagEndDate = endDateText.getTag();
            if (tagEndDate instanceof Long) {
                return DateUtil.isBefore(timeMillis, (long) tagEndDate);
            }
        } else {
            Object tagStartDate = startDateText.getTag();
            if (tagStartDate instanceof Long) {
                return DateUtil.isAfter(timeMillis, (long) tagStartDate);
            }
        }
        return true;
    }

    /**
     * @param text current effect
     * @return cache time millis, default now
     */
    private long getCacheTimeMillis(TextView text) {
        Object cache = text.getTag();
        return cache instanceof Long ? (long) cache : System.currentTimeMillis();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

}
