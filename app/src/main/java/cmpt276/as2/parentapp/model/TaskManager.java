package cmpt276.as2.parentapp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * - The task manager will add, remove, and edit tasks.
 * - The task manager will hold a list of task using an arraylist.
 * - Task manager will update the children names in the tasks when a child's name
 * has been removed or edited from the child list.
 */
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
        listOfTasks.get(taskNumber).setTaskTitle(newTaskName);
    }

    public List<Task> getListOfTasks() {
        return listOfTasks;
    }

    public void updateChildNameForNewTask(String deletedChildName, String newChildName) {
        if (childNameForNewTask.equals(deletedChildName)) {
            childNameForNewTask = newChildName;
        }
    }

    public void editChildName(String old, String newName) {
        for (Task task : listOfTasks) {
            task.editChildName(old, newName);
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
            for (int i = 0; i < childList.size(); i++) {
                if (childList.get(i).getName().equals(childNameForNewTask)) {
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

    public void updateTaskNameHistoryList(String oldName, String newName) {
        for (int i = 0; i < listOfTasks.size(); i++) {
            listOfTasks.get(i).updateHistoryChildName(oldName, newName);
        }
    }

    public List<String> getChildHistoryList(int taskNumber) {
        return listOfTasks.get(taskNumber).getChildrenHistoryList();
    }

    public List<String> getDateHistoryList(int taskNumber) {
        return listOfTasks.get(taskNumber).getDateHistoryList();
    }
}
