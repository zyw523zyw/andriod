package assignment1.eventplan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import assignment1.eventplan.Contact;
import assignment1.eventplan.ContactsPickerActivity;
import assignment1.eventplan.R;

public class Contacts extends AppCompatActivity {

    TextView contactsDisplay;
    Button pickContacts;
    final int CONTACT_PICK_REQUEST = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        contactsDisplay = (TextView) findViewById(R.id.txt_selected_contacts);
        pickContacts = (Button) findViewById(R.id.btn_pick_contacts);

        pickContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentContactPick = new Intent(Contacts.this, ContactsPickerActivity.class);
                Contacts.this.startActivityForResult(intentContactPick, CONTACT_PICK_REQUEST);
            }
        });
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
            contactsDisplay.setText("Selected Contacts : \n\n" + display);

        }

    }
}
