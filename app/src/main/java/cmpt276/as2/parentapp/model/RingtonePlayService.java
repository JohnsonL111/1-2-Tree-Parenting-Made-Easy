package cmpt276.as2.parentapp.model;


import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;

import androidx.annotation.Nullable;

/**
 * The Service will start the alarm and vibration
 */
public class RingtonePlayService extends Service {
    private MediaPlayer alarm;
    private Vibrator vibrator;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        alarm = MediaPlayer.create(getBaseContext(), alarmSound);

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        final long[] pattern = {0, 1000, 1000};
        if(Build.VERSION.SDK_INT >= 26) {
            VibrationEffect vibrationEffect = VibrationEffect.createWaveform(pattern, 0);
            vibrator.vibrate(vibrationEffect);
        }
        else{
            vibrator.vibrate(pattern, 0);
        }
        alarm.start();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        alarm.stop();
        alarm.release();
        vibrator.cancel();
    }
}
