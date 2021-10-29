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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import storper.matt.c196_progress_pal.Database.Entities.Course;
import storper.matt.c196_progress_pal.Database.Entities.Term;
import storper.matt.c196_progress_pal.Fragments.InstructorFragment;
import storper.matt.c196_progress_pal.Fragments.ListFragment;
import storper.matt.c196_progress_pal.Fragments.NoteFragment;
import storper.matt.c196_progress_pal.R;
import storper.matt.c196_progress_pal.Utilities.DataIntegrity;
import storper.matt.c196_progress_pal.Utilities.MenuHandler;
import storper.matt.c196_progress_pal.Utilities.StringWithTag;
import storper.matt.c196_progress_pal.ViewModel.CourseViewModel;
import storper.matt.c196_progress_pal.ViewModel.TermViewModel;

public class ModifyCourseActivity extends AppCompatActivity implements ListFragment.OnListItemListener {
    private static final String TAG = "ModifyCourse";
    private static final String modifyTermActivity = "ModifyTermActivity";
    private final ArrayList<StringWithTag> termList = new ArrayList<>();
    private final List<String> progressItems = Arrays.asList("Not Started", "In-Progress", "Completed");
    private final ArrayList<String> progressList = new ArrayList<>();

    public CourseViewModel mCourseViewModel;
    public MenuHandler mMenuHandler = new MenuHandler();
    public DataIntegrity verify = new DataIntegrity();
    private String launchingActivity;
    private int id = -1;
    private int courseId;
    private int termId;
    private Object tag;
    private String tempId;
    public String status;

    Button addInstructorBtn;
    Button saveBtn;
    Button addAssessmentBtn;
    Button addNoteBtn;
    EditText editName;
    EditText startDate;
    EditText endDate;
    FragmentContainerView instructorFrag;
    Spinner progressSelection;
    Spinner termSelection;
    TextView nameLabel;
    TextView instructorFragTitle;
    LinearLayout detailsButton;
    ImageView upArrow;
    ImageView downArrow;

    View[] mDynamicViews;

    @Override
    public void onItemSelected() {

    }

    public interface OnPassDataToFragment {
        void onPassData(boolean isAssessment, String data, String data2);
    }

