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
 *
 */
public class ChangeOrderMenuAdapter extends RecyclerView.Adapter<ChangeOrderMenuAdapter.ChangeOrderViewHolder> {

    private final ArrayList<clickObserverChangeOrder> observerChangeOrder = new ArrayList<>();
    private Context context;
    private ArrayList<Child> childList;
    private int pick;

    public ChangeOrderMenuAdapter(Context context, ArrayList<Child> childList) {
        this.context = context;
        this.childList = childList;
    }

    @NonNull
    @Override
    public ChangeOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate((R.layout.coin_fliip_change_order_view), parent, false);
        return new ChangeOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChangeOrderViewHolder holder, int position) {
        Child child = childList.get(position);

        holder.childName.setOnClickListener(view ->
        {
            pick = holder.getAdapterPosition();
            for (clickObserverChangeOrder obs : observerChangeOrder) {
                obs.notifyChangeOrder();
            }
        });

        holder.childPhoto.setOnClickListener(view ->
        {
            pick = holder.getAdapterPosition();
            for (clickObserverChangeOrder obs : observerChangeOrder) {
                obs.notifyChangeOrder();
            }
        });

        if (!child.getName().isEmpty()) {
            holder.childName.setText(child.getName());
            holder.childPhoto.setImageBitmap(EditChildActivity.decodeBase64(child.getIcon()));
        } else {
            holder.childName.setText(R.string.nobody);
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
