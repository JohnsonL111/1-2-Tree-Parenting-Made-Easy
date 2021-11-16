package cmpt276.as2.parentapp.model;


import java.util.List;

public class Task {
    private String childName;
    private String taskTitle;
    private List<Child> childList;

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

    private void getChildList() {
        ChildManager childManager = ChildManager.getInstance();
        childList = childManager.getChildList();
    }

    private int getChildPositionInList() {
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

    public void updateNextChildToDoTask() {
        getChildList();
        int childPositionInTheList = getChildPositionInList();
        if (!childList.isEmpty()) {
            int childListSize = childList.size();
            int nextChildPositionInTheList = (childPositionInTheList + 1) % childListSize;
            this.childName = childList.get(nextChildPositionInTheList).getName();
        } else {
            this.childName = "";
        }
    }
}