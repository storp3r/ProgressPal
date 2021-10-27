package storper.matt.c196_progress_pal.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Observable;

import storper.matt.c196_progress_pal.Database.Entities.Assessment;
import storper.matt.c196_progress_pal.Database.Entities.Course;
import storper.matt.c196_progress_pal.R;
import storper.matt.c196_progress_pal.Utilities.Alert;
import storper.matt.c196_progress_pal.Utilities.DataIntegrity;
import storper.matt.c196_progress_pal.Utilities.StringWithTag;
import storper.matt.c196_progress_pal.ViewModel.AssessmentViewModel;
import storper.matt.c196_progress_pal.ViewModel.CourseViewModel;

public class ModifyAssessmentActivity extends AppCompatActivity {

    private static final String TAG = "ModifyAssessment";
    public AssessmentViewModel mAssessmentViewModel;
    public DataIntegrity verify = new DataIntegrity();
    private final ArrayList<StringWithTag> courseList = new ArrayList<>();
    private final List<String> typeItems = Arrays.asList("Objective", "Performance");
    private final ArrayList<String> typeList = new ArrayList<>();
    private Date now = new Date(System.currentTimeMillis());

    private int courseId = -1;
    private int assessmentId = -1;
    private Object tag;
    private String type;

    Button saveAssessmentBtn;
    EditText editName;
    EditText dueDate;
    TextView title;
    TextView dueDateLabel;
    Spinner courseSpinner;
    Spinner typeSpinner;

    public interface OnPassDataToFragment {
        void onPassData(boolean isAssessment, String data, String data2);
    }

    public OnPassDataToFragment assessmentDataPasser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_assessment);

        Bundle extras = getIntent().getExtras();
        mAssessmentViewModel = new ViewModelProvider(this).get(AssessmentViewModel.class);

        if (extras != null) {
            courseId = extras.getInt("courseId");
            assessmentId = extras.getInt("id");
            mAssessmentViewModel.setCurrentAssessment(assessmentId);
        }

        saveAssessmentBtn = findViewById(R.id.saveAssessmentBtn);
        editName = findViewById(R.id.editAssessmentName);
        dueDate = findViewById(R.id.editStartDate);
        dueDateLabel = findViewById(R.id.startDateLabel);
        title = findViewById(R.id.assessmentTitle);
        courseSpinner = findViewById(R.id.courseSpinner);
        typeSpinner = findViewById(R.id.typeSpinner);

        initViewModel();

    }

    public void initViewModel() {
        dueDateLabel.setText("Due Date: ");
        if (assessmentId != -1) {
            title.setText("Edit Assessment");
        } else {
            title.setText("Add Assessment");
        }
        typeList.addAll(typeItems);
        courseSpinner.setOnItemSelectedListener(courseListener);
        typeSpinner.setOnItemSelectedListener(typeListener);
        saveAssessmentBtn.setOnClickListener(saveAssessment);

        final Observer<Assessment> assessmentObserver = new Observer<Assessment>() {
            @Override
            public void onChanged(Assessment assessment) {

                if(assessment != null) {
                    editName.setText(assessment.getName());
                    dueDate.setText(assessment.getDueDate());
                    tag = assessment.getCourseId();
                    type = assessment.getType();
                } else {
                    tag = courseId;
                }
            }
        };
        mAssessmentViewModel.mAssessment.observe(this, assessmentObserver);
        setCourseSpinner();
        setTypeSpinner(type);
    }

    private void setCourseSpinner() {
        ArrayAdapter<StringWithTag> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item
                ,courseList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        courseSpinner.setAdapter(adapter);

        CourseViewModel mCourseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
        mCourseViewModel.getAllCourses().observe(this, new Observer<List<Course>>() {
            @Override
            public void onChanged(List<Course> courses) {
                for(Course course : courses) {
                    courseList.add(new StringWithTag(course.getName(), course.getId(), course.getStartDate(), course.getEndDate()));
                }

                for(int i = 0; courseList.size() > i; i++) {
                    StringWithTag child = (StringWithTag) courseSpinner.getItemAtPosition(i);
                    if(child.mTag == tag) {
                        courseSpinner.setSelection(i);
                        String startDate = (String) child.mStartDate;
                        String endDate = (String) child.mEndDate;
                        sendParentDates(startDate, endDate);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void setTypeSpinner(String current) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item
                ,typeList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        typeSpinner.setAdapter(adapter);
        for(int i = 0; typeSpinner.getCount() > i; i++) {
            if(typeSpinner.getItemAtPosition(i).toString().equals(current)) {
                typeSpinner.setSelection(i);
            }
        }
    }

    private AdapterView.OnItemSelectedListener courseListener = new AdapterView.OnItemSelectedListener() {
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

    private AdapterView.OnItemSelectedListener typeListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
            type = (String) parent.getItemAtPosition(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    public View.OnClickListener saveAssessment = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String name = editName.getText().toString().trim();
            String dueDateString = dueDate.getText().toString().trim();
            courseId = (int) tag;
            if(verify.noNullStrings(name, dueDateString, type) && courseId > -1) {
                mAssessmentViewModel.saveCurrentAssessment(name, type, dueDateString, courseId);
                Toast.makeText(ModifyAssessmentActivity.this, "Assessment Successfully Saved", Toast.LENGTH_LONG).show();
            } else {
                Alert.emptyFields(ModifyAssessmentActivity.this);
            }

        }
    };

    public void sendParentDates(String startDate, String endDate) {
        if (assessmentDataPasser != null) {
            assessmentDataPasser.onPassData(true, startDate, endDate);
        }
    }
}