package storper.matt.c196_progress_pal.Database.Repositories;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;

import storper.matt.c196_progress_pal.Database.Dao.TermDao;
import storper.matt.c196_progress_pal.Database.Entities.Term;
import storper.matt.c196_progress_pal.Database.RoomDatabase;
import storper.matt.c196_progress_pal.Utilities.Alert;
import storper.matt.c196_progress_pal.Utilities.Transaction;
import storper.matt.c196_progress_pal.Utilities.Transaction.Status;


public class TermRepository {

    Alert alert = new Alert();


    private static final String TAG = "TermRepository";

    private TermDao mTermDao;
    private LiveData<List<Term>> mAllTerms;
    public Transaction.Status mTransactionStatus;




    //Instantiate Repository
    public TermRepository(Application application) {
        RoomDatabase db = RoomDatabase.getDatabase(application);
        mTermDao = db.termDao();
        mAllTerms = mTermDao.getTerms();
        mTransactionStatus = Status.FAILED;
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

    public Transaction.Status deleteTerm(Term term) {
        Log.d(TAG, "deleteTerm: REPO ran");
            RoomDatabase.databaseWriteExecutor.execute(() -> {
               try {
                   mTermDao.deleteTerm(term);
                    mTransactionStatus = Status.SUCCESS;
               } catch(android.database.sqlite.SQLiteConstraintException e) {
                    mTransactionStatus = Status.FAILED;
               }
            });
        return mTransactionStatus;
    }


    public LiveData<List<Term>> getAllTerms() {
        Log.d(TAG, "getAllTerms: called");
        return mAllTerms;
    }

    public Term getTermById(int termId) {
        return mTermDao.getTermById(termId);
    }

}
