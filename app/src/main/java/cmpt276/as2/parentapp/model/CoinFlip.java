package cmpt276.as2.parentapp.model;

import android.content.Context;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

import cmpt276.as2.parentapp.R;

/**
 * Class handle the logic of coin toss, generate string for the result with time stamp.
 */
public class CoinFlip {
    private final String TS_FORMAT = "@yyyy-MM-dd HH:mm:ss";
    private final int HEAD = 0;
    private final int TAIL = 1;

    private ArrayList<Child> savedPickers;
    private String timeStamp;
    private ArrayList<String> historyTS;
    private ArrayList<String> historyChildName;
    private int userPick;
    private int result;

    public CoinFlip() {
        savedPickers = new ArrayList<>();
        historyTS = new ArrayList<>();
        historyChildName = new ArrayList<>();
    }

    public void tossTheCoin() {
        Random random = new Random();
        if (random.nextInt() % 2 == 0) {
            result = HEAD;
        } else {
            result = TAIL;
        }
        formTimeStamp();
    }

    private void formTimeStamp() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern(TS_FORMAT);
        timeStamp = now.format(format);
    }

    public void setResult(int result) {
        this.userPick = result;
    }

    public void saveResult(Context context) {
        for (int i = 0; i < savedPickers.size(); i++) {
            if (savedPickers.get(i).getName().isEmpty()) {
                savedPickers.remove(i);
                break;
            }
        }

        if (savedPickers.size() > 0) {
            String pickerName = savedPickers.get(0).getName();
            Child tmp = savedPickers.get(0);
            savedPickers.remove(0);
            savedPickers.add(tmp);

            String[] name = context.getResources().getStringArray(R.array.coin_two_side_name);
            String record;
            if (userPick == result) {
                record = context.getString(R.string.coin_toss_save_history_win, name[userPick], name[result], timeStamp);
            } else {
                record = context.getString(R.string.coin_toss_save_history_lost, name[userPick], name[result], timeStamp);
            }
            historyTS.add(record);
            historyChildName.add(pickerName);

        }
    }

    public void setCurrentPickerName() {
    }

    public ArrayList<String> getHistoryTS() {
        return historyTS;
    }

    public ArrayList<Child> getPickerList() {
        return savedPickers;
    }

    public String getResult(Context context) {
        String[] name = context.getResources().getStringArray(R.array.coin_two_side_name);
        return name[result];
    }

    public int getResultInt() {
        return result;
    }

    public boolean pickerWin() {
        return result == userPick;
    }

    public ArrayList<String> getHistoryName() {
        return historyChildName;
    }

    public void addChild(Child childToAdd) {
        this.savedPickers.add(childToAdd);
        setCurrentPickerName();
    }

    public void removeChild(String deleteName) {

        ArrayList<Child> tmpPicker = new ArrayList<>();
        for (int i = 0; i < savedPickers.size(); i++) {
            if (!savedPickers.get(i).getName().equals(deleteName)) {
                tmpPicker.add(savedPickers.get(i));
            }
        }
        this.savedPickers = tmpPicker;

        if (savedPickers.size() > 0) {
            setCurrentPickerName();
        } else {
        }

        ArrayList<String> tmpNameList = new ArrayList<>();
        ArrayList<String> tmpTSList = new ArrayList<>();

        for (int i = 0; i < historyChildName.size(); i++) {
            if (!historyChildName.get(i).equals(deleteName)) {
                tmpNameList.add(historyChildName.get(i));
                tmpTSList.add(historyTS.get(i));
            }
        }

        this.historyChildName = tmpNameList;
        this.historyTS = tmpTSList;
    }

    public void editChildName(String childToEditName, String newChildName) {
        for (int i = 0; i < savedPickers.size(); i++) {
            if (savedPickers.get(i).getName().equals(childToEditName)) {
                savedPickers.get(i).setName(newChildName);
            }
        }

        for (int i = 0; i < historyChildName.size(); i++) {
            if (historyChildName.get(i).equals(childToEditName)) {
                historyChildName.set(i, newChildName);
            }
        }
    }

    public void changeOrder(int pick) {
        if (pick < savedPickers.size()) {
            Child tmp = savedPickers.get(pick);
            savedPickers.remove(pick);
            savedPickers.add(0, tmp);
        }
        setCurrentPickerName();
    }

    public void changeIcon(String childName, String newIcon) {
        for (int i = 0; i < savedPickers.size(); i++) {
            if (savedPickers.get(i).getName().equals(childName)) {
                savedPickers.get(i).setIcon(newIcon);
            }
        }
    }
}
