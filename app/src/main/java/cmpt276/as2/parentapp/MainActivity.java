package cmpt276.as2.parentapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import cmpt276.as2.parentapp.UI.CoinFlipActivity;
import cmpt276.as2.parentapp.UI.EditChildActivity;
import cmpt276.as2.parentapp.UI.HelpMenuActivity;
import cmpt276.as2.parentapp.UI.TaskManagerActivity;
import cmpt276.as2.parentapp.UI.TimeoutActivity;

/**
 * Entry to application with the menu UI.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button timeoutButton = findViewById(R.id.TimeoutButton);

        timeoutButton.setOnClickListener(view -> {
            Intent i = TimeoutActivity.makeIntent(MainActivity.this);
            startActivity(i);
        });

        Button coinFlip = findViewById(R.id.flipCoin);

        coinFlip.setOnClickListener(view ->
        {
            Intent i = CoinFlipActivity.makeIntent(MainActivity.this);
            startActivity(i);
        });

        Button editChild = findViewById(R.id.editChildren);

        editChild.setOnClickListener(view ->
        {
            Intent i = EditChildActivity.makeIntent(MainActivity.this);
            startActivity(i);
        });

        Button task = findViewById(R.id.task_btn);
        task.setOnClickListener(view ->
        {
            Intent i = TaskManagerActivity.makeIntent(MainActivity.this);
            startActivity(i);
        });

        Button help = findViewById(R.id.help_btn);
        help.setOnClickListener(view ->
        {
            Intent i = HelpMenuActivity.makeIntent(MainActivity.this);
            startActivity(i);
        });
    }

    public static Intent makeIntent(Context context)
    {
        return new Intent(context, MainActivity.class);
    }
}