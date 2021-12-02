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
    private String btnText;
    private String helpMsg;

    public WaitingToInhaleState(BreathActivity context) {
        super(context);
        btnText = context.getString(R.string.in_button_text);
        helpMsg = context.getString(R.string.hold_button_text);
    }

    @Override
    public void helpTextHandler(BreathActivity context) {
        context.setText(btnText, helpMsg);
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
                helpMsg = context.getString(R.string.release_button_text);
                context.setText(btnText, helpMsg);
            }
        };
        mainBtn.setOnTouchListener(new View.OnTouchListener() {
            long timeMilliStart;
            long timeMilliEnd;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    context.startInhaleBreatheSound();
                    helpMsg = context.getString(R.string.inhaling_text);
                    context.setText(btnText, helpMsg);
                    int tenSeconds = 10000;
                    timeMilliStart = System.currentTimeMillis();
                    mainBtn.setPressed(true);
                    handler.postDelayed(run, tenSeconds);
                    return true;
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    context.stopBreatheSounds();
                    int threeSeconds = 3000;
                    helpMsg = context.getString(R.string.hold_button_text);
                    context.setText(btnText, helpMsg);
                    timeMilliEnd = System.currentTimeMillis();
                    long totalTime = timeMilliEnd - timeMilliStart;
                    handler.removeCallbacks(run);
                    if (totalTime >= threeSeconds) {
                        context.setState(new InhaledForThreeSecondsState(context));
                    }
                    return false;
                }
                return false;
            }
        });
    }
}
