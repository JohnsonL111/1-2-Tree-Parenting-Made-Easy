package cmpt276.as2.parentapp.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cmpt276.as2.parentapp.R;
import cmpt276.as2.parentapp.model.ChildManager;

public class ActitivityEditSingleChildActivity extends AppCompatActivity {

    private static ChildManager childManager;

    // Intent Tags.
    private static final String CHILD_NAME_TAG = "child name";

    // Intent Data
    static boolean isEditChild;
    String childName;
    Bitmap childIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actitivity_edit_single_child);
        childManager = ChildManager.getInstance();

        extractIntent();
        Button finishedButton = findViewById(R.id.finishedButton);

        if (isEditChild) {
            setTitle("Editing Child");
            finishedButton.setText("Edit Child");
        } else {
            setTitle("Adding New Child");
            finishedButton.setText("Add Child");
        }

        addChild();

    }

    private void addChild() {

        Button addChildButton = findViewById(R.id.finishedButton);

        addChildButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get child's name.
                EditText childNameSlot = findViewById(R.id.editTextPersonName);
                String childName = childNameSlot.getText().toString();
                childManager.addChild(childName);
                finish();


                /*
                // Only attempt to add string if it isn't empty.
                if (!childName.equals("")) {
                    // Check if new name is available and execute apt case.
                    if (!childManager.checkIfNameExist(childName)) {
                        Toast.makeText(ActitivityEditSingleChildActivity.this, "Added " + childName, Toast.LENGTH_SHORT).show();
                        childManager.addChild(childName);
                        childNameSlot.setText("");
                        //finish();
                    } else {
                        Toast.makeText(ActitivityEditSingleChildActivity.this, "Child already exists!", Toast.LENGTH_SHORT).show();
                    }
                }
            */
            }


        });
    }

    // create Intent to pass data
    public static Intent makeIntent(Context context, String childName, int childIndex, boolean isEditChild) {
        Intent intent = new Intent(context, ActitivityEditSingleChildActivity.class);
        // Load child name and bit image through SP.
        intent.putExtra(CHILD_NAME_TAG, childName);

        ActitivityEditSingleChildActivity.isEditChild = isEditChild;
        return intent;
    }

    private void extractIntent() {
        Intent intent = getIntent();
        childName = intent.getStringExtra(CHILD_NAME_TAG);
        // extract bitmap here

        // inject data into sections
        EditText nameEditText = findViewById(R.id.editTextPersonName);

        nameEditText.setText(childName);
        // inject bitmap here
    }

}