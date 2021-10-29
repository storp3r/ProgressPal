package storper.matt.c196_progress_pal.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import storper.matt.c196_progress_pal.Database.Entities.Note;
import storper.matt.c196_progress_pal.Database.Repositories.NoteRepository;

public class NoteViewModel extends AndroidViewModel {
    private static final String TAG = "NoteVM";
    private LiveData<List<Note>> mNotesByCourse;
    public MutableLiveData<Note> mNote = new MutableLiveData<>();
    private final NoteRepository mRepository;
    Executor e = Executors.newSingleThreadExecutor();
    Date now = new Date(System.currentTimeMillis());

    public NoteViewModel(Application application) {
        super(application);
        mRepository = new NoteRepository(application);
    }

    public void setCurrentNote(final int id) {
        e.execute(new Runnable() {
            @Override
            public void run() {
                Note currentNote = mRepository.getNoteById(id);
                mNote.postValue(currentNote);
            }
        });
    }

    public LiveData<List<Note>> getNotesByCourse(int courseId) {
        mNotesByCourse = mRepository.getCourseNotes(courseId);
        return mNotesByCourse;
    }

    public void deleteCurrentNote(Note note) {
        mRepository.deleteNote(note);
    }

    public void saveCurrentNote(String name, String details, int courseId){
        Note currentNote = mNote.getValue();

        if(currentNote ==  null) {
            currentNote = new Note(name, details, courseId);
            mRepository.insertNote(currentNote);
        } else {
            currentNote.setName(name);
            currentNote.setDetails(details);
            currentNote.setCourseId(courseId);
            mRepository.updateNote(currentNote);
        }
    }
}
