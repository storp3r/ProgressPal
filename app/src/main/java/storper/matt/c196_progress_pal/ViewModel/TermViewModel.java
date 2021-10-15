package storper.matt.c196_progress_pal.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import storper.matt.c196_progress_pal.Database.Entities.Term;
import storper.matt.c196_progress_pal.Database.Repositories.TermRepository;
import storper.matt.c196_progress_pal.Utilities.Transaction;

public class TermViewModel extends AndroidViewModel {

    private final LiveData<List<Term>> mAllTerms;
    public MutableLiveData<Term> mTerm = new MutableLiveData<>();
    public Transaction.Status mTransactionStatus;

    private TermRepository mRepository;
    Executor e = Executors.newSingleThreadExecutor();

    public TermViewModel(Application application) {
        super(application);
        mRepository = new TermRepository(application);

        mAllTerms = mRepository.getAllTerms();
    }

    public LiveData<List<Term>> getAllTerms() {
        return mAllTerms;
    }

    public void setCurrentTerm(final int id){
        e.execute(new Runnable() {
            @Override
            public void run() {
                Term currentTerm = mRepository.getTermById(id);
                mTerm.postValue(currentTerm);
            }
        });
    }

    public void deleteCurrentTerm(Term term) {
        mRepository.deleteTerm(term);
        mTransactionStatus = mRepository.mTransactionStatus;
    }

//    public LiveData<Transaction.Status> getNetworkStatus() {
//        return mNetworkStatus;
//    }


    public void saveCurrentTerm(String name, String startDate, String endDate) {
        Term currentTerm = mTerm.getValue();

        if(currentTerm == null) {
            currentTerm = new Term(name, startDate, endDate);
            mRepository.insertTerm(currentTerm);
        } else {
            currentTerm.setName(name);
            currentTerm.setStartDate(startDate);
            currentTerm.setEndDate(endDate);
            mRepository.updateTerm(currentTerm);
        }
    }

//    public void insert(Term term) {
//        mRepository.insertTerm(term);
//    }
}
