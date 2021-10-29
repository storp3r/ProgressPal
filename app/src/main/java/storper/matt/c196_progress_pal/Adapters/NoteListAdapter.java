package storper.matt.c196_progress_pal.Adapters;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import storper.matt.c196_progress_pal.Database.Entities.Note;
import storper.matt.c196_progress_pal.ViewHolders.EntityViewHolder;

public class NoteListAdapter extends ListAdapter<Note, EntityViewHolder> {

    public NoteListAdapter(@NonNull DiffUtil.ItemCallback<Note> diffCallBack) {
        super(diffCallBack);
    }

    @Override
    public EntityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return EntityViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull EntityViewHolder holder, int position) {
        Note current = getItem(position);
        holder.bind(EntityViewHolder.Type.NOTE, current.getId(), current.getName(), current.getDetails(), null);
    }

    public static class NoteDiff extends DiffUtil.ItemCallback<Note> {

        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getName().equals(newItem.getName());
        }
    }
}
