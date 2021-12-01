package cmpt276.as2.parentapp.model.State;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import cmpt276.as2.parentapp.R;
import cmpt276.as2.parentapp.UI.BreathActivity;

public class WaitingToInhaleState extends State {

    public WaitingToInhaleState(BreathActivity context) {
        super(context);
    }

    @Override
    public void helpTextHandler(BreathActivity context) {
        context.setText("In", "Hold button and breathe in");
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onClickHandler(BreathActivity context) {
        context.disableBreathsMenu();
        Button mainBtn = context.findViewById(R.id.breath_main_btn);
        Handler handler = new Handler();
        Runnable run = new Runnable() {
            @Override
            public void run() {
                context.setText("In", "Release button and breathe out");
            }
        };
        mainBtn.setOnTouchListener(new View.OnTouchListener() {
            long timeMilliStart;
            long timeMilliEnd;
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    //context.startBreatheInSound();
                    int tenSeconds = 10000;
                    timeMilliStart = System.currentTimeMillis();
                    mainBtn.setPressed(true);
                    handler.postDelayed(run,tenSeconds);
                    return true;
                }
                else if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    //context.stopSounds();
                    int threeSeconds = 3000;
                    timeMilliEnd = System.currentTimeMillis();
                    long totalTime = timeMilliEnd - timeMilliStart;
                    Toast.makeText(context, " "+totalTime, Toast.LENGTH_SHORT).show();
                    handler.removeCallbacks(run);
                    if(totalTime >= threeSeconds) {
                        context.setState(new InhaledForThreeSecondsState(context));
                    }
                    return false;
                }
                return false;
            }
        });
    }
}
