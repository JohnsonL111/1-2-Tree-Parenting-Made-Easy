package cmpt276.as2.parentapp.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import cmpt276.as2.parentapp.R;
import cmpt276.as2.parentapp.model.Child;

public class ActitivityEditSingleChildActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actitivity_edit_single_child);
        getSupportActionBar().setTitle("Add New Child");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // create Intent to pass data
    public static Intent makeIntent(Context context, Child childEntry, int childIndex, boolean isEditChild) {
        Intent intent = new Intent(context, ActitivityEditSingleChildActivity.class);

        // Load child name and bit image through SP.
        if (isEditChild) {

        }
        return intent;
    }

}