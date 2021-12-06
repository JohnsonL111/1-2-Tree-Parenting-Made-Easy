package cmpt276.as2.parentapp.model.State;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import cmpt276.as2.parentapp.R;
import cmpt276.as2.parentapp.UI.BreathActivity;

/**
 * First state in the breath process. Sets up cycle and transitions into WaitingToInhale state.
 */
public class ReadyToStartState extends State {

    public ReadyToStartState(BreathActivity context) {
        super(context);
    }

    @Override
    public void helpTextHandler(BreathActivity context) {
        String buttonText = context.getString(R.string.begin_button_text);
        String helpMsgText = context.getString(R.string.intial_message_breath);
        context.setText(buttonText, helpMsgText);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onClickHandler(BreathActivity context) {
        Button mainBtn = context.findViewById(R.id.breath_main_btn);
        context.setUpOption();
        context.resetNumBreathLeft();
        mainBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        mainBtn.setPressed(true);
                        mainBtn.setPressed(false);
                        context.setState(new WaitingToInhaleState(context));
                        return true;
                }
                return false;
            }
        });

    }
}
