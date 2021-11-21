package cmpt276.as2.parentapp.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class TaskManager {


    private List<Task> listOfTasks = new ArrayList<>();
    private String childNameForNewTask = "";

    public void addTask(String taskTitle, List<Child> childList) {
        getChildNameForNewTask(childList);
        Task task = new Task(taskTitle, childNameForNewTask);
        listOfTasks.add(task);
    }

    public void removeTask(int taskNumber) {
        listOfTasks.remove(taskNumber);
    }

    public void editTaskTitle(String newTaskName, int taskNumber) {
        String childName = listOfTasks.get(taskNumber).getChildName();
        Task editedTask = new Task(newTaskName, childName);
        listOfTasks.set(taskNumber, editedTask);
    }

    public List<Task> getListOfTasks() {
        return listOfTasks;
    }

    public void editTasksWithDeletedChildName(String childNameDeleted, String newChildName) {
        int taskSize = listOfTasks.size();
        if (!listOfTasks.isEmpty()) {
            for (int i = 0; i < taskSize; i++) {
                String childName = listOfTasks.get(i).getChildName();
                if (childName.equals(childNameDeleted)) {
                    String taskTitle = listOfTasks.get(i).getTaskTitle();
                    Task task = new Task(taskTitle, newChildName);
                    listOfTasks.set(i, task);
                }
            }
        }
    }

    public void updateChildNameForNewTask(String deletedChildName, String newChildName) {
        if (childNameForNewTask.equals(deletedChildName)) {
            childNameForNewTask = newChildName;
        }
    }

    //updates current task child name to the next child name
    public void updateNextChildToDoTask(int taskNumber, List<Child> childList) {
        Task task = listOfTasks.get(taskNumber);
        task.updateNextChildToDoTask(childList);
    }

    private void getChildNameForNewTask(List<Child> childList) {
        if (!childList.isEmpty()) {
            int position = 0;
            for(int i = 0; i < childList.size(); i++) {
                if(childList.get(i).getName().equals(childNameForNewTask)) {
                    position = (i + 1) % childList.size();
                }
            }
            childNameForNewTask = childList.get(position).getName();
        }
    }

    public void checkForUpdate(List<Child> childList) {
        for (int i = 0; i < listOfTasks.size(); i++) {
            if (listOfTasks.get(i).getChildName().isEmpty()) {
                updateNextChildToDoTask(i, childList);
            }
        }
    }
}
