package cmpt276.as2.parentapp.model.State;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import cmpt276.as2.parentapp.R;
import cmpt276.as2.parentapp.UI.BreathActivity;

/**
 * Second state in the breath process. Controls and responds to possible inhale outcomes (e.g., button animation).
 * Transitions to InhaledForThreeSecondsState.
 */
public class WaitingToInhaleState extends State {
    private BreathActivity context;
    private String btnText;
    private String helpMsg;
    private int countTick;

    public WaitingToInhaleState(BreathActivity context) {
        super(context);
        this.context = context;
        btnText = context.getString(R.string.in_button_text);
        helpMsg = context.getString(R.string.hold_button_text);
        Button mainBtn = context.findViewById(R.id.breath_main_btn);
        ImageView circle = context.findViewById(R.id.breath_circle);
        mainBtn.setBackgroundResource(R.drawable.round_btn);
        circle.setVisibility(View.VISIBLE);
        circle.setImageResource(R.drawable.breath_circle);
        context.checkBtnSize();
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
        ImageView circle = context.findViewById(R.id.breath_circle);
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
            CountDownTimer threeSecondTimer;
            CountDownTimer tenSecondTimer;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    circle.setVisibility(View.VISIBLE);
                    context.startInhaleBreatheSound();
                    helpMsg = context.getString(R.string.inhaling_text);
                    context.setText(btnText, helpMsg);
                    int tenSeconds = 10000;
                    timeMilliStart = System.currentTimeMillis();
                    mainBtn.setPressed(true);
                    handler.postDelayed(run, tenSeconds);

                    threeSecondTimer = new CountDownTimer(3000, 1000) {
                        @Override
                        public void onTick(long l) {

                        }

                        @Override
                        public void onFinish() {
                            context.setText("Out", "Inhaled");
                        }
                    }.start();

                    tenSecondTimer = new CountDownTimer(10000, 100) {
                        @Override
                        public void onTick(long l) {
                            ViewGroup.LayoutParams params = mainBtn.getLayoutParams();
                            params.width -= 2;
                            params.height -= 2;
                            countTick++;
                            mainBtn.setLayoutParams(params);

                            ViewGroup.LayoutParams paramC = circle.getLayoutParams();
                            paramC.width -= 1;
                            paramC.height -= 1;
                            countTick++;
                            circle.setLayoutParams(paramC);

                        }

                        @Override
                        public void onFinish() {
                            context.stopBreatheSounds();


                        }
                    }.start();
                    return true;

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                    if (threeSecondTimer != null) {
                        threeSecondTimer.cancel();
                        tenSecondTimer.cancel();
                    }

                    context.stopBreatheSounds();
                    int threeSeconds = 3000;

                    helpMsg = context.getString(R.string.hold_button_text);
                    context.setText(btnText, helpMsg);
                    timeMilliEnd = System.currentTimeMillis();
                    long totalTime = timeMilliEnd - timeMilliStart;
                    handler.removeCallbacks(run);

                    if (totalTime >= threeSeconds) {
                        btnText = "Out";
                        context.setText(btnText, helpMsg);
                        context.setState(new InhaledForThreeSecondsState(context));
                    } else {
                        ViewGroup.LayoutParams params = mainBtn.getLayoutParams();
                        params.width += countTick * 2;
                        params.height += countTick * 2;
                        mainBtn.setLayoutParams(params);

                        ViewGroup.LayoutParams paramC = circle.getLayoutParams();
                        paramC.width += countTick;
                        paramC.height += countTick;
                        circle.setLayoutParams(paramC);

                        countTick = 0;
                    }

                    return false;
                }
                return false;
            }
        });
    }
}
