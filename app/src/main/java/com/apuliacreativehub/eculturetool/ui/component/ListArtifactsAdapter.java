package com.apuliacreativehub.eculturetool.ui.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.opengl.Visibility;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.cardview.widget.CardView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.apuliacreativehub.eculturetool.R;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;

import java.util.ArrayList;

public class ListArtifactsAdapter extends RecyclerView.Adapter<ListArtifactsAdapter.ViewHolder> {

    private int layout;
    private final Context context;
    private final ArrayList<String> dataSet;
    private int progressiveCounter;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private BadgeDrawable badgeDrawable;
        private final Button btnDescription;

        @OptIn(markerClass = com.google.android.material.badge.ExperimentalBadgeUtils.class)
        public ViewHolder(View view, Context context) {
            super(view);
            this.view = view;
            badgeDrawable = BadgeDrawable.create(context);
            badgeDrawable.setVerticalOffset(20);
            badgeDrawable.setHorizontalOffset(-20);
            badgeDrawable.setNumber(30); //TODO: RESOLVE BADGE NOT UPDATING
            badgeDrawable.setVisible(true);
            BadgeUtils.attachBadgeDrawable(badgeDrawable, view.findViewById(R.id.cardArtifactPath), view.findViewById(R.id.frameLayoutBadge));
            btnDescription = view.findViewById(R.id.btnDescription);
        }

        public Button getBtnDescription() {
            return btnDescription;
        }
        
        public View getView() {
            return view;
        }

    }

    public ListArtifactsAdapter(int layout, ArrayList<String> dataSet, Context mContext) {
        this.layout = layout;
        this.dataSet = dataSet;
        this.context = mContext;
        this.progressiveCounter = 0;
    }

    @Override @NonNull
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(layout, viewGroup, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        View view = viewHolder.getView();

        view.findViewById(R.id.cardArtifactPath).setOnClickListener(v -> {

        });
        //TODO: add all value to personalize single component
        // TODO: Show object description in the second message of dialog
        viewHolder.getBtnDescription().setOnClickListener(v -> new Dialog(context.getString(R.string.description), "Object description", "OBJECT_DESCRIPTION").show(((AppCompatActivity)context).getSupportFragmentManager(), Dialog.TAG));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}