package com.apuliacreativehub.eculturetool.ui.paths.fragment;

import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.data.ErrorStrings;
import com.apuliacreativehub.eculturetool.data.entity.Path;
import com.apuliacreativehub.eculturetool.data.entity.Place;
import com.apuliacreativehub.eculturetool.data.repository.NoInternetConnectionException;
import com.apuliacreativehub.eculturetool.data.repository.RepositoryNotification;
import com.apuliacreativehub.eculturetool.ui.component.Dialog;
import com.apuliacreativehub.eculturetool.ui.paths.viewmodel.EditPathViewModel;
import com.apuliacreativehub.eculturetool.ui.places.NodeObject;
import com.apuliacreativehub.eculturetool.ui.places.fragment.CEPathFragment;

import java.util.HashMap;
import java.util.List;

public class EditPathFragment extends CEPathFragment {
    private final Path path;


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
                    ((EditPathViewModel) cePathViewModel).setGraphDatasetFromOrderedObjects();
                    setDynamicCircleArtifactRecycleView();
                } else {
                    Log.d("Dialog", "show dialog here");
                    new Dialog(getString(R.string.error_dialog_title), errorStrings.errors.get(notification.getErrorMessage()), "GET_ZONES_ERROR").show(getChildFragmentManager(), Dialog.TAG);
                }
            } else {
                Log.d("CALLBACK", "I am in thread " + Thread.currentThread().getName());
                Log.d("CALLBACK", "An exception occurred: " + notification.getException().getMessage());
                new Dialog(getString(R.string.error_dialog_title), getString(R.string.unexpected_exception_dialog), "GET_ZONES_EXCEPTION").show(getChildFragmentManager(), Dialog.TAG);
            }
        }
    };
    private final AdapterView.OnItemClickListener adapterViewOnItemClickListener = new AdapterView.OnItemClickListener() {
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


    public EditPathFragment(Place place, Path path) {
        super(place);
        setReadyDatasetObserver(readyDatasetObserver);
        setAdapterViewOnItemClickListener(adapterViewOnItemClickListener);
        this.path = path;
    }

    protected void initializeViewModel() {
        cePathViewModel = new ViewModelProvider(this).get(EditPathViewModel.class);
        cePathViewModel.setPlace(place);
        ((EditPathViewModel) cePathViewModel).setPath(path);
    }

    protected void setToolbarTitle(Toolbar toolbar) {
        toolbar.setTitle(R.string.edit_path_screen_title);
    }

    protected void persistPath() {
        if (((EditPathViewModel) cePathViewModel).getPath().getUserId() == cePathViewModel.getPlace().getUserId()) {
            try {
                ((EditPathViewModel) cePathViewModel).editPath().observe(this, savePathObserver);
            } catch (NoInternetConnectionException e) {
                new Dialog(getString(R.string.error_dialog_title), getString(R.string.err_no_internet_connection), "NO_INTERNET_CONNECTION_ERROR").show(getChildFragmentManager(), Dialog.TAG);
            }
        } else {
            try {
                cePathViewModel.addPath().observe(this, savePathObserver);
            } catch (NoInternetConnectionException e) {
                new Dialog(getString(R.string.error_dialog_title), getString(R.string.err_no_internet_connection), "NO_INTERNET_CONNECTION_ERROR").show(getChildFragmentManager(), Dialog.TAG);
            }
        }
    }


}