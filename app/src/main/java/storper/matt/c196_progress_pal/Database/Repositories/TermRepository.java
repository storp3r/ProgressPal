package storper.matt.c196_progress_pal.Database.Repositories;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;

import storper.matt.c196_progress_pal.Database.Dao.TermDao;
import storper.matt.c196_progress_pal.Database.Entities.Term;
import storper.matt.c196_progress_pal.Database.RoomDatabase;
import storper.matt.c196_progress_pal.Utilities.Alert;
import storper.matt.c196_progress_pal.Utilities.Transaction;
import storper.matt.c196_progress_pal.Utilities.Transaction.Status;


public class TermRepository {

    private static final String TAG = "TermRepository";
    private TermDao mTermDao;
    private LiveData<List<Term>> mAllTerms;

    //Instantiate Repository
    public TermRepository(Application application) {
        RoomDatabase db = RoomDatabase.getDatabase(application);
        mTermDao = db.termDao();
        mAllTerms = mTermDao.getTerms();
    }

    public void insertTerm(Term term) {
        Log.d(TAG, "insertTerm: started");
        RoomDatabase.databaseWriteExecutor.execute(()->{
            mTermDao.insertTerm(term);
        });
    }

    public void updateTerm(Term term) {
        RoomDatabase.databaseWriteExecutor.execute(() -> {
            mTermDao.updateTerm(term);
        });
    }

    public Future<Transaction.Status> deleteTerm(Term term) {
        Future<Transaction.Status> statusFuture = RoomDatabase.databaseWriteExecutor.submit(new Callable<Status>() {
            @Override
            public Status call() throws Exception {
                try {
                    mTermDao.deleteTerm(term);
                } catch(android.database.sqlite.SQLiteConstraintException e) {
                    return Status.FAILED;
                }
                return Status.SUCCESS;
            }
        });
        return statusFuture;
    }


    public LiveData<List<Term>> getAllTerms() {
        Log.d(TAG, "getAllTerms: called");
        return mAllTerms;
    }

    public Term getTermById(int termId) {
        return mTermDao.getTermById(termId);
    }

    public LiveData<Integer> getTermCount() {
        return mTermDao.getTermCount();
    }

}
