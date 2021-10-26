package storper.matt.c196_progress_pal.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import storper.matt.c196_progress_pal.Database.Entities.Assessment;
import storper.matt.c196_progress_pal.Database.Repositories.AssessmentRepository;

public class AssessmentViewModel extends AndroidViewModel {

    private final AssessmentRepository mRepository;
    private final LiveData<List<Assessment>> mAllAssessments;
    private LiveData<List<Assessment>> mAssessmentsByCourse;
    public MutableLiveData<Assessment> mAssessment;
    Executor e = Executors.newSingleThreadExecutor();


    public AssessmentViewModel(Application application) {
        super(application);
        mRepository = new AssessmentRepository(application);
        mAllAssessments = mRepository.getAllAssessments();
    }

    public LiveData<List<Assessment>> getAllAssessments() {
        return mAllAssessments;
    }

    public LiveData<List<Assessment>> getAssessmentsByCourse(int courseId) {
        mAssessmentsByCourse = mRepository.getAssessmentsByCourse(courseId);
        return mAssessmentsByCourse;
    }

    public void setCurrentAssessment(final int id) {
        e.execute(new Runnable() {
            @Override
            public void run() {
                Assessment currentAssessment = mRepository.getAssessmentById(id);
                mAssessment.postValue(currentAssessment);
            }
        });
    }

    public void saveCurrentAssessment(String name, String type, String dueDate, int courseId) {
        Assessment currentAssessment = mAssessment.getValue();

        if(currentAssessment == null) {
            currentAssessment = new Assessment(name, type, dueDate, courseId);
            mRepository.insertAssessment(currentAssessment);
        } else {
            currentAssessment.setName(name);
            currentAssessment.setType(type);
            currentAssessment.setDueDate(dueDate);
            currentAssessment.setCourseId(courseId);
            mRepository.updateAssessment(currentAssessment);
        }
    }

    public void deleteCurrentAssessment(Assessment assessment) {
        mRepository.deleteAssessment(assessment);
    }
}
