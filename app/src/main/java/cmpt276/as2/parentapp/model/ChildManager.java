package cmpt276.as2.parentapp.model;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages all the children
 * - stores children and their names
 * - adds new children by name
 * - removes children by name
 */
public class ChildManager {
    private List<Child> childList = new ArrayList<>();
    public TaskManager task = new TaskManager();
    // Singleton support.
    private static ChildManager instance;
    public CoinFlip coinFlip = new CoinFlip();

    // Get instance of a singleton ChildManager.
    public static ChildManager getInstance() {
        if (instance == null) {
            instance = new ChildManager();
        }
        return instance;
    }

    public void addChild(String name, String icon) {
        Child childToAdd = new Child(name, icon);
        if (!name.equals("") && !checkIfNameExist(name)) {
            childList.add(childToAdd);
        }
        coinFlip.addChild(childToAdd);
        task.checkForUpdate(childList);
    }

    public void removeChild(String name) {

        // Guard if for sure child does not exist.
        if (!checkIfNameExist(name)) {
            return;
        }
        coinFlip.removeChild(name);

        // Get the index of the child to remove
        int numChildren = childList.size();
        for (int i = 0; i < numChildren; ++i) {

            String currChildName = childList.get(i).getName();
            if (currChildName.equals(name)) {
                String nextChildName = "";
                if (numChildren > 1) {
                    int nextChildPosition = (i + 1) % numChildren;
                    nextChildName = childList.get(nextChildPosition).getName();
                }
                childList.remove(i);
                updateTaskChildNames(currChildName, nextChildName);
                break;
            }
        }
    }

    public List<Child> getChildList() {
        return childList;
    }

    public boolean checkIfNameExist(String name) {
        boolean nameExist = false;

        int numOfChildren = childList.size();
        for (int i = 0; i < numOfChildren; ++i) {
            String currChildName = childList.get(i).getName();
            if (currChildName.equals(name)) {
                nameExist = true;
                break;
            }
        }
        return nameExist;
    }

    public void updateTaskChildNames(String currChildName, String newChildName) {
        task.editTasksWithDeletedChildName(currChildName, newChildName);
        task.updateChildNameForNewTask(currChildName, newChildName);
        coinFlip.editChildName(currChildName, newChildName);
    }

    public void addTask(String taskName)
    {
        task.addTask(taskName, childList);
    }
}
