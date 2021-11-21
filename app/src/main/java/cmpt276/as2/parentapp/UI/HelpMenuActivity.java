package cmpt276.as2.parentapp.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import cmpt276.as2.parentapp.R;


public class HelpMenuActivity extends AppCompatActivity {
    TextView sourceText;

    public static Intent makeIntent(Context context) {
        return new Intent(context, HelpMenuActivity.class);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_menu);
        setDeveloperTextView();
        setSourcesTextView();


    }


    private void setDeveloperTextView() {
        TextView DeveloperName = findViewById(R.id.DeveloperName);


        DeveloperName.setText(getString(R.string.repoManager) +
                getString(R.string.productOwner) +
                getString(R.string.teamMember) +
                getString(R.string.scrumMaster));
    }

    private void setSourcesTextView() {
        sourceText = findViewById(R.id.sourceText);

        SpannableString span_source = createSpanSource();
        sourceText.setText(span_source);

        //Set link movement to the hyperlink text
        sourceText.setMovementMethod(LinkMovementMethod.getInstance());
    }

    //Return a spannable string for each source
    private SpannableString createSpanSource() {
        String source = getString(R.string.timerBackground)
                + getString(R.string.timerBackSound)
                + getString(R.string.treeForIcon)
                + getString(R.string.menuBackGround)
                + getString(R.string.defaultPhoto)
                + getString(R.string.menuButton)
                + getString(R.string.helpButton);

        //Initialize spannable String for different hyperlink texts
        SpannableString span_source = new SpannableString(source);

        //Set Click Listeners
        ClickableSpan span_timer_bg = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Intent i = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(getString(R.string.timer_background)));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        };

        ClickableSpan span_timer_bs = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Intent i = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(getString(R.string.timer_backsound)));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        };

        ClickableSpan span_tree_icon = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Intent i = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(getString(R.string.tree_icon)));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        };

        ClickableSpan span_menu_bg = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Intent i = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(getString(R.string.menu_background)));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        };

        ClickableSpan span_default_photo = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Intent i = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(getString(R.string.default_photo)));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        };
        ClickableSpan span_menu_btn = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Intent i = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(getString(R.string.menu_button)));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        };
        ClickableSpan span_help_btn = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Intent i = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(getString(R.string.help_button)));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        };

        //Set span
        span_source.setSpan(span_timer_bg, 0, 17, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span_source.setSpan(span_timer_bs, 18, 33, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span_source.setSpan(span_tree_icon, 34, 47, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span_source.setSpan(span_menu_bg, 48, 63, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span_source.setSpan(span_default_photo, 64, 77, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span_source.setSpan(span_menu_btn, 78, 89, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span_source.setSpan(span_help_btn, 90, 101, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return span_source;
    }
}