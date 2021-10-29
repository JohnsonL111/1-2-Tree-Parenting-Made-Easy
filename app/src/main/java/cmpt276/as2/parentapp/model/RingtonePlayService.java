package cmpt276.as2.parentapp.model;


import android.app.Service;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class RingtonePlayService extends Service {
    private Ringtone alarm;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        this.alarm = RingtoneManager.getRingtone(this, alarmSound);
        alarm.play();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        alarm.stop();
    }
}
