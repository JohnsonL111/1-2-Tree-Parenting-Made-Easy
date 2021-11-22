package cmpt276.as2.parentapp.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

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
    public static final String CHILD_LIST = "childListNames";
    public static final String CHILD_LIST_TAG = "childList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_child);

        childManager = getChildData();
        setTitle(R.string.editChildActivityTitle);
        populateListView();
        startListViewClickable();

        addChild();
        removeChild();
    }

    // Citation: https://www.youtube.com/watch?v=WRANgDgM2Zg (Brian Fraser: list view with images/text)
    private void populateListView() {
        ArrayAdapter<Child> adapter = new myListAdapter();
        ListView list = findViewById(R.id.childListView);
        list.setAdapter(adapter);
    }

    private class myListAdapter extends ArrayAdapter<Child> {
        public myListAdapter() {
            super(EditChildActivity.this, R.layout.child_view, childManager.getChildList());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Ensures we have a view to work with (non-null)
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.child_view, parent, false);
            }

            List<Child> currentChildren = childManager.getChildList();

            // Find child to work with
            Child currentChild = currentChildren.get(position);

            // Fill the icon
            ImageView imageView = (ImageView) itemView.findViewById(R.id.childIcon);
            imageView.setImageBitmap(decodeBase64(currentChild.getIcon()));

            // Fill the name
            TextView childNameSlot = (TextView) itemView.findViewById(R.id.childName);
            childNameSlot.setText(currentChild.getName());

            return itemView;
        }
    }

    // Configure child object to be clickable.
    private void startListViewClickable() {
        ListView list = findViewById(R.id.childListView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                Intent appInfo = EditSingleChildActivity.makeIntent(
                        getApplicationContext(),
                        childManager.getChildList().get(position).getName(),
                        position,
                        true,
                        decodeBase64(childManager.getChildList().get(position).getIcon()));
                startActivity(appInfo);
                saveChildData();
            }
        });
    }

    private void addChild() {
        Button addChildButton = findViewById(R.id.addChildButton);

        addChildButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Switches to edit single child activity.
                EditText childNameField = findViewById(R.id.addChildBox);
                String childName = childNameField.getText().toString();

                Intent appInfo = EditSingleChildActivity.makeIntent(
                        getApplicationContext(),
                        childName,
                        -1,
                        false,
                        null);
                startActivity(appInfo);
                saveChildData();
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
                    populateListView();
                    childNameSlot.setText("");
                } else {
                    Toast.makeText(EditChildActivity.this, "Child does not exist!", Toast.LENGTH_SHORT).show();
                }
                populateListView();
                saveChildData();
            }
        });
    }

    // Maintain same list when coming back to the activity.
    @Override
    protected void onResume() {
        super.onResume();

        // Reset the add name slot.
        EditText childNameSlot = findViewById(R.id.addChildBox);
        childNameSlot.setText("");

        childManager = getChildData();
        populateListView();
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, EditChildActivity.class);
    }

    private ChildManager getChildData() {
        SharedPreferences prefs = this.getSharedPreferences(CHILD_LIST_TAG, MODE_PRIVATE);
        if (!prefs.contains(CHILD_LIST)) {
            return ChildManager.getInstance();
        } else {
            Gson gson = new Gson();
            return gson.fromJson(prefs.getString(CHILD_LIST, ""), ChildManager.class);
        }
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

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
