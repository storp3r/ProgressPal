package storper.matt.c196_progress_pal.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import storper.matt.c196_progress_pal.Database.Entities.Course;
import storper.matt.c196_progress_pal.Database.Entities.Term;
import storper.matt.c196_progress_pal.R;
import storper.matt.c196_progress_pal.Utilities.DataIntegrity;
import storper.matt.c196_progress_pal.Utilities.StringWithTag;
import storper.matt.c196_progress_pal.ViewModel.CourseViewModel;
import storper.matt.c196_progress_pal.ViewModel.TermViewModel;

public class ModifyCourseActivity extends AppCompatActivity  {
    private static final String TAG = "ModifyCourse";
    private static final String modifyTermActivity = "ModifyTermActivity";
    private final ArrayList<StringWithTag> termList = new ArrayList<>();
    private final List<String> progressItems = Arrays.asList("Not Started", "In-Progress", "Completed");
    private final ArrayList<String> progressList = new ArrayList<>();
    public CourseViewModel mCourseViewModel;
    public DataIntegrity mIntegrity = new DataIntegrity();
    private String launchingActivity;
    private String parentStartDate;
    private String parentEndDate;

    int id;
    private Object tag;
    public String status;
    public String currentCourse = null;
    private EditText editName;
    private TextView nameLabel;
    private EditText startDate;
    private EditText endDate;
    private Spinner progressSelection;
    private Spinner termSelection;
    private Button saveBtn;
    int termId;
    private String tempId;

    public interface OnPassDataToFragment {
        void onPassData(String data, String data2);
    }

   public OnPassDataToFragment dataPasser;


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
//            parentStartDate = extras.getString("parentStartDate");
//            parentEndDate = extras.getString("parentEndDate");
            mCourseViewModel.setCurrentCourse(id);
        }

        editName = findViewById(R.id.editEntityName);
        nameLabel = findViewById(R.id.entityNameLabel);
        startDate = findViewById(R.id.editStartDate);
        endDate = findViewById(R.id.editEndDate);
        progressSelection = findViewById(R.id.progressSpinner);
        termSelection = findViewById(R.id.termSpinner);
        saveBtn = findViewById(R.id.saveCourseBtn);

        initViewModel();
    }

    private void initViewModel() {
        nameLabel.setText(R.string.course_label);
        saveBtn.setOnClickListener(saveCourse);
        progressList.addAll(progressItems);

        progressSelection.setOnItemSelectedListener(progressListener);
        termSelection.setOnItemSelectedListener(termListener);

        final Observer<Course> courseObserver = new Observer<Course>() {
            @Override
            public void onChanged(@Nullable final Course course) {

                if(course != null) {
                    editName.setText(course.getName());
                    startDate.setText(course.getStartDate());
                    endDate.setText(course.getEndDate());
                    tag = course.getTermId();
                    status = course.getStatus();
                    setProgressSpinner(status);
                } else {
                    tag = tempId;
                }
            }
        };
        mCourseViewModel.mCourse.observe(this, courseObserver);

        setTermSpinner();
        setProgressSpinner(status);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.modified_menu, menu);
        MenuItem item = menu.getItem(0);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent;
                if(launchingActivity.equals(modifyTermActivity)) {
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

    public View.OnClickListener saveCourse = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String name  = editName.getText().toString().trim();
            String sDate = startDate.getText().toString().trim();
            String eDate = endDate.getText().toString().trim();            ;
            termId = (int) tag;

            if(mIntegrity.noNullStrings(name, sDate, eDate, status) && termId > -1) {
                mCourseViewModel.saveCurrentCourse(name, sDate, eDate, status, termId);
                Intent intent;
                if(launchingActivity.equals(modifyTermActivity)) {
                    intent = new Intent(getApplicationContext(), ModifyTermActivity.class);
                    intent.putExtra("id", termId);
                } else {
                    intent = new Intent(getApplicationContext(), CourseListActivity.class);
                }
                startActivity(intent);
            }

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
                for(Term term : terms) {
                    termList.add(new StringWithTag(term.getName(), term.getId(), term.getStartDate(), term.getEndDate()));
                }

                for(int i = 0; termList.size() > i; i++) {
                    StringWithTag child = (StringWithTag) termSelection.getItemAtPosition(i);
                    if(child.mTag == tag) {
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

        for(int i = 0; progressSelection.getCount() > i; i++) {
            if(progressSelection.getItemAtPosition(i).toString().trim().equals(current)) {
                progressSelection.setSelection(i);

            }
        }
    }


    public AdapterView.OnItemSelectedListener termListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
            StringWithTag currentSelection = (StringWithTag) parent.getItemAtPosition(position);
            tag = (int) currentSelection.mTag;
            parentStartDate = (String) currentSelection.mStartDate;
            parentEndDate = (String) currentSelection.mEndDate;
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
        if(dataPasser != null) {
            dataPasser.onPassData(startDate, endDate);
        }
    }



}