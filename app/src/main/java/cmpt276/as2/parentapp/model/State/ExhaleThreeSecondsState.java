package cmpt276.as2.parentapp.model.State;

import android.annotation.SuppressLint;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import cmpt276.as2.parentapp.R;
import cmpt276.as2.parentapp.UI.BreathActivity;

/**
 * Fifth state in the breath process. Controls and responds to possible exhale outcomes (e.g., button animations)
 * Transitions into
 */
public class ExhaleThreeSecondsState extends State {
    private Handler handler = new Handler();
    private Runnable run;
    private String helpMsg;
    private String btnText;
    private CountDownTimer tenSecondTimer;

    public ExhaleThreeSecondsState(BreathActivity context) {
        super(context);
        Button mainBtn = context.findViewById(R.id.breath_main_btn);
        helpMsg = context.getString(R.string.exhale_or_press_button_text);
        btnText = context.getString(R.string.in_button_text);

        tenSecondTimer = new CountDownTimer(7000,100) {
            @Override
            public void onTick(long l) {
                ViewGroup.LayoutParams params = mainBtn.getLayoutParams();
                params.width += 2;
                params.height += 2;
                mainBtn.setLayoutParams(params);
            }

            @Override
            public void onFinish() {
                context.stopBreatheSounds();
            }
        }.start();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onClickHandler(BreathActivity context) {
        context.updateDecreaseBreathsText();
        timingHandler(context);
        Button btn = context.findViewById(R.id.breath_main_btn);
        if (context.getNumOfBreathLeft() == 0) {
            btnText = context.getString(R.string.goodjob_button_text);
            btn.setText(btnText);
        }
        btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        handler.removeCallbacks(run);
                        tenSecondTimer.cancel();
                        btn.setPressed(true);
                        btn.setPressed(false);
                        context.setState(new DoneExhaleState(context));

                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void helpTextHandler(BreathActivity context) {
        context.setText(btnText, helpMsg);
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
