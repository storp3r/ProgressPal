package storper.matt.c196_progress_pal.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import storper.matt.c196_progress_pal.Activities.ModifyTermActivity;
import storper.matt.c196_progress_pal.Adapters.AssessmentListAdapter;
import storper.matt.c196_progress_pal.Adapters.CourseListAdapter;
import storper.matt.c196_progress_pal.Adapters.InstructorListAdapter;
import storper.matt.c196_progress_pal.Adapters.NoteListAdapter;
import storper.matt.c196_progress_pal.Adapters.TermListAdapter;
import storper.matt.c196_progress_pal.Database.Entities.Assessment;
import storper.matt.c196_progress_pal.Database.Entities.Course;
import storper.matt.c196_progress_pal.Database.Entities.Instructor;
import storper.matt.c196_progress_pal.Database.Entities.Note;
import storper.matt.c196_progress_pal.Database.Entities.Term;
import storper.matt.c196_progress_pal.R;
import storper.matt.c196_progress_pal.Utilities.Alert;
import storper.matt.c196_progress_pal.Utilities.Transaction;
import storper.matt.c196_progress_pal.ViewModel.AssessmentViewModel;
import storper.matt.c196_progress_pal.ViewModel.CourseViewModel;
import storper.matt.c196_progress_pal.ViewModel.InstructorViewModel;
import storper.matt.c196_progress_pal.ViewModel.NoteViewModel;
import storper.matt.c196_progress_pal.ViewModel.TermViewModel;


public class ListFragment extends Fragment  {

    private static final String TAG = "ListFragment: ";
    private static String parentId;
    private TermViewModel mTermViewModel;
    private CourseViewModel mCourseViewModel;
    private AssessmentViewModel mAssessmentViewModel;
    private InstructorViewModel mInstructorViewModel;
    private NoteViewModel mNoteViewModel;
    private Alert alert = new Alert();
    final Observer<Term> termObserver = new Observer<Term>() {
        @Override
        public void onChanged(Term term) {
            Future<Transaction.Status> status = mTermViewModel.deleteCurrentTerm(term);
            try {
                if (status.get() == Transaction.Status.FAILED) {
                    alert.deleteError(getContext(), "This Term can not be deleted while Courses are attached to it." +
                            " Please delete or change all Courses that are associated with this Term in order to delete");
                } else {
                    Toast.makeText(getContext(), "Term successfully deleted", Toast.LENGTH_LONG).show();
                }
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    };
    final Observer<Course> courseObserver = new Observer<Course>() {
        @Override
        public void onChanged(Course course) {
            Future<Transaction.Status> status = mCourseViewModel.deleteCurrentCourse(course);
            try {
                if (status.get() == Transaction.Status.FAILED) {
                    alert.deleteError(getContext(), "This Course can not be deleted while Assessments are attached to it." +
                            " Please delete or change all Assessments that are associated with this Course in order to delete");
                } else {
                    Toast.makeText(getContext(), "Term successfully deleted", Toast.LENGTH_LONG).show();
                }
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    };
    private ListAdapter adapter = null;
    private ENTITY entityType;
    private RecyclerView recyclerView;
    ItemTouchHelper.SimpleCallback touchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return true;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAbsoluteAdapterPosition();
            RecyclerView.ViewHolder swipedViewHolder = recyclerView.findViewHolderForLayoutPosition(position);
            assert swipedViewHolder != null;
            View swipedItem = swipedViewHolder.itemView;
            TextView entityName = swipedItem.findViewById(R.id.entityName);
            String name  = entityName.getText().toString();
            if (direction > 0) {
            new AlertDialog.Builder(swipedItem.getContext())
                    .setTitle("Confrim Delete")
                    .setMessage("Are you sure you want to delete " + name + "?")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (entityType == ENTITY.TERM) {
                                handleTermSwipe(swipedItem.getId());
                            } else if (entityType == ENTITY.COURSE) {
                                handleCourseSwipe(swipedItem.getId());
                            } else if (entityType == ENTITY.ASSESSMENT) {
                                handleAssessmentSwipe(swipedItem.getId());
                            } else if (entityType == ENTITY.INSTRUCTOR) {
                                handleInstructorSwipe(swipedItem.getId());
                            } else if (entityType == ENTITY.NOTE) {
                                handleNoteSwipe(swipedItem.getId());
                            }
                            adapter.notifyItemChanged(position);
                        }
                    }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    adapter.notifyItemChanged(position);
                }
            }).create().show();

            }
        }
    };

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
        if (entityType != ENTITY.NULL) {
            setCurrentAdapter(view);
        }
        return view;
    }

    public void setCurrentAdapter(View view) {
        recyclerView = view.findViewById(R.id.entity_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        int currentParentId = -1;

        if (parentId != null) {
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
                if (currentParentId == -1) {
                    mCourseViewModel.getAllCourses().observe(getViewLifecycleOwner(), adapter::submitList);
                } else {
                    mCourseViewModel.getCoursesByTermId(currentParentId).observe(getViewLifecycleOwner(), adapter::submitList);
                }
                break;
            case ASSESSMENT:
                mAssessmentViewModel = new ViewModelProvider(this).get(AssessmentViewModel.class);
                adapter = new AssessmentListAdapter(new AssessmentListAdapter.AssessmentDiff());
                if (currentParentId == -1) {
                    mAssessmentViewModel.getAllAssessments().observe(getViewLifecycleOwner(), adapter::submitList);
                } else {
                    mAssessmentViewModel.getAssessmentsByCourse(currentParentId).observe(getViewLifecycleOwner(), adapter::submitList);
                }
                break;
            case INSTRUCTOR:
                Log.d(TAG, "setCurrentAdapter: ranInstructor");
                if (currentParentId != -1) {
                    mInstructorViewModel = new ViewModelProvider(this).get(InstructorViewModel.class);
                    adapter = new InstructorListAdapter(new InstructorListAdapter.InstructorDiff());
                    mInstructorViewModel.getInstructorByCourse(currentParentId).observe(getViewLifecycleOwner(), adapter::submitList);
                }
                break;
            case NOTE:
                if (currentParentId != -1) {
                    mNoteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
                    adapter = new NoteListAdapter((new NoteListAdapter.NoteDiff()));
                    mNoteViewModel.getNotesByCourse(currentParentId).observe(getViewLifecycleOwner(), adapter::submitList);
                }
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

    public void handleTermSwipe(int id) {
        mTermViewModel.setCurrentTerm(id);
        mTermViewModel.mTerm.observe(this, termObserver);
    }

    public void handleCourseSwipe(int id) {
        mCourseViewModel.setCurrentCourse(id);
        mCourseViewModel.mCourse.observe(this, courseObserver);
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

    public void handleNoteSwipe(int id) {
        mNoteViewModel.setCurrentNote(id);
        mNoteViewModel.mNote.observe(getViewLifecycleOwner(), new Observer<Note>() {
            @Override
            public void onChanged(Note note) {
                mNoteViewModel.deleteCurrentNote(note);
            }
        });
    }

    public enum ENTITY {TERM, COURSE, ASSESSMENT, INSTRUCTOR, NOTE, NULL}
}
