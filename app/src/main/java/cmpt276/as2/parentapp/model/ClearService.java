package cmpt276.as2.parentapp.model;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;

import cmpt276.as2.parentapp.UI.TimeoutActivity;

/**
 * Service to check if user leave the app
 */
public class ClearService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(TimeoutActivity.TIMER_IS_RUNNING, false);
        editor.apply();
        super.onDestroy();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(TimeoutActivity.TIMER_IS_RUNNING, false);
        editor.apply();
        stopSelf();
    }
}
