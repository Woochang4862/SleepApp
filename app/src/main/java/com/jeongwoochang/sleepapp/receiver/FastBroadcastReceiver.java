package com.jeongwoochang.sleepapp.receiver;

/**
 * Created by Ilya Anshmidt on 19.09.2017.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.jeongwoochang.sleepapp.activity.FastAlarmActivity;
import timber.log.Timber;

public class FastBroadcastReceiver extends BroadcastReceiver {

    public static final String ONE_TIME = "onetime";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent fastAlarmIntent = new Intent(context, FastAlarmActivity.class);
        Timber.d(intent.getBooleanExtra("isStart", false)+"");
        fastAlarmIntent.putExtra("isStart", intent.getBooleanExtra("isStart", false));
        context.startActivity(fastAlarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

}
