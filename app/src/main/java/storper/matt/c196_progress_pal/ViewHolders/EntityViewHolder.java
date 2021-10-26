package storper.matt.c196_progress_pal.ViewHolders;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import storper.matt.c196_progress_pal.Activities.ModifyCourseActivity;
import storper.matt.c196_progress_pal.Fragments.ListFragment;
import storper.matt.c196_progress_pal.Activities.ModifyTermActivity;
import storper.matt.c196_progress_pal.R;

public class EntityViewHolder extends RecyclerView.ViewHolder implements ListFragment.OnListItemListener {


    public enum Type {TERM, COURSE, ASSESSMENT, NULL}

    public Type currentType = Type.NULL;

    public  TextView mEntityName;
    public  TextView mExtraDetails1;
    public TextView mExtraDetails2;


    private static final String TAG = "EntityViewHolder";



    private EntityViewHolder(View itemView) {
        super(itemView);
        mEntityName = itemView.findViewById(R.id.entityName);
        mExtraDetails1 = itemView.findViewById(R.id.itemDetails1);
        mExtraDetails2 = itemView.findViewById(R.id.itemDetails2);
    }

    public void bind(Type type, int id, String name, String details1 , String details2 ) {
        Log.d(TAG, "bind: started");
        currentType = type;

        itemView.setTag(id);
        itemView.setId(id);
        mEntityName.setText(name);
        mExtraDetails1.setText(details1);
        mExtraDetails2.setText(details2);

        Object current;

        if (type == Type.TERM) {
            current = ModifyTermActivity.class;
        } else {
            current = ModifyCourseActivity.class;
        }

        Object finalCurrent = current;
        Context rootView = itemView.getRootView().getContext();

        String launchingActivity;
        launchingActivity = rootView.toString();
        String[] stringArray = launchingActivity.split("\\.|@");
        launchingActivity = stringArray[stringArray.length - 2];

        String finalLaunchingActivity = launchingActivity;
        itemView.setOnClickListener(new View.OnClickListener() {
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
        System.out.println("TAGGED: " + mEntityName.getTag());
    }


    public static EntityViewHolder create(ViewGroup parent) {
        Log.d(TAG, "create: started");
        View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_entity, parent, false);
        return new EntityViewHolder(view);

    }

//    public int targetLayout() {
//        int layout;
//        if(currentType != Type.NULL) {
//            layout = R.layout.list_item_entity;
//        } else {
//            layout = R.layout.new_item_entity;
//        }
//        return layout;
//    }

//    @NonNull
//    public Type determineType(String adapterType) {
//        String[] stringArray = adapterType.split("\\.|@");
//        adapterType = stringArray[stringArray.length - 2];
//        Type type = Type.TERM;
//        System.out.println("startedThis " + adapterType);
//
//        if (adapterType.equals("TermListAdapter")) {
//            type = type.TERM;
//        } else if (adapterType.equals("CourseListAdapter")) {
//            type = type.COURSE;
//        } else {
//            type = type.ASSESSMENT;
//        }
//        return type;
//    }


}

