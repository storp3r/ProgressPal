package storper.matt.c196_progress_pal.Database.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import storper.matt.c196_progress_pal.Database.Dao.AssessmentDao;
import storper.matt.c196_progress_pal.Database.Entities.Assessment;
import storper.matt.c196_progress_pal.Database.RoomDatabase;

public class AssessmentRepository {

    private AssessmentDao mAssessmentDao;
    private LiveData<List<Assessment>> mAllAssessments;
    private LiveData<List<Assessment>> mAssessmentsByCourse;

    public AssessmentRepository(Application application) {
        RoomDatabase db = RoomDatabase.getDatabase(application);
        mAssessmentDao = db.assessmentDao();
        mAllAssessments = mAssessmentDao.getAssessments();
    }

    public LiveData<List<Assessment>> getAllAssessments() {
        return mAllAssessments;
    }

    public LiveData<List<Assessment>> getAssessmentsByCourse(int courseId) {
        try {
            mAssessmentsByCourse = mAssessmentDao.getAssessmentByCourseId(courseId);
        } catch(NullPointerException e) {
            e.printStackTrace();
        }
        return mAssessmentsByCourse;
    }

    public Assessment getAssessmentById(int id) {
        return mAssessmentDao.getAssessmentById(id);
    }

    public void insertAssessment(Assessment assessment) {
        RoomDatabase.databaseWriteExecutor.execute(()->{
            mAssessmentDao.insertAssessment(assessment);
        });
    }

    public void updateAssessment(Assessment assessment) {
        RoomDatabase.databaseWriteExecutor.execute(()->{
            mAssessmentDao.updateAssessment(assessment);
        });
    }

    public void deleteAssessment(Assessment assessment) {
        RoomDatabase.databaseWriteExecutor.execute(()->{
            mAssessmentDao.deleteAssessment(assessment);
        });
    }
}
