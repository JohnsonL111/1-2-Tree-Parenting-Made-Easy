package cmpt276.as2.parentapp.model;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import cmpt276.as2.parentapp.UI.TimeoutActivity;

public class TimerNotification extends Application {
    public static final String TIMER_CHANNEL_ID = "timerChannel";
    @Override
    public void onCreate() {
        super.onCreate();
        createChannels();
    }

    public void createChannels() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel timerNotificationChannel = new NotificationChannel(
                    TIMER_CHANNEL_ID,
                    "Timer Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(timerNotificationChannel);
        }
    }
}
