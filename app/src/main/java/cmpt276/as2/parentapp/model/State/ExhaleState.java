package cmpt276.as2.parentapp.model.State;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import cmpt276.as2.parentapp.R;
import cmpt276.as2.parentapp.UI.BreathActivity;

public class ExhaleState extends State {
    private String helpMsg;
    private String btnText;

    public ExhaleState(BreathActivity context) {
        super(context);
        Button mainBtn = context.findViewById(R.id.breath_main_btn);
        mainBtn.setBackgroundResource(R.drawable.round_blue_btn);
        Animation shrinkButton = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.button_grow); //reference the animator
        shrinkButton.setDuration(10000);
        mainBtn.startAnimation(shrinkButton);
        helpMsg = context.getString(R.string.exhaling_text);
        btnText = context.getString(R.string.out_button_text);
    }

    @Override
    public void helpTextHandler(BreathActivity context) {
        context.setText(btnText, helpMsg);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onClickHandler(BreathActivity context) {
        timingHandler(context);
        context.stopBreatheSounds();
        context.startExhaleBreatheSound();
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
        handler.postDelayed(run, 3000);
    }
}
