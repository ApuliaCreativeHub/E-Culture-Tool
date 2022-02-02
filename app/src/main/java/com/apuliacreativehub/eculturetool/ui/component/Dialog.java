package com.apuliacreativehub.eculturetool.ui.component;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.apuliacreativehub.eculturetool.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class Dialog extends DialogFragment {
    public static String TAG;
    private final String title;
    private final String message;

    public Dialog(String title, String message, String tag) {
        this.title = title;
        this.message = message;
        Dialog.TAG = tag;
    }

    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new MaterialAlertDialogBuilder(requireContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(getString(R.string.ok), (dialog, which) -> {
                })
                .create();
    }

}