package storper.matt.c196_progress_pal.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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

import storper.matt.c196_progress_pal.Fragments.ListFragment;
import storper.matt.c196_progress_pal.R;
import storper.matt.c196_progress_pal.Utilities.Alert;
import storper.matt.c196_progress_pal.Utilities.MenuHandler;
import storper.matt.c196_progress_pal.ViewModel.TermViewModel;

public class CourseListActivity extends AppCompatActivity {
    private static final String TAG = "CouseList";
    private MenuHandler mMenuHandler;
    Alert alert = new Alert();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);
        getSupportActionBar().setTitle("Course List"); // for set actionbar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.list_term_fragment_container);

        mMenuHandler = new MenuHandler();

        if(fragment == null) {
            Log.d(TAG, "onCreate: courseList");
            fragment = new ListFragment();
            Bundle args = new Bundle();

            args.putSerializable("entityType", ListFragment.ENTITY.COURSE);
            fragment.setArguments(args);
            fragmentManager.beginTransaction()
                    .add(R.id.list_term_fragment_container, fragment)
                    .commit();
        }

        initViewModel();
    }

    private void initViewModel() {
        FloatingActionButton addButton = findViewById(R.id.addButton);
        TermViewModel mTermViewModel = new ViewModelProvider(this).get(TermViewModel.class);
        mTermViewModel.getTermCount().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                int count = 0;
                if(integer != null) count = integer;
                if(count > 0) {
                    addButton.setOnClickListener(addCourse);
                } else {
                    addButton.setOnClickListener(noTermAlert);
                }
            }
        });
    }

    private View.OnClickListener addCourse = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(CourseListActivity.this, ModifyCourseActivity.class);
            startActivity(intent);
        }
    };

    private View.OnClickListener noTermAlert = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            alert.noParentFound(CourseListActivity.this, "Term", "Course");
        }
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
        Intent intent = new Intent(CourseListActivity.this, targetActivity);

        startActivity(intent);

        return super.onOptionsItemSelected(item);
    }
}