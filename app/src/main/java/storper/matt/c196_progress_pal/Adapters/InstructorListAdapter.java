package storper.matt.c196_progress_pal.Adapters;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import storper.matt.c196_progress_pal.Database.Entities.Instructor;
import storper.matt.c196_progress_pal.ViewHolders.EntityViewHolder;

public class InstructorListAdapter extends ListAdapter<Instructor, EntityViewHolder> {

    public InstructorListAdapter(@NonNull DiffUtil.ItemCallback<Instructor> diffCallBack) {
        super(diffCallBack);
    }

    @Override
    public EntityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return EntityViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull EntityViewHolder holder, int position) {
        Instructor current = getItem(position);
        holder.bind(current.getName(), current.getId());
    }

    public static class InstructorDiff extends DiffUtil.ItemCallback<Instructor> {

        @Override
        public boolean areItemsTheSame(@NonNull Instructor oldItem, @NonNull Instructor newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Instructor oldItem, @NonNull Instructor newItem) {
            return oldItem.getName().equals(newItem.getName());
        }
    }
}
