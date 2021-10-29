package storper.matt.c196_progress_pal.Adapters;

import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import storper.matt.c196_progress_pal.Database.Entities.Instructor;
import storper.matt.c196_progress_pal.ViewHolders.EntityViewHolder;

public class InstructorListAdapter extends ListAdapter<Instructor, EntityViewHolder> {
    private static final String TAG = "InAdapter";

    public InstructorListAdapter(@NonNull DiffUtil.ItemCallback<Instructor> diffCallBack) {
        super(diffCallBack);
    }

    @Override
    public EntityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return EntityViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull EntityViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: ");
        Instructor current = getItem(position);
        holder.bind(EntityViewHolder.Type.INSTRUCTOR, current.getId(), "Name: " + current.getName()
                , "Phone: \n" + current.getPhone(), "Email: \n"  + current.getEmail());
        Log.d(TAG, "onBindViewHolder: " + current.getName());
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
