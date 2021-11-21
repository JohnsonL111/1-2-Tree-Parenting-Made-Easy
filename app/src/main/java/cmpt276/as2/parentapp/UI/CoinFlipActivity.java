package cmpt276.as2.parentapp.UI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;

import cmpt276.as2.parentapp.R;
import cmpt276.as2.parentapp.model.ChangeOrderMenuAdapter;
import cmpt276.as2.parentapp.model.Child;
import cmpt276.as2.parentapp.model.ChildManager;
import cmpt276.as2.parentapp.model.CoinFlipMenuAdapter;

/**
 * The activity to handle th coin flip ui, will let user chose between head and tail and show a animation of coin toss then show the result.
 * Now also contain a menu allow user to manually select current picker,
 * if user chose nobody, then it only toss the coin and show the result and the order of picker will not change
 */
public class CoinFlipActivity extends AppCompatActivity {

    private ChildManager childManager;

    private ViewPager2 viewPager2;
    private CoinFlipMenuAdapter adapter;
    private ChangeOrderMenuAdapter adapterChangeOrder;
    private ImageView flip;
    private boolean saveGame = true;
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_flip);

        getChildManager();
        flip = findViewById(R.id.flip_animation);

        viewPager2 = findViewById(R.id.coin_viewpager2);

        setPageAdapter();
        viewPager2.setAdapter(adapter);

        setBoardCallBack();

        if (childManager.getChildList().isEmpty()) {
            tossCoin();
        }
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
                    , childManager.coinFlip.getPickerList().get(0)
                    , getResources().getStringArray(R.array.coin_two_side_name));
        } else {
            adapter = new CoinFlipMenuAdapter(this
                    , new Child("", null)
                    , getResources().getStringArray(R.array.coin_two_side_name));
        }
    }

    private void setBoardCallBack() {
        CoinFlipMenuAdapter.clickObserverAnimation obs = () -> tossCoin();

        CoinFlipMenuAdapter.clickObserverEditChild obsEdit = () ->
        {
            Intent childIntent = EditChildActivity.makeIntent(CoinFlipActivity.this);
            startActivity(childIntent);
        };

        CoinFlipMenuAdapter.clickObserverChangeOrder obsOrder = () -> showChangeOrderMenu();

        adapter.registerChangeCallBack(obs);
        adapter.registerChangeCallBack(obsEdit);
        adapter.registerChangeCallBack(obsOrder);
    }

    private void tossCoin() {
        childManager.coinFlip.setResult(adapter.getResult());
        childManager.coinFlip.tossTheCoin();

        if (childManager.coinFlip.getResultInt() == 0) {
            showAnimation(R.raw.sound_h, R.drawable.gif_h);
        } else {
            showAnimation(R.raw.sound_t, R.drawable.gif_t);
        }

        flip.setVisibility(View.VISIBLE);
        viewPager2.setVisibility(View.INVISIBLE);
        saveResult();
    }

    private void showAnimation(int idSound, int id) {
        mp = MediaPlayer.create(CoinFlipActivity.this, idSound);
        mp.start();

        Glide.with(this).load(id).addListener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                if (resource instanceof GifDrawable) {
                    ((GifDrawable) resource).setLoopCount(1);
                }
                return false;
            }
        }).into(flip);

        mp.setOnCompletionListener(mediaPlayer ->
        {
            if (childManager.getChildList().size() > 0 && saveGame) {
                showResultWithPicker();
            } else {
                showResultWithOutPicker();
            }
            mp.release();
        });
    }

    private void showChangeOrderMenu() {

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        View v = LayoutInflater.from(this).inflate(R.layout.coin_flip_change_order, null);
        RecyclerView childList = v.findViewById(R.id.coin_flip_change_listview);
        childList.setLayoutManager(mLayoutManager);
        DividerItemDecoration decoration = new DividerItemDecoration(childList.getContext(), mLayoutManager.getOrientation());
        decoration.setDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.recyclerview_divider, null));
        childList.addItemDecoration(decoration);

        getChildManager();
        adapterChangeOrder = new ChangeOrderMenuAdapter(this, childManager.coinFlip.getPickerList());
        childManager.coinFlip.getPickerList().add(new Child("", null));

        childList.setAdapter(adapterChangeOrder);

        AlertDialog.Builder build = new AlertDialog.Builder(this).setView(v)
                .setTitle(R.string.change_order)
                .setNegativeButton(R.string.result_leave_option, (dialogInterface, i) ->
                {
                    dialogInterface.dismiss();
                    for (int j = 0; j < childManager.coinFlip.getPickerList().size(); j++) {
                        if (childManager.coinFlip.getPickerList().get(j).getName().isEmpty()) {
                            childManager.coinFlip.getPickerList().remove(j);
                            break;
                        }
                    }
                });

        Dialog dialog = build.create();
        dialog.show();

        ChangeOrderMenuAdapter.clickObserverChangeOrder obsChangeOrder = () ->
        {
            dialog.dismiss();
            for (int j = 0; j < childManager.coinFlip.getPickerList().size(); j++) {
                if (childManager.coinFlip.getPickerList().get(j).getName().equals("")) {
                    childManager.coinFlip.getPickerList().remove(j);
                    break;
                }
            }

            childManager.coinFlip.changeOrder(adapterChangeOrder.getPick());

            if (adapterChangeOrder.getPick() == childManager.coinFlip.getPickerList().size()) {
                saveGame = false;
                tossCoin();
            } else {
                saveChildManager();
                setPageAdapter();
                viewPager2.setAdapter(adapter);
                setBoardCallBack();
            }
        };

        adapterChangeOrder.registerChangeCallBack(obsChangeOrder);
    }

    private void showResultWithPicker() {
        View v = LayoutInflater.from(this).inflate(R.layout.coin_toss_result_withpicker, null);
        TextView textView = v.findViewById(R.id.change_order_child_name);

        ImageView childPhoto = v.findViewById(R.id.change_order_child_photo);
        ImageView icon = v.findViewById(R.id.toss_history_icon2);

        textView.setText(childManager.coinFlip.getResult(this));
        if (childManager.coinFlip.pickerWin()) {
            icon.setImageResource(R.drawable.win);
        } else {
            icon.setImageResource(R.drawable.loss);
        }

        Child child = childManager.coinFlip.getPickerList().get(childManager.coinFlip.getPickerList().size() - 1);
        childPhoto.setImageBitmap(EditChildActivity.decodeBase64(child.getIcon()));

        AlertDialog.Builder build = new AlertDialog.Builder(this).setView(v)
                .setTitle(R.string.Result)
                .setPositiveButton(R.string.result_repeat_option, (dialogInterface, i) -> reset())
                .setNegativeButton(R.string.result_leave_option, (dialogInterface, i) -> this.finish());

        Dialog dialog = build.create();
        dialog.show();
    }

    private void showResultWithOutPicker() {

        String result = childManager.coinFlip.getResult(this);
        TextView message = new TextView(this);
        message.setText(result);
        message.setTextSize(48);
        message.setGravity(Gravity.CENTER_HORIZONTAL);

        AlertDialog.Builder build = new AlertDialog.Builder(this)
                .setTitle(R.string.Result)
                .setPositiveButton(R.string.result_repeat_option, (dialogInterface, i) -> reset())
                .setNeutralButton(R.string.add_child_option, (dialogInterface, i) ->
                {
                    Intent childIntent = EditChildActivity.makeIntent(CoinFlipActivity.this);
                    startActivity(childIntent);
                })
                .setNegativeButton(R.string.result_leave_option, (dialogInterface, i) -> this.finish())
                .setTitle(R.string.Result)
                .setView(message);

        Dialog dialog = build.create();
        dialog.show();
    }

    private void reset() {
        saveGame = true;
        viewPager2.setVisibility(View.VISIBLE);
        flip.setVisibility(View.INVISIBLE);

        getChildManager();
        setPageAdapter();

        viewPager2.setAdapter(adapter);
        setBoardCallBack();

        if (childManager.getChildList().isEmpty()) {
            tossCoin();
        }
    }

    private void saveResult() {

        if (saveGame) {
            childManager.coinFlip.saveResult(this);
        }
        saveChildManager();
    }

    private void saveChildManager() {

        SharedPreferences prefs = this.getSharedPreferences(EditChildActivity.CHILD_LIST_TAG, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Gson gson = new Gson();
        editor.putString(EditChildActivity.CHILD_LIST, gson.toJson(childManager));
        editor.apply();

    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, CoinFlipActivity.class);
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

    @Override
    protected void onPostResume() {
        super.onPostResume();
        reset();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mp.release();
    }
}