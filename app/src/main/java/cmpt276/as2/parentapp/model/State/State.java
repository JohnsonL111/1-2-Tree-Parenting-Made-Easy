package cmpt276.as2.parentapp.model.State;

import cmpt276.as2.parentapp.UI.BreathActivity;

/**
 * Defines the general State class. Other states implement the methods in this class.
 */
public abstract class State {
    private BreathActivity context;

    public State(BreathActivity context) {
        this.context = context;
    }

    public void onClickHandler(BreathActivity context) {
    }

    ;

    public void helpTextHandler(BreathActivity context) {
    }

    ;

    public void timingHandler(BreathActivity context) {
    }

    ;
}
