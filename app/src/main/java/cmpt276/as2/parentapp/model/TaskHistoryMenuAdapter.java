package cmpt276.as2.parentapp.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cmpt276.as2.parentapp.R;
import cmpt276.as2.parentapp.UI.EditChildActivity;

/**
 * Adapter for the coin flip history ui, display string to state the result, also display different icons for picker win and loss.
 * Now on the left of history, show the picker's photo.
 */
public class TaskHistoryMenuAdapter extends RecyclerView.Adapter<TaskHistoryMenuAdapter.TaskHistoryViewHolder> {

    private final Context context;
    private final Task task;
    private final List<Child> childList;

    public TaskHistoryMenuAdapter(Context context, Task task, List<Child> childList) {
        this.context = context;
        this.task = task;
        this.childList = childList;
    }

    @NonNull
    @Override
    public TaskHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TaskHistoryViewHolder(LayoutInflater.from(context).inflate((R.layout.task_sigle_view), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHistoryViewHolder holder, int position) {

        holder.timeStamp.setText(task.getDateHistoryList().get(holder.getAdapterPosition()));
        if (task.getChildrenHistoryList().get(holder.getAdapterPosition()).isEmpty()) {
            holder.childName.setText(R.string.delete_child_name);
            holder.childPhoto.setImageResource(R.drawable.default_photo_nobody);

        } else {

            holder.childName.setText(task.getChildrenHistoryList().get(holder.getAdapterPosition()));
            String nameStr = task.getChildrenHistoryList().get(holder.getAdapterPosition());

            for (int i = 0; i < childList.size(); i++) {
                if (childList.get(i).getName().equals(nameStr)) {
                    String photo = childList.get(i).getIcon();
                    holder.childPhoto.setImageBitmap(EditChildActivity.decodeBase64(photo));
                    break;
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        if (task != null) {
            return task.getDateHistoryList().size();
        } else {
            return 0;
        }
    }

    protected class TaskHistoryViewHolder extends RecyclerView.ViewHolder {
        public TextView timeStamp;
        public ImageView childPhoto;
        public TextView childName;

        public TaskHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            timeStamp = itemView.findViewById(R.id.task_detail_task_name);
            childPhoto = itemView.findViewById(R.id.task_detail_child_photo);
            childName = itemView.findViewById(R.id.task_detail_child_name);
        }
    }
}
