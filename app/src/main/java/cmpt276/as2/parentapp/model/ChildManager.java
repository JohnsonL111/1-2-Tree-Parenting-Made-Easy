package cmpt276.as2.parentapp.model;

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

    // Singleton support.
    private static ChildManager instance;

    // Get instance of a singleton ChildManager.
    public static ChildManager getInstance() {
        if (instance == null) {
            instance = new ChildManager();
        }
        return instance;
    }

    public void addChild(String name) {
        if (!checkIfNameExist(name)) {
            Child childToAdd = new Child(name);
            childList.add(childToAdd);
        }
    }

    public void removeChild(String name) {
        // Guard if for sure child does not exist.
        if (!checkIfNameExist(name)) {
            return;
        }

        int numOfChildren = childList.size();
        for (int i = 0; i < numOfChildren; ++i) {
            String currChildName = childList.get(i).getName();
            if (currChildName.equals(name)) {
                childList.remove(i);
            }
        }
    }

    public List<Child> getChildList() {
        return childList;
    }

    private boolean checkIfNameExist(String name) {
        int numOfChildren = childList.size();
        boolean nameExist = false;
        for (int i = 0; i < numOfChildren; ++i) {
            String currChildName = childList.get(i).getName();
            if (currChildName.equals(name)) {
                nameExist = true;
            }
        }

        return nameExist;
    }
}
