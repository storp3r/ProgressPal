package storper.matt.c196_progress_pal.Fragments;

import android.os.Bundle;
import android.provider.Telephony;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import storper.matt.c196_progress_pal.R;
import storper.matt.c196_progress_pal.Utilities.DataIntegrity;

public class SmsFragment extends DialogFragment {
    private static final String TAG = "SMS FRAG";

    private DataIntegrity verify = new DataIntegrity();

    private EditText phoneNumber;
    private EditText editMessage;
    private Button sendBtn;
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

        editMessage.setText(messageContents);
        sendBtn.setOnClickListener(sendSMS);
    }

    private final View.OnClickListener sendSMS = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String phone = phoneNumber.getText().toString().trim();
            String message = editMessage.getText().toString().trim();

            if(verify.noNullStrings(phone,message)){
                //TODO
            }

        }
    };
}
