package cmpt276.as2.parentapp.model;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import cmpt276.as2.parentapp.R;
import cmpt276.as2.parentapp.UI.EditChildActivity;

/**
 * Adapter for the coin flip ui, display a picture of head & tail, a string to show current picker, and a button lead to edit child activity.
 * Add picture for current picker, user can click it to show the menu which allow user to select picker manually.
 */
public class CoinFlipMenuAdapter extends RecyclerView.Adapter<CoinFlipMenuAdapter.HorizontalViewHolder> {

    private final ArrayList<clickObserverAnimation> observerAnimations = new ArrayList<>();
    private final ArrayList<clickObserverEditChild> observerEditChild = new ArrayList<>();
    private final ArrayList<clickObserverChangeOrder> observerChangeOrder = new ArrayList<>();

    private int result;
    private final Context context;
    private final Child picker;
    private final String[] option;
    private final TypedArray image;

    public CoinFlipMenuAdapter(Context context, Child picker, String[] option) {
        this.context = context;
        this.picker = picker;
        this.image = context.getResources().obtainTypedArray(R.array.coin_two_side);
        this.option = option;
    }

    public int getResult() {
        return result;
    }

    @NonNull
    @Override
    public HorizontalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HorizontalViewHolder(LayoutInflater.from(context).inflate((R.layout.coin_btn), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalViewHolder holder, int position) {

        if (!picker.getName().isEmpty()) {
            holder.childName.setText(context.getString(R.string.coin_toss_picker, picker.getName()));

            holder.childPhoto.setImageBitmap(EditChildActivity.decodeBase64(picker.getIcon()));

        } else {
            holder.childName.setText(R.string.no_save_child);
        }

        holder.coinPicture.setImageResource(image.getResourceId(position, 0));
        result = holder.getAdapterPosition();
        holder.text.setText(option[position]);
    }

    @Override
    public int getItemCount() {
        return image.length();
    }

    protected class HorizontalViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout layOut;
        public TextView childName;
        public TextView text;
        public ImageView coinPicture;
        public FloatingActionButton editChild;
        public ImageView childPhoto;

        public HorizontalViewHolder(@NonNull View itemView) {
            super(itemView);

            layOut = itemView.findViewById(R.id.lay_coin);
            childName = itemView.findViewById(R.id.coin_btn_child_name);
            text = itemView.findViewById(R.id.coin_text);
            editChild = itemView.findViewById(R.id.edit_child_btn);
            childPhoto = itemView.findViewById(R.id.child_photo);

            coinPicture = itemView.findViewById(R.id.coin_view);
            coinPicture.setOnClickListener(view ->
            {
                for (clickObserverAnimation obs : observerAnimations) {
                    obs.notifyClickerPlayAnimation();
                }
            });

            editChild.setOnClickListener(view ->
            {
                for (clickObserverEditChild obs : observerEditChild) {
                    obs.notifyClickerEditChild();
                }
            });

            childPhoto.setOnClickListener(view ->
            {
                for (clickObserverChangeOrder obs : observerChangeOrder) {
                    obs.notifyClickerChangeOrder();
                }
            });

            childName.setOnClickListener(view ->
            {
                for (clickObserverChangeOrder obs : observerChangeOrder) {
                    obs.notifyClickerChangeOrder();
                }
            });
        }
    }

    public interface clickObserverAnimation {
        void notifyClickerPlayAnimation();
    }

    public interface clickObserverEditChild {
        void notifyClickerEditChild();
    }

    public interface clickObserverChangeOrder {
        void notifyClickerChangeOrder();
    }

    public void registerChangeCallBack(clickObserverAnimation obs) {
        observerAnimations.add(obs);
    }

    public void registerChangeCallBack(clickObserverEditChild obs) {
        observerEditChild.add(obs);
    }

    public void registerChangeCallBack(clickObserverChangeOrder obs) {
        observerChangeOrder.add(obs);
    }
}
