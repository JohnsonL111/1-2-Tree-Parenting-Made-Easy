package cmpt276.as2.parentapp.model;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import cmpt276.as2.parentapp.R;

public class CoinFlipMenuAdapter extends RecyclerView.Adapter<CoinFlipMenuAdapter.HorizontalViewHolder>
{
    private final ArrayList<clickObserverAnimation> observerAnimations = new ArrayList<>();
    private final ArrayList<clickObserverEditChild> observerEditChild = new ArrayList<>();
    private int result;
    private Context context;
    private String picker;
    private String[] option;

    private TypedArray image;

    public CoinFlipMenuAdapter(Context context, String picker, String[] option)
    {
        this.context = context;
        this.picker = picker;
        this.image = context.getResources().obtainTypedArray(R.array.coin_two_side);
        this.option = option;
    }

    public int getResult()
    {
        return result;
    }

    @NonNull
    @Override
    public HorizontalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new HorizontalViewHolder(LayoutInflater.from(context).inflate((R.layout.coin_btn), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalViewHolder holder, int position)
    {
        holder.title.setText(picker);
        holder.coinPicture.setImageResource(image.getResourceId(position,0));
        result = holder.getAdapterPosition();
        holder.text.setText(option[position]);
    }

    @Override
    public int getItemCount()
    {
        return image.length();
    }

    protected class HorizontalViewHolder extends RecyclerView.ViewHolder
    {
        public LinearLayout layOut;
        public TextView title;
        public TextView text;
        public ImageView coinPicture;
        public FloatingActionButton editChild;

        public HorizontalViewHolder(@NonNull View itemView)
        {
            super(itemView);
            layOut = itemView.findViewById(R.id.lay_coin);
            title = itemView.findViewById(R.id.coin_btn_title);
            text = itemView.findViewById(R.id.coin_text);
            editChild = itemView.findViewById(R.id.edit_child_btn);

            coinPicture = itemView.findViewById(R.id.coin_view);
            coinPicture.setOnClickListener(view ->
            {
                for(clickObserverAnimation obs: observerAnimations)
                {
                    obs.notifyClickerPlayAnimation();
                }
            });

            editChild.setOnClickListener(view ->
            {
                for(clickObserverEditChild obs: observerEditChild)
                {
                    obs.notifyClickerEditChild();
                }
            });
        }
    }

    public interface clickObserverAnimation
    {
        void notifyClickerPlayAnimation();
    }

    public interface clickObserverEditChild
    {
        void notifyClickerEditChild();
    }

    public void registerChangeCallBack(clickObserverAnimation obs)
    {
        observerAnimations.add(obs);
    }

    public void registerChangeCallBack(clickObserverEditChild obs)
    {
        observerEditChild.add(obs);
    }
}
