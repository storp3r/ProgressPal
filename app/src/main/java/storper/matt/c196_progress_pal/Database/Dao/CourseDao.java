package storper.matt.c196_progress_pal.Database.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import storper.matt.c196_progress_pal.Database.Entities.Course;

@Dao
public interface CourseDao {

    @Query("SELECT * FROM courses")
    LiveData<List<Course>> getCourses();

    @Query("SELECT * FROM courses WHERE termId = :termId")
    LiveData<List<Course>> getCoursesByTermId(int termId);

    @Query("SELECT * FROM courses WHERE courseId = :courseId")
    Course getCourseById(int courseId);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    public void insertCourse(Course course);

    @Update
    public void updateCourse(Course course);

    @Delete
    public void deleteCourse(Course course);
}
