package storper.matt.c196_progress_pal.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import storper.matt.c196_progress_pal.Database.Entities.Term;
import storper.matt.c196_progress_pal.Fragments.ListFragment;
import storper.matt.c196_progress_pal.R;
import storper.matt.c196_progress_pal.Utilities.MenuHandler;
import storper.matt.c196_progress_pal.Utilities.NotificationService;
import storper.matt.c196_progress_pal.ViewModel.TermViewModel;
import storper.matt.c196_progress_pal.Utilities.DataIntegrity;

public class ModifyTermActivity extends AppCompatActivity implements ListFragment.OnListItemListener  {

    public TermViewModel mTermViewModel;
    public DataIntegrity mIntegrity = new DataIntegrity();
    public MenuHandler mMenuHandler;
    private static final String TAG = "ModifyTerm";
    private static final String NOTIFICATION_CHANNEL_ID = "Term";
    private int NOTIFICATION_ID = 1000;
    int id = -1;
    String termId;

    TextView courseFragTitle;
    TextView nameLabel;
    EditText editName;
    EditText startDate;
    EditText endDate;
    Button saveBtn;
    Button addCourseBtn;
    Switch termReminder;
    LinearLayout detailsBtn;
    ImageView upArrow;
    ImageView downArrow;
    FragmentContainerView courseFragment;
    View[] mDynamicViews;






    public interface OnPassDataToFragment {
        void onPassData(String test);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_term);
        createNotificationChannel();

        Bundle extras = getIntent().getExtras();
        mTermViewModel = new ViewModelProvider(this).get(TermViewModel.class);

        if (extras != null) {
            id = extras.getInt("id");
            termId = String.valueOf(id);
            mTermViewModel.setCurrentTerm(id);
            NOTIFICATION_ID = NOTIFICATION_ID + id;
        }

        editName = findViewById(R.id.editEntityName);
        nameLabel = findViewById(R.id.entityNameLabel);
        startDate = findViewById(R.id.editStartDate);
        endDate = findViewById(R.id.editEndDate);
        saveBtn = findViewById(R.id.saveTermBtn);
        addCourseBtn = findViewById(R.id.add_course_btn);
        detailsBtn = findViewById(R.id.detailsButton);
        termReminder = findViewById(R.id.termReminderSwitch);
        downArrow = findViewById(R.id.downArrow);
        upArrow = findViewById(R.id.upArrrow);
        courseFragTitle = findViewById(R.id.courseFragTitle);
        courseFragment = findViewById(R.id.courseFragmentView);
        detailsBtn.setVisibility(View.GONE);

        mDynamicViews = new View[]{
                addCourseBtn,
                courseFragment,
                courseFragTitle,
                downArrow,
                upArrow
        };

       initViewModel();
    }

    private void initViewModel() {
        nameLabel.setText(R.string.term_label);
        saveBtn.setOnClickListener(saveTerm);
        addCourseBtn.setOnClickListener(addCourse);
        detailsBtn.setOnClickListener(showHideDetails);
        termReminder.setOnClickListener(setTermNotification);

        final Observer<Term> termObserver = new Observer<Term>() {
            @Override
            public void onChanged(@Nullable final Term term) {
                assert term != null;
                editName.setText(term.getName());
                startDate.setText(term.getStartDate());
                endDate.setText(term.getEndDate());
                detailsBtn.setVisibility(View.VISIBLE);
            }
        };

        mTermViewModel.mTerm.observe(this, termObserver);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.list_term_fragment_container);
        mMenuHandler = new MenuHandler();

//Pass information to fragment and start fragment
        if(fragment == null) {
            String currentClass = mMenuHandler.getCurrentClass(ModifyTermActivity.class);
            fragment = ListFragment.newInstance(ListFragment.ENTITY.COURSE, termId);
            fragmentManager.beginTransaction()
                    .replace(R.id.courseFragmentView, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.modified_menu, menu);
        MenuItem item = menu.getItem(0);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(ModifyTermActivity.this, TermListActivity.class);
                startActivity(intent);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public View.OnClickListener saveTerm = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
           String name  = editName.getText().toString().trim();
           String sDate = startDate.getText().toString().trim();
           String eDate = endDate.getText().toString().trim();

           if(mIntegrity.noNullStrings(name, sDate, eDate)) {
               mTermViewModel.saveCurrentTerm(name, sDate, eDate);
               Intent intent = new Intent(getApplicationContext(), TermListActivity.class);
               startActivity(intent);
           }

        }
    };

    public View.OnClickListener addCourse = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), ModifyCourseActivity.class);
            intent.putExtra("termId", termId);
            intent.putExtra("launchActivity", "ModifyTermActivity");
            startActivity(intent);
        }
    };

    public View.OnClickListener showHideDetails = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mMenuHandler.setVisibility(id ,mDynamicViews);
        }
    };

    public View.OnClickListener setTermNotification = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            boolean on  = ((Switch) view).isChecked();

            if(on) {
                System.out.println("Switch On");
                Toast.makeText(ModifyTermActivity.this, "Reminder Set", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(ModifyTermActivity.this, NotificationService.class);
                intent.putExtra("notificationId", NOTIFICATION_ID);
                intent.putExtra("notificationChannelId", NOTIFICATION_CHANNEL_ID);
                PendingIntent pi = PendingIntent.getBroadcast(ModifyTermActivity.this, 0, intent, 0);

                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                long timeAtButtonClick = System.currentTimeMillis();
                long  tenSecondAlarm = 1000 * 3;
                alarmManager.set(AlarmManager.RTC_WAKEUP, timeAtButtonClick + tenSecondAlarm, pi);
                //TODO
            } else {
                System.out.println("canceled");



                System.out.println("Switch Off");
                //TODO
            }
        }
    };


    @Override
    public void onItemSelected() {

    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "notifyProgressPal";
            String description = "this is a test";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}