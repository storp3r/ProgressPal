package storper.matt.c196_progress_pal.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import storper.matt.c196_progress_pal.Adapters.CourseListAdapter;
import storper.matt.c196_progress_pal.Adapters.InstructorListAdapter;
import storper.matt.c196_progress_pal.Adapters.TermListAdapter;
import storper.matt.c196_progress_pal.Database.Entities.Course;
import storper.matt.c196_progress_pal.Database.Entities.Instructor;
import storper.matt.c196_progress_pal.Database.Entities.Term;
import storper.matt.c196_progress_pal.R;
import storper.matt.c196_progress_pal.ViewModel.CourseViewModel;
import storper.matt.c196_progress_pal.ViewModel.InstructorViewModel;
import storper.matt.c196_progress_pal.ViewModel.TermViewModel;


public class ListFragment extends Fragment {


    private TermViewModel mTermViewModel;
    private CourseViewModel mCourseViewModel;
    private InstructorViewModel mInstructorViewModel;

    private static final String termTypeString = "TermListActivity";
    private static final String courseTypeString = "CourseListActivity";
    private static final String assessmentTypeString = "AssessmentListActivity";
    private static final String modifyTermTypeString = "ModifyTermActivity";
    private static final String modifyCourseTypeString = "ModifyCourseActivity";
    private static final String modifyCourseInstructor = "ModifyCourseInstructor";
    private static final String TAG = "ListFragment: ";
    public static String parentActivity;
    public static String parentId;
    //Reference to the activity
    public OnListItemListener mListener;
    public boolean isTerm;
    public boolean isCourse;
    public boolean isAssessment;
    public boolean isModifyTerm;
    public boolean isModifyCourse;
    public boolean isInstructor;

    public RecyclerView recyclerView;


    public interface OnListItemListener {
        void onItemSelected();
    }

    public static ListFragment newInstance(String parentActivity, String parentEntityId) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putString("parentActivity", parentActivity);
        args.putString("parentId", parentEntityId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedBundleInstance) {
        super.onCreate(savedBundleInstance);

//        System.out.println("TermId: " + termId);
        if (getArguments() != null) {
            parentActivity = getArguments().getString("parentActivity");
            parentId = getArguments().getString("parentId");
            isTerm = parentActivity.equals(termTypeString);
            isCourse = parentActivity.equals(courseTypeString);
            isModifyTerm = parentActivity.equals(modifyTermTypeString);
            isAssessment = parentActivity.equals(assessmentTypeString);
            isInstructor = parentActivity.equals(modifyCourseInstructor);
        }

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstance) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        setCurrentAdapter(view);

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof OnListItemListener) {
            mListener = (OnListItemListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnListItemListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setCurrentAdapter(View view) {

        recyclerView = view.findViewById(R.id.entity_recycler_view);
        ListAdapter adapter = null;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTermViewModel = new ViewModelProvider(this).get(TermViewModel.class);
        mCourseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
        mInstructorViewModel = new ViewModelProvider(this).get(InstructorViewModel.class);

        if (isTerm) {
            adapter = new TermListAdapter(new TermListAdapter.TermDiff());
            mTermViewModel.getAllTerms().observe(getViewLifecycleOwner(), adapter::submitList);
        } else if (isCourse) {
            adapter = new CourseListAdapter(new CourseListAdapter.CourseDiff());
            mCourseViewModel.getAllCourses().observe(getViewLifecycleOwner(), adapter::submitList);
        } else if(isAssessment) {
            //TODO
        } else if(isModifyTerm && parentId != null) {
            adapter = new CourseListAdapter(new CourseListAdapter.CourseDiff());
            int currentId = Integer.parseInt(parentId);
            mCourseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
            mCourseViewModel.getCoursesByTermId(currentId).observe(getViewLifecycleOwner(), adapter::submitList);
        } else if(isInstructor && parentId != null){
            Log.d(TAG, "setCurrentAdapter: Instructor started");
            int currentId = Integer.parseInt(parentId);
            adapter = new InstructorListAdapter(new InstructorListAdapter.InstructorDiff());
            mInstructorViewModel.getInstructorByCourse(currentId).observe(getViewLifecycleOwner(), adapter::submitList);
        }

        recyclerView.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchHelperCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    ItemTouchHelper.SimpleCallback touchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAbsoluteAdapterPosition();
            RecyclerView.ViewHolder swipedViewHolder = recyclerView.findViewHolderForLayoutPosition(position);
            View swipedItem = swipedViewHolder.itemView;

            if (isTerm) {
                handleTermSwipe(swipedItem.getId());
            } else if (isCourse || isModifyTerm) {
                handleCourseSwipe(swipedItem.getId());
            } else if(isInstructor) {
                handleInstructorSwipe(swipedItem.getId());
            }
        }
    };

    public void handleTermSwipe(int id) {
        mTermViewModel.setCurrentTerm(id);

        mTermViewModel.mTerm.observe(getViewLifecycleOwner(), new Observer<Term>() {
            @Override
            public void onChanged(Term term) {
                mTermViewModel.deleteCurrentTerm(term);
            }
        });
    }

    public void handleCourseSwipe(int id){
        mCourseViewModel.setCurrentCourse(id);
        mCourseViewModel.mCourse.observe(getViewLifecycleOwner(), new Observer<Course>() {
            @Override
            public void onChanged(Course course) {
                mCourseViewModel.deleteCurrentCourse(course);
            }
        });
    }

    public void handleAssessmentSwipe(int id) {

    }

    public void handleInstructorSwipe(int id) {
        mInstructorViewModel.setCurrentInstructor(id);
        mInstructorViewModel.mInstructor.observe(getViewLifecycleOwner(), new Observer<Instructor>() {
            @Override
            public void onChanged(Instructor instructor) {
                mInstructorViewModel.deleteCurrentInstructor(instructor);
            }
        });
    }


}
