package com.apuliacreativehub.eculturetool.ui.places.adapter;

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

public class PlacePathsAdapter extends RecyclerView.Adapter<PlacePathsAdapter.ViewHolder> {

    private final ArrayList<Path> dataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textPathName;
        private final TextView textPlaceNameAndAddress;
        private final ImageView imagePath;

        public ViewHolder(View view) {
            super(view);
            //TODO: Define click listener for the ViewHolder's View
            textPathName = view.findViewById(R.id.listComponentPathName);
            textPlaceNameAndAddress = view.findViewById(R.id.listComponentPathPlace);
            imagePath = view.findViewById(R.id.listComponentPathImage);
        }

        public TextView getTextPathName() {
            return textPathName;
        }

        public TextView getTextPlaceNameAndAddress() {
            return textPlaceNameAndAddress;
        }

        public ImageView getImagePath() {
            return imagePath;
        }
    }

    public PlacePathsAdapter(ArrayList<Path> dataSet) {
        this.dataSet = dataSet;
    }

    @Override @NonNull
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.component_card_curator_path, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getTextPathName().setText(this.dataSet.get(position).getPathName());
        viewHolder.getTextPlaceNameAndAddress().setText(this.dataSet.get(position).getPlaceName() + " - " + this.dataSet.get(position).getPlaceAddress());
        //TODO: add all value to personalize single component
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}