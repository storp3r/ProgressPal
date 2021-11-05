package storper.matt.c196_progress_pal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toolbar;

import storper.matt.c196_progress_pal.Activities.AssessmentListActivity;
import storper.matt.c196_progress_pal.Activities.CourseListActivity;
import storper.matt.c196_progress_pal.Activities.TermListActivity;
import storper.matt.c196_progress_pal.Utilities.MenuHandler;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    public View.OnClickListener mainButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            switch (v.getId()) {
                case R.id.termBtn:
                    intent = new Intent(MainActivity.this, TermListActivity.class);
                    break;
                case R.id.courseBtn:
                    intent = new Intent(MainActivity.this, CourseListActivity.class);
                    break;
                case R.id.assessmentBtn:
                    intent = new Intent(MainActivity.this, AssessmentListActivity.class);
                    break;
            }
            startActivity(intent);
        }
    };
    private Menu mMenu;
    private MenuHandler mMenuHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().setTitle("Progress Pal"); // for set actionbar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);


        Button termButton = findViewById(R.id.termBtn);
        Button courseButton = findViewById(R.id.courseBtn);
        Button assessmentButton = findViewById(R.id.assessmentBtn);

        termButton.setOnClickListener(mainButtonClicked);
        courseButton.setOnClickListener(mainButtonClicked);
        assessmentButton.setOnClickListener(mainButtonClicked);

        mMenuHandler = new MenuHandler();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar_menu, menu);
        mMenuHandler.setMenuOptions(getClass(),menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Class<?> targetActivity = mMenuHandler.handleMenuSelection(item);

        Intent intent = new Intent(MainActivity.this, targetActivity);
        startActivity(intent);

        return super.onOptionsItemSelected(item);
    }
}