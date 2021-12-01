package cmpt276.as2.parentapp.model.State;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import cmpt276.as2.parentapp.R;
import cmpt276.as2.parentapp.UI.BreathActivity;

public class ExhaleState extends State{

    public ExhaleState(BreathActivity context) {
        super(context);
    }

    @Override
    public void helpTextHandler(BreathActivity context) {
        context.setText("Out", "Start Exhaling");
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onClickHandler(BreathActivity context) {
        timingHandler(context);
        Button btn = context.findViewById(R.id.breath_main_btn);
        btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
    }

    @Override
    public void timingHandler(BreathActivity context) {
        Handler handler = new Handler();
        Runnable run = new Runnable() {
            @Override
            public void run() {
                context.setState(new ExhaleThreeSecondsState(context));
            }
        };
        handler.postDelayed(run,3000);
    }
}
