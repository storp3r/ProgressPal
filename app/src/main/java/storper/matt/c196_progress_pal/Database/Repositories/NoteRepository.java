package storper.matt.c196_progress_pal.Database.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import storper.matt.c196_progress_pal.Database.Dao.NoteDao;
import storper.matt.c196_progress_pal.Database.Entities.Note;
import storper.matt.c196_progress_pal.Database.RoomDatabase;

public class NoteRepository {
    private static final String TAG = "Note Repo";
    private NoteDao mNoteDao;
    private LiveData<List<Note>>  mCourseNotes;

    public NoteRepository(Application application) {
        RoomDatabase db = RoomDatabase.getDatabase(application);
        mNoteDao = db.noteDao();
    }

    public void insertNote(Note note) {
        RoomDatabase.databaseWriteExecutor.execute(()->{
            mNoteDao.insertNote(note);
        });
    }

    public void updateNote(Note note) {
        RoomDatabase.databaseWriteExecutor.execute(()->{
            mNoteDao.updateNote(note);
        });
    }

    public void deleteNote(Note note) {
        RoomDatabase.databaseWriteExecutor.execute(()->{
            mNoteDao.deleteNote(note);
        });
    }

    public Note getNoteById(int noteId) {
        return mNoteDao.getNoteById(noteId);
    }

    public LiveData<List<Note>> getCourseNotes(int courseId){
        try {
            mCourseNotes = mNoteDao.getNoteByCourseId(courseId);
        } catch(NullPointerException e) {
            e.printStackTrace();
        }
        return mCourseNotes;
    }

}
