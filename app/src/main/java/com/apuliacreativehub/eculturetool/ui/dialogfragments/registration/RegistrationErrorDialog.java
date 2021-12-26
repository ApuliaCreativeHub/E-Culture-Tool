package com.apuliacreativehub.eculturetool.ui.dialogfragments.registration;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.apuliacreativehub.eculturetool.R;

public class RegistrationErrorDialog extends DialogFragment {
    private String message;

    public RegistrationErrorDialog(String message){
        this.message=message;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireContext())
                // TODO: Set message as resource string
                .setMessage(message)
                .setPositiveButton(getString(R.string.ok), (dialog, which) -> {} )
                .create();
    }

    public static String TAG = "RegistrationDialog";
}

