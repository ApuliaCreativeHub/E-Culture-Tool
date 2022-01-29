package com.apuliacreativehub.eculturetool.ui.paths;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.data.entity.Path;
import com.apuliacreativehub.eculturetool.data.repository.NoInternetConnectionException;
import com.apuliacreativehub.eculturetool.ui.component.ConfirmationDialog;
import com.apuliacreativehub.eculturetool.ui.component.Dialog;

import java.util.ArrayList;

public class PathsAdapter extends RecyclerView.Adapter<PathsAdapter.ViewHolder> implements ConfirmationDialog.ConfirmationDialogListener {
    private final Context context;
    private final ArrayList<Path> dataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imagePath;
        private final TextView textPathName;
        private final TextView textPlaceNameAndAddress;
        private final Button btnOptions;

        public ViewHolder(View view) {
            super(view);
            imagePath = view.findViewById(R.id.listComponentPathImage);
            textPathName = view.findViewById(R.id.listComponentPathName);
            textPlaceNameAndAddress = view.findViewById(R.id.listComponentPathPlace);
            btnOptions = view.findViewById(R.id.btnPathOptions);
        }
        
        public ImageView getImagePath() {
            return imagePath;
        }

        public TextView getTextPathName() {
            return textPathName;
        }

        public TextView getTextPlaceNameAndAddress() {
            return textPlaceNameAndAddress;
        }

        public Button getBtnOptions() {
            return btnOptions;
        }
    }

    public PathsAdapter(Context context, ArrayList<Path> dataSet) {
        this.context = context;
        this.dataSet = dataSet;
    }

    @Override @NonNull
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.component_card_path, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getTextPathName().setText(this.dataSet.get(position).getPathName());
        viewHolder.getTextPlaceNameAndAddress().setText(this.dataSet.get(position).getPlaceName() + " - " + this.dataSet.get(position).getPlaceAddress());
        viewHolder.getBtnOptions().setOnClickListener(view -> showMenu(view, R.menu.context_menu_path));
    }

    private void showMenu(View view, int menu) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.inflate(menu);

        popupMenu.setOnMenuItemClickListener(menuItem -> {
            switch(menuItem.getItemId()) {
                case R.id.sharePath:
                    // TODO: Share Path API
                    break;
                case R.id.downloadPath:
                    // TODO: Download Path API
                    break;
                case R.id.deletePath:
                    showNoticeDialog();
                    break;
            }
            return true;
        });

        popupMenu.show();
    }

    public void showNoticeDialog() {
        DialogFragment dialog = new ConfirmationDialog(context.getString(R.string.warning_dialog_title), context.getString(R.string.warning_delete_path), "DELETE_PATH");
        dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "NoticeDialogFragment");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        Log.i("Response", "AOPOSITIVE");
        // TODO: Delete Path API
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        Log.i("Response", "NEGATIVE");
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}