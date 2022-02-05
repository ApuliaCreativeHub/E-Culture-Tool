package com.apuliacreativehub.eculturetool.ui.paths.fragment;

import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.data.ErrorStrings;
import com.apuliacreativehub.eculturetool.data.UserPreferencesManager;
import com.apuliacreativehub.eculturetool.data.entity.Path;
import com.apuliacreativehub.eculturetool.data.entity.Place;
import com.apuliacreativehub.eculturetool.data.repository.NoInternetConnectionException;
import com.apuliacreativehub.eculturetool.data.repository.RepositoryNotification;
import com.apuliacreativehub.eculturetool.ui.component.Dialog;
import com.apuliacreativehub.eculturetool.ui.component.DialogTags;
import com.apuliacreativehub.eculturetool.ui.paths.viewmodel.EditPathViewModel;
import com.apuliacreativehub.eculturetool.ui.places.NodeObject;
import com.apuliacreativehub.eculturetool.ui.places.fragment.CEPathFragment;

import java.util.HashMap;
import java.util.List;

public class EditPathFragment extends CEPathFragment {
    public final static int FROM_PLACE_PATHS = 0;
    public final static int FROM_PATHS = 1;

    private final Path path;
    private final int fromScreen;

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

    public EditPathFragment(Place place, Path path, int fromScreen) {
        super(place);
        setReadyDatasetObserver(readyDatasetObserver);
        setAdapterViewOnItemClickListener(adapterViewOnItemClickListener);
        this.path = path;
        this.fromScreen = fromScreen;
    }

    @Override
    protected void initializeViewModel() {
        cePathViewModel = new ViewModelProvider(this).get(EditPathViewModel.class);
        cePathViewModel.setPlace(place);
        ((EditPathViewModel) cePathViewModel).setPath(path);
    }

    @Override
    protected void setToolbarTitle(Toolbar toolbar) {
        toolbar.setTitle(R.string.edit_path_screen_title);
    }

    @Override
    protected void setPathName() {
        ((EditText) view.findViewById(R.id.txtPathName)).setText(path.getName());
    }

    @Override
    protected void persistPath() {
        if (((EditPathViewModel) cePathViewModel).getPath().getUserId() == UserPreferencesManager.getUser().getId()) {
            try {
                view.findViewById(R.id.buildPathProgressBar).setVisibility(View.VISIBLE);
                ((EditPathViewModel) cePathViewModel).editPath().observe(this, savePathObserver);
            } catch (NoInternetConnectionException e) {
                new Dialog(getString(R.string.error_dialog_title), getString(R.string.err_no_internet_connection), DialogTags.NO_INTERNET_CONNECTION_ERROR).show(getChildFragmentManager(), Dialog.TAG);
            }
        } else {
            try {
                view.findViewById(R.id.buildPathProgressBar).setVisibility(View.VISIBLE);
                cePathViewModel.addPath().observe(this, savePathObserver);
            } catch (NoInternetConnectionException e) {
                new Dialog(getString(R.string.error_dialog_title), getString(R.string.err_no_internet_connection), DialogTags.NO_INTERNET_CONNECTION_ERROR).show(getChildFragmentManager(), Dialog.TAG);
            }
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        onSaveSuccessful();
    }

    @Override
    protected void onSaveSuccessful() {
        requireActivity().finish();
    }
}