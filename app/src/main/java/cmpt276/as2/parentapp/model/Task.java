package cmpt276.as2.parentapp.model;


import java.util.List;

/**
 * -The task class holds information about a single task such as the task title,and the child's
 * name.
 * - The task class will update the child's name when the task has been completed
 */

public class Task {
    private String childName;
    private String taskTitle;

    public Task(String taskTitle, String childName) {
        this.taskTitle = taskTitle;
        this.childName = childName;
    }

    public String getChildName() {
        return childName;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    private int getChildPositionInList(List<Child> childList) {
        int childPosition = -1;
        int childListSize = childList.size();
        for (int i = 0; i < childListSize; i++) {
            Child child = childList.get(i);
            String childNameInTheList = child.getName();
            if (childNameInTheList.equals(this.childName)) {
                childPosition = i;
            }
        }
        return childPosition;
    }

    public void updateNextChildToDoTask(List<Child> childList) {
        int childPositionInTheList = getChildPositionInList(childList);
        if (!childList.isEmpty()) {
            int childListSize = childList.size();
            int nextChildPositionInTheList = (childPositionInTheList + 1) % childListSize;
            this.childName = childList.get(nextChildPositionInTheList).getName();
        } else {
            this.childName = "";
        }
    }
}