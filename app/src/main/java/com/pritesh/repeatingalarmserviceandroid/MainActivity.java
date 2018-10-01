package com.pritesh.repeatingalarmserviceandroid;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity
{
    private final String TAG = MainActivity.class.getCanonicalName();
    MyReceiver mMyReceiver;
    TextView lblTitle;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lblTitle = findViewById(R.id.lblTitle);
        mMyReceiver = new MyReceiver();
        startAlarmServices();
    }

    @Override
    protected void onStop()
    {
        unregisterReceiver(mMyReceiver);
        super.onStop();
    }

    //https://developer.android.com/training/scheduling/alarms
    //http://codetheory.in/android-broadcast-receivers/
    private void startAlarmServices()
    {
        Toast.makeText(this, "Alarm Started", Toast.LENGTH_SHORT).show();
        alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // Set the alarm to start at 8:30 a.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        Log.d(TAG, String.valueOf(System.currentTimeMillis()));

        Intent intent = new Intent(this, ShowNotificationIntentService.class);
        intent.putExtra("TIME", System.currentTimeMillis());
        //alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        alarmIntent = PendingIntent.getService(this, 0, intent, 0);


        //calendar.set(Calendar.HOUR_OF_DAY, calendar.getTime().getHours());
        //calendar.set(Calendar.MINUTE, calendar.getTime().getMinutes());
        Log.d(TAG, calendar.getTime().getHours() + ":" + calendar.getTime().getMinutes());

        // setRepeating() lets you specify a precise custom interval--in this case,
        // 20 minutes.
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                1000 * 60 * 1, alarmIntent);

    }

    @Override
    protected void onResume()
    {
        IntentFilter filter = new IntentFilter("com.pritesh.BroadcastReceiver");

        registerReceiver(mMyReceiver, filter);
        super.onResume();
    }

    public class MyReceiver extends BroadcastReceiver
    {

        private String TAG = MyReceiver.class.getSimpleName();

        public MyReceiver()
        {
        }

        @Override
        public void onReceive(Context context, Intent intent)
        {
            String time = intent.getStringExtra("TIME");
            Log.d(TAG, "MyReceiver : " + time);
            lblTitle.setText("Hello Alarm : " + time);
        }
    }
}
