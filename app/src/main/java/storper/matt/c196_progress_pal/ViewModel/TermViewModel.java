package storper.matt.c196_progress_pal.ViewModel;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import storper.matt.c196_progress_pal.Activities.ModifyTermActivity;
import storper.matt.c196_progress_pal.Activities.TermListActivity;
import storper.matt.c196_progress_pal.Database.Entities.Term;
import storper.matt.c196_progress_pal.Database.Repositories.TermRepository;
import storper.matt.c196_progress_pal.Fragments.ListFragment;
import storper.matt.c196_progress_pal.Utilities.Alert;
import storper.matt.c196_progress_pal.Utilities.Transaction;

public class TermViewModel extends AndroidViewModel {
    private static final String TAG = "TermVM";
    private final LiveData<List<Term>> mAllTerms;
    public MutableLiveData<Term> mTerm = new MutableLiveData<>();


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

    public LiveData<Integer> getTermCount() {
        return mRepository.getTermCount();
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

    public Future<Transaction.Status> deleteCurrentTerm(Term term) {
        return mRepository.deleteTerm(term);
    }

//    public LiveData<Transaction.Status> getNetworkStatus() {
//        return mNetworkStatus;
//    }


    public boolean saveCurrentTerm(String name, String startDate, String endDate) {
        Term currentTerm = mTerm.getValue();

        boolean isNew;
        if(currentTerm == null) {
            currentTerm = new Term(name, startDate, endDate);
            mRepository.insertTerm(currentTerm);
            mTerm.postValue(currentTerm);

            isNew = true;
        } else {
            currentTerm.setName(name);
            currentTerm.setStartDate(startDate);
            currentTerm.setEndDate(endDate);
            mRepository.updateTerm(currentTerm);
            isNew = false;
        }
        return isNew;
    }

//    public void insert(Term term) {
//        mRepository.insertTerm(term);
//    }
}
