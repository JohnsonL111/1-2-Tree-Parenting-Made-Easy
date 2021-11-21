package cmpt276.as2.parentapp.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import cmpt276.as2.parentapp.R;
import cmpt276.as2.parentapp.UI.EditChildActivity;

/**
 * Adapter for the coin flip history ui, display string to state the result, also display different icons for picker win and loss.
 */
public class CoinHistoryMenuAdapter extends RecyclerView.Adapter<CoinHistoryMenuAdapter.HistoryViewHolder> {

    private final Context context;
    private final ArrayList<String> historyListTS;
    private final ArrayList<String> historyListName;
    private final ArrayList<Child> childList;

    public CoinHistoryMenuAdapter(Context context, ArrayList<String> historyName, ArrayList<String> historyList, ArrayList<Child> childList) {
        this.context = context;
        this.historyListName = historyName;
        this.historyListTS = historyList;
        this.childList = childList;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HistoryViewHolder(LayoutInflater.from(context).inflate((R.layout.coin_toss_history_view), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        String name = historyListName.get(position);
        String ts = historyListTS.get(position);
        holder.detail.setText(context.getString(R.string.history_string_coin_flip, name, ts));

        for (int i = 0; i < childList.size(); i++) {
            if (childList.get(i).getName().equals(name)) {

                holder.childPhoto.setImageBitmap(EditChildActivity.decodeBase64(childList.get(i).getIcon()));
                break;
            }
        }

        if (ts.contains(context.getString(R.string.win_text))) {
            holder.icon.setImageResource(R.drawable.win);
        } else {
            holder.icon.setImageResource(R.drawable.loss);
        }
    }

    @Override
    public int getItemCount() {
        if (historyListName == null) {
            return 0;
        }
        return historyListName.size();
    }

    protected class HistoryViewHolder extends RecyclerView.ViewHolder {
        public ImageView childPhoto;
        public ImageView icon;
        public TextView detail;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            childPhoto = itemView.findViewById(R.id.change_order_child_photo);
            icon = itemView.findViewById(R.id.toss_history_icon2);
            detail = itemView.findViewById(R.id.change_order_child_name);
        }
    }
}
