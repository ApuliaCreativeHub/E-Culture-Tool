package com.apuliacreativehub.eculturetool.ui.component;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apuliacreativehub.eculturetool.R;

import java.util.ArrayList;


public class ListArtifactsAdapter extends RecyclerView.Adapter<ListArtifactsAdapter.ViewHolder> {

    private final ArrayList<String> dataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(View view) {
            super(view);
            //TODO: Define click listener for the ViewHolder's View
        }

    }

    public ListArtifactsAdapter(ArrayList<String> dataSet) {
        this.dataSet = dataSet;
    }

    @Override @NonNull
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.component_card_artifact, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        //TODO: add all value to personalize single component
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}