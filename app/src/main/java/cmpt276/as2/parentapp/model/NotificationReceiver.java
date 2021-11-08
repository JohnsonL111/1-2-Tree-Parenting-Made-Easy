package cmpt276.as2.parentapp.model;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


/**
 * Create a broadcast receiver for the notification
 */
public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final int DEFAULT_NOTIFICATION_ID = 1;
        int notification_id = intent.getIntExtra("notification_id", DEFAULT_NOTIFICATION_ID);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(notification_id);
        Intent stopAlarmIntent = new Intent(context, RingtonePlayService.class);
        context.stopService(stopAlarmIntent);
    }
}
