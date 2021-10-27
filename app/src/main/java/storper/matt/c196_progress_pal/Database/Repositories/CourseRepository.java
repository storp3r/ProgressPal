package storper.matt.c196_progress_pal.Database.Repositories;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;

import storper.matt.c196_progress_pal.Database.Dao.CourseDao;
import storper.matt.c196_progress_pal.Database.Entities.Course;
import storper.matt.c196_progress_pal.Database.RoomDatabase;
import storper.matt.c196_progress_pal.Utilities.Transaction;

public class CourseRepository {

    private static final String TAG = "CourseRepository";

    private CourseDao mCourseDao;
    private LiveData<List<Course>> mAllCourses;
    private LiveData<List<Course>> mCoursesByTerm;

    public Transaction.Status transactionStatus;


    public CourseRepository(Application application) {
        Log.d(TAG, "CourseRepository: started");
        RoomDatabase db = RoomDatabase.getDatabase(application);
        mCourseDao = db.courseDao();
        mAllCourses = mCourseDao.getCourses();
        transactionStatus = Transaction.Status.FAILED;
    }

    public void insertCourse(Course course) {
        Log.d(TAG, "insertCourse: started");
        RoomDatabase.databaseWriteExecutor.execute(()->{
            mCourseDao.insertCourse(course);
        });
    }

    public void updateCourse(Course course) {
        RoomDatabase.databaseWriteExecutor.execute(() -> {
            mCourseDao.updateCourse(course);
        });
    }

    public Transaction.Status deleteCourse(Course course) {
        RoomDatabase.databaseWriteExecutor.execute(() -> {
            try{
                mCourseDao.deleteCourse(course);
                transactionStatus = Transaction.Status.SUCCESS;
            } catch(android.database.sqlite.SQLiteConstraintException e) {
                transactionStatus = Transaction.Status.FAILED;
            }
        });
        return transactionStatus;
    }

    public LiveData<List<Course>> getAllCourses() {
        return mAllCourses;
    }

    public LiveData<List<Course>> getCoursesByTermId(int termId) {

        try {
            mCoursesByTerm = mCourseDao.getCoursesByTermId(termId);
            System.out.println(termId);
        } catch(NullPointerException e) {
            System.out.println("null issue");
        }
        Log.d(TAG, "getCoursesByTermId: REPO");
        System.out.println(mCoursesByTerm);
        return mCoursesByTerm;
    }

    public Course getCourseById(int courseId) {
        return mCourseDao.getCourseById(courseId);
    }

    public int getLastCourseId() {
        return mCourseDao.getLastCourseId();
    }

}
