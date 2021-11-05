package cmpt276.as2.parentapp.UI;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import androidx.appcompat.app.AppCompatActivity;



import cmpt276.as2.parentapp.R;

//timer button= start and stop button
public class TimeoutActivity extends AppCompatActivity {
    private static final String DURATION_SETTING = "Duration Settings";
    private static final String DURATION_CHOICE = "Duration Choice";
    private static final String BACKGROUND_TITLE = "beach";
    private static final int BACKGROUND_SIZE = 7;
    ImageView timerAnimation;
    Button timerButton;
    Button resetButton;
    Button optionButton;
    TextView timeoutText;
    TableLayout animationLayout;
    ImageSwitcher background;
    boolean timerIsRunning;
    int initialTime;
    int timeLeft;
    CountDownTimer timer;
    CountDownTimer BackgroundTimer;

    int backgroundList[];
    int counter=0;

    public static Intent makeIntent(Context context){
        Intent intent = new Intent(context, TimeoutActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialTime= getDuration();
        timeLeft=initialTime;
        setContentView(R.layout.activity_timeout);

        timerButton = findViewById(R.id.StartStopButton);
        resetButton = findViewById(R.id.ResetButton);
        optionButton=findViewById(R.id.OptionButton);

        timeoutText=findViewById(R.id.timeoutText);
        timeoutText.setTextSize(40);
        timerButton.setText("START");

        timerAnimation = new ImageView(this)       ;
        timerAnimation.setImageResource(R.drawable.round);
        timerAnimation.setAlpha(0.5F);

        background = (ImageSwitcher) findViewById(R.id.background);
        background=findViewById(R.id.background);
        background.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView myView = new ImageView(getApplicationContext());
                return myView;
            }
        });

        animationLayout= findViewById(R.id.animationLayout);
        animationLayout.addView(timerAnimation);

        backgroundList= new int[9];
        fillBackgroundList();



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
                rotate(0);

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

    private void changeBackground(int i) {

        background.setImageResource(backgroundList[i]);
        Animation in = AnimationUtils.loadAnimation(TimeoutActivity.this,android.R.anim.fade_in);
        Animation out = AnimationUtils.loadAnimation(TimeoutActivity.this,android.R.anim.fade_out);
        background.setInAnimation(in);
        background.setOutAnimation(out);

    }


    @Override
    protected void onResume() {
        super.onResume();
        if(getDuration()  != initialTime){
            initialTime=getDuration();
            timeLeft=initialTime;
            updateTimer(timeLeft);
        }
        else{
            updateTimer(timeLeft);
        }

    }

    private void stopTimer() {
        timer.cancel();
    }

    private void startTimer() {
        changeBackground((int)timeLeft*8/initialTime);
        timer=new CountDownTimer(timeLeft*1000, 1000) {
            public void onTick(long secondsLeft) {
                timeLeft=((int)secondsLeft)/1000;
                updateTimer((int)secondsLeft / 1000);
                float angle=timeLeft*360/initialTime;
                rotate(angle);
                timerAnimation.setMaxWidth(animationLayout.getWidth());
                timerAnimation.setMaxHeight(animationLayout.getHeight());
                changeBackground((initialTime-timeLeft)*8/initialTime);
                Log.e("gelo",(initialTime-timeLeft)*8/initialTime+"");
            }
            public void onFinish() {
                timeoutText.setText("done!");
                timerIsRunning=false;
                timerButton.setText("START");
                optionButton.setAlpha(1);
                timeLeft=initialTime;
                counter=0;
            }
        };
        timer.start();
    }


    public void fillBackgroundList(){
        backgroundList[0]=R.drawable.beach1;
        backgroundList[1]=R.drawable.beach2;
        backgroundList[2]=R.drawable.beach3;
        backgroundList[3]=R.drawable.beach4;
        backgroundList[4]=R.drawable.beach5;
        backgroundList[5]=R.drawable.beach6;
        backgroundList[6]=R.drawable.beach7;
        backgroundList[7]=R.drawable.beach8;
        backgroundList[8]=R.drawable.beach9;
    }


    private void rotate(float angle) {
        timerAnimation.setRotation(angle*-1);
    }


    private void updateTimer(int l) {
        int minute=l/60;
        int second=l%60;
        timeoutText.setText(getString(R.string.timerTextFormat,minute,second));
    }
    public int getDuration(){
        int newTime = TimeoutOptionActivity.getDuration(this);
        return newTime;
    }

}
