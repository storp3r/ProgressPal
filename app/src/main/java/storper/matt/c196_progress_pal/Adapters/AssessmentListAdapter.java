package storper.matt.c196_progress_pal.Adapters;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import storper.matt.c196_progress_pal.Database.Entities.Assessment;
import storper.matt.c196_progress_pal.ViewHolders.EntityViewHolder;

public class AssessmentListAdapter extends ListAdapter<Assessment, EntityViewHolder> {

    public AssessmentListAdapter(@NonNull DiffUtil.ItemCallback<Assessment> diffCallBack) {
        super(diffCallBack);
    }

    @NonNull
    @Override
    public EntityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return EntityViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull EntityViewHolder holder, int position) {
        Assessment current = getItem(position);
        holder.bind(EntityViewHolder.Type.ASSESSMENT, current.getId(), current.getName(), "Due: " + current.getDueDate()
                , "Type: " + current.getType());
    }

    public static class AssessmentDiff extends DiffUtil.ItemCallback<Assessment> {

        @Override
        public boolean areItemsTheSame(@NonNull Assessment oldItem, @NonNull Assessment newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Assessment oldItem, @NonNull Assessment newItem) {
            return oldItem.getName().equals(newItem.getName());
        }
    }
}
