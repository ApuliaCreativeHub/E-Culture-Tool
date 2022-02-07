package com.apuliacreativehub.eculturetool.ui.component;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public class TransactionHelper {

    public static void transactionWithAddToBackStack(FragmentActivity fragmentActivity, int id, Fragment fragment) {
        fragmentActivity.getSupportFragmentManager()
                .beginTransaction()
                .replace(id, fragment)
                .addToBackStack(null)
                .commit();
    }

    public static void transactionWithoutAddToBackStack(FragmentActivity fragmentActivity, int id, Fragment fragment) {
        fragmentActivity.getSupportFragmentManager()
                .beginTransaction()
                .replace(id, fragment)
                .commit();
    }

}