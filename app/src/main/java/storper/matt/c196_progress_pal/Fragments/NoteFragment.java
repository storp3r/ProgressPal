package storper.matt.c196_progress_pal.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;


import storper.matt.c196_progress_pal.R;
import storper.matt.c196_progress_pal.Utilities.DataIntegrity;

public class NoteFragment extends DialogFragment {

    private static final String TAG = "NoteFrag";
    private TextView title;
    private TextView nameLabel;
    private TextView noteContentsLabel;
    private EditText editName;
    private EditText editNoteContents;
    private Button cancelBtn;
    private Button saveBtn;
    public static int courseId = -1;
    DataIntegrity verify = new DataIntegrity();


    public NoteFragment() {

    }

    public static NoteFragment newInstance(int id) {
        NoteFragment fragment = new NoteFragment();
        Bundle args = new Bundle();
        args.putInt("courseid", id);
        fragment.setArguments(args);
        courseId = args.getInt("courseId");
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.note_fragment, container);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        title = view.findViewById(R.id.noteTitle);
        nameLabel = view.findViewById(R.id.noteNameLabel);
        noteContentsLabel = view.findViewById(R.id.noteLabel);
        editName = view.findViewById(R.id.editNoteName);
        editNoteContents = view.findViewById(R.id.editNoteContents);
        cancelBtn = view.findViewById(R.id.cancelNoteBtn);
        saveBtn = view.findViewById(R.id.saveNoteBtn);
    }

    public View.OnClickListener dismissFragment = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dismiss();
        }
    };

    public View.OnClickListener saveNote = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String name = editName.getText().toString();
            String contents = editNoteContents.getText().toString();
            if(verify.noNullStrings(name, contents) && courseId != -1) {

            }
        }
    };


}
