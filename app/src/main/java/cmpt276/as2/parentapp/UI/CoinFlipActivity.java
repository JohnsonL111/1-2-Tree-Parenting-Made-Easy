package cmpt276.as2.parentapp.UI;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import cmpt276.as2.parentapp.R;
import cmpt276.as2.parentapp.model.CoinFlip;
import cmpt276.as2.parentapp.model.CoinFlipMenuAdapter;

public class CoinFlipActivity extends AppCompatActivity
{
    private CoinFlip coinFlip;
    public static final String COIN_TAG = "CoinToss";
    public static final String COIN_HISTORY = "TossHistory";
    public static final String COIN_LAST_PICKER = "LastPicker";
    public static final String COIN_PICKER_LIST = "PickerList";

    private ViewPager2 viewPager2;
    private CoinFlipMenuAdapter adapter;
    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_flip);

        coinFlip = new CoinFlip(
                getChildList(),
                getData(COIN_PICKER_LIST),
                getData(COIN_HISTORY),
                getLstPicker(COIN_LAST_PICKER));

        videoView = findViewById(R.id.flip);

        viewPager2 = findViewById(R.id.coin_viewpager2);
        adapter = new CoinFlipMenuAdapter(this);
        viewPager2.setAdapter(adapter);

        setBoardCallBack();
    }

    @Override
    protected void onPostResume()
    {
        super.onPostResume();
        updateChildList();
    }

    private void setBoardCallBack()
    {
        CoinFlipMenuAdapter.clickObserver obs = () -> showVideo();
        adapter.registerChangeCallBack(obs);
    }

    private void showVideo()
    {
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.full_lfps);
        videoView.setVideoURI(videoUri);

        viewPager2.setVisibility(View.INVISIBLE);
        videoView.setZOrderOnTop(true);
        videoView.requestFocus();
        videoView.start();
    }

    private String[] getChildList()
    {
        //
        return new String[]{"aaa", "aa"};
    }

    private String[] getData(String tag)
    {
        SharedPreferences prefs = this.getSharedPreferences(COIN_TAG, MODE_PRIVATE);
        if(!prefs.contains(tag))
        {
            return new String[]{""};
        }
        else
        {
            Set<String> newSet = prefs.getStringSet(tag, null);
            return newSet.toArray(new String[newSet.size()]);
        }
    }

    private String getLstPicker(String tag)
    {
        SharedPreferences prefs = this.getSharedPreferences(COIN_TAG, MODE_PRIVATE);
        if(!prefs.contains(tag))
        {
            return "";
        }
        else
        {
            return prefs.getString(tag,"");
        }
    }

    private void updateChildList()
    {
        coinFlip.setChildrenList(new ArrayList<>(Arrays.asList(getChildList())));
    }
}