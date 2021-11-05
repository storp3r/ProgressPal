package storper.matt.c196_progress_pal.Database.Dao;

import android.app.usage.UsageEvents;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import storper.matt.c196_progress_pal.Database.Entities.Term;

@Dao
public interface TermDao {

    @Query("SELECT * FROM terms")
    LiveData<List<Term>> getTerms();

    @Query("SELECT * FROM terms WHERE termId = :termId")
    Term getTermById(int termId);

    @Query("SELECT COUNT(termId) from terms")
    LiveData<Integer> getTermCount();

    @Query("DELETE FROM terms")
    public void deleteAll();

    @Insert(onConflict = OnConflictStrategy.ABORT)
    public void insertTerm(Term term);

    @Update
    public void updateTerm(Term term);

    @Delete
    public void deleteTerm(Term term);
}
