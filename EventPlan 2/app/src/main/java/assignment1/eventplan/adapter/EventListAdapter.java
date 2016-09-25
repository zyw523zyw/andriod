package assignment1.eventplan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import assignment1.eventplan.R;
import assignment1.eventplan.entity.EventEntity;

/**
 * Created by zz on 2016/8/26.
 */
public class EventListAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<EventEntity> list;

    public EventListAdapter(Context context, List<EventEntity> list) {
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }

    public void setList(List<EventEntity> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list == null ? null : list.get(position);
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
        viewHolder.tvTitle.setText(list.get(position).getTitle());
        viewHolder.tvStartDate.setText(list.get(position).getStartDate()+" "+list.get(position).getStartTime());
        viewHolder.tvEndDate.setText(list.get(position).getEndDate()+" "+list.get(position).getEndTime());
        viewHolder.tvVenue.setText(list.get(position).getVeune());
        return convertView;
    }

    static class ViewHolder {
        TextView tvTitle;
        TextView tvStartDate;
        TextView tvEndDate;
        TextView tvVenue;

    }
}
