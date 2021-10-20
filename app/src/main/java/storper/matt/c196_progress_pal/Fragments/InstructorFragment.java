package storper.matt.c196_progress_pal.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import storper.matt.c196_progress_pal.R;
import storper.matt.c196_progress_pal.Utilities.DataIntegrity;

public class InstructorFragment extends DialogFragment {

    private TextView title;
    private EditText editName;
    private EditText editPhone;
    private EditText editEmail;
    private Button cancelBtn;
    private Button saveBtn;
    DataIntegrity verify = new DataIntegrity();


    public InstructorFragment() {

    }

    public static InstructorFragment newInstance(String id) {
        InstructorFragment fragment = new InstructorFragment();
        Bundle args = new Bundle();
        args.putString("instructorId", id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.instructor_fragment, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        title = view.findViewById(R.id.instructorTitle);
        editName = view.findViewById(R.id.editInstructorName);
        editPhone = view.findViewById(R.id.editInstructorPhone);
        editEmail = view.findViewById(R.id.editInstructorEmail);
        cancelBtn = view.findViewById(R.id.cancelBtn);
        saveBtn = view.findViewById(R.id.saveInstructorBtn);

        cancelBtn.setOnClickListener(dismissFragment);
    }

    public View.OnClickListener dismissFragment = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dismiss();
        }
    };

    public View.OnClickListener saveInstructor = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String name = editName.getText().toString();
            String email = editEmail.getText().toString();
            String phone = editPhone.getText().toString();

            if(verify.noNullStrings(name, email, phone)){

            }
        }
    };


}
