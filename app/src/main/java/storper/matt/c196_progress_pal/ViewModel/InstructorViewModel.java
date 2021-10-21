package storper.matt.c196_progress_pal.ViewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import storper.matt.c196_progress_pal.Database.Entities.Instructor;
import storper.matt.c196_progress_pal.Database.Repositories.InstructorRepository;

public class InstructorViewModel extends AndroidViewModel
{
    private static final String TAG = "InstructorViewModel";
    private final LiveData<List<Instructor>> mAllInstructors;
    private LiveData<List<Instructor>> mInstructorsByCourse;
    public MutableLiveData<Instructor> mInstructor = new MutableLiveData<>();
    private InstructorRepository mRepository;
    Executor e = Executors.newSingleThreadExecutor();

    public InstructorViewModel(@NonNull Application application) {
        super(application);
        mRepository = new InstructorRepository(application);
        mAllInstructors = mRepository.getAllInstructors();
    }

    public LiveData<List<Instructor>> getAllInstructors() {
        return mAllInstructors;
    }

    public void setCurrentInstructor(final int id) {
        e.execute(new Runnable() {
            @Override
            public void run() {
                Instructor currentInstructor = mRepository.getInstructorById(id);
                mInstructor.postValue(currentInstructor);
            }
        });
    }

    public LiveData<List<Instructor>> getInstructorByCourse(int courseId) {
        mInstructorsByCourse = mRepository.getCourseInstructors(courseId);

        return mInstructorsByCourse;
    }

    public void deleteCurrentInstructor(Instructor instructor) {
        mRepository.deleteInstructor(instructor);
    }

    public void saveCurrentInstructor(String name, String phone, String email, int courseId) {
        Log.d(TAG, "saveCurrentInstructor: started");
        Instructor currentInstructor = mInstructor.getValue();

        if(currentInstructor == null) {
            currentInstructor = new Instructor(name, phone, email, courseId);
            mRepository.insertInstructor(currentInstructor);
        } else {
            currentInstructor.setName(name);
            currentInstructor.setPhone(phone);
            currentInstructor.setEmail(email);
            currentInstructor.setCourseId(courseId);
            mRepository.updateInstructor(currentInstructor);
        }
    }
}
