package com.apuliacreativehub.eculturetool.ui.component;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.apuliacreativehub.eculturetool.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class ConfirmationDialog extends DialogFragment {
    private final String title;
    private final String message;
    private int positiveButtonLabel = R.string.confirm;
    private int negativeButtonLabel = R.string.cancel;

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

    public void setCustomPositiveButtonLabel(int resourceId) {
        positiveButtonLabel = resourceId;
    }

    public void setCustomNegativeButtonLabel(int resourceId) {
        negativeButtonLabel = resourceId;
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
                .setPositiveButton(positiveButtonLabel, (dialog, id) -> listener.onDialogPositiveClick(ConfirmationDialog.this))
                .setNegativeButton(negativeButtonLabel, (dialog, id) -> listener.onDialogNegativeClick(ConfirmationDialog.this));
        // Create the AlertDialog object and return it
        return builder.create();
    }

}