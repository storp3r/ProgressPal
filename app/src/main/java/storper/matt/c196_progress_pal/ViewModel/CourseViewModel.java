package storper.matt.c196_progress_pal.ViewModel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import storper.matt.c196_progress_pal.Database.Entities.Course;
import storper.matt.c196_progress_pal.Database.Repositories.CourseRepository;

public class CourseViewModel extends AndroidViewModel {
    private static final String TAG = "CourseViewModel";
    private final LiveData<List<Course>> mAllCourses;
    private LiveData<List<Course>> mCoursesByTerm;
    public MutableLiveData<Course> mCourse = new MutableLiveData<>();
    public int mLastId;
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

    public int getLastId() {
        e.execute(new Runnable() {
            @Override
            public void run() {
                mLastId = mRepository.getLastCourseId();
            }
        });
        return mLastId;
    }

    public void deleteCurrentCourse(Course course) {
        mRepository.deleteCourse(course);
    }

    public void saveCurrentCourse(String name, String startDate, String endDate, String status, int termId) {
        Course currentCourse = mCourse.getValue();

        if(currentCourse == null) {
            currentCourse = new Course(name, startDate, endDate, status, termId);
            mRepository.insertCourse(currentCourse);
        } else {
            currentCourse.setName(name);
            currentCourse.setStartDate(startDate);
            currentCourse.setEndDate(endDate);
            currentCourse.setStatus(status);
            currentCourse.setTermId(termId);
            mRepository.updateCourse(currentCourse);
        }
    }

//    public void insert(Course course) {
//        mRepository.insertCourse(course);
//    }


}
