package storper.matt.c196_progress_pal.ViewHolders;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import storper.matt.c196_progress_pal.Activities.ModifyCourseActivity;
import storper.matt.c196_progress_pal.Fragments.ListFragment;
import storper.matt.c196_progress_pal.Activities.ModifyTermActivity;
import storper.matt.c196_progress_pal.R;
import storper.matt.c196_progress_pal.Utilities.MenuHandler;

public class EntityViewHolder extends RecyclerView.ViewHolder implements ListFragment.OnListItemListener {


    enum Type {TERM, COURSE, ASSESSMENT}

    public ListFragment.OnListItemListener mListener;
    public final TextView mEntityItemView;
    private static final String TAG = "EntityViewHolder";
    public Context currentContext;
    private MenuHandler menuHandler;


    private EntityViewHolder(View itemView) {
        super(itemView);
        mEntityItemView = itemView.findViewById(R.id.entityName);
    }

    public void bind(String text, int id) {
        Log.d(TAG, "bind: started");
        mEntityItemView.setText(text);
        mEntityItemView.setTag(id);
        mEntityItemView.setId(id);
        Object current;

        Type type = determineType(getBindingAdapter().toString());
        if (type == type.TERM) {
            current = ModifyTermActivity.class;
        } else {
            current = ModifyCourseActivity.class;
        }
        System.out.println("startedThis " + determineType(getBindingAdapter().toString()));
        Object finalCurrent = current;
        Context rootView = mEntityItemView.getRootView().getContext();

        String launchingActivity;
        launchingActivity = rootView.toString();
        String[] stringArray = launchingActivity.split("\\.|@");
        launchingActivity = stringArray[stringArray.length - 2];

        String finalLaunchingActivity = launchingActivity;
        mEntityItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootView, (Class<?>) current);
                intent.putExtra("id", id);
                intent.putExtra("launchActivity", finalLaunchingActivity);
                rootView.startActivity(intent);
            }
        });

    }

    @Override
    public void onItemSelected() {
        System.out.println("TAGGED: " + mEntityItemView.getTag());
    }


    public static EntityViewHolder create(ViewGroup parent) {
        Log.d(TAG, "create: started");
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_entity, parent, false);

        return new EntityViewHolder(view);

    }

    @NonNull
    public Type determineType(String adapterType) {
        String[] stringArray = adapterType.split("\\.|@");
        adapterType = stringArray[stringArray.length - 2];
        Type type = Type.TERM;
        System.out.println("startedThis " + adapterType);

        if (adapterType.equals("TermListAdapter")) {
            type = type.TERM;
        } else if (adapterType.equals("CourseListAdapter")) {
            type = type.COURSE;
        } else {
            type = type.ASSESSMENT;
        }
        return type;
    }


}

