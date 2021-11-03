package cmpt276.as2.parentapp.model;

/**
 * Encapsulates a single child into a class.
 */
public class Child {
    private String name;

    public Child(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Child name: " + name;
    }
}
