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
public class TaskMenuAdapter extends RecyclerView.Adapter<TaskMenuAdapter.TaskViewHolder>
{
    private final ArrayList<TaskMenuAdapter.clickObserverViewDetail> observerViewDetail = new ArrayList<>();
    private final ArrayList<TaskMenuAdapter.clickObserverEditTask> observerEditTask = new ArrayList<>();

    private Context context;
    private ArrayList<String> historyList;
    private int pick;

    public TaskMenuAdapter(Context context, ArrayList<String> historyList)
    {
        this.context = context;
        this.historyList = historyList;
    }
    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new TaskViewHolder(LayoutInflater.from(context).inflate((R.layout.task_sigle_view), parent, false));
    }

    public int getPick()
    {
        return pick;
    }
    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position)
    {
        View.OnClickListener listenerViewDetail = view ->
        {
            pick = holder.getAdapterPosition();
            for(TaskMenuAdapter.clickObserverViewDetail obs :observerViewDetail)
            {
                obs.notifyViewDetail();
            }
        };

        holder.detail.setText("View Detail");
        holder.detail.setOnClickListener(listenerViewDetail);

        holder.nameOfTask.setText("View Detail");
        holder.nameOfTask.setOnClickListener(view ->
        {
            pick = holder.getAdapterPosition();
            for(TaskMenuAdapter.clickObserverEditTask obs :observerEditTask)
            {
                obs.notifyClickerEditTask();
            }
        });

        /**
         * holder.childPhoto.setImageResource();
         * set child photo
         */
        holder.nameOfTask.setOnClickListener(listenerViewDetail);

        holder.nameOfTask.setText("View Detail");
        holder.nameOfTask.setOnClickListener(listenerViewDetail);
    }

    @Override
    public int getItemCount()
    {
        return historyList.size();
    }

    protected class TaskViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView childPhoto;
        public TextView nameOfTask;
        public TextView nameOfChild;
        public TextView detail;

        public TaskViewHolder(@NonNull View itemView)
        {
            super(itemView);
            childPhoto = itemView.findViewById(R.id.task_view_child_photo);
            nameOfTask = itemView.findViewById(R.id.task_view_task_name);
            nameOfChild = itemView.findViewById(R.id.task_view_child_name);
            detail = itemView.findViewById(R.id.task_view_task_Detail);
        }
    }

    public interface clickObserverViewDetail
    {
        void notifyViewDetail();
    }

    public interface clickObserverEditTask
    {
        void notifyClickerEditTask();
    }

    public void registerViewDetail(TaskMenuAdapter.clickObserverViewDetail obs)
    {
        observerViewDetail.add(obs);
    }

    public void registerEditTask(TaskMenuAdapter.clickObserverEditTask obs)
    {
        observerEditTask.add(obs);
    }
}
