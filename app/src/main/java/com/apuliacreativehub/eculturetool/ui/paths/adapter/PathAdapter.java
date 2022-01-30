package com.apuliacreativehub.eculturetool.ui.paths.adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.data.entity.Object;
import com.apuliacreativehub.eculturetool.ui.component.Dialog;
import com.apuliacreativehub.eculturetool.ui.component.TransactionHelper;
import com.apuliacreativehub.eculturetool.ui.places.fragment.EditObjectFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class PathAdapter extends RecyclerView.Adapter<PathAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<Object> dataSet;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgObject;
        private final TextView txtName;
        private final Button btnDescription;

        public ViewHolder(View view) {
            super(view);

            imgObject = view.findViewById(R.id.imgObject);
            txtName = view.findViewById(R.id.txtName);
            btnDescription = view.findViewById(R.id.btnDescription);
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
    }

    public PathAdapter(Context context, ArrayList<Object> dataSet) {
        this.context = context;
        this.dataSet = dataSet;
    }

    @Override @NonNull
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.component_card_show_path, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getTxtName().setText(this.dataSet.get(position).getName());
        viewHolder.getBtnDescription().setOnClickListener(v -> new Dialog(context.getString(R.string.description), this.dataSet.get(position).getDescription(), "OBJECT_DESCRIPTION").show(((AppCompatActivity)context).getSupportFragmentManager(), Dialog.TAG));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}