    public OnPassDataToFragment courseDataPasser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_course);

        Bundle extras = getIntent().getExtras();
        mCourseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);

        if (extras != null) {
            id = extras.getInt("id");
            launchingActivity = extras.getString("launchActivity");
            tempId = extras.getString("termId");
            mCourseViewModel.setCurrentCourse(id);
        }

        editName = findViewById(R.id.editEntityName);
        nameLabel = findViewById(R.id.entityNameLabel);
        startDate = findViewById(R.id.editStartDate);
        endDate = findViewById(R.id.editEndDate);
        progressSelection = findViewById(R.id.progressSpinner);
        termSelection = findViewById(R.id.termSpinner);
        addInstructorBtn = findViewById(R.id.addInstructorBtn);
        saveBtn = findViewById(R.id.saveCourseBtn);
        addAssessmentBtn = findViewById(R.id.add_assessment_btn) ;
        addNoteBtn = findViewById(R.id.add_note_btn);
        detailsButton = findViewById(R.id.detailsButton);
        upArrow = findViewById(R.id.upArrrow);
        downArrow = findViewById(R.id.downArrow);
        instructorFragTitle = findViewById(R.id.instructorFragTitle);
        instructorFrag = findViewById(R.id.instructorListFragment);

        mDynamicViews = new View[]{
            upArrow, downArrow, instructorFragTitle, instructorFrag, addInstructorBtn
        };

        initViewModel();
    }

    private void initViewModel() {
        Log.d(TAG, "initViewModel: started" );
        nameLabel.setText(R.string.course_label);
        progressList.addAll(progressItems);
        saveBtn.setOnClickListener(saveCourse);
        addAssessmentBtn.setOnClickListener(addAssessment);
        detailsButton.setOnClickListener(showHideDetails);
        progressSelection.setOnItemSelectedListener(progressListener);
        termSelection.setOnItemSelectedListener(termListener);

        final Observer<Course> courseObserver = new Observer<Course>() {
            @Override
            public void onChanged(@Nullable final Course course) {

                if (course != null) {
                    editName.setText(course.getName());
                    startDate.setText(course.getStartDate());
                    endDate.setText(course.getEndDate());
                    tag = course.getTermId();
                    status = course.getStatus();
                    setProgressSpinner(status);
                    courseId = course.getId();
                    addInstructorBtn.setOnClickListener(editInstructor);
                    addNoteBtn.setOnClickListener(editNote);

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    Fragment instructorFragment = fragmentManager.findFragmentById(R.id.list_term_fragment_container);
                    Log.d(TAG, "initViewModel: courseID is " + courseId);
                    if (instructorFragment == null) {
                        instructorFragment = ListFragment.newInstance(ListFragment.ENTITY.INSTRUCTOR, String.valueOf(courseId));
                        fragmentManager.beginTransaction()
                                .add(R.id.instructorListFragment, instructorFragment)
                                .commit();
                    }
                    FragmentManager fragmentManager2 = getSupportFragmentManager();
                    Fragment assessmentFragement = fragmentManager2.findFragmentById(R.id.list_term_fragment_container);
                    if(assessmentFragement == null) {
                        assessmentFragement = ListFragment.newInstance(ListFragment.ENTITY.ASSESSMENT, String.valueOf(courseId));
                        fragmentManager2.beginTransaction()
                                .add(R.id.assessmentListFragment, assessmentFragement)
                                .commit();
                    }
                    FragmentManager fragmentManager3 = getSupportFragmentManager();
                    Fragment noteFragment = fragmentManager3.findFragmentById(R.id.list_term_fragment_container);
                    if(noteFragment == null) {
                        noteFragment = ListFragment.newInstance(ListFragment.ENTITY.NOTE, String.valueOf(courseId));
                        fragmentManager3.beginTransaction()
                                .add(R.id.noteListFragment, noteFragment)
                                .commit();
                    }
                } else {
                    tag = tempId;
                    addInstructorBtn.setOnClickListener(addInstructor);
                }
            }
        };
        mCourseViewModel.mCourse.observe(this, courseObserver);
        setTermSpinner();
        setProgressSpinner(status);
        Log.d(TAG, "initViewModel: done");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.modified_menu, menu);
        MenuItem item = menu.getItem(0);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent;
                if (launchingActivity.equals(modifyTermActivity)) {
                    intent = new Intent(getApplicationContext(), ModifyTermActivity.class);
                    termId = (int) tag;
                    intent.putExtra("id", termId);
                } else {
                    intent = new Intent(ModifyCourseActivity.this, CourseListActivity.class);
                }
                startActivity(intent);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public View.OnClickListener addInstructor = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            InstructorFragment fragment = new InstructorFragment();
            fragment.show(fragmentManager, "fragment_add_instructor");
        }
    };

    public View.OnClickListener editInstructor = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            //TODO add instructor id
            InstructorFragment fragment = InstructorFragment.newInstance(courseId, 0);
            fragment.show(fragmentManager, "fragment_edit_instructor");
        }
    };

    public View.OnClickListener editNote = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            NoteFragment fragment = NoteFragment.newInstance(courseId, 0);
            fragment.show(fragmentManager, "fragment_edit_note");
        }
    };

    public View.OnClickListener addAssessment = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), ModifyAssessmentActivity.class);
            intent.putExtra("courseId", courseId);
            intent.putExtra("id", 0);
            startActivity(intent);
        }
    };

    public View.OnClickListener saveCourse = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String name = editName.getText().toString().trim();
            String sDate = startDate.getText().toString().trim();
            String eDate = endDate.getText().toString().trim();
            termId = (int) tag;

            if (verify.noNullStrings(name, sDate, eDate, status) && termId > -1) {
                mCourseViewModel.saveCurrentCourse(name, sDate, eDate, status, termId);
                Intent intent;
                if (launchingActivity.equals(modifyTermActivity)) {
                    intent = new Intent(getApplicationContext(), ModifyTermActivity.class);
                    intent.putExtra("id", termId);
                } else {
                    intent = new Intent(getApplicationContext(), CourseListActivity.class);
                }
                startActivity(intent);
            }
        }
    };

    public View.OnClickListener showHideDetails = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mMenuHandler.setVisibility(id, mDynamicViews);
        }
    };

    private void setTermSpinner() {
        Log.d(TAG, "setTermSpinner: started");
        ArrayAdapter<StringWithTag> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, termList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        termSelection.setAdapter(adapter);

        TermViewModel mTermViewModel = new ViewModelProvider(this).get(TermViewModel.class);
        mTermViewModel.getAllTerms().observe(this, new Observer<List<Term>>() {
            @Override
            public void onChanged(List<Term> terms) {
                for (Term term : terms) {
                    termList.add(new StringWithTag(term.getName(), term.getId(), term.getStartDate(), term.getEndDate()));
                }

                for (int i = 0; termList.size() > i; i++) {
                    StringWithTag child = (StringWithTag) termSelection.getItemAtPosition(i);
                    if (child.mTag == tag) {
                        termSelection.setSelection(i);
                        String startDate = (String) child.mStartDate;
                        String endDate = (String) child.mEndDate;
                        sendParentDates(startDate, endDate);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void setProgressSpinner(String current) {
        Log.d(TAG, "setProgressSpinner: started");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, progressList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        progressSelection.setAdapter(adapter);

        for (int i = 0; progressSelection.getCount() > i; i++) {
            if (progressSelection.getItemAtPosition(i).toString().trim().equals(current)) {
                progressSelection.setSelection(i);

            }
        }
    }


    public AdapterView.OnItemSelectedListener termListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
            StringWithTag currentSelection = (StringWithTag) parent.getItemAtPosition(position);
            tag = (int) currentSelection.mTag;
            String parentStartDate = (String) currentSelection.mStartDate;
            String parentEndDate = (String) currentSelection.mEndDate;
            sendParentDates(parentStartDate, parentEndDate);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    public AdapterView.OnItemSelectedListener progressListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
            status = (String) parent.getItemAtPosition(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    public void sendParentDates(String startDate, String endDate) {
        if (courseDataPasser != null) {
            courseDataPasser.onPassData(false,startDate, endDate);
        }
    }


}