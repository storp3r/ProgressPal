package storper.matt.c196_progress_pal.Fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import storper.matt.c196_progress_pal.Activities.ModifyAssessmentActivity;
import storper.matt.c196_progress_pal.Activities.ModifyCourseActivity;
import storper.matt.c196_progress_pal.R;
import storper.matt.c196_progress_pal.Utilities.Alert;

public class DateFragment extends Fragment implements ModifyCourseActivity.OnPassDataToFragment
        , ModifyAssessmentActivity.OnPassDataToFragment {
    private static final String TAG = "TempDate";
    final Calendar calendar = Calendar.getInstance();
    private TextView endDateLabel;
    private EditText editStartDate;
    private EditText editEndDate;
    private ImageButton startDateButton;
    private ImageButton endDateButton;
    private Boolean isStartDateButton;
    private Date parentStartDate;
    private Date parentEndDate = null;
    private Date startDate;
    private Date endDate;
    private Date minDate;
    private Date maxDate;
    private Date now = new Date(System.currentTimeMillis());
    private String parent = "Term";
    private String child = "Course";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.date_fragment, parent, false);
        initViewCreated(view);
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ModifyCourseActivity) {
            ((ModifyCourseActivity) context).courseDataPasser = this;
        } else if (context instanceof ModifyAssessmentActivity) {
            ((ModifyAssessmentActivity) context).assessmentDataPasser = this;
        }
    }

    public void initViewCreated(View view) {
        startDateButton = view.findViewById(R.id.startDateButton);
        endDateButton = view.findViewById(R.id.endDateButton);
        editStartDate = view.findViewById(R.id.editStartDate);
        editEndDate = view.findViewById(R.id.editEndDate);
        startDateButton.setOnClickListener(selectDateListener);
        endDateButton.setOnClickListener(selectDateListener);
        endDateLabel = view.findViewById(R.id.endDateLabel);
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            fillInSelectedDate();
        }
    };

    public View.OnClickListener selectDateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(editStartDate != null && editEndDate != null) {
                startDate = convertStringToDate(editStartDate.getText().toString());
                endDate = convertStringToDate(editEndDate.getText().toString());
            }
            setMinDate();
            setMaxDate();

            switch (v.getId()) {
                case R.id.startDateButton:
                    if(datesOkay()){
                        datePicker(minDate, maxDate).show();
                    } else {
                        Alert.parentHasEnded(getContext(), parent, child);
                    }
                    //TODO make alert if course or term has ended
                    isStartDateButton = true;
                    break;
                case R.id.endDateButton:
                    datePicker(startDate, parentEndDate).show();
                    isStartDateButton = false;
                    break;
            }
        }
    };

    //create datePicker
    public DatePickerDialog datePicker(Date minDate, Date maxDate) {
        DatePickerDialog datePicker = new DatePickerDialog(getContext(), date, calendar
                .get(Calendar.YEAR), calendar
                .get(Calendar.MONTH), calendar
                .get(calendar.DAY_OF_MONTH));
        datePicker.getDatePicker().setMinDate(minDate.getTime() + 1000);
        if (maxDate != null) {
            datePicker.getDatePicker().setMaxDate(maxDate.getTime() + 1000);
        }
        return datePicker;
    }

    // add text to editDateText field
    public void fillInSelectedDate() {
        String dateFormat = "yyyy-MM-dd";
        SimpleDateFormat formatDate = new SimpleDateFormat(dateFormat, Locale.US);

        if (isStartDateButton) {
            editStartDate.setText(formatDate.format(calendar.getTime()));
            endDateButton.setEnabled(true);
            startDate = calendar.getTime();
        } else {
            editEndDate.setText(formatDate.format(calendar.getTime()));
            endDate = calendar.getTime();
        }
    }


    public Date convertStringToDate(String date) {
        Date temp = null;
        try {
            temp = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return temp;
    }

    public String convertDateToString(Date date) {
        String dateFormat = "yyyy-MM-dd";
        SimpleDateFormat formatDate = new SimpleDateFormat(dateFormat, Locale.US);

        return formatDate.format(date.getTime());
    }

    //set minimum date range
    public void setMinDate() {
        List<Date> minDateList = new ArrayList<>();
        minDateList.add(now);
        minDateList.add(parentStartDate);

        minDate = null;

        for (Date date : minDateList) {
            if (date != null) {
                if (minDate == null) {
                    minDate = date;
                } else if (minDate.compareTo(date) < 0) {
                    minDate = date;
                }
            }
        }
        if (parentStartDate != null && parentEndDate != null && startDate != null) {
            if (parentStartDate.compareTo(startDate) > 0 || parentEndDate.compareTo(startDate) < 0) {
                editStartDate.setText(convertDateToString(parentStartDate));
            }
        } else if (startDate == null && parentStartDate != null) {
            editStartDate.setText(convertDateToString(parentStartDate));
        }
        Log.d(TAG, "setMinDate: minDate " + minDate);
    }

    //Set calendars maxDate range
    public void setMaxDate() {
        List<Date> maxDateList = new ArrayList<>();

        maxDateList.add(endDate);
        maxDateList.add(parentEndDate);
        maxDate = null;

        for (Date date : maxDateList) {
            if (date != null) {
                if (maxDate == null) {
                    maxDate = date;
                } else if (maxDate.compareTo(date) > 0) {
                    maxDate = date;
                }
            }
        }
    }

    public boolean datesOkay() {
        if (parentEndDate == null) {
            return true;
        } else return parentEndDate.compareTo(now) > 0;
    }

    //    Get parent date constraints
    @Override
    public void onPassData(boolean isAssessment, @NonNull String data, @NonNull String data2) {
        parentStartDate = convertStringToDate(data);
        parentEndDate = convertStringToDate(data2);
        Log.d(TAG, "onPassData: ");
        if(!editStartDate.getText().toString().equals("")){
            startDate = convertStringToDate(editStartDate.getText().toString());
            if(parentStartDate.compareTo(startDate) > 0) {
                editStartDate.setText(convertDateToString(parentStartDate));
            }
            if(!isAssessment) {
                endDate = convertStringToDate(editEndDate.getText().toString());
                if(parentEndDate.compareTo(endDate) < 0) {
                    editEndDate.setText("");
                }
            }
        }
        setMinDate();

        if (isAssessment) {
            endDateButton.setVisibility(View.GONE);
            endDateLabel.setVisibility(View.GONE);
            editEndDate.setVisibility(View.GONE);
            parent = "Course";
            child = "Assessment";
        }

    }
}



