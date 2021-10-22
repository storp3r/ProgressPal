package storper.matt.c196_progress_pal.Adapters;

import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import storper.matt.c196_progress_pal.Database.Entities.Course;
import storper.matt.c196_progress_pal.ViewHolders.EntityViewHolder;

public class CourseListAdapter extends ListAdapter<Course, EntityViewHolder> {
    private static final String TAG = "CourseListAdapter";

    public CourseListAdapter(@NonNull DiffUtil.ItemCallback<Course> diffCallBack) {
        super(diffCallBack);
    }

    @Override
    public EntityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return EntityViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(EntityViewHolder holder, int position){
        Course current = getItem(position);
        holder.bind(EntityViewHolder.Type.COURSE,  current.getId(),"Course: " + current.getName(), "Starts: " +current.getStartDate()
                , "Ends: " + current.getEndDate());
        Log.d(TAG, "onBindViewHolder: ");
    }

    public static class CourseDiff extends DiffUtil.ItemCallback<Course> {

        @Override
        public boolean areItemsTheSame(@NonNull Course oldItem, Course newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Course oldItem, Course newItem) {
            return oldItem.getName().equals(newItem.getName());
        }
    }
}
