package storper.matt.c196_progress_pal.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import storper.matt.c196_progress_pal.Database.Entities.Note;
import storper.matt.c196_progress_pal.R;
import storper.matt.c196_progress_pal.Utilities.Alert;
import storper.matt.c196_progress_pal.Utilities.DataIntegrity;
import storper.matt.c196_progress_pal.ViewModel.InstructorViewModel;
import storper.matt.c196_progress_pal.ViewModel.NoteViewModel;

public class NoteFragment extends DialogFragment {

    private static final String TAG = "NoteFrag";
    private NoteViewModel mNoteViewModel;
    private TextView title;
    private TextView nameLabel;
    private TextView noteContentsLabel;
    private EditText editName;
    private EditText editNoteContents;
    private Button cancelBtn;
    private Button saveBtn;
    public static int courseId;
    public static int noteId;
    DataIntegrity verify = new DataIntegrity();
    Alert alert = new Alert();


    public NoteFragment() {

    }

    public static NoteFragment newInstance(int currentCourseId, int currentNoteId) {
        NoteFragment fragment = new NoteFragment();
        Bundle args = new Bundle();
        args.putInt("courseId", currentCourseId);
        args.putInt("noteId", currentNoteId);
        fragment.setArguments(args);
        courseId = args.getInt("courseId");
        noteId = args.getInt("noteId");
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.note_fragment, container);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViewModel(view);
    }

    public void initViewModel(View view){
        mNoteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);

        title = view.findViewById(R.id.noteTitle);
        nameLabel = view.findViewById(R.id.noteNameLabel);
        noteContentsLabel = view.findViewById(R.id.noteLabel);
        editName = view.findViewById(R.id.editNoteName);
        editNoteContents = view.findViewById(R.id.editNoteContents);
        cancelBtn = view.findViewById(R.id.cancelNoteBtn);
        saveBtn = view.findViewById(R.id.saveNoteBtn);

        cancelBtn.setOnClickListener(dismissFragment);
        saveBtn.setOnClickListener(saveNote);

        if(noteId != 0){
            mNoteViewModel.setCurrentNote(noteId);
            mNoteViewModel.mNote.observe(getViewLifecycleOwner(), new Observer<Note>() {
                @Override
                public void onChanged(Note note) {
                    title.setText("Edit Note");
                    editName.setText(note.getName());
                    editNoteContents.setText(note.getDetails());
                    editNoteContents.setSelection(editNoteContents.getText().length());
                    courseId = note.getCourseId();
                }
            });
        } else {
            title.setText("Add Note");
        }

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
            String name = editName.getText().toString().trim();
            String contents = editNoteContents.getText().toString().trim();
            if(verify.noNullStrings(name, contents) && courseId != -1) {
                System.out.println(courseId);
                mNoteViewModel.saveCurrentNote(name, contents, courseId);
                Toast.makeText(getContext(), "Note Saved Successfully!", Toast.LENGTH_LONG).show();
                dismiss();
            } else {
                alert.emptyFields(getContext());
            }
        }
    };


}
