package cmpt276.as2.parentapp.model.State;

import cmpt276.as2.parentapp.UI.BreathActivity;

public abstract class State {
    private BreathActivity context;
    public State(BreathActivity context) {
        this.context = context;
    }
    public void onClickHandler(BreathActivity context){};
    public void helpTextHandler(BreathActivity context){};
    public void timingHandler(BreathActivity context){};
}
