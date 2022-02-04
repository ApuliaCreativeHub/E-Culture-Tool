package com.apuliacreativehub.eculturetool.ui.places.fragment;

import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.data.ErrorStrings;
import com.apuliacreativehub.eculturetool.data.entity.Path;
import com.apuliacreativehub.eculturetool.data.entity.Place;
import com.apuliacreativehub.eculturetool.data.repository.NoInternetConnectionException;
import com.apuliacreativehub.eculturetool.data.repository.RepositoryNotification;
import com.apuliacreativehub.eculturetool.ui.component.Dialog;
import com.apuliacreativehub.eculturetool.ui.component.DialogTags;
import com.apuliacreativehub.eculturetool.ui.places.NodeObject;
import com.apuliacreativehub.eculturetool.ui.places.viewmodel.CreatePathViewModel;

import java.util.HashMap;
import java.util.List;

public class CreatePathFragment extends CEPathFragment {
    final Observer<RepositoryNotification<HashMap<String, List<NodeObject>>>> readyDatasetObserver = new Observer<RepositoryNotification<HashMap<String, List<NodeObject>>>>() {
        @Override
        public void onChanged(RepositoryNotification<HashMap<String, List<NodeObject>>> notification) {
            ErrorStrings errorStrings = ErrorStrings.getInstance(getResources());
            if (notification.getException() == null) {
                Log.d("CALLBACK", "I am in thread " + Thread.currentThread().getName());
                Log.d("CALLBACK", String.valueOf(notification.getData()));
                if (notification.getErrorMessage() == null) {
                    setSelectElement();
                    setDynamicArtifactRecycleView();
                    setDynamicCircleArtifactRecycleView();
                    view.findViewById(R.id.buildPathProgressBar).setVisibility(View.GONE);
                } else {
                    Log.d("Dialog", "show dialog here");
                    view.findViewById(R.id.buildPathProgressBar).setVisibility(View.GONE);
                    new Dialog(getString(R.string.error_dialog_title), errorStrings.errors.get(notification.getErrorMessage()), DialogTags.GET_ZONES_ERROR).show(getChildFragmentManager(), Dialog.TAG);
                }
            } else {
                Log.d("CALLBACK", "I am in thread " + Thread.currentThread().getName());
                Log.d("CALLBACK", "An exception occurred: " + notification.getException().getMessage());
                view.findViewById(R.id.buildPathProgressBar).setVisibility(View.GONE);
                new Dialog(getString(R.string.error_dialog_title), getString(R.string.unexpected_exception_dialog), DialogTags.GET_ZONES_EXCEPTION).show(getChildFragmentManager(), Dialog.TAG);
            }
        }
    };

    final Observer<RepositoryNotification<Path>> addPathObserver = notification -> {
        ErrorStrings errorStrings = ErrorStrings.getInstance(getResources());
        view.findViewById(R.id.buildPathProgressBar).setVisibility(View.GONE);
        if (notification.getException() == null) {
            Log.d("CALLBACK", "I am in thread " + Thread.currentThread().getName());
            Log.d("CALLBACK", String.valueOf(notification.getData()));
            if (notification.getErrorMessage() == null || notification.getErrorMessage().isEmpty()) {
                Log.i("addPlace", "OK");
                requireActivity().getSupportFragmentManager().popBackStackImmediate();
            } else {
                Log.i("addPlace", "Not OK");
                Log.d("Dialog", "show dialog here");
                new Dialog(getString(R.string.error_dialog_title), errorStrings.errors.get(notification.getErrorMessage()), DialogTags.UPDATE_PROFILE_ERROR).show(getChildFragmentManager(), Dialog.TAG);
            }
        } else {
            Log.i("addPlace", "Not OK exception");
            Log.d("CALLBACK", "I am in thread " + Thread.currentThread().getName());
            Log.d("CALLBACK", "An exception occurred: " + notification.getException().getMessage());
            new Dialog(getString(R.string.error_dialog_title), getString(R.string.unexpected_exception_dialog), DialogTags.UPDATE_PROFILE_EXCEPTION).show(getChildFragmentManager(), Dialog.TAG);
        }
    };

    public CreatePathFragment(Place place) {
        super(place);
        setReadyDatasetObserver(readyDatasetObserver);
        AdapterView.OnItemClickListener adapterViewOnItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (autoCompleteTextView.getInputType() != EditorInfo.TYPE_NULL)
                    autoCompleteTextView.setInputType(EditorInfo.TYPE_NULL);

                if (autoCompleteTextView.getError() != null)
                    autoCompleteTextView.setError(null);

                cePathViewModel.setCurrentlySelectedZoneName((String) ((TextView) view.findViewById(R.id.zoneName)).getText());
                Log.i("CurrentZone", cePathViewModel.getCurrentlySelectedZoneName());
                setDynamicArtifactRecycleView();
            }
        };
        setAdapterViewOnItemClickListener(adapterViewOnItemClickListener);
    }

    @Override
    protected void setToolbarTitle(Toolbar toolbar) {
        toolbar.setTitle(R.string.create_path_screen_title);
    }

    @Override
    protected void initializeViewModel() {
        cePathViewModel = new ViewModelProvider(this).get(CreatePathViewModel.class);
        cePathViewModel.setPlace(place);
    }

    @Override
    protected void persistPath() {
        try {
            view.findViewById(R.id.buildPathProgressBar).setVisibility(View.VISIBLE);
            cePathViewModel.addPath().observe(this, addPathObserver);
        } catch (NoInternetConnectionException e) {
            view.findViewById(R.id.buildPathProgressBar).setVisibility(View.GONE);
            new Dialog(getString(R.string.error_dialog_title), getString(R.string.err_no_internet_connection), DialogTags.NO_INTERNET_CONNECTION_ERROR).show(getChildFragmentManager(), Dialog.TAG);
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    protected void onSaveSuccessful() {
        requireActivity().getSupportFragmentManager().popBackStack();
    }
}