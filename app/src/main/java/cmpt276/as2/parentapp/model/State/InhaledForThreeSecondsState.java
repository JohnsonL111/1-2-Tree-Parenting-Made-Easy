package cmpt276.as2.parentapp.model.State;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import cmpt276.as2.parentapp.R;
import cmpt276.as2.parentapp.UI.BreathActivity;

public class InhaledForThreeSecondsState extends State{

    public InhaledForThreeSecondsState(BreathActivity context) {
        super(context);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onClickHandler(BreathActivity context) {
        Button btn = context.findViewById(R.id.breath_main_btn);
        btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
        context.setState(new ExhaleState(context));
    }
}
