package cmpt276.as2.parentapp.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import cmpt276.as2.parentapp.R;
import cmpt276.as2.parentapp.model.Child;
import cmpt276.as2.parentapp.model.ChildManager;

public class EditChildActivity extends AppCompatActivity {

    private ChildManager childManager; // singleton

    private ArrayAdapter<Child> listAdapter;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_child);

        getSupportActionBar().setTitle(R.string.editChildActivityTitle);

        childManager = childManager.getInstance();
        startChildList();
        addChildButton();
    }

    private void addChildButton() {
        Button addChildButton = findViewById(R.id.addChildButton);

        addChildButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get child's name.
                EditText childNameSlot = findViewById(R.id.addChildBox);
                String childName = childNameSlot.getText().toString();

                // Pop toast and add child if name doesn't exist.
                if (!childManager.checkIfNameExist(childName)) {
                    Toast.makeText(EditChildActivity.this, "Added " + childName, Toast.LENGTH_SHORT).show();
                    childManager.addChild(childName);
                    startChildList();
                } else {
                    Toast.makeText(EditChildActivity.this, "Child already exists!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Maintain same list when coming back to the activity.
    @Override
    protected void onResume() {
        super.onResume();

        // Refresh child list listView.
        startChildList();
    }


    private void startChildList() {
        // Create list of children.
        List<Child> childItems = childManager.getChildList();


        // build adapter
        listAdapter = new ArrayAdapter<>(
                this, // context
                R.layout.children_list, // layout to use (Create many)
                childItems); // items to be displayed

        // configure the list view
        list = findViewById(R.id.childListView);
        list.setAdapter(listAdapter);

    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, EditChildActivity.class);
    }

}