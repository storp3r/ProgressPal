package storper.matt.c196_progress_pal.Database.Repositories;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

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

    public Future<Transaction.Status> deleteCourse(Course course) {
        Future<Transaction.Status> statusFuture = RoomDatabase.databaseWriteExecutor.submit(new Callable<Transaction.Status>() {
            @Override
            public Transaction.Status call() throws Exception {
                try {
                    mCourseDao.deleteCourse(course);
                } catch(android.database.sqlite.SQLiteConstraintException e) {
                    return Transaction.Status.FAILED;
                }
                return Transaction.Status.SUCCESS;
            }
        });
        return statusFuture;
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

    public LiveData<Integer> getCourseCount() {
        Log.d(TAG, "getCourseCount: " + mCourseDao.getCourseCount());
        return mCourseDao.getCourseCount();
    }

}
