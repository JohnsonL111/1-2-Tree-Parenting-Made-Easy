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

import java.util.ArrayList;
import java.util.List;

import cmpt276.as2.parentapp.R;

public class CoinFlipMenuAdapter extends RecyclerView.Adapter<CoinFlipMenuAdapter.HorizontalViewHolder>
{
    private final ArrayList<clickObserver> observer = new ArrayList<>();
    private List<Integer> backgrounds;
    private Context context;

    private TypedArray image;

    public CoinFlipMenuAdapter(Context context)
    {
        this.context = context;
        if (backgrounds == null) {
            backgrounds = new ArrayList<>();
            backgrounds.add(android.R.color.holo_blue_bright);
            backgrounds.add(android.R.color.holo_red_dark);
            image = context.getResources().obtainTypedArray(R.array.coin_two_side);
        }
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
        holder.title.setText(new StringBuilder().append("").append(position + 1).toString());
        holder.coinPicture.setImageResource(image.getResourceId(position,0));
        holder.layOut.setBackgroundResource(backgrounds.get(position));
    }

    @Override
    public int getItemCount()
    {
        if (backgrounds == null)
        {
            return 0;
        }
        return backgrounds.size();
    }

    protected class HorizontalViewHolder extends RecyclerView.ViewHolder
    {
        public LinearLayout layOut;
        public TextView title;
        public ImageView coinPicture;

        HorizontalViewHolder(@NonNull View itemView)
        {
            super(itemView);
            layOut = itemView.findViewById(R.id.lay_coin);
            title = itemView.findViewById(R.id.coin_text);
            coinPicture = itemView.findViewById(R.id.coin_view);
            coinPicture.setOnClickListener(view ->
            {
                for(clickObserver obs: observer)
                {
                    obs.notifyClicker();
                }
            });
        }
    }

    public interface clickObserver
    {
        void notifyClicker();
    }

    public void registerChangeCallBack(clickObserver obs)
    {
        observer.add(obs);
    }
}
