package cmpt276.as2.parentapp.model.State;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import cmpt276.as2.parentapp.R;
import cmpt276.as2.parentapp.UI.BreathActivity;

/**
 * Sixth and last state. Deals mostly with textview changes and logistics.
 */
public class DoneExhaleState extends State {

    public DoneExhaleState(BreathActivity context) {
        super(context);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onClickHandler(BreathActivity context) {
        context.stopBreatheSounds();
        Button inBtn = context.findViewById(R.id.breath_main_btn);
        inBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
        if(context.getNumOfBreathLeft() > 0) {
            context.setState(new WaitingToInhaleState(context));
        }
        else{
            context.setState(new ReadyToStartState(context));
        }
    }
}
