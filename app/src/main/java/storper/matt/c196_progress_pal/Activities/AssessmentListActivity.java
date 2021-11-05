package storper.matt.c196_progress_pal.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import storper.matt.c196_progress_pal.Database.Entities.Course;
import storper.matt.c196_progress_pal.Fragments.ListFragment;
import storper.matt.c196_progress_pal.R;
import storper.matt.c196_progress_pal.Utilities.MenuHandler;
import storper.matt.c196_progress_pal.ViewModel.CourseViewModel;

public class AssessmentListActivity extends AppCompatActivity {
    private static final String TAG = "Assessment List";
    private MenuHandler mMenuHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);
        getSupportActionBar().setTitle("Assessment List"); // for set actionbar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.list_term_fragment_container);

        mMenuHandler = new MenuHandler();

        initViewModel();

        if(fragment == null) {
            fragment = new ListFragment();
            Bundle args = new Bundle();
            args.putSerializable("entityType", ListFragment.ENTITY.ASSESSMENT);
            fragment.setArguments(args);
            fragmentManager.beginTransaction()
                    .add(R.id.list_term_fragment_container, fragment)
                    .commit();
        }

    }

    private void initViewModel() {
        FloatingActionButton addButton = findViewById(R.id.addButton);
        CourseViewModel mCourseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
        mCourseViewModel.getCourseCount().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                int count = 0;
                if(integer != null) count = integer;
                if(count > 0) {
                    addButton.setOnClickListener(addAssessment);
                } else {
                    addButton.setOnClickListener(noCourseAlert);
                }
            }
        });

    }

    private View.OnClickListener addAssessment = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(AssessmentListActivity.this, ModifyAssessmentActivity.class);
            startActivity(intent);
        }
    };

    private View.OnClickListener noCourseAlert = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick: Sorry no course");
        }
        //TODO add alert
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar_menu, menu);
        mMenuHandler.setMenuOptions(getClass(), menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Class<?> targetActivity = mMenuHandler.handleMenuSelection(item);

        Intent intent = new Intent(AssessmentListActivity.this, targetActivity);
        startActivity(intent);

        return super.onOptionsItemSelected(item);
    }
}