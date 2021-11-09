package cmpt276.as2.parentapp.model;


import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;

import androidx.annotation.Nullable;

/**
 * service for timer. the service will start a timer and it will keep running in the background.
 */
public class TimerService extends Service {


    private static final String TIME_LEFT ="time left" ;
    private static final String INTENT_FILTER = "countdown";
     CountDownTimer timer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(INTENT_FILTER);

        final int timeLeft = intent.getIntExtra(TIME_LEFT, 0);
        if(timer!=null){
            timer.cancel();
        }
        timer = new CountDownTimer(timeLeft * 1000, 1000) {
            @Override
            public void onTick(long secondsLeft) {
                broadcastIntent.putExtra(TIME_LEFT, ((int) secondsLeft) / 1000);
                sendBroadcast(broadcastIntent);

            }
            @Override
            public void onFinish() {
            }
        };
        timer.start();
        return START_NOT_STICKY;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

}






