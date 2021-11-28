package cmpt276.as2.parentapp.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import cmpt276.as2.parentapp.R;

/**
 *
 */
public class BreathMenuAdapter extends RecyclerView.Adapter<BreathMenuAdapter.BreathViewHolder> {

    private final ArrayList<clickObserverOption> observerBreath = new ArrayList<>();

    private int result;
    private final Context context;
    private final int[] optionList;

    public BreathMenuAdapter(Context context, int[] option) {
        this.context = context;
        this.optionList = option;
    }

    public int getResult() {
        return result;
    }

    @NonNull
    @Override
    public BreathViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BreathViewHolder(LayoutInflater.from(context).inflate((R.layout.breath_option), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BreathViewHolder holder, int position) {

        holder.option.setText(optionList[position] + "");
        holder.option.setOnClickListener(view ->
        {
            result = holder.getAdapterPosition();
            for(clickObserverOption obs: observerBreath){
                obs.notifyClickerChangeOption();

            }
        });
    }

    @Override
    public int getItemCount() {
        return optionList.length;
    }

    protected class BreathViewHolder extends RecyclerView.ViewHolder {
        public TextView option;

        public BreathViewHolder(@NonNull View itemView) {
            super(itemView);

            option = itemView.findViewById(R.id.task_detail_task_name);

        }
    }
    public interface clickObserverOption {
        void notifyClickerChangeOption();
    }

    public void registerOptionCallBack(clickObserverOption obs) {
        observerBreath.add(obs);
    }
}
