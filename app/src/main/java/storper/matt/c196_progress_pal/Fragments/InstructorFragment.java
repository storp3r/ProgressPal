package storper.matt.c196_progress_pal.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import storper.matt.c196_progress_pal.Database.Dao.CourseDao;
import storper.matt.c196_progress_pal.Database.Entities.Course;
import storper.matt.c196_progress_pal.Database.Entities.Instructor;
import storper.matt.c196_progress_pal.R;
import storper.matt.c196_progress_pal.Utilities.Alert;
import storper.matt.c196_progress_pal.Utilities.DataIntegrity;
import storper.matt.c196_progress_pal.ViewModel.CourseViewModel;
import storper.matt.c196_progress_pal.ViewModel.InstructorViewModel;
import storper.matt.c196_progress_pal.ViewModel.TermViewModel;

public class InstructorFragment extends DialogFragment {
    private static final String TAG = "Instructor fragment";
    private TextView title;
    private EditText editName;
    private EditText editPhone;
    private EditText editEmail;
    private Button cancelBtn;
    private Button saveBtn;
    private Course currentCourse;
    private static int courseId;
    private static int instructorId;
    DataIntegrity verify = new DataIntegrity();
    InstructorViewModel mInstructorViewModel;
    CourseViewModel mCourseViewModel;
    Alert alert = new Alert();


    public InstructorFragment() {

    }

    public static InstructorFragment newInstance(int currentCourseId, int currentInstructorId) {
        Log.d(TAG, "newInstance: ran");
        InstructorFragment fragment = new InstructorFragment();
        Bundle args = new Bundle();
//        args.putSerializable("entityType", ListFragment.ENTITY.INSTRUCTOR);
        args.putInt("courseId", currentCourseId);
        args.putInt("instructorId", currentInstructorId);
        fragment.setArguments(args);
        courseId = args.getInt("courseId");
        instructorId = args.getInt("instructorId");
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.instructor_fragment, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewModel(view);
    }

    public void initViewModel(View view) {
        mInstructorViewModel = new ViewModelProvider(this).get(InstructorViewModel.class);
        mCourseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);

        title = view.findViewById(R.id.instructorTitle);
        editName = view.findViewById(R.id.editInstructorName);
        editPhone = view.findViewById(R.id.editInstructorPhone);
        editEmail = view.findViewById(R.id.editInstructorEmail);
        cancelBtn = view.findViewById(R.id.cancelBtn);
        saveBtn = view.findViewById(R.id.saveInstructorBtn);

        cancelBtn.setOnClickListener(dismissFragment);
        saveBtn.setOnClickListener(saveInstructor);

        if (instructorId != 0) {
            mInstructorViewModel.setCurrentInstructor(instructorId);
            mInstructorViewModel.mInstructor.observe(getViewLifecycleOwner(), new Observer<Instructor>() {
                @Override
                public void onChanged(Instructor instructor) {
                    title.setText("EDIT INSTRUCTOR");
                    editName.setText(instructor.getName());
                    editPhone.setText(instructor.getPhone());
                    editEmail.setText(instructor.getEmail());
                    courseId = instructor.getCourseId();
                }
            });
        }
    }

    public View.OnClickListener dismissFragment = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dismiss();
        }
    };

    public View.OnClickListener saveInstructor = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String name = editName.getText().toString();
            String email = editEmail.getText().toString();
            String phone = editPhone.getText().toString();
            Log.d(TAG, "onChanged: " + courseId);
            if (verify.noNullStrings(name, email, phone) && courseId > -1) {
                Log.d(TAG, "onClick: running save");
                mInstructorViewModel.saveCurrentInstructor(name, phone, email, courseId);
                dismiss();
            } else {
                alert.emptyFields(getContext());
            }
        }
    };


}
