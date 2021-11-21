package cmpt276.as2.parentapp.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.Objects;

import cmpt276.as2.parentapp.R;
import cmpt276.as2.parentapp.model.ChildManager;
import cmpt276.as2.parentapp.model.CoinHistoryMenuAdapter;

/**
 * Activity that display the history of coin toss, show different icon if the user win and loss.
 * Image on the left now show the picker's photo
 */
public class CoinFlipHistoryActivity extends AppCompatActivity {

    private RecyclerView historyList;
    private CoinHistoryMenuAdapter adapter;
    private ChildManager childManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_flip_history);

        getChildManager();

        historyList = findViewById(R.id.coin_history_list);

        adapter = new CoinHistoryMenuAdapter(this
                , childManager.coinFlip.getHistoryName()
                , childManager.coinFlip.getHistoryTS()
                , childManager.coinFlip.getPickerList());

        populateList();
        this.setTitle(getString(R.string.toss_history));
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

    private void populateList() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        historyList.setLayoutManager(mLayoutManager);
        DividerItemDecoration decoration = new DividerItemDecoration(historyList.getContext(), mLayoutManager.getOrientation());
        decoration.setDrawable(Objects.requireNonNull(ResourcesCompat.getDrawable(getResources(), R.drawable.recyclerview_divider, null)));
        historyList.setAdapter(adapter);
        historyList.addItemDecoration(decoration);
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, CoinFlipHistoryActivity.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_coin_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.go_back_coin_history:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}