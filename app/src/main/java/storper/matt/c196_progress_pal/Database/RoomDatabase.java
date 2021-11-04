package storper.matt.c196_progress_pal.Database;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import storper.matt.c196_progress_pal.Database.Dao.InstructorDao;
import storper.matt.c196_progress_pal.Database.Dao.NoteDao;
import storper.matt.c196_progress_pal.Database.Entities.Assessment;
import storper.matt.c196_progress_pal.Database.Entities.Course;
import storper.matt.c196_progress_pal.Database.Dao.AssessmentDao;
import storper.matt.c196_progress_pal.Database.Dao.CourseDao;
import storper.matt.c196_progress_pal.Database.Dao.TermDao;
import storper.matt.c196_progress_pal.Database.Entities.Instructor;
import storper.matt.c196_progress_pal.Database.Entities.Note;
import storper.matt.c196_progress_pal.Database.Entities.Term;


@androidx.room.Database(entities = {Term.class, Course.class, Assessment.class, Instructor.class, Note.class}, version = 5)
public abstract class RoomDatabase extends androidx.room.RoomDatabase {


    private static final String DATABASE_NAME = "Room.db";
    private static final Object LOCK = new Object();
    private static final String TAG = "Database";

    public abstract TermDao termDao();
    public abstract CourseDao courseDao();
    public abstract AssessmentDao assessmentDao();
    public abstract InstructorDao instructorDao();
    public abstract NoteDao noteDao();


    private static volatile RoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    //Singleton
    public static RoomDatabase getDatabase(Context context) {
        Log.d(TAG, "getInstance: database started");

        if(INSTANCE == null) {
            synchronized (LOCK) {
                if(INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), RoomDatabase.class,
                            DATABASE_NAME).fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                    Log.d(TAG, "getInstance: database built");
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Log.d(TAG, "onCreate: ");
           new PopulateDbAsyncTask(INSTANCE).execute();
        }
    };




    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {

        private final TermDao mTermDao;
        private final CourseDao mCourseDao;
        private final AssessmentDao mAssessmentDao;
        private final InstructorDao mInstructorDao;


        private PopulateDbAsyncTask(RoomDatabase db) {

            mTermDao = db.termDao();
            mCourseDao = db.courseDao();
            mAssessmentDao = db.assessmentDao();
            mInstructorDao = db.instructorDao();

        }

        @Override
        protected Void doInBackground(Void... voids) {
//            Log.d(TAG, "doInBackground: insertions started");
//
//            //Populate database first time
//            mTermDao.insertTerm(new Term("Term10", "2021-10-06", "2022-02-22"));
//            mTermDao.insertTerm(new Term("Term2","2021-11-06", "2022-10-06"));
//            mCourseDao.insertCourse(new Course("Course 1", "2021-10-06", "2021-10-08", "in-progess", 1));
//            mAssessmentDao.insertAssessment(new Assessment("Assessment 1", "objective", "2021-10-18", 1));
//            mInstructorDao.insertInstructor(new Instructor("john", "4984984", "dfssdf", 1));
//
//            Log.d(TAG, "doInBackground: instructors"
//            + mInstructorDao.getInstructorCount());
//
//            Log.d(TAG, "doInBackground: courses " + mCourseDao.getCourseCount());

            return null;
        }
    }



}
