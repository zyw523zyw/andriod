package assignment1.eventplan.activity;

/**
 * Created by yumizhang on 16/8/25.
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.ArraySet;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import assignment1.eventplan.R;
import assignment1.eventplan.adapter.ContactsListAdapter;
import assignment1.eventplan.db.master.ContactMemory;
import assignment1.eventplan.entity.Contact;

public class ContactsPickerActivity extends AppCompatActivity implements View.OnClickListener {

    ListView contactsList;
    EditText filterEdit;
    ContactsListAdapter contactsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_picker);
        initViews();
        loadData();
    }

    private void initViews() {
        ListView contactsList = (ListView) findViewById(R.id.lst_contacts_chooser);
        contactsList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        EditText filterEdit = (EditText) findViewById(R.id.txt_filter);
        filterEdit.addTextChangedListener(watcher);

        contactsList.setAdapter(contactsAdapter = new ContactsListAdapter(getLayoutInflater()));

        this.filterEdit = filterEdit;
        this.contactsList = contactsList;
    }

    private void loadData() {
        contactsAdapter.setOriginContactList(ContactMemory.get().getAllContacts());
    }


    final TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            contactsAdapter.filter(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        filterEdit.removeTextChangedListener(watcher);
    }

    @Override
    public void onClick(View v) {
        SparseBooleanArray checkedArray = contactsList.getCheckedItemPositions();
        if (null == checkedArray || checkedArray.size() == 0) {
            setResult(RESULT_CANCELED);
        } else {
            ArrayList<Contact> contacts = new ArrayList<>(contactsList.getCheckedItemCount());
            for (int i = 0, size = checkedArray.size(); i < size; i++) {
                int position = checkedArray.keyAt(i);
                boolean checked = checkedArray.valueAt(i);
                if (checked) {
                    contacts.add(contactsAdapter.getItem(position));
                }
            }
            Intent resultIntent = new Intent();
            resultIntent.putParcelableArrayListExtra("SelectedContacts", contacts);
            setResult(RESULT_OK, resultIntent);

        }
        finish();
    }


}