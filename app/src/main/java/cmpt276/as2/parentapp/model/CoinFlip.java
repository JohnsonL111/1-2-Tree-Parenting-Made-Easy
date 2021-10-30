package cmpt276.as2.parentapp.model;

import android.content.Context;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

import cmpt276.as2.parentapp.R;

public class CoinFlip
{
    private final String TS_FORMAT = "@yyyy-MM-dd HH:mm:ss";
    private final int HEAD = 0;
    private final int TAIL = 1;

    private String currentPicker = "";
    private Context context;
    private ArrayList<String> savedPickers;
    private String timeStamp;
    private ArrayList<String> history;
    private int userPick;
    private int result;

    public CoinFlip(ArrayList<String> savedPickers, ArrayList<String> history, Context context)
    {
        this.savedPickers = savedPickers;
        this.history = history;
        this.context = context;

        if(!this.savedPickers.isEmpty())
        {
            currentPicker = this.savedPickers.get(0);
        }
    }

    public void tossTheCoin()
    {
        Random random = new Random();
        if(random.nextInt()% 2 == 0)
        {
            result = HEAD;
        }
        else
        {
            result = TAIL;
        }
        formTimeStamp();
    }

    private void formTimeStamp()
    {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern(TS_FORMAT);
        timeStamp = now.format(format);
    }

    public void setResult(int result)
    {
        this.userPick = result;
    }

    public void saveResult()
    {
        if(!currentPicker.isEmpty())
        {
            String tmp = savedPickers.get(0);
            savedPickers.remove(0);
            savedPickers.add(savedPickers.size(), tmp);

            String[] name = context.getResources().getStringArray(R.array.coin_two_side_name);
            String record;
            if(userPick == result)
            {
                record = context.getString(R.string.coin_toss_save_history_win, currentPicker, name[userPick], name[result], timeStamp);
            }
            else
            {
                record = context.getString(R.string.coin_toss_save_history_lost, currentPicker, name[userPick], name[result], timeStamp);
            }
            history.add(record);
        }
    }

    public ArrayList<String> getHistory()
    {
        return history;
    }

    public ArrayList<String> getPickerList()
    {
        return savedPickers;
    }

    public void setSavedPickers(ArrayList<String> savedPickers)
    {
        this.savedPickers = savedPickers;
    }

    public String getResult()
    {
        String[] name = context.getResources().getStringArray(R.array.coin_two_side_name);
        return name[result];
    }

    public boolean pickerWin()
    {
        return result == userPick;
    }
}
