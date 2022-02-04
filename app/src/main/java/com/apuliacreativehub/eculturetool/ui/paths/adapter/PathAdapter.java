package com.apuliacreativehub.eculturetool.ui.paths.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.data.entity.Object;
import com.apuliacreativehub.eculturetool.ui.component.Dialog;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;

import java.util.List;

public class PathAdapter extends RecyclerView.Adapter<PathAdapter.ViewHolder> {
    private final Context context;
    private final List<Object> dataSet;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgObject;
        private final TextView txtName;
        private final Button btnDescription;
        private final BadgeDrawable badgeCounter;

        @OptIn(markerClass = com.google.android.material.badge.ExperimentalBadgeUtils.class)
        public ViewHolder(View view, Context context) {
            super(view);
            imgObject = view.findViewById(R.id.imgObject);
            txtName = view.findViewById(R.id.txtName);
            btnDescription = view.findViewById(R.id.btnDescription);
            badgeCounter = BadgeDrawable.create(context);
            badgeCounter.setVerticalOffset(20);
            badgeCounter.setHorizontalOffset(-20);
            badgeCounter.setBackgroundColor(context.getColor(R.color.md_theme_dark_secondary));
            badgeCounter.setVisible(true);
            BadgeUtils.attachBadgeDrawable(badgeCounter, view.findViewById(R.id.cardArtifactPath), view.findViewById(R.id.frameLayoutBadge));
        }

        public ImageView getImgObject() {
            return imgObject;
        }

        public TextView getTxtName() {
            return txtName;
        }

        public Button getBtnDescription() {
            return btnDescription;
        }

        public void setBadgeCounter(int number) {
            badgeCounter.setNumber(number);
        }
    }

    public PathAdapter(Context context, List<Object> dataSet) {
        this.context = context;
        this.dataSet = dataSet;
    }

    @Override @NonNull
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.component_card_show_path, viewGroup, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getTxtName().setText(this.dataSet.get(position).getName());
        viewHolder.setBadgeCounter(position+1);
        viewHolder.getBtnDescription().setOnClickListener(v -> new Dialog(context.getString(R.string.description), this.dataSet.get(position).getDescription(), "OBJECT_DESCRIPTION").show(((AppCompatActivity)context).getSupportFragmentManager(), Dialog.TAG));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}