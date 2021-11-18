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
    public CoinFlip coinFlip;

    // Get instance of a singleton ChildManager.
    public static ChildManager getInstance() {
        if (instance == null) {
            instance = new ChildManager();
        }
        return instance;
    }

    private ChildManager()
    {
        coinFlip = new CoinFlip();
    }

    public void addChild(String name) {
        Child childToAdd = new Child(name);
        if (!name.equals("")) {
            childList.add(childToAdd);
            coinFlip.addChild(childToAdd);
        }
    }

    public void removeChild(String name) {
        // Guard if for sure child does not exist.
        if (!checkIfNameExist(name)) {
            return;
        }
        // Get the index of the child to remove
        int numChildren = childList.size();
        for (int i = 0; i < numChildren; ++i) {
            String currChildName = childList.get(i).getName();
            if (currChildName.equals(name)) {
                childList.remove(i);
                break;
            }
        }

        for(int i = 0; i< numChildren; i++)
        {
            String current = coinFlip.getPickerList().get(i).getName();
            if(current.equals(name))
            {
                coinFlip.removeChild(i);
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
}
