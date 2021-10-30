package cmpt276.as2.parentapp.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.gson.Gson;

import java.util.ArrayList;

import cmpt276.as2.parentapp.R;
import cmpt276.as2.parentapp.model.CoinFlip;
import cmpt276.as2.parentapp.model.CoinFlipMenuAdapter;

public class CoinFlipActivity extends AppCompatActivity
{
    private CoinFlip coinFlip;
    public static final String COIN_TAG = "CoinToss";
    public static final String COIN_HISTORY = "TossHistory";
    public static final String COIN_PICKER_LIST = "PickerList";

    private ViewPager2 viewPager2;
    private CoinFlipMenuAdapter adapter;
    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_flip);

        coinFlip = new CoinFlip(getData(COIN_PICKER_LIST), getData(COIN_HISTORY), this);

        videoView = findViewById(R.id.flip);

        viewPager2 = findViewById(R.id.coin_viewpager2);

        adapter = new CoinFlipMenuAdapter(this,
                getString(R.string.coin_toss_picker,coinFlip.getPickerList().get(0)),
                getResources().getStringArray(R.array.coin_two_side_name));

        viewPager2.setAdapter(adapter);

        setBoardCallBack();
    }

    @Override
    protected void onPostResume()
    {
        super.onPostResume();
        checkUpdate();
    }

    private void setBoardCallBack()
    {
        CoinFlipMenuAdapter.clickObserver obs = () -> tossCoin();
        adapter.registerChangeCallBack(obs);
    }

    private void tossCoin()
    {
        coinFlip.setResult(adapter.getResult());
        coinFlip.tossTheCoin();
        
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.full_lfps);
        videoView.setVideoURI(videoUri);

        viewPager2.setVisibility(View.INVISIBLE);
        videoView.setZOrderOnTop(true);
        videoView.requestFocus();
        videoView.start();

        saveResult();
        showResult();
    }

    private String[] getChildList()
    {
        return new String[]{""};
    }

    private ArrayList getData(String tag)
    {
        SharedPreferences prefs = this.getSharedPreferences(COIN_TAG, MODE_PRIVATE);
        if(!prefs.contains(tag))
        {
            return new ArrayList<>();
        }
        else
        {
            Gson gson = new Gson();
            return gson.fromJson(prefs.getString(tag, ""), ArrayList.class);
        }
    }

    private void showResult()
    {
        if(coinFlip.getPickerList().isEmpty())
        {

        }
        else
        {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_coin_acitivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.go_back:
                this.finish();
                return true;
            case R.id.history:
                Intent intent = CoinFlipHistoryActivity.makeIntent(this);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveResult()
    {
        coinFlip.saveResult();
        SharedPreferences prefs = this.getSharedPreferences(COIN_TAG, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Gson gson = new Gson();
        editor.putString(COIN_HISTORY, gson.toJson(coinFlip.getHistory()));
        editor.putString(COIN_PICKER_LIST,gson.toJson(coinFlip.getPickerList()));
        editor.apply();
    }

    private void checkUpdate()
    {
        coinFlip.setSavedPickers(getData(COIN_PICKER_LIST));
    }

    public static Intent makeIntent(Context context)
    {
        return new Intent(context, CoinFlipActivity.class);
    }


}