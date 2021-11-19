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
/**
 * Adapter for the coin flip history ui, display string to state the result, also display different icons for picker win and loss.
 */
public class CoinHistoryMenuAdapter extends RecyclerView.Adapter<CoinHistoryMenuAdapter.HistoryViewHolder>
{
    private Context context;
    private ArrayList<String> historyList;

    public CoinHistoryMenuAdapter(Context context, ArrayList<String> historyList)
    {
        this.context = context;
        this.historyList = historyList;
    }
    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new HistoryViewHolder(LayoutInflater.from(context).inflate((R.layout.coin_toss_history_view), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position)
    {
        String str = historyList.get(position);
        holder.detail.setText(str);

        if(str.contains(context.getString(R.string.win_text)))
        {
            holder.icon1.setImageResource(R.drawable.win);
            holder.icon2.setImageResource(R.drawable.win);
        }
        else
        {
            holder.icon1.setImageResource(R.drawable.loss);
            holder.icon2.setImageResource(R.drawable.loss);
        }
    }

    @Override
    public int getItemCount()
    {
        if (historyList == null)
        {
            return 0;
        }
        return historyList.size();
    }

    protected class HistoryViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView icon1;
        public ImageView icon2;
        public TextView detail;

        public HistoryViewHolder(@NonNull View itemView)
        {
            super(itemView);
            icon1 = itemView.findViewById(R.id.toss_history_icon1);
            icon2 = itemView.findViewById(R.id.task_detail_child_photo);
            detail = itemView.findViewById(R.id.task_detail_task_name);
        }
    }
}
