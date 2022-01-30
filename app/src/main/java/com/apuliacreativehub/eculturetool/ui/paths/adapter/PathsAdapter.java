package com.apuliacreativehub.eculturetool.ui.paths.adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.data.entity.Path;
import com.apuliacreativehub.eculturetool.ui.LandscapeActivity;
import com.apuliacreativehub.eculturetool.ui.SubActivity;
import com.apuliacreativehub.eculturetool.ui.component.TransactionHelper;
import com.apuliacreativehub.eculturetool.ui.paths.fragment.ShowPathFragment;
import com.apuliacreativehub.eculturetool.ui.places.fragment.CreatePathFragment;
import com.apuliacreativehub.eculturetool.ui.places.fragment.ManagePlaceFragment;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class PathsAdapter extends RecyclerView.Adapter<PathsAdapter.ViewHolder> {
    private final static int SHARE_PATH = R.id.sharePath;
    private final static int DOWNLOAD_PATH = R.id.downloadPath;
    private final static int EDIT_PATH = R.id.editPath;
    private final static int DELETE_PATH = R.id.deletePath;
    private final Context context;
    private final FragmentManager fragmentManager;
    private final ArrayList<Path> dataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final MaterialCardView cardPath;
        private final ImageView imagePath;
        private final TextView textPathName;
        private final TextView textPlaceNameAndAddress;
        private final Button btnOptions;

        public ViewHolder(View view) {
            super(view);
            cardPath = view.findViewById(R.id.cardPath);
            imagePath = view.findViewById(R.id.listComponentPathImage);
            textPathName = view.findViewById(R.id.listComponentPathName);
            textPlaceNameAndAddress = view.findViewById(R.id.listComponentPathPlace);
            btnOptions = view.findViewById(R.id.btnPathOptions);
        }

        public MaterialCardView getCardPath() {
            return cardPath;
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

    public PathsAdapter(Context context, FragmentManager fragmentManager, ArrayList<Path> dataSet) {
        this.context = context;
        this.fragmentManager = fragmentManager;
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
        String placeNameAndAddress = this.dataSet.get(position).getPlaceName() + " - " + this.dataSet.get(position).getPlaceAddress();
        viewHolder.getTextPlaceNameAndAddress().setText(placeNameAndAddress);
        viewHolder.getBtnOptions().setOnClickListener(view -> showMenu(view, R.menu.context_menu_path, position));
        viewHolder.getCardPath().setOnClickListener(
                view -> context.startActivity(new Intent(((AppCompatActivity) context), LandscapeActivity.class).putExtra(LandscapeActivity.SHOW_FRAGMENT, LandscapeActivity.SHOW_PATH_FRAGMENT)
        ));
    }

    private void showMenu(View view, int menu, int position) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.inflate(menu);

        popupMenu.setOnMenuItemClickListener(menuItem -> {
            switch(menuItem.getItemId()) {
                case SHARE_PATH:
                    // TODO: Parse JSON Path Here
                    sharePath("JSON Parse");
                    break;
                case DOWNLOAD_PATH:
                    // TODO: Download Path API
                    break;
                case EDIT_PATH:
                    // TODO: Read Path API
                    context.startActivity(new Intent(((AppCompatActivity) context), SubActivity.class).putExtra(SubActivity.SHOW_FRAGMENT, SubActivity.EDIT_PATH_FRAGMENT));
                    break;
                case DELETE_PATH:
                    Bundle result = new Bundle();
                    result.putInt("pathId", this.dataSet.get(position).getPathId());
                    fragmentManager.setFragmentResult("pathKey", result);
                    break;
            }
            return true;
        });

        popupMenu.show();
    }

    private void sharePath(String text) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        Intent shareIntent = Intent.createChooser(sendIntent, null);
        context.startActivity(shareIntent);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}