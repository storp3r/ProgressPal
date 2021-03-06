package storper.matt.c196_progress_pal.ViewModel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import storper.matt.c196_progress_pal.Database.Entities.Course;
import storper.matt.c196_progress_pal.Database.Repositories.CourseRepository;
import storper.matt.c196_progress_pal.Utilities.Transaction;

public class CourseViewModel extends AndroidViewModel {
    private static final String TAG = "CourseViewModel";
    private final LiveData<List<Course>> mAllCourses;
    private LiveData<List<Course>> mCoursesByTerm;
    public MutableLiveData<Course> mCourse = new MutableLiveData<>();
    public int courseCount;
    private final CourseRepository mRepository;
    Executor e = Executors.newSingleThreadExecutor();

    public CourseViewModel(Application application) {
        super(application);
        mRepository = new CourseRepository(application);
        mAllCourses = mRepository.getAllCourses();
    }

    public LiveData<List<Course>> getAllCourses(){
        return mAllCourses;
    };

    public LiveData<List<Course>> getCoursesByTermId(int termId) {
        mCoursesByTerm = mRepository.getCoursesByTermId(termId);
        return mCoursesByTerm;
    }

    public void setCurrentCourse(final int id) {
        e.execute(new Runnable() {
            @Override
            public void run() {
                Course currentCourse = mRepository.getCourseById(id);
                mCourse.postValue(currentCourse);
            }
        });
    }

    public LiveData<Integer> getCourseCount() {
        return mRepository.getCourseCount();
    }

    public Future<Transaction.Status> deleteCurrentCourse(Course course) {
        return mRepository.deleteCourse(course);
    }

    public boolean saveCurrentCourse(String name, String startDate, String endDate, String status, int termId) {
        Course currentCourse = mCourse.getValue();
        boolean isNew;

        if(currentCourse == null) {
            currentCourse = new Course(name, startDate, endDate, status, termId);
            mRepository.insertCourse(currentCourse);
            mCourse.postValue(currentCourse);
            isNew = true;
        } else {
            currentCourse.setName(name);
            currentCourse.setStartDate(startDate);
            currentCourse.setEndDate(endDate);
            currentCourse.setStatus(status);
            currentCourse.setTermId(termId);
            mRepository.updateCourse(currentCourse);
            isNew = false;
        }
        return isNew;
    }

//    public void insert(Course course) {
//        mRepository.insertCourse(course);
//    }


}
