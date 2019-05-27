package com.jeongwoochang.sleepapp.util.helper.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.jeongwoochang.sleepapp.R;
import com.jeongwoochang.sleepapp.util.data.AlarmParams;

import java.util.List;
import java.util.Map;

/**
 * Created by Ilya Anshmidt on 24.10.2017.
 */

public class AlarmsListAdapter extends SimpleAdapter {

    public static final String THREE_DOTS = "...";
    private AlarmParams alarmParams;
    private Context context;

    public AlarmsListAdapter(Context context, List<? extends Map<String, ?>> data, int resource,
                             String[] from, int[] to, AlarmParams alarmParams) {
        super(context, data, resource, from, to);
        this.alarmParams = alarmParams;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView timeTextView = view.findViewById(R.id.textview_main_alarmslist);

        String timeValue = timeTextView.getText().toString();
        ImageView alarmIcon = view.findViewById(R.id.img_main_alarmslist);
        if (timeValue.equals(THREE_DOTS)) {
            alarmIcon.setVisibility(View.INVISIBLE);
        } else {
            alarmIcon.setVisibility(View.VISIBLE);
        }

        int textColor;
        if (alarmParams.getTurnedOn()) {
            textColor = ContextCompat.getColor(context, R.color.colorPrimary);
        } else {
            textColor = ContextCompat.getColor(context, R.color.main_disabled_textcolor);
        }
        timeTextView.setTextColor(textColor);
        alarmIcon.setColorFilter(textColor);

        return view;
    }
}