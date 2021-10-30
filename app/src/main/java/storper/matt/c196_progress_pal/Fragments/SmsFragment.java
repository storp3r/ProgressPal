package storper.matt.c196_progress_pal.Fragments;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import storper.matt.c196_progress_pal.Activities.ModifyCourseActivity;
import storper.matt.c196_progress_pal.MainActivity;
import storper.matt.c196_progress_pal.R;
import storper.matt.c196_progress_pal.Utilities.DataIntegrity;

public class SmsFragment extends DialogFragment {
    private static final String TAG = "SMS FRAG";
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;

    private DataIntegrity verify = new DataIntegrity();

    private EditText phoneNumber;
    private EditText editMessage;
    private Button sendBtn;
    private Button cancelBtn;
    public static String messageContents;

    public SmsFragment() {

    }

    public static SmsFragment newInstance(String message) {
        SmsFragment fragment = new SmsFragment();
        Bundle args = new Bundle();
        args.putString("message", message);
        fragment.setArguments(args);
        messageContents = args.getString("message");
        Log.d(TAG, "newInstance: " + messageContents);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.sms_fragment, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewModel(view);

    }

    public void initViewModel(View view) {

        phoneNumber = view.findViewById(R.id.editSmsPhone);
        editMessage = view.findViewById(R.id.editSmsMessage);
        sendBtn = view.findViewById(R.id.sendSmsBtn);
        cancelBtn = view.findViewById(R.id.cancelSmsBtn);

        editMessage.setText(messageContents);
        sendBtn.setOnClickListener(sendSMS);
        cancelBtn.setOnClickListener(cancelSms);
    }

    private final View.OnClickListener sendSMS = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String phone = phoneNumber.getText().toString().trim();
            String message = editMessage.getText().toString().trim();

            if(verify.noNullStrings(phone, message)){

                if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS)== PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phone, null, message, null, null);
                    Toast.makeText(getContext(), "Note Sent!", Toast.LENGTH_LONG).show();
                    dismiss();
                }  else {
                    ModifyCourseActivity courseActivity = (ModifyCourseActivity) getContext();
                    ActivityCompat.requestPermissions(courseActivity,new String[]{Manifest.permission.SEND_SMS},100);
                    System.out.println("Message Failed");
                }
            }

        }
    };

    private final View.OnClickListener cancelSms = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dismiss();
        }
    };

}

