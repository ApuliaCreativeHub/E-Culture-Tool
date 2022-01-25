package com.apuliacreativehub.eculturetool.ui.component;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.apuliacreativehub.eculturetool.R;

import java.util.ArrayList;

public class ListArtifactsAdapter extends RecyclerView.Adapter<ListArtifactsAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<String> dataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final Button btnDescription;

        public ViewHolder(View view) {
            super(view);
            btnDescription = view.findViewById(R.id.btnDescription);
        }

        public Button getBtnDescription() {
            return btnDescription;
        }

    }

    public ListArtifactsAdapter(Context context, ArrayList<String> dataSet) {
        this.context = context;
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
        // TODO: Show object description in the second message of dialog
        viewHolder.getBtnDescription().setOnClickListener(view -> new Dialog(context.getString(R.string.description), "Object description", "OBJECT_DESCRIPTION").show(((AppCompatActivity)context).getSupportFragmentManager(), Dialog.TAG));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}