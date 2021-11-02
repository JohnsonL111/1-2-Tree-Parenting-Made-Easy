package cmpt276.as2.parentapp.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;



import cmpt276.as2.parentapp.R;

//timer button= start and stop button
public class TimeoutActivity extends AppCompatActivity {
    private static final String DURATION_SETTING = "Duration Settings";
    private static final String DURATION_CHOICE = "Duration Choice";

    Animation rotate;
    Button timerButton;
    Button resetButton;
    Button optionButton;
    TextView timeoutText;
    boolean timerIsRunning;
    int initialTime;
    int timeLeft;
    CountDownTimer timer;

    public static Intent makeIntent(Context context){
        Intent intent = new Intent(context, TimeoutActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDuration();
        setContentView(R.layout.activity_timeout);
        timerButton = findViewById(R.id.StartStopButton);
        resetButton = findViewById(R.id.ResetButton);
        optionButton=findViewById(R.id.OptionButton);
        timeoutText=findViewById(R.id.timeoutText);
        timeoutText.setTextSize(40);
        timerButton.setText("START");
        timerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(!timerIsRunning) {
                    startTimer();
                    timerIsRunning=true;
                    timerButton.setText("STOP");
                    optionButton.setAlpha(0);

                }
                else{
                    stopTimer();
                    timerIsRunning=false;
                    timerButton.setText("START");
                    optionButton.setAlpha(1);


                }
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.cancel();
                timeLeft=initialTime;
                timerIsRunning=false;
                optionButton.setAlpha(1);
                timerButton.setText("START");
                updateTimer(timeLeft);

            }
        });

        optionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!timerIsRunning){
                    Intent i = TimeoutOptionActivity.makeIntent(TimeoutActivity.this);
                    startActivity(i);
                }
                else{
                    Toast.makeText(TimeoutActivity.this, "A timer is running",Toast.LENGTH_SHORT);
                }
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        getDuration();
        updateTimer(timeLeft);
    }

    private void stopTimer() {
        rotate.cancel();
        rotate.setFillAfter(true);
        timer.cancel();
    }

    private void startTimer() {
        setTimerAnimation();
        timer=new CountDownTimer(timeLeft*1000, 1000) {
            public void onTick(long secondsLeft) {
                timeLeft=((int)secondsLeft)/1000;
                updateTimer((int)secondsLeft / 1000);
            }
            public void onFinish() {
                timeoutText.setText("done!");
                timerIsRunning=false;
                timerButton.setText("START");
                timeLeft=initialTime;
            }
        };
        timer.start();
    }

    private void setTimerAnimation() {
        rotate = AnimationUtils.loadAnimation(TimeoutActivity.this,R.anim.rotate);
        TableLayout animationLayout= findViewById(R.id.animationLayout);
        ImageView timerTop = new ImageView(this)       ;
        ImageView timerBottom = new ImageView(this)       ;
        timerTop.setImageResource(R.drawable.round);

        animationLayout.removeAllViewsInLayout();
        animationLayout.addView(timerTop);
        animationLayout.addView(timerBottom);

        rotate.setDuration((long)timeLeft*1000);
        timerTop.startAnimation(rotate);
        rotate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                timerTop.setVisibility(View.VISIBLE);
                timerTop.setImageAlpha(100);
                timerBottom.setVisibility(View.VISIBLE);
                timerBottom.setImageAlpha(100);
            }

            @Override
            public void onAnimationEnd(Animation animation) {


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void updateTimer(int l) {
        int minute=l/60;
        int second=l%60;
        timeoutText.setText(getString(R.string.timerTextFormat,minute,second));
    }
    public void getDuration(){
        initialTime = TimeoutOptionActivity.getDuration(this);
        timeLeft=initialTime;
    }

}
