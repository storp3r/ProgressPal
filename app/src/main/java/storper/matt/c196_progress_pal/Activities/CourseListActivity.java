package storper.matt.c196_progress_pal.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import storper.matt.c196_progress_pal.Fragments.ListFragment;
import storper.matt.c196_progress_pal.R;
import storper.matt.c196_progress_pal.Utilities.MenuHandler;

public class CourseListActivity extends AppCompatActivity implements ListFragment.OnListItemListener {

    private MenuHandler mMenuHandler;

    @Override
    public void onItemSelected() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.list_term_fragment_container);

        mMenuHandler = new MenuHandler();


        if(fragment == null) {
            fragment = new ListFragment();
            Bundle args = new Bundle();
            String currentClass = mMenuHandler.getCurrentClass(CourseListActivity.class);
            args.putString("parentActivity", currentClass);
            fragment.setArguments(args);
            fragmentManager.beginTransaction()
                    .add(R.id.list_term_fragment_container, fragment)
                    .commit();
        }

        FloatingActionButton addButton = findViewById(R.id.addButton);

        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(CourseListActivity.this, ModifyCourseActivity.class);
            startActivity(intent);
        });
    }



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