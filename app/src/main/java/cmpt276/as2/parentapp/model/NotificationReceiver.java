package cmpt276.as2.parentapp.model;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import cmpt276.as2.parentapp.UI.TimeoutActivity;


public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int notification_id = intent.getIntExtra("notification_id", 0);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(notification_id);
        Intent stopAlarmIntent = new Intent(context, RingtonePlayService.class);
        context.stopService(stopAlarmIntent);
    }
}
