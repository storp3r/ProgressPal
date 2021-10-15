package storper.matt.c196_progress_pal.Database.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import storper.matt.c196_progress_pal.Database.Entities.Note;

@Dao
public interface NoteDao {

    @Query("SELECT * FROM note WHERE courseId = :courseId")
    LiveData<List<Note>> getNoteByCourseId(int courseId);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    public void insertNote(Note note);
}
