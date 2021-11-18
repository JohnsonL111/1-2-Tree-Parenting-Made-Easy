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
 *
 */
public class CoinHistoryChangeOrderMenuAdapter extends RecyclerView.Adapter<CoinHistoryChangeOrderMenuAdapter.ChangeOrderViewHolder> {

    private final ArrayList<clickObserverChangeOrder> observerChangeOrder = new ArrayList<>();
    private Context context;
    private ArrayList<Child> childList;
    private int pick;

    public CoinHistoryChangeOrderMenuAdapter(Context context, ArrayList<Child> childList) {
        this.context = context;
        this.childList = childList;
    }

    @NonNull
    @Override
    public ChangeOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChangeOrderViewHolder(LayoutInflater.from(context).inflate((R.layout.coin_toss_history_view), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChangeOrderViewHolder holder, int position) {
        Child child = childList.get(position);
        pick = holder.getAdapterPosition();

        if (!child.getName().isEmpty()) {
            holder.childName.setText(child.getName());

            //set child photo
            holder.childPhoto.setImageResource(R.drawable.default_child_photo);
        } else {
            holder.childName.setText(R.string.nobody);

            //set child photo
            holder.childPhoto.setImageResource(R.drawable.default_photo_nobody);
        }
    }

    public int getPick() {
        return pick;
    }

    @Override
    public int getItemCount() {
        return childList.size();
    }

    protected class ChangeOrderViewHolder extends RecyclerView.ViewHolder {
        public ImageView childPhoto;
        public TextView childName;

        public ChangeOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            childPhoto = itemView.findViewById(R.id.change_order_child_photo);
            childName = itemView.findViewById(R.id.change_order_child_name);
        }
    }

    public interface clickObserverChangeOrder {
        void notifyChangeOrder();
    }

    public void registerChangeCallBack(clickObserverChangeOrder obs) {
        observerChangeOrder.add(obs);
    }
}
