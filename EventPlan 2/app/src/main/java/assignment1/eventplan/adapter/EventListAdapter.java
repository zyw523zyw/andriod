package assignment1.eventplan.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import assignment1.eventplan.R;
import assignment1.eventplan.entity.EventPlan;
import assignment1.eventplan.utils.DateUtil;

/**
 * Created by zz on 2016/8/26.
 */
public class EventListAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    @NonNull
    private List<EventPlan> list;

    public EventListAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.list = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public EventPlan getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.event_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_event_title);
            viewHolder.tvStartDate = (TextView) convertView.findViewById(R.id.tv_event_start_date);
            viewHolder.tvEndDate = (TextView) convertView.findViewById(R.id.tv_event_end_date);
            viewHolder.tvVenue = (TextView) convertView.findViewById(R.id.tv_event_venue);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final EventPlan plan = list.get(position);
        viewHolder.tvTitle.setText(plan.getTitle());
        viewHolder.tvStartDate.setText(DateUtil.format(plan.getStartDateTime()));
        viewHolder.tvEndDate.setText(DateUtil.format(plan.getEndDateTime()));
        viewHolder.tvVenue.setText(plan.getAddress());
        return convertView;
    }

    @Nullable
    public EventPlan remove(int position) {
        return list.remove(position);
    }

    public void addData(EventPlan plan) {
        list.add(plan);
    }

    public void replaceData(Collection<EventPlan> allEvent) {
        list.clear();
        list.addAll(allEvent);
    }

    @NonNull
    public List<EventPlan> getList() {
        return list;
    }

    private static class ViewHolder {
        TextView tvTitle;
        TextView tvStartDate;
        TextView tvEndDate;
        TextView tvVenue;

    }
}
