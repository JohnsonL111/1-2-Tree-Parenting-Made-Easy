package cmpt276.as2.parentapp.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import cmpt276.as2.parentapp.R;
import cmpt276.as2.parentapp.model.Child;
import cmpt276.as2.parentapp.model.ChildManager;

/**
 * Encapsulates functionality for editing the current children list.
 */
public class EditChildActivity extends AppCompatActivity {

    private ChildManager childManager;
    private ArrayAdapter<Child> listAdapter;
    private ListView list;
    private String newChildName = ""; // Store new child name on edit here.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_child);

        getSupportActionBar().setTitle(R.string.editChildActivityTitle);

        childManager = childManager.getInstance();
        startChildList();
        addChild();
        removeChild();
        startListViewClickable();
    }

    private void addChild() {
        Button addChildButton = findViewById(R.id.addChildButton);

        addChildButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get child's name.
                EditText childNameSlot = findViewById(R.id.addChildBox);
                String childName = childNameSlot.getText().toString();

                // Only attempt to add string if it isn't empty.
                if (!childName.equals("")) {
                    // Check if new name is available and execute apt case.
                    if (!childManager.checkIfNameExist(childName)) {
                        Toast.makeText(EditChildActivity.this, "Added " + childName, Toast.LENGTH_SHORT).show();
                        childManager.addChild(childName);
                        startChildList();
                        childNameSlot.setText("");
                    } else {
                        Toast.makeText(EditChildActivity.this, "Child already exists!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void removeChild() {
        Button removeChildButton = findViewById(R.id.removeChildButton);
        removeChildButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Child's name.
                EditText childNameSlot = findViewById(R.id.removeChildBox);
                String childName = childNameSlot.getText().toString();

                // Check if new name is available and execute apt case.
                if (childManager.checkIfNameExist(childName)) {
                    Toast.makeText(EditChildActivity.this, "Removed " + childName, Toast.LENGTH_SHORT).show();
                    childManager.removeChild(childName);
                    startChildList();
                    childNameSlot.setText("");
                } else {
                    Toast.makeText(EditChildActivity.this, "Child does not exist!", Toast.LENGTH_SHORT).show();
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


    // Resets the list view.
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

    // Configure child object to be clickable.
    private void startListViewClickable(){
        // Citation: https://easysavecode.com/FezSDcUC
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                Child childToEdit = childManager.getChildList().get(position);

                // Functionality so user can change child name in listview.
                editChildName(childToEdit);

                // Check if new name is available and execute apt case.
                if (!childManager.checkIfNameExist(newChildName)) {
                    // Get the List, find the child's index, and change its name.
                    List<Child> childList = childManager.getChildList();
                    int childToEditIdx = childList.indexOf(childToEdit);
                    childList.get(childToEditIdx).setName(newChildName);

                    Toast.makeText(EditChildActivity.this, "Changed name to " + newChildName, Toast.LENGTH_SHORT).show();
                    //startChildList();
                } else {
                    Toast.makeText(EditChildActivity.this, "Child already exists!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    // Takes in user input for new child name and decides if valid or not.
    private void editChildName(Child childToEdit) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Child Name");
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                newChildName = input.getText().toString();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, EditChildActivity.class);
    }

}