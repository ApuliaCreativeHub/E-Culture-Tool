package com.apuliacreativehub.eculturetool.ui.component;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.apuliacreativehub.eculturetool.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class ErrorDialog extends DialogFragment {
    public static String TAG;
    private final String title;
    private final String message;

    public ErrorDialog(String title, String message, String tag) {
        this.title = title;
        this.message = message;
        ErrorDialog.TAG = tag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new MaterialAlertDialogBuilder(requireContext())
                .setTitle(title)
                // TODO: Set message as resource string
                .setMessage(message)
                .setPositiveButton(getString(R.string.ok), (dialog, which) -> {
                })
                .create();
    }

}