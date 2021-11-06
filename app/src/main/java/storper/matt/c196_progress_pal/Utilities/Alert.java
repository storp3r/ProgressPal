package storper.matt.c196_progress_pal.Utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import androidx.fragment.app.DialogFragment;

import javax.security.auth.callback.Callback;

import storper.matt.c196_progress_pal.Activities.TermListActivity;
import storper.matt.c196_progress_pal.Fragments.ListFragment;

public class Alert extends DialogFragment {
    private static final String TAG = "ALERT";



    public void deleteError(Context context, String message) {
        Log.d(TAG, "deleteError: RAN");
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setTitle("Can not delete this item");
        builder.setCancelable(true);
        builder.setNeutralButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void parentHasEnded(Context context, String parent, String child) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Unable to Edit Dates");
        builder.setMessage("The " + parent + " you have selected for this " + child + " has ended. To modify dates " +
                "please adjust the " + parent + "'s dates or selected a new " + parent);
        builder.setCancelable(true);
        builder.setNeutralButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void noParentFound(Context context, String parent, String child) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Cannot Add " + child);
        builder.setMessage("You must create a " + parent + " before you can add a " + child);
        builder.setCancelable(true);
        builder.setNeutralButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void emptyFields(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Cannot Save - Empty Fields Detected");
        builder.setMessage("Please ensure ALL fields have been filled");
        builder.setCancelable(true);
        builder.setNeutralButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }



}
