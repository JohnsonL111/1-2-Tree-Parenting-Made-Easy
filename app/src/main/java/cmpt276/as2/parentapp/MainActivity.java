package cmpt276.as2.parentapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import cmpt276.as2.parentapp.UI.TimeoutActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Button timeoutButton = findViewById(R.id.TimeoutButton);

        timeoutButton.setOnClickListener(view -> {
            Intent i = TimeoutActivity.makeIntent(MainActivity.this);
            startActivity(i);
        });
    }
}