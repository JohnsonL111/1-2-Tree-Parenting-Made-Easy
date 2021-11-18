package cmpt276.as2.parentapp.UI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.gson.Gson;

import cmpt276.as2.parentapp.R;
import cmpt276.as2.parentapp.model.Child;
import cmpt276.as2.parentapp.model.ChildManager;
import cmpt276.as2.parentapp.model.CoinFlipMenuAdapter;

/**
 * The activity to handle th coin flip ui, will let user chose between head and tail and show a animation of coin toss then show the result.
 */
public class CoinFlipActivity extends AppCompatActivity {

    private ChildManager childManager;

    private ViewPager2 viewPager2;
    private CoinFlipMenuAdapter adapter;
    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_flip);

        getChildManager();
        videoView = findViewById(R.id.flip);
        viewPager2 = findViewById(R.id.coin_viewpager2);

        setPageAdapter();
        viewPager2.setAdapter(adapter);

        setBoardCallBack();
    }

    private void getChildManager() {
        SharedPreferences prefs = this.getSharedPreferences(EditChildActivity.CHILD_LIST_TAG, MODE_PRIVATE);
        if (!prefs.contains(EditChildActivity.CHILD_LIST)) {
            childManager = ChildManager.getInstance();
        } else {
            Gson gson = new Gson();
            childManager = gson.fromJson(prefs.getString(EditChildActivity.CHILD_LIST, ""), ChildManager.class);
        }
    }

    private void setPageAdapter() {
        if (childManager.coinFlip.getPickerList().size() != 0) {
            adapter = new CoinFlipMenuAdapter(this
                    ,childManager.coinFlip.getPickerList().get(0)
                    ,getResources().getStringArray(R.array.coin_two_side_name));
        } else {
            adapter = new CoinFlipMenuAdapter(this,
                    new Child(""),
                    getResources().getStringArray(R.array.coin_two_side_name));
        }
    }

    private void setBoardCallBack() {
        CoinFlipMenuAdapter.clickObserverAnimation obs = () -> tossCoin();
        CoinFlipMenuAdapter.clickObserverEditChild obsEdit = () ->
        {
            Intent childIntent = EditChildActivity.makeIntent(CoinFlipActivity.this);
            startActivity(childIntent);
        };

        CoinFlipMenuAdapter.clickObserverChangeOrder obsOrder = () ->
        {
            Toast.makeText(this, "ChangeOrder", Toast.LENGTH_SHORT).show();
            /**
             *
             */
        };

        adapter.registerChangeCallBack(obs);
        adapter.registerChangeCallBack(obsEdit);
        adapter.registerChangeCallBack(obsOrder);

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        reset();
    }

    private void tossCoin() {
        childManager.coinFlip.setResult(adapter.getResult());
        childManager.coinFlip.tossTheCoin();

        Uri videoUri;
        if (childManager.coinFlip.getResultInt() == 0) {
            videoUri = Uri.parse(getString(R.string.resources) + getPackageName() + "/" + R.raw.full_h1);
        } else {
            videoUri = Uri.parse(getString(R.string.resources) + getPackageName() + "/" + R.raw.full_t1);
        }

        videoView.setVideoURI(videoUri);
        videoView.setVisibility(View.VISIBLE);
        viewPager2.setVisibility(View.INVISIBLE);
        videoView.setZOrderOnTop(true);
        videoView.requestFocus();
        videoView.start();

        saveResult();

        videoView.setOnCompletionListener(mediaPlayer -> showResult());
    }

    private void showResult() {
        View v = LayoutInflater.from(this).inflate(R.layout.coin_flip_result, null);
        TextView textView = v.findViewById(R.id.coin_flip_result_text);
        ImageView imageView = v.findViewById(R.id.coin_flip_result_image);

        textView.setText(childManager.coinFlip.getResult(this));
        if (childManager.coinFlip.pickerWin()) {
            imageView.setImageResource(R.drawable.win);
        } else {
            imageView.setImageResource(R.drawable.loss);
        }

        AlertDialog.Builder build = new AlertDialog.Builder(this).setView(v)
                .setTitle(R.string.Result)
                .setPositiveButton(R.string.result_repeat_option, (dialogInterface, i) -> reset())
                .setNegativeButton(R.string.result_leave_option, (dialogInterface, i) -> this.finish());

        Dialog dialog = build.create();
        dialog.show();
    }

    private void reset() {
        viewPager2.setVisibility(View.VISIBLE);
        videoView.setVisibility(View.INVISIBLE);

        getChildManager();
        setPageAdapter();

        viewPager2.setAdapter(adapter);
        setBoardCallBack();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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

    private void saveResult() {
        childManager.coinFlip.saveResult(this);
        SharedPreferences prefs = this.getSharedPreferences(EditChildActivity.CHILD_LIST_TAG, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Gson gson = new Gson();
        editor.putString(EditChildActivity.CHILD_LIST, gson.toJson(childManager));
        editor.apply();
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, CoinFlipActivity.class);
    }
}