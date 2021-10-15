package storper.matt.c196_progress_pal.Database.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import storper.matt.c196_progress_pal.Database.Entities.Instructor;

@Dao
public interface InstructorDao {

    @Query("SELECT * FROM instructors WHERE courseId = :courseId")
    LiveData<List<Instructor>> getInstructorsByCourseId(int courseId);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    public void insertInstructor(Instructor instructor);
}