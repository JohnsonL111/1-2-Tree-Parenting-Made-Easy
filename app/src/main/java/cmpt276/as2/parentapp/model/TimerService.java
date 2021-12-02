package cmpt276.as2.parentapp.model;


import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;

import androidx.annotation.Nullable;

import cmpt276.as2.parentapp.R;

/**
 * service for timer. the service will start a timer and it will keep running in the background.
 */
public class TimerService extends Service {


    private static final String TIME_LEFT ="time left" ;
    private static final String INTENT_FILTER = "countdown";
    private static final String INTERVAL = "interval";
     CountDownTimer timer;
     Intent broadcastIntent;
//    int currentTime;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        broadcastIntent = new Intent();
        broadcastIntent.setAction(INTENT_FILTER);
        final int timeLeft = intent.getIntExtra(TIME_LEFT, 0);
        final int interval = intent.getIntExtra(INTERVAL, 1000);
        if(timer!=null){
            timer.cancel();
        }
        timer = new CountDownTimer(timeLeft * interval, interval) {
            @Override
            public void onTick(long secondsLeft) {
//                currentTime=(int)secondsLeft;

                setBroadcast((int)secondsLeft/interval);

            }
            @Override
            public void onFinish() {
            }
        };
        timer.start();
        return START_NOT_STICKY;
    }

    public void setBroadcast(int secondsLeft){
        broadcastIntent.putExtra(TIME_LEFT, (secondsLeft));
        sendBroadcast(broadcastIntent);
    }





    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

}






