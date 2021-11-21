package cmpt276.as2.parentapp.model;

import android.graphics.Bitmap;

/**
 * Encapsulates a single child and its functionality into a class.
 */
public class Child {
    private String name;
    private String icon;

    public Child(String name, String icon) {
        this.name = name;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        name = newName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "Child name: " + name;
    }


}
