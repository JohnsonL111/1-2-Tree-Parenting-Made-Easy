package cmpt276.as2.parentapp.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cmpt276.as2.parentapp.MainActivity;
import cmpt276.as2.parentapp.R;
import cmpt276.as2.parentapp.model.Child;
import cmpt276.as2.parentapp.model.ChildManager;

/**
 * Encapsulates functionality for editing the current children list and displaying
 * in the list view.
 */
public class EditChildActivity extends AppCompatActivity {

    private static ChildManager childManager;
    private ArrayAdapter<String> listAdapter;
    private List<String> childNames; // For display on the listview
    private ListView list;
    private String newChildName = ""; // Store new child name on edit here.
    public static final String CHILD_LIST = "childListNames";
    public static final String CHILD_LIST_TAG = "childList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_child);

        setTitle(R.string.editChildActivityTitle);

        childManager = getChildData(CHILD_LIST);
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
                // Switches to edit single child activity.
                EditText childNameField = findViewById(R.id.addChildBox);
                String childName = childNameField.getText().toString();

                Intent appInfo = ActitivityEditSingleChildActivity.makeIntent(
                        getApplicationContext(),
                        childName,
                        -1,
                        false);
                startActivity(appInfo);
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

                startChildList();
            }
        });
    }

    // Maintain same list when coming back to the activity.
    @Override
    protected void onResume() {
        super.onResume();

        Log.d("bruh", "childlist size is: " + childManager.getChildList().size());

        // Reset the add name slot.
        EditText childNameSlot = findViewById(R.id.addChildBox);
        childNameSlot.setText("");
        // Refresh child list listView.
        startChildList();
    }


    // Resets the list view.
    private void startChildList() {
        // Forms list of names to display.
        convertChildListToNames();

        //childNames
        // build adapter
        listAdapter = new ArrayAdapter<>(
                this, // context
                android.R.layout.simple_list_item_1,
                childNames); // items to be displayed

        // configure the list view
        list = findViewById(R.id.childListView);
        list.setAdapter(listAdapter);
        saveChildData();
    }

    // For displaying child names onto listview.
    private void convertChildListToNames() {
        // Create list of children.
        List<Child> childItems = childManager.getChildList();

        childNames = new ArrayList<>();
        int numOfChild = childItems.size();
        for (int i = 0; i < numOfChild; i++) {
            Child currentChild = childItems.get(i);
            childNames.add(currentChild.getName());
        }
        java.util.Collections.sort(childNames);
    }

    // Configure child object to be clickable.
    private void startListViewClickable() {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {

                /*
                List<Child> childList = childManager.getChildList();
                String childNameClicked = childNames.get(position);


                for (int i = 0; i < childList.size(); ++i) {
                    String childName = childList.get(i).getName();
                    if (childNameClicked.equals(childName)) {
                        // User can change the child's name by clicking on the child in the listview.
                        editChildNamePopUp(childList.get(i));
                    }
                }
                */
                // Switches to edit single child activity.
                Intent appInfo = ActitivityEditSingleChildActivity.makeIntent(
                        getApplicationContext(),
                        childManager.getChildList().get(position).getName(),
                        position, true);
                startActivity(appInfo);

            }
        });
    }

    // Takes in user input for new child name via dialog popup.
    private void editChildNamePopUp(Child childToEdit) {
        // Citation: https://stackoverflow.com/questions/10903754/input-text-dialog-android
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
                newChildName = input.getText().toString();
                editChildName(childToEdit);
                dialog.dismiss();
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

    // Actually decides the validity of the new name and changes the name if possible.
    private void editChildName(Child childToEdit) {
        // Check if new name is available and execute apt case.
        if (!childManager.checkIfNameExist(newChildName)) {
            // Get the List, find the child's index, and change its name.
            List<Child> childList = childManager.getChildList();
            int childToEditIdx = childList.indexOf(childToEdit);
            if (!newChildName.equals("")) {
                String oldChildName = childList.get(childToEditIdx).getName();
                childList.get(childToEditIdx).setName(newChildName);
                childManager.updateTaskChildNames(oldChildName, newChildName);
                Toast.makeText(EditChildActivity.this, "Changed name to " + newChildName, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(EditChildActivity.this, "Child already exists!", Toast.LENGTH_SHORT).show();
        }
        startChildList();
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, EditChildActivity.class);
    }

    private ChildManager getChildData(String tag) {
        SharedPreferences prefs = this.getSharedPreferences(CHILD_LIST_TAG, MODE_PRIVATE);
        if (!prefs.contains(tag)) {
            return new ChildManager();
        }
        Gson gson = new Gson();
        return gson.fromJson(prefs.getString(tag, ""), ChildManager.class);
    }

    private void saveChildData() {
        SharedPreferences prefs = this.getSharedPreferences(CHILD_LIST_TAG, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // Saves the class into json.
        Gson gson = new Gson();
        editor.putString(CHILD_LIST, gson.toJson(childManager));
        editor.apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getSupportActionBar() != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        getMenuInflater().inflate(R.menu.menu_edit_child, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.go_back_edit_child:
                this.finish();
                return true;
            case R.id.main_menu:
                Intent i = MainActivity.makeIntent(EditChildActivity.this);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
