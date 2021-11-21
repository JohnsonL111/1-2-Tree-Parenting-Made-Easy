package cmpt276.as2.parentapp.UI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.Objects;

import cmpt276.as2.parentapp.R;
import cmpt276.as2.parentapp.model.Child;
import cmpt276.as2.parentapp.model.ChildManager;
import cmpt276.as2.parentapp.model.Task;
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
        setUpSwipe();
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
        decoration.setDrawable(Objects.requireNonNull(ResourcesCompat.getDrawable(getResources(), R.drawable.recyclerview_divider, null)));
        taskList.setAdapter(adapter);
        taskList.addItemDecoration(decoration);

        setCallBack();
    }

    private void setCallBack() {
        TaskMenuAdapter.clickObserverEditTask obs = () -> showEditTask(adapter.getPick());
        TaskMenuAdapter.clickObserverViewDetail obsDetail = () -> showDetail(adapter.getPick());
        adapter.registerEditTask(obs);
        adapter.registerViewDetail(obsDetail);
    }

    private void showDetail(int index) {
        View v = LayoutInflater.from(this).inflate(R.layout.task_pop_up, null);
        ImageView childPhoto = v.findViewById(R.id.task_detail_child_photo);
        TextView taskTitle = v.findViewById(R.id.task_detail_task_name);
        TextView childName = v.findViewById(R.id.task_detail_child_name);
        Button doneBtn = v.findViewById(R.id.task_detail_done_btn);


        if (childManager.getChildList().size() > 0) {
            Child child = childManager.getChildList().get(0);

            for (int i = 0; i < childManager.getChildList().size(); i++) {
                child = childManager.getChildList().get(i);
                if (child.getName().equals(childManager.task.getListOfTasks().get(index).getChildName())) {
                    break;
                }
            }
            childPhoto.setImageBitmap(EditChildActivity.decodeBase64(child.getIcon()));



        } else {
            childPhoto.setImageResource(R.drawable.default_photo_nobody);
        }

        Task tmp = childManager.task.getListOfTasks().get(index);
        taskTitle.setText(tmp.getTaskTitle());
        childName.setText(tmp.getChildName());

        AlertDialog.Builder build = new AlertDialog.Builder(this).setView(v)
                .setTitle(R.string.task_detail)
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> dialogInterface.cancel());

        Dialog dialog = build.create();
        dialog.show();

        doneBtn.setText(R.string.done);
        doneBtn.setOnClickListener(view ->
        {
            childManager.task.updateNextChildToDoTask(index, childManager.getChildList());
            saveResult();
            refresh();
            dialog.dismiss();
        });
    }

    private void setUpSwipe() {
        ItemTouchHelper.SimpleCallback deleteTaskCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.LEFT) {
                    childManager.task.removeTask(viewHolder.getAdapterPosition());
                    saveResult();
                    refresh();
                }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(deleteTaskCallback);
        itemTouchHelper.attachToRecyclerView(taskList);
    }

    private void setAddTask() {
        FloatingActionButton btn = findViewById(R.id.add_task_btn);
        btn.setOnClickListener(view -> showAddTaskMenu());
    }


    private void showAddTaskMenu() {
        View v = LayoutInflater.from(this).inflate(R.layout.task_edit, null);
        EditText taskNameIn = v.findViewById(R.id.edit_task_input);
        AlertDialog.Builder build = new AlertDialog.Builder(this).setView(v)
                .setTitle(R.string.add_task)
                .setPositiveButton(R.string.add, (dialogInterface, i) ->
                {
                    if (!taskNameIn.getText().toString().isEmpty()) {
                        childManager.addTask(taskNameIn.getText().toString());
                        saveResult();
                        refresh();
                    } else {
                        Toast.makeText(this, getString(R.string.add_edit_warning), Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> dialogInterface.cancel());

        Dialog dialog = build.create();
        dialog.show();
    }

    private void showEditTask(int index) {
        View v = LayoutInflater.from(this).inflate(R.layout.task_edit, null);
        EditText taskNameIn = v.findViewById(R.id.edit_task_input);
        AlertDialog.Builder build = new AlertDialog.Builder(this).setView(v)
                .setTitle(R.string.edit_task)
                .setPositiveButton(R.string.edit, (dialogInterface, i) ->
                {
                    if (!taskNameIn.getText().toString().isEmpty()) {
                        childManager.task.editTaskTitle(taskNameIn.getText().toString(), index);
                        saveResult();
                        refresh();
                    } else {
                        Toast.makeText(this, getString(R.string.add_edit_warning), Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> dialogInterface.cancel());

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_coin_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.go_back_coin_history:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}