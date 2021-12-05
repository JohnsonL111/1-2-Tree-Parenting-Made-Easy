package cmpt276.as2.parentapp.UI;

import static cmpt276.as2.parentapp.model.TimerNotification.TIMER_CHANNEL_ID;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


import cmpt276.as2.parentapp.R;
import cmpt276.as2.parentapp.model.NotificationReceiver;
import cmpt276.as2.parentapp.model.RingtonePlayService;
import cmpt276.as2.parentapp.model.TimerService;

/**
 * activity for start, stop, and reset the timer service
 */
public class TimeoutActivity extends AppCompatActivity {
    private static final String TIMER_SITUATION = "timer situation";
    private static final String TIME_LEFT = "time left";
    private static final String INTENT_FILTER = "countdown";
    private static final String TIMER_IS_RUNNING = "timer is running";
    private static final String INTERVAL = "interval";
    private static final String START = "START";
    private static final String STOP = "STOP";
    public static MediaPlayer beach_sound;

    ImageView timerAnimation;
    Button timerButton;
    Button resetButton;
    Button optionButton;
    TextView timeoutText;
    TextView intervalText;

    TableLayout animationLayout;


    boolean timerIsRunning;
    int initialTime;
    int timeLeft;
    int counter = 0;
    int interval;
    int maxSpeed;
    int minSpeed;
    int currentInterval = 3;

    int intervalList[];


