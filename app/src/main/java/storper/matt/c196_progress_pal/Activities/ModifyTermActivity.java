package storper.matt.c196_progress_pal.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NavUtils;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import storper.matt.c196_progress_pal.Database.Entities.Term;
import storper.matt.c196_progress_pal.Fragments.ListFragment;
import storper.matt.c196_progress_pal.R;
import storper.matt.c196_progress_pal.Utilities.Alert;
import storper.matt.c196_progress_pal.Utilities.DateConverter;
import storper.matt.c196_progress_pal.Utilities.MenuHandler;
import storper.matt.c196_progress_pal.Utilities.NotificationService;
import storper.matt.c196_progress_pal.ViewModel.TermViewModel;
import storper.matt.c196_progress_pal.Utilities.DataIntegrity;

public class ModifyTermActivity extends AppCompatActivity  {

    private TermViewModel mTermViewModel;
    private final DataIntegrity mIntegrity = new DataIntegrity();
    private final MenuHandler mMenuHandler = new MenuHandler();
    private final NotificationService notificationService = new NotificationService();
    private final DateConverter dateConverter = new DateConverter();
    private static final String TAG = "ModifyTerm";
    private static final String NOTIFICATION_CHANNEL_ID = "Term";
    private String termStart;
    private String termEnd;
    private String termName;
    private int NOTIFICATION_ID_START = 1000;
    private int NOTIFICATION_ID_END = 2000;
    int id = -1;
    String termId;
    TextView nameLabel;
    EditText editName;
    EditText startDate;
    EditText endDate;
    Button saveBtn;
    Button addCourseBtn;
    Switch termReminder;
    ConstraintLayout termDetails;
    ScrollView scrollView;
    Alert alert = new Alert();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_term);
        createNotificationChannel("ModifyTerm",NOTIFICATION_CHANNEL_ID);
        getSupportActionBar().setTitle("Modify Term");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTermViewModel = new ViewModelProvider(this).get(TermViewModel.class);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getInt("id");
            termId = String.valueOf(id);
            mTermViewModel.setCurrentTerm(id);
            NOTIFICATION_ID_START = NOTIFICATION_ID_START + id;
            NOTIFICATION_ID_END = NOTIFICATION_ID_END + id;

        }

        editName = findViewById(R.id.editEntityName);
        nameLabel = findViewById(R.id.entityNameLabel);
        startDate = findViewById(R.id.editStartDate);
        endDate = findViewById(R.id.editEndDate);
        saveBtn = findViewById(R.id.saveTermBtn);
        addCourseBtn = findViewById(R.id.add_course_btn);
        termReminder = findViewById(R.id.termReminderSwitch);
        termDetails = findViewById(R.id.termDetails);
        termDetails.setVisibility(View.GONE);
        scrollView = findViewById(R.id.termScrollView);

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            scrollView.smoothScrollTo(0,0);
        }

        initViewModel(savedInstanceState);

    }

    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("termName", editName.getText().toString());
        savedInstanceState.putString("termStart", startDate.getText().toString());
        savedInstanceState.putString("termEnd", endDate.getText().toString());
    }

    private void initViewModel(Bundle savedInstanceState) {

        nameLabel.setText(R.string.term_label);
        saveBtn.setOnClickListener(saveTerm);
        addCourseBtn.setOnClickListener(addCourse);
        termReminder.setChecked(false);
        termReminder.setOnClickListener(setTermNotification);

        final Observer<Term> termObserver = new Observer<Term>() {
            @Override
            public void onChanged(@Nullable final Term term) {
                if(term != null) {
                    SharedPreferences sharedPreferences = getSharedPreferences("notificationState", MODE_PRIVATE);
                    termReminder.setChecked(sharedPreferences.getBoolean("termNotification" + termId, true));

                    if(savedInstanceState == null) {
                        termName = term.getName();
                        termStart = term.getStartDate();
                        termEnd = term.getEndDate();
                    } else {
                        termName = savedInstanceState.getString("termName");
                        termStart = savedInstanceState.getString("termStart");
                        termEnd = savedInstanceState.getString("termEnd");
                    }
                    editName.setText(termName);
                    startDate.setText(termStart);
                    endDate.setText(termEnd);
                    termDetails.setVisibility(View.VISIBLE);
                    Log.d(TAG, "onChanged: not null");
                } else {
                    termDetails.setVisibility(View.GONE);
                    termReminder.setChecked(false);
                }

            }
        };

        mTermViewModel.mTerm.observe(this, termObserver);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.list_term_fragment_container);
        if(fragment == null) {
            fragment = ListFragment.newInstance(ListFragment.ENTITY.COURSE, termId);
            fragmentManager.beginTransaction()
                    .replace(R.id.courseFragmentView, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Class<?> targetActivity = mMenuHandler.handleMenuSelection(item);
        Intent intent = new Intent(ModifyTermActivity.this, targetActivity);

        startActivity(intent);

        return super.onOptionsItemSelected(item);
    }

    public View.OnClickListener saveTerm = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
           String name  = editName.getText().toString().trim();
           String sDate = startDate.getText().toString().trim();
           String eDate = endDate.getText().toString().trim();
            termStart = sDate;
            termEnd = eDate;
           if(mIntegrity.noNullStrings(name, sDate, eDate)) {
               mTermViewModel.saveCurrentTerm(name, sDate, eDate);
               if(termReminder.isChecked()) {
                   termReminder.setChecked(false);
                   termReminder.setChecked(true);
               }
               termDetails.setVisibility(View.VISIBLE);
           } else {
               alert.emptyFields(ModifyTermActivity.this);
           }

        }
    };

    public View.OnClickListener addCourse = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(ModifyTermActivity.this, ModifyCourseActivity.class);
            intent.putExtra("termId", termId);
            intent.putExtra("launchActivity", "ModifyTermActivity");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        }
    };


    public View.OnClickListener setTermNotification = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            boolean on  = ((Switch) view).isChecked();
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            PendingIntent pi = notificationService.setPendingIntent(getApplicationContext(), NOTIFICATION_ID_START, NOTIFICATION_CHANNEL_ID
                    , "TERM " + termName, "Term has Started!");
            PendingIntent pi2 = notificationService.setPendingIntent(getApplicationContext(), NOTIFICATION_ID_END, NOTIFICATION_CHANNEL_ID
                    , "TERM " + termName, "Term has Ended!");
            if(on) {
                Toast.makeText(ModifyTermActivity.this, "Reminder Set", Toast.LENGTH_LONG).show();

                alarmManager.set(AlarmManager.RTC_WAKEUP, dateConverter.convertStringToDate(termStart).getTime(), pi);
                alarmManager.set(AlarmManager.RTC_WAKEUP, dateConverter.convertStringToDate(termEnd).getTime()
                        , pi2);
                setNotificationState(true);

            } else {
                alarmManager.cancel(pi);
                alarmManager.cancel(pi2);
                setNotificationState(false);
            }
        }
    };

    private void setNotificationState(boolean isSet) {
        SharedPreferences.Editor editor = getSharedPreferences("notificationState", MODE_PRIVATE).edit();
        editor.putBoolean("termNotification" + termId, isSet);
        editor.apply();
    }

    private void createNotificationChannel(CharSequence name,String channelId) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}