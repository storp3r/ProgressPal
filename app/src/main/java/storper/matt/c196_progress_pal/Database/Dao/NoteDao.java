package storper.matt.c196_progress_pal.Database.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import storper.matt.c196_progress_pal.Database.Entities.Note;

@Dao
public interface NoteDao {

    @Query("SELECT * FROM note WHERE courseId = :courseId")
    LiveData<List<Note>> getNoteByCourseId(int courseId);

    @Query("SELECT * FROM note WHERE noteId = :noteId")
    Note getNoteById(int noteId);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    public void insertNote(Note note);

    @Update
    public void updateNote(Note note);

    @Delete
    public void deleteNote(Note note);
}
