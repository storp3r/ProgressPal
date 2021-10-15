package storper.matt.c196_progress_pal.Database.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import storper.matt.c196_progress_pal.Database.Entities.Assessment;

@Dao
public interface AssessmentDao {

    @Query("SELECT * FROM assessments")
    LiveData<List<Assessment>> getAssessments();

    @Query("SELECT * FROM assessments WHERE courseId = :courseId")
    LiveData<List<Assessment>> getAssessmentByCourseId(int courseId);


    @Query("SELECT * FROM assessments WHERE assessmentId = :assessmentId")
    Assessment getAssessmentById(int assessmentId);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    public void insertAssessment(Assessment assessment);

    @Update
    public void updateAssessment(Assessment assessment);

    @Delete
    public void deleteAssessment(Assessment assessment);
}
