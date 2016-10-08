package assignment1.eventplan.adapter;

/**
 * Created by yumizhang on 16/8/25.
 */

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import assignment1.eventplan.R;
import assignment1.eventplan.entity.Contact;


public class ContactsListAdapter extends BaseAdapter {


    @NonNull
    private final LayoutInflater inflater;
    private final NameFilter filter;
    private List<Contact> filteredContactsList;

    public ContactsListAdapter(@NonNull LayoutInflater inflater) {
        this.inflater = inflater;
        this.filter = new NameFilter(this);
    }

    @Override
    public int getCount() {
        return null == filteredContactsList ? 0 : filteredContactsList.size();
    }

    @Override
    public Contact getItem(int position) {
        return filteredContactsList.get(position);
    }

    public void setOriginContactList(List<Contact> list) {
        filter.setOriginContactList(list);
        this.filteredContactsList = list;
        notifyDataSetChanged();
    }

    public void filter(String filterContactName) {
        filter.filter(filterContactName);
    }


    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final TextView box;
        if (null == convertView) {
            box = (TextView) inflater.inflate(R.layout.contact_item, parent, false);
        } else {
            box = (TextView) convertView;
        }
        box.setText(getItem(position).toString());
        return box;
    }


    private static class NameFilter extends Filter {
        final List<Contact> originList;
        final ContactsListAdapter adapter;

        NameFilter(ContactsListAdapter adapter) {
            this.adapter = adapter;
            this.originList = new ArrayList<>(0);
        }


        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<Contact> filterList = new ArrayList<>(0);
            for (Contact contact : originList) {
                if (null != contact.getName() && contact.getName().toLowerCase().contains(constraint)) {
                    filterList.add(contact);
                }
            }
            results.values = filterList;
            return results;
        }


        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            adapter.filteredContactsList = (List<Contact>) results.values;
            adapter.notifyDataSetChanged();
        }

        void setOriginContactList(List<Contact> originContactList) {
            this.originList.clear();
            this.originList.addAll(originContactList);
        }
    }
}
