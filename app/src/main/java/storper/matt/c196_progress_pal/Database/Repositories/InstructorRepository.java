package storper.matt.c196_progress_pal.Database.Repositories;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;

import storper.matt.c196_progress_pal.Database.Dao.InstructorDao;
import storper.matt.c196_progress_pal.Database.Entities.Instructor;
import storper.matt.c196_progress_pal.Database.RoomDatabase;
import storper.matt.c196_progress_pal.Utilities.Transaction;

public class InstructorRepository {
    private static final String TAG = "Instructor Repo";
    private InstructorDao mInstructorDao;
    private LiveData<List<Instructor>> mAllInstructors;
    private LiveData<List<Instructor>> mCourseInstructors;
    public Transaction.Status mTransactionStatus;


    public InstructorRepository(Application application) {
        RoomDatabase db = RoomDatabase.getDatabase(application);
        mInstructorDao = db.instructorDao();
        mAllInstructors = mInstructorDao.getAllInstructors();

    }

    public void insertInstructor(Instructor instructor) {
        
        RoomDatabase.databaseWriteExecutor.execute(() ->{
            Log.d(TAG, "insertInstructor: started");
            mInstructorDao.insertInstructor(instructor);
        });
    }

    public void updateInstructor(Instructor instructor) {
        RoomDatabase.databaseWriteExecutor.execute(()->{
            mInstructorDao.updateInstructor(instructor);
        });
    }

    public LiveData<List<Instructor>> getCourseInstructors(int courseId) {
        try {
            mCourseInstructors = mInstructorDao.getInstructorsByCourseId(courseId);
        } catch(NullPointerException e) {
            e.printStackTrace();
        }
        return mCourseInstructors;
    }

    public LiveData<List<Instructor>> getAllInstructors() {
        return mAllInstructors;
    }

    public Instructor getInstructorById(int instructorId) {
        return mInstructorDao.getInstructorById(instructorId);
    }

    public void deleteInstructor(Instructor instructor) {
        RoomDatabase.databaseWriteExecutor.execute(()->{
                mInstructorDao.deleteInstructor(instructor);
        });
    }
}
