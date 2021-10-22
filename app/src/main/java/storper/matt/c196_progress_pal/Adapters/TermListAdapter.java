package storper.matt.c196_progress_pal.Adapters;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import java.util.ArrayList;
import java.util.List;

import storper.matt.c196_progress_pal.Database.Entities.Term;
import storper.matt.c196_progress_pal.ViewHolders.EntityViewHolder;
import storper.matt.c196_progress_pal.ViewModel.TermViewModel;

public class TermListAdapter extends ListAdapter<Term, EntityViewHolder> {

    private List<Term> termList = new ArrayList<>();
    private TermViewModel termViewModel;

    public TermListAdapter(@NonNull DiffUtil.ItemCallback<Term> diffCallback) {
        super(diffCallback);

    }

    @Override
    public EntityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return EntityViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(EntityViewHolder holder, int position) {
        Term current = getItem(position);
        holder.bind(EntityViewHolder.Type.TERM, current.getId(),"Term: " + current.getName(), "Starts: " + current.getStartDate()
                , "Ends: " + current.getEndDate());
        termList.add(current);
        System.out.println("List size: " + termList.size());
    }

    public Term getTermByPosition(int position) {
        if(termList.size() > 0) {
            System.out.println("Ran term");
            return termList.get(position);
        }
        return null;
    }


    public static class TermDiff extends DiffUtil.ItemCallback<Term> {

        @Override
        public boolean areItemsTheSame(@NonNull Term oldItem, @NonNull Term newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Term oldItem, @NonNull Term newItem) {
            return oldItem.getName().equals(newItem.getName());
        }
    }

}