    public static Intent makeIntent(Context context) {
        Intent intent = new Intent(context, TimeoutActivity.class);
        return intent;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        setUpMusic();

        initialTime = getDuration();
        timeLeft = initialTime;
        setContentView(R.layout.activity_timeout);
        intervalList = getResources().getIntArray(R.array.interval);
        maxSpeed = getResources().getInteger(R.integer.maxSpeed);
        minSpeed = getResources().getInteger(R.integer.minSpeed);


        timerButton = findViewById(R.id.StartStopButton);
        resetButton = findViewById(R.id.ResetButton);
        optionButton = findViewById(R.id.OptionButton);
        optionButton = findViewById(R.id.OptionButton);


        timeoutText = findViewById(R.id.timeoutText);
        timeoutText.setTextSize(40);
        intervalText = findViewById(R.id.interval);
        updateButton();


        timerAnimation = new ImageView(this);
        timerAnimation.setImageResource(R.drawable.round);
        timerAnimation.setAlpha(0.5F);


        animationLayout = findViewById(R.id.animationLayout);
        animationLayout.addView(timerAnimation);


        timerButton.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("Range")
            @Override
            public void onClick(View view) {
                if (!timerIsRunning) {
                    startTimer();
                    timerIsRunning = true;

                } else {
                    stopTimer();
                    timerIsRunning = false;
                }
                updateButton();
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timerIsRunning = false;
                beach_sound.pause();
                stopService(new Intent(TimeoutActivity.this, TimerService.class));
                initialTime = getDuration();
                timeLeft = initialTime;
                updateTimer(timeLeft);
                updateButton();
                updateAnimation(timeLeft);

            }
        });

        optionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = TimeoutOptionActivity.makeIntent(TimeoutActivity.this);
                startActivity(i);

            }
        });
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(INTENT_FILTER);
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int newTimeLeft = intent.getIntExtra(TIME_LEFT, -1);
                if (newTimeLeft != -1) {
                    timerIsRunning = true;
                    updateButton();
                    timeLeft = newTimeLeft;
                    SharedPreferences prefs = getSharedPreferences(TIMER_SITUATION, MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt(TIME_LEFT, timeLeft);
                    editor.putBoolean(TIMER_IS_RUNNING, true);
                    initialTime = getDuration();
                    updateTimer(newTimeLeft);
                    updateAnimation(newTimeLeft);
                    counter++;
                    editor.apply();
                }
                if (newTimeLeft == 0) {
                    stopService(new Intent(TimeoutActivity.this, TimerService.class));
                    sendTimerNotification();
                    if (beach_sound.isPlaying()) {
                        beach_sound.pause();
                    }
                    SharedPreferences prefs = getSharedPreferences(TIMER_SITUATION, MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    timerIsRunning = false;
                    editor.putBoolean(TIMER_IS_RUNNING, false);
                    editor.putInt(TIME_LEFT, initialTime);
                    editor.apply();
                    updateButton();
                    initialTime = getDuration();
                    timeLeft = initialTime;
                    updateTimer(timeLeft);
                    editor.apply();
                }
            }
        };
        registerReceiver(broadcastReceiver, intentFilter);
    }

    private void updateAnimation(int newTimeLeft) {
        float angle = newTimeLeft * 360 / initialTime;
        rotate(angle);
    }


    private void stopTimer() {
        timerIsRunning = false;
        updateButton();
        beach_sound.pause();
        stopService(new Intent(this, TimerService.class));
    }

    private void speedUpTimer() {
        if (interval == maxSpeed) {
            //toast
        } else {
            currentInterval--;
            interval = intervalList[currentInterval];
            int speed = (100000 / interval);
            intervalText.setText(speed + "%");
            if (timerIsRunning) {
                stopService(new Intent(this, TimerService.class));
                Intent timerIntent = new Intent(TimeoutActivity.this, TimerService.class);
                timerIntent.putExtra(TIME_LEFT, timeLeft);
                timerIntent.putExtra(INTERVAL, interval);
                startService(timerIntent);
            }
        }

    }

    private void slowDownTimer() {
        if (interval == minSpeed) {
            //toast
        } else {
            currentInterval++;
            interval = intervalList[currentInterval];
            int speed = (100000 / interval);
            intervalText.setText(speed + "%");
            if (timerIsRunning) {
                stopService(new Intent(this, TimerService.class));
                Intent timerIntent = new Intent(TimeoutActivity.this, TimerService.class);
                timerIntent.putExtra(TIME_LEFT, timeLeft);
                timerIntent.putExtra(INTERVAL, interval);
                startService(timerIntent);
            }

        }

    }


    private void setUpMusic() {
        beach_sound = MediaPlayer.create(TimeoutActivity.this, R.raw.beach_sound);
    }

    private void startTimer() {
        beach_sound.start();
        updateButton();
        Intent timerIntent = new Intent(TimeoutActivity.this, TimerService.class);
        timerIntent.putExtra(TIME_LEFT, timeLeft);
        timerIntent.putExtra(INTERVAL, interval);
        startService(timerIntent);
    }

    private void updateButton() {
        if (!timerIsRunning) {
            timerButton.setText(START);
            optionButton.setAlpha(1);
            optionButton.setClickable(true);

        } else {
            timerButton.setText(STOP);
            optionButton.setAlpha(0);
            optionButton.setClickable(false);


        }
    }

    public void sendTimerNotification() {
        String title = getString(R.string.timerNotificationTitle);
        String message = getString(R.string.timerNotificationDescription);
        String actionButtonText = getString(R.string.notificationActionButtonText);
        int notificationID = 1;

        Intent notificationReceiverBroadcast = new Intent(this, NotificationReceiver.class);
        notificationReceiverBroadcast.putExtra("notification_id", notificationID);
        PendingIntent actionButtonIntent = PendingIntent.getBroadcast(this, 0,
                notificationReceiverBroadcast, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, TIMER_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_timer_24)
                .setContentTitle(title)
                .setColor(Color.GREEN)
                .setContentText(message)
                .addAction(R.drawable.ic_sharp_clear_24, actionButtonText, actionButtonIntent)
                .setFullScreenIntent(actionButtonIntent, true)
                .setDeleteIntent(actionButtonIntent)
                .setPriority(NotificationCompat.PRIORITY_MAX);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(notificationID, builder.build());

        Intent stopAlarmIntent = new Intent(TimeoutActivity.this, RingtonePlayService.class);
        TimeoutActivity.this.stopService(stopAlarmIntent);
        Intent startAlarmIntent = new Intent(TimeoutActivity.this, RingtonePlayService.class);
        TimeoutActivity.this.startService(startAlarmIntent);
    }

    private void rotate(float angle) {
        timerAnimation.setRotation(angle * -1);
    }

    private void updateTimer(int l) {
        int minute = l / 60;
        int second = l % 60;
        timeoutText.setText(getString(R.string.timerTextFormat, minute, second));
    }

    public int getDuration() {
        return TimeoutOptionActivity.getDuration(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.speedUp:
                speedUpTimer();
                return true;
            case R.id.slowDown:
                slowDownTimer();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        beach_sound.pause();
        SharedPreferences prefs = getSharedPreferences(TIMER_SITUATION, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(TIMER_IS_RUNNING, timerIsRunning);
        editor.putInt(TIME_LEFT, timeLeft);

        editor.apply();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMusic();
        SharedPreferences prefs = getSharedPreferences(TIMER_SITUATION, MODE_PRIVATE);
        timerIsRunning = prefs.getBoolean(TIMER_IS_RUNNING, false);
        initialTime = getDuration();
        if (timerIsRunning) {
            beach_sound.start();
            updateButton();
            updateTimer(timeLeft);
        } else {
            initialTime = getDuration();
            timeLeft = initialTime;
            updateAnimation(timeLeft);
            updateTimer(timeLeft);
            updateButton();
        }
        if (getDuration() != initialTime) {
            initialTime = getDuration();
            timeLeft = getDuration();
            updateTimer(timeLeft);
            updateAnimation(timeLeft);

        }
    }


}
