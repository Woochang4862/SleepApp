package com.jeongwoochang.sleepapp.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextClock;
import com.jeongwoochang.sleepapp.R;
import com.jeongwoochang.sleepapp.util.helper.RingtonePlayer;
import com.jeongwoochang.sleepapp.util.helper.TimerManager;
import com.jeongwoochang.sleepapp.util.helper.data.DBAdapter;
import com.jeongwoochang.sleepapp.util.helper.data.SharedPreferencesHelper;
import com.jeongwoochang.sleepapp.util.helper.view.DismissButtonNameGiver;
import org.joda.time.DateTime;
import timber.log.Timber;

import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

/**
 * Created by Ilya Anshmidt on 19.09.2017.
 */

public class FastAlarmActivity extends AppCompatActivity implements RingtonePlayer.OnFinishListener {

    Button dismissButton, successBtn;
    TextClock textClock;
    RingtonePlayer ringtonePlayer;
    SharedPreferencesHelper sharPrefHelper;
    TimerManager timerManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fast_alarm);
        showOnLockedScreen();

        ringtonePlayer = new RingtonePlayer(FastAlarmActivity.this);
        sharPrefHelper = new SharedPreferencesHelper(FastAlarmActivity.this);
        timerManager = new TimerManager(FastAlarmActivity.this);
        View layout = findViewById(R.id.dismissLayout);

        layout.setSystemUiVisibility(SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        dismissButton = findViewById(R.id.button_dismiss_alarm);
        textClock = findViewById(R.id.text_clock_dismiss);
        successBtn = findViewById(R.id.button_success_alarm);

        if (getIntent().getBooleanExtra("isStart", false)) {
            timerManager.setNextFastStartAlarm();
            successBtn.setVisibility(View.GONE);
        } else {
            timerManager.setNextFastEndAlarm();
        }

        ringtonePlayer.start();

        successBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ringtonePlayer.stop();
                DBAdapter dbAdapter = DBAdapter.getInstance();
                DBAdapter.connect(FastAlarmActivity.this);
                DateTime d = new DateTime().minusDays(1).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);
                dbAdapter.setFastAchievement(d.getMillis(), true);
                dbAdapter.close();
                finish();
            }
        });
        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ringtonePlayer.stop();
                if (getIntent().getBooleanExtra("isStart", false)) {
                    DBAdapter dbAdapter = DBAdapter.getInstance();
                    DBAdapter.connect(FastAlarmActivity.this);
                    DateTime d = new DateTime().minusDays(1).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);
                    dbAdapter.setFastAchievement(d.getMillis(), false);
                    dbAdapter.close();
                }
                finish();
            }
        });
    }

    @Override
    public void onPlayerFinished() {
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        /*if (hasWindowFocus()) {
            Timber.d("stopped");
            ringtonePlayer.stop();
        }*/
    }

    private void showOnLockedScreen() {

        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        win.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        win.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timerManager.close();
    }
}
