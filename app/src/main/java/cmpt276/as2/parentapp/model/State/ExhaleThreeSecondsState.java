package cmpt276.as2.parentapp.model.State;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cmpt276.as2.parentapp.R;
import cmpt276.as2.parentapp.UI.BreathActivity;

public class ExhaleThreeSecondsState extends State{
    private Handler handler = new Handler();
    private Runnable run;

    public ExhaleThreeSecondsState(BreathActivity context) {
        super(context);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onClickHandler(BreathActivity context) {
        context.updateDecreaseBreathsText();
        timingHandler(context);
        Button btn = context.findViewById(R.id.breath_main_btn);
        if(context.getNumOfBreathLeft() <= 0) {
            btn.setText("Goodjob");
        }
        btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        handler.removeCallbacks(run);
                        context.setState(new DoneExhaleState(context));
                        btn.setPressed(true);
                        btn.setPressed(false);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void helpTextHandler(BreathActivity context) {
        context.setText("In", "Continue to breathe out or press button");
        Log.i("TAG",""+context.getNumOfBreathLeft());
    }

    @Override
    public void timingHandler(BreathActivity context) {
        run = new Runnable() {
            @Override
            public void run() {
                context.setState(new DoneExhaleState(context));
            }
        };
        handler.postDelayed(run, 7000);
    }
}
