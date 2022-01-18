package com.apuliacreativehub.eculturetool.ui.dialogfragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.apuliacreativehub.eculturetool.R;

public class ConfirmationDialog extends DialogFragment {
    public static String TAG;
    private final String title;
    private final String message;

    public interface ConfirmationDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    ConfirmationDialogListener listener;

    public ConfirmationDialog(String title, String message, String tag) {
        this.title = title;
        this.message = message;
        ErrorDialog.TAG = tag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        listener = (ConfirmationDialogListener) getParentFragment();
        // Use the Builder class for convenient dialog construction
        assert getActivity() != null;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onDialogPositiveClick(ConfirmationDialog.this);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onDialogNegativeClick(ConfirmationDialog.this);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

}
