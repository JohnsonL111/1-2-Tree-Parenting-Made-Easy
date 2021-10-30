package cmpt276.as2.parentapp.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Set;

import cmpt276.as2.parentapp.R;
import cmpt276.as2.parentapp.model.CoinHistoryMenuAdapter;

public class CoinFlipHistoryActivity extends AppCompatActivity
{
    private ArrayList<String> coinTossHistory;
    private RecyclerView historyList;
    private CoinHistoryMenuAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_flip_history);

        historyList = findViewById(R.id.coin_history_list);
        updateHistory();
        adapter = new CoinHistoryMenuAdapter(this,coinTossHistory);
        populateList();
        this.setTitle("History");
    }

    private void updateHistory()
    {
        SharedPreferences prefs = this.getSharedPreferences(CoinFlipActivity.COIN_TAG, MODE_PRIVATE);
        if(!prefs.contains(CoinFlipActivity.COIN_HISTORY))
        {
            coinTossHistory = new ArrayList<>();
        }
        else
        {
            Gson gson = new Gson();
            coinTossHistory = gson.fromJson(prefs.getString(CoinFlipActivity.COIN_HISTORY, ""), ArrayList.class);
        }
    }

    private void populateList()
    {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        historyList.setLayoutManager(mLayoutManager);
        historyList.setAdapter(adapter);
    }

    public static Intent makeIntent(Context context)
    {
        return new Intent(context, CoinFlipHistoryActivity.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_coin_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.go_back_coin_history:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}