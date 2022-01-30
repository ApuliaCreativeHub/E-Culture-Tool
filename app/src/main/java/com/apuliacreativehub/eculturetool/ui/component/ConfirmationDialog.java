package com.apuliacreativehub.eculturetool.ui.component;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.ui.paths.PathsAdapter;
import com.apuliacreativehub.eculturetool.ui.paths.PathsFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class ConfirmationDialog extends DialogFragment {
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
        Dialog.TAG = tag;
    }

    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        listener = (ConfirmationDialogListener) getParentFragment();
        // Use the Builder class for convenient dialog construction
        assert getActivity() != null;
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.confirm, (dialog, id) -> listener.onDialogPositiveClick(ConfirmationDialog.this))
                .setNegativeButton(R.string.cancel, (dialog, id) -> listener.onDialogNegativeClick(ConfirmationDialog.this));
        // Create the AlertDialog object and return it
        return builder.create();
    }

}