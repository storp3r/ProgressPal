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

import storper.matt.c196_progress_pal.Activities.ModifyTermActivity;
import storper.matt.c196_progress_pal.Adapters.AssessmentListAdapter;
import storper.matt.c196_progress_pal.Adapters.CourseListAdapter;
import storper.matt.c196_progress_pal.Adapters.InstructorListAdapter;
import storper.matt.c196_progress_pal.Adapters.TermListAdapter;
import storper.matt.c196_progress_pal.Database.Entities.Assessment;
import storper.matt.c196_progress_pal.Database.Entities.Course;
import storper.matt.c196_progress_pal.Database.Entities.Instructor;
import storper.matt.c196_progress_pal.Database.Entities.Term;
import storper.matt.c196_progress_pal.R;
import storper.matt.c196_progress_pal.Utilities.Alert;
import storper.matt.c196_progress_pal.Utilities.Transaction;
import storper.matt.c196_progress_pal.ViewModel.AssessmentViewModel;
import storper.matt.c196_progress_pal.ViewModel.CourseViewModel;
import storper.matt.c196_progress_pal.ViewModel.InstructorViewModel;
import storper.matt.c196_progress_pal.ViewModel.TermViewModel;


public class ListFragment extends Fragment {

    public enum ENTITY {TERM, COURSE, ASSESSMENT, INSTRUCTOR, NOTE, NULL}

    private TermViewModel mTermViewModel;
    private CourseViewModel mCourseViewModel;
    private AssessmentViewModel mAssessmentViewModel;
    private InstructorViewModel mInstructorViewModel;
    private Alert alert = new Alert();
    private ListAdapter adapter = null;



    private static final String TAG = "ListFragment: ";
    private ENTITY entityType;
    public static String parentId;
    //Reference to the activity
    public OnListItemListener mListener;
    public boolean isTerm;
    public boolean isCourse;
    public boolean isAssessment;
    public boolean isModifyTerm;
    public boolean isModifyCourse;
    public boolean isInstructor;
    int i = 0;

    public RecyclerView recyclerView;


    public interface OnListItemListener {
        void onItemSelected();
    }

    public static ListFragment newInstance(final ENTITY type, String parentEntityId) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putSerializable("entityType", type);
        args.putString("parentId", parentEntityId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedBundleInstance) {
        super.onCreate(savedBundleInstance);
        entityType = ENTITY.NULL;
        if (getArguments() != null) {
            Log.d(TAG, "onCreate: " + getContext());
            parentId = getArguments().getString("parentId");
            entityType = (ENTITY) getArguments().get("entityType");
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstance) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        if(entityType != ENTITY.NULL) {
            setCurrentAdapter(view);
        }

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
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        int currentParentId = -1;

        if(parentId != null) {
            currentParentId = Integer.parseInt(parentId);
        }

        switch (entityType) {
            case TERM:
                mTermViewModel = new ViewModelProvider(this).get(TermViewModel.class);
                adapter = new TermListAdapter(new TermListAdapter.TermDiff());
                mTermViewModel.getAllTerms().observe(getViewLifecycleOwner(), adapter::submitList);
                break;
            case COURSE:
                Log.d(TAG, "setCurrentAdapter: setCourse");
                mCourseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
                adapter = new CourseListAdapter(new CourseListAdapter.CourseDiff());
                if(currentParentId == -1) {
                    mCourseViewModel.getAllCourses().observe(getViewLifecycleOwner(), adapter::submitList);
                } else {
                    mCourseViewModel.getCoursesByTermId(currentParentId).observe(getViewLifecycleOwner(), adapter::submitList);
                }
                break;
            case ASSESSMENT:
                mAssessmentViewModel = new ViewModelProvider(this).get(AssessmentViewModel.class);
                adapter = new AssessmentListAdapter(new AssessmentListAdapter.AssessmentDiff());
                if(currentParentId == -1) {
                    mAssessmentViewModel.getAllAssessments().observe(getViewLifecycleOwner(), adapter::submitList);
                } else {
                    mAssessmentViewModel.getAssessmentsByCourse(currentParentId).observe(getViewLifecycleOwner(), adapter::submitList);
                }
                break;
            case INSTRUCTOR:
                Log.d(TAG, "setCurrentAdapter: ranInstructor");
                if(currentParentId != -1) {
                    mInstructorViewModel = new ViewModelProvider(this).get(InstructorViewModel.class);
                    adapter = new InstructorListAdapter(new InstructorListAdapter.InstructorDiff());
                    mInstructorViewModel.getInstructorByCourse(currentParentId).observe(getViewLifecycleOwner(), adapter::submitList);
                }
                break;
            default:
                Log.d(TAG, "setCurrentAdapter: adapter not supported");
                break;
        }
        recyclerView.setAdapter(adapter);


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchHelperCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    ItemTouchHelper.SimpleCallback touchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT ) {

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return true;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAbsoluteAdapterPosition();
            RecyclerView.ViewHolder swipedViewHolder = recyclerView.findViewHolderForLayoutPosition(position);
            View swipedItem = swipedViewHolder.itemView;



            if(direction > 0) {
                if (entityType == ENTITY.TERM) {
                    handleTermSwipe(swipedItem.getId());
                } else if (entityType == ENTITY.COURSE) {
                    handleCourseSwipe(swipedItem.getId());
                } else if(entityType == ENTITY.ASSESSMENT) {
                    handleAssessmentSwipe(swipedItem.getId());
                }
                else if(entityType == ENTITY.INSTRUCTOR) {
                    handleInstructorSwipe(swipedItem.getId());
                }
                adapter.notifyDataSetChanged();
            }

        }
    };

    public void handleTermSwipe(int id) {
        mTermViewModel.setCurrentTerm(id);

        mTermViewModel.mTerm.observe(getViewLifecycleOwner(), new Observer<Term>() {
            @Override
            public void onChanged(Term term) {
                int i = 0;
                if(mTermViewModel.deleteCurrentTerm(term) == Transaction.Status.FAILED) {
                    i++;
//                    alert.deleteError(getContext());


                    Log.d(TAG, "onChanged: HAS RAN " + i + " times!");
                    //TODO fix so does not run multiple time when updating fragment
                }
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
    mAssessmentViewModel.setCurrentAssessment(id);
    mAssessmentViewModel.mAssessment.observe(getViewLifecycleOwner(), new Observer<Assessment>() {
        @Override
        public void onChanged(Assessment assessment) {
            mAssessmentViewModel.deleteCurrentAssessment(assessment);
        }
    });
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
