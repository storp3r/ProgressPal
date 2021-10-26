package storper.matt.c196_progress_pal.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import storper.matt.c196_progress_pal.Database.Entities.Term;
import storper.matt.c196_progress_pal.Fragments.ListFragment;
import storper.matt.c196_progress_pal.R;
import storper.matt.c196_progress_pal.Utilities.MenuHandler;
import storper.matt.c196_progress_pal.ViewModel.TermViewModel;
import storper.matt.c196_progress_pal.Utilities.DataIntegrity;

public class ModifyTermActivity<dynamicViews> extends AppCompatActivity implements ListFragment.OnListItemListener  {

    public TermViewModel mTermViewModel;
    public DataIntegrity mIntegrity = new DataIntegrity();
    public MenuHandler mMenuHandler;
    private static final String TAG = "ModifyTerm";
    int id = -1;
    String termId;

    TextView courseFragTitle;
    TextView nameLabel;
    EditText editName;
    EditText startDate;
    EditText endDate;
    Button saveBtn;
    Button addCourseBtn;
    LinearLayout detailsBtn;
    ImageView upArrow;
    ImageView downArrow;
    FragmentContainerView courseFragment;
    View[] mDynamicViews;





    public interface OnPassDataToFragment {
        void onPassData(String test);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_term);

        Bundle extras = getIntent().getExtras();
        mTermViewModel = new ViewModelProvider(this).get(TermViewModel.class);

        if (extras != null) {
            id = extras.getInt("id");
            termId = String.valueOf(id);
            mTermViewModel.setCurrentTerm(id);
        }

        editName = findViewById(R.id.editEntityName);
        nameLabel = findViewById(R.id.entityNameLabel);
        startDate = findViewById(R.id.editStartDate);
        endDate = findViewById(R.id.editEndDate);
        saveBtn = findViewById(R.id.saveTermBtn);
        addCourseBtn = findViewById(R.id.add_course_btn);
        detailsBtn = findViewById(R.id.detailsButton);
        downArrow = findViewById(R.id.downArrow);
        upArrow = findViewById(R.id.upArrrow);
        courseFragTitle = findViewById(R.id.courseFragTitle);
        courseFragment = findViewById(R.id.courseFragmentView);

        mDynamicViews = new View[]{
                addCourseBtn,
                courseFragment,
                courseFragTitle,
                downArrow,
                upArrow
        };

       initViewModel();
    }

    private void initViewModel() {
        nameLabel.setText(R.string.term_label);
        saveBtn.setOnClickListener(saveTerm);
        addCourseBtn.setOnClickListener(addCourse);
        detailsBtn.setOnClickListener(showHideDetails);

        final Observer<Term> termObserver = new Observer<Term>() {
            @Override
            public void onChanged(@Nullable final Term term) {
                assert term != null;
                editName.setText(term.getName());
                startDate.setText(term.getStartDate());
                endDate.setText(term.getEndDate());
            }
        };

        mTermViewModel.mTerm.observe(this, termObserver);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.list_term_fragment_container);
        mMenuHandler = new MenuHandler();

//Pass information to fragment and start fragment
        if(fragment == null) {
            String currentClass = mMenuHandler.getCurrentClass(ModifyTermActivity.class);
            fragment = ListFragment.newInstance(ListFragment.ENTITY.COURSE, termId);
            fragmentManager.beginTransaction()
                    .replace(R.id.courseFragmentView, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.modified_menu, menu);
        MenuItem item = menu.getItem(0);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(ModifyTermActivity.this, TermListActivity.class);
                startActivity(intent);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public View.OnClickListener saveTerm = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
           String name  = editName.getText().toString().trim();
           String sDate = startDate.getText().toString().trim();
           String eDate = endDate.getText().toString().trim();

           if(mIntegrity.noNullStrings(name, sDate, eDate)) {
               mTermViewModel.saveCurrentTerm(name, sDate, eDate);
               Intent intent = new Intent(getApplicationContext(), TermListActivity.class);
               startActivity(intent);
           }

        }
    };

    public View.OnClickListener addCourse = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), ModifyCourseActivity.class);
            intent.putExtra("termId", termId);
            intent.putExtra("launchActivity", "ModifyTermActivity");
            startActivity(intent);
        }
    };

    public View.OnClickListener showHideDetails = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mMenuHandler.setVisibility(id ,mDynamicViews);
        }
    };


    @Override
    public void onItemSelected() {

    }
}