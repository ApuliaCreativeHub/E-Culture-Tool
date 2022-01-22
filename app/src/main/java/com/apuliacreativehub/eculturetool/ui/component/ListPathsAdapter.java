package com.apuliacreativehub.eculturetool.ui.component;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.data.entity.Path;

import java.util.ArrayList;


public class ListPathsAdapter extends RecyclerView.Adapter<ListPathsAdapter.ViewHolder> {

    private final ArrayList<Path> dataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView textPathName;
        private final TextView textPlaceName;
        private final ImageView imagePath;

        public ViewHolder(View view) {
            super(view);
            //TODO: Define click listener for the ViewHolder's View
            textPathName = view.findViewById(R.id.listComponentPathName);
            textPlaceName = view.findViewById(R.id.listComponentPathPlace);
            imagePath = view.findViewById(R.id.listComponentPathImage);
        }

        public TextView getTextPathName() {
            return textPathName;
        }

        public TextView getTextPlaceName() {
            return textPlaceName;
        }

        public ImageView getImagePath() {
            return imagePath;
        }
    }

    public ListPathsAdapter(ArrayList<Path> dataSet) {
        this.dataSet = dataSet;
    }

    @Override @NonNull
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.component_card_path, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getTextPathName().setText(this.dataSet.get(position).getName());
        viewHolder.getTextPlaceName().setText(this.dataSet.get(position).getPlaceName());
        //TODO: add all value to personalize single component
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}