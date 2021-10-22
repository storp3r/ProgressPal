package storper.matt.c196_progress_pal.Database.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import storper.matt.c196_progress_pal.Database.Entities.Instructor;

@Dao
public interface InstructorDao {

    @Query("SELECT *  FROM instructors")
    LiveData<List<Instructor>> getAllInstructors();

    @Query("SELECT COUNT(*) FROM instructors")
    int getInstructorCount();

    @Query("SELECT * FROM instructors WHERE courseId = :courseId")
    LiveData<List<Instructor>> getInstructorsByCourseId(int courseId);

    @Query("SELECT * FROM instructors WHERE instructorId = :instructorId")
    Instructor getInstructorById(int instructorId);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    public void insertInstructor(Instructor instructor);

    @Update
    public void updateInstructor(Instructor instructor);

    @Delete
    public void deleteInstructor(Instructor instructor);
}
