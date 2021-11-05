package storper.matt.c196_progress_pal.ViewHolders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import storper.matt.c196_progress_pal.Activities.ModifyAssessmentActivity;
import storper.matt.c196_progress_pal.Activities.ModifyCourseActivity;
import storper.matt.c196_progress_pal.Database.Entities.Note;
import storper.matt.c196_progress_pal.Fragments.InstructorFragment;
import storper.matt.c196_progress_pal.Fragments.ListFragment;
import storper.matt.c196_progress_pal.Activities.ModifyTermActivity;
import storper.matt.c196_progress_pal.Fragments.NoteFragment;
import storper.matt.c196_progress_pal.Fragments.SmsFragment;
import storper.matt.c196_progress_pal.R;

public class EntityViewHolder extends RecyclerView.ViewHolder  {

    private static final String TAG = "EntityViewHolder";
    public enum Type {TERM, COURSE, ASSESSMENT, NOTE, INSTRUCTOR, NULL}

    public Type currentType = Type.NULL;
    private final TextView mEntityName;
    private final  TextView mExtraDetails1;
    private final TextView mExtraDetails2;
    private final ImageButton shareBtn;
    private Context rootView;


    private EntityViewHolder(View itemView) {
        super(itemView);
        mEntityName = itemView.findViewById(R.id.entityName);
        mExtraDetails1 = itemView.findViewById(R.id.itemDetails1);
        mExtraDetails2 = itemView.findViewById(R.id.itemDetails2);
        shareBtn = itemView.findViewById(R.id.shareBtn);
    }

    public void bind(Type type, int id, String name, String details1 , String details2 ) {
        rootView = itemView.getRootView().getContext();

        currentType = type;
        itemView.setTag(id);
        itemView.setId(id);
        mEntityName.setText(name);
        mExtraDetails1.setText(details1);
        mExtraDetails2.setText(details2);
        mExtraDetails2.setVisibility(View.VISIBLE);
        shareBtn.setOnClickListener(shareListener);

        if(type == Type.NOTE){
            mExtraDetails2.setVisibility(View.GONE);
            shareBtn.setVisibility(View.VISIBLE);
        }

        Object currentActivity;

        if (type == Type.TERM) {
            currentActivity = ModifyTermActivity.class;
        } else if(type == Type.COURSE){
            currentActivity = ModifyCourseActivity.class;
        } else if(type == Type.ASSESSMENT){
            currentActivity = ModifyAssessmentActivity.class;
        } else {
            currentActivity = null;
        }

        String launchingActivity = rootView.toString();
        String[] stringArray = launchingActivity.split("\\.|@");
        launchingActivity = stringArray[stringArray.length - 2];
        String finalLaunchingActivity = launchingActivity;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentActivity != null) {
                    Intent intent = new Intent(rootView, (Class<?>) currentActivity);
                    intent.putExtra("id", id);
                    intent.putExtra("launchActivity", finalLaunchingActivity);
                    rootView.startActivity(intent);
                } else {

                    ModifyCourseActivity courseActivity = (ModifyCourseActivity) rootView;
                    FragmentManager fragmentManager = courseActivity.getSupportFragmentManager();

                    if(type == Type.INSTRUCTOR) {
                        InstructorFragment fragment = InstructorFragment.newInstance(0, id);
                        fragment.show(fragmentManager, "instructor_frag");
                    } else if(type == Type.NOTE) {
                        NoteFragment fragment = NoteFragment.newInstance(0, id);
                        fragment.show(fragmentManager, "note_frag");
                    }
                }
            }
        });
    }

    public static EntityViewHolder create(ViewGroup parent) {
        Log.d(TAG, "create: started");
        View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_entity, parent, false);
        return new EntityViewHolder(view);
    }

    public View.OnClickListener shareListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String message = mExtraDetails1.getText().toString().trim();
            System.out.println(message);
            ModifyCourseActivity courseActivity = (ModifyCourseActivity) rootView;
            FragmentManager fragmentManager = courseActivity.getSupportFragmentManager();
            SmsFragment fragment = SmsFragment.newInstance(message);
            fragment.show(fragmentManager, "sms_fragment");
        }
    };





}

