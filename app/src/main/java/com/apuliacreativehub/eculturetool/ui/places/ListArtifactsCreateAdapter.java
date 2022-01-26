package com.apuliacreativehub.eculturetool.ui.places;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.ui.component.Dialog;

import java.util.ArrayList;

public class ListArtifactsCreateAdapter extends RecyclerView.Adapter<ListArtifactsCreateAdapter.ViewHolder> {

    private final int layout;
    private final Context context;
    private final ArrayList<String> dataSet;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private final Button btnDescription;

        public ViewHolder(View view, Context context) {
            super(view);
            this.view = view;

            btnDescription = view.findViewById(R.id.btnDescription);
        }

        public Button getBtnDescription() {
            return btnDescription;
        }

        public View getView() {
            return view;
        }

    }

    public ListArtifactsCreateAdapter(int layout, ArrayList<String> dataSet, Context mContext) {
        this.layout = layout;
        this.dataSet = dataSet;
        this.context = mContext;
    }

    @Override @NonNull
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        //View view = viewHolder.getView();
        //view.findViewById(R.id.cardArtifactPath).setOnClickListener(v -> {

        //});
        //TODO: add all value to personalize single component
        // TODO: Show object description in the second message of dialog
        viewHolder.getBtnDescription().setOnClickListener(v -> new Dialog(context.getString(R.string.description), "Object description", "OBJECT_DESCRIPTION").show(((AppCompatActivity)context).getSupportFragmentManager(), Dialog.TAG));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}