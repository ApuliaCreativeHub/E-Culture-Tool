package com.apuliacreativehub.eculturetool.ui.paths.adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.data.entity.Path;
import com.apuliacreativehub.eculturetool.ui.LandscapeActivity;
import com.apuliacreativehub.eculturetool.ui.SubActivity;
import com.apuliacreativehub.eculturetool.ui.paths.fragment.EditPathFragment;
import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PathsAdapter extends RecyclerView.Adapter<PathsAdapter.ViewHolder> {
    private final static int SHARE_PATH = R.id.sharePath;
    private final static int DOWNLOAD_PATH = R.id.downloadPath;
    private final static int EDIT_PATH = R.id.editPath;
    private final static int DELETE_PATH = R.id.deletePath;
    private final Context context;
    private final FragmentManager fragmentManager;
    private final List<Path> dataSet;

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

    public PathsAdapter(Context context, FragmentManager fragmentManager, List<Path> dataSet) {
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
        viewHolder.getTextPathName().setText(this.dataSet.get(position).getName());
        String placeNameAndAddress = this.dataSet.get(position).getPlace().getName() + " - " + this.dataSet.get(position).getPlace().getAddress();
        viewHolder.getTextPlaceNameAndAddress().setText(placeNameAndAddress);
        viewHolder.getBtnOptions().setOnClickListener(view -> showMenu(view, R.menu.context_menu_path, position));
        viewHolder.getCardPath().setOnClickListener(
                view -> context.startActivity(new Intent(context, LandscapeActivity.class)
                        .putExtra(LandscapeActivity.SHOW_FRAGMENT, LandscapeActivity.SHOW_PATH_FRAGMENT)
                        .putExtra("path", dataSet.get(position))
                ));
    }

    private void showMenu(View view, int menu, int position) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.inflate(menu);

        popupMenu.setOnMenuItemClickListener(menuItem -> {
            switch(menuItem.getItemId()) {
                case SHARE_PATH:
                    // TODO: Convert in human readable text
                    sharePath(new Gson().toJson(this.dataSet.get(position)));
                    break;
                case DOWNLOAD_PATH:
                    downloadPath(new Gson().toJson(this.dataSet.get(position)));
                    break;
                case EDIT_PATH:
                    context.startActivity(
                            new Intent(context, SubActivity.class).putExtra(SubActivity.SHOW_FRAGMENT, SubActivity.EDIT_PATH_FRAGMENT)
                                    .putExtra("place", dataSet.get(position).getPlace())
                                    .putExtra("path", dataSet.get(position))
                                    .putExtra("fromScreen", EditPathFragment.FROM_PATHS)
                    );
                    break;
                case DELETE_PATH:
                    Bundle result = new Bundle();
                    result.putInt("pathId", this.dataSet.get(position).getId());
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

    private void downloadPath(String text) {
        ActivityCompat.requestPermissions((AppCompatActivity) context, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        FileOutputStream fileOutputStream = null;
        File file = getDisc();

        if(!file.exists() && !file.mkdirs()) {
            file.mkdirs();
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyymmsshhmmss");
        String date = simpleDateFormat.format(new Date());
        String name = "PATH" + date + ".txt";
        String file_name = file.getAbsolutePath() + "/" + name;
        File new_file = new File(file_name);

        try {
            PrintStream printStream = new PrintStream(new_file);
            printStream.print(text);
            printStream.flush();
            printStream.close();
            Toast.makeText(context.getApplicationContext(), context.getString(R.string.path_download_success), Toast.LENGTH_SHORT).show();
        } catch(IOException exception) {
            Toast.makeText(context.getApplicationContext(), context.getString(R.string.path_download_error), Toast.LENGTH_SHORT).show();
        }

        refreshDocument(new_file);
    }

    private void refreshDocument(File file) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        context.getApplicationContext().sendBroadcast(intent);
    }

    private File getDisc() {
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        return new File(file, "Download");
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}