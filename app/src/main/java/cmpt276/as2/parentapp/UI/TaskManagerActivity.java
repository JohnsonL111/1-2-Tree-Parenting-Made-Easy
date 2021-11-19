package cmpt276.as2.parentapp.UI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import cmpt276.as2.parentapp.R;
import cmpt276.as2.parentapp.model.ChildManager;
import cmpt276.as2.parentapp.model.TaskMenuAdapter;

public class TaskManagerActivity extends AppCompatActivity {

    private ChildManager childManager;
    private TaskMenuAdapter adapter;
    private RecyclerView taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_manager);

        taskList = findViewById(R.id.task_list);
        getChildManager();
        populateList();
        setAddTask();
    }

    private void getChildManager() {
        SharedPreferences prefs = this.getSharedPreferences(EditChildActivity.CHILD_LIST_TAG, MODE_PRIVATE);
        if (!prefs.contains(EditChildActivity.CHILD_LIST)) {
            childManager = ChildManager.getInstance();
        } else {
            Gson gson = new Gson();
            childManager = gson.fromJson(prefs.getString(EditChildActivity.CHILD_LIST, ""), ChildManager.class);
        }
    }

    private void populateList() {
        adapter = new TaskMenuAdapter(this, childManager.task.getListOfTasks(), childManager.getChildList());

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        taskList.setLayoutManager(mLayoutManager);
        DividerItemDecoration decoration = new DividerItemDecoration(taskList.getContext(), mLayoutManager.getOrientation());
        decoration.setDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.recyclerview_divider, null));
        taskList.setAdapter(adapter);
        taskList.addItemDecoration(decoration);
    }

    private void setAddTask()
    {
        FloatingActionButton btn = findViewById(R.id.add_task_btn);
        btn.setOnClickListener(view -> showAddTaskMenu());
    }

    private void showAddTaskMenu()
    {
        View v = LayoutInflater.from(this).inflate(R.layout.task_edit, null);

        EditText taskNameIn = v.findViewById(R.id.edit_task_input);

        AlertDialog.Builder build = new AlertDialog.Builder(this).setView(v)
                .setTitle("Add Task")
                .setPositiveButton("Add", (dialogInterface, i) ->
                {
                    if(!taskNameIn.getText().toString().isEmpty()){
                        childManager.addTask(taskNameIn.getText().toString());
                        saveResult();
                        refresh();
                    } else {
                        Toast.makeText(this, "You need to enter a name for task", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.cancel());

        Dialog dialog = build.create();
        dialog.show();
    }

    private void saveResult() {
        SharedPreferences prefsPub = this.getSharedPreferences(EditChildActivity.CHILD_LIST_TAG, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefsPub.edit();
        Gson gson = new Gson();
        editor.putString(EditChildActivity.CHILD_LIST, gson.toJson(childManager));
        editor.apply();
    }

    private void refresh() {
        getChildManager();
        populateList();
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, TaskManagerActivity.class);
    }
}