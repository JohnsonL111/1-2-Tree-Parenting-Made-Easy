package cmpt276.as2.parentapp.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class CoinFlip
{
    private final String TS_FORMAT = "@yyyy-MM-dd HH:mm:ss";
    private final int HEAD = 0;
    private final int TAIL = 1;

    private String currentPicker;
    private String lastPicker;

    private ArrayList<String> savedPickers;
    private ArrayList<String> unSavedPickers;
    private ArrayList<String> childrenList;
    private String timeStamp;
    private ArrayList<String> history;
    private int userPick;
    private int result;

    public CoinFlip(String[] childrenList, String[] savedPickers, String[] history, String lastPicker)
    {
        this.childrenList = new ArrayList<>(Arrays.asList(childrenList));
        this.savedPickers = new ArrayList<>(Arrays.asList(savedPickers));
        this.history = new ArrayList<>(Arrays.asList(history));
        this.lastPicker = lastPicker;
    }

    public void chosePicker()
    {
        if(!savedPickers.isEmpty() && !childrenList.isEmpty())
        {
            if (checkListChange())
            {
                updatePickerList();

                if(unSavedPickers.size() > 1)
                {
                    notifyToShowPickMenu();
                }
                else
                {
                    currentPicker = unSavedPickers.get(0);
                    lastPicker = currentPicker;
                }
            }
            else
            {
                for(int i = 0; i < savedPickers.size(); i++)
                {
                    if(savedPickers.get(i).equals(lastPicker))
                    {
                        if(i == savedPickers.size()-1)
                        {
                            currentPicker = savedPickers.get(0);
                        }
                        else
                        {
                            currentPicker = savedPickers.get(i+1);
                        }
                    }
                }
            }
        }
    }

    private void notifyToShowPickMenu()
    {

    }

    private boolean checkListChange()
    {
        if(savedPickers.size() != childrenList.size())
        {
            return true;
        }

        for(int i = 0; i< savedPickers.size();i++)
        {
            if(!childrenList.contains(savedPickers.get(i)))
                return true;
        }

        return false;
    }

    private void updatePickerList()
    {
        ArrayList<String> tmpList = new ArrayList<>();

        for(int i = 0; i< savedPickers.size(); i++)
        {
            if(childrenList.contains(savedPickers.get(i)))
            {
                tmpList.add(savedPickers.get(i));
            }
        }

        for(int i = 0; i < childrenList.size(); i++)
        {
            if(!savedPickers.contains(childrenList.get(i)))
            {
                unSavedPickers.add(childrenList.get(i));
            }
        }
        this.savedPickers = tmpList;
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

    public String getTimeStamp()
    {
        return timeStamp;
    }

    private void saveResult(String str)
    {
        history.add(str);
    }

    public ArrayList<String> getHistory()
    {
        return history;
    }

    public boolean checkPickerWin()
    {
        return result == userPick;
    }

    public String getCurrentPicker()
    {
        return currentPicker;
    }

    public void setCurrentPicker(String currentPicker) {
        this.currentPicker = currentPicker;
    }

    public void setSavedPickers(ArrayList<String> savedPickers) {
        this.savedPickers = savedPickers;
    }

    public void setChildrenList(ArrayList<String> childrenList) {
        this.childrenList = childrenList;
    }

    public void setHistory(ArrayList<String> history) {
        this.history = history;
    }
}
