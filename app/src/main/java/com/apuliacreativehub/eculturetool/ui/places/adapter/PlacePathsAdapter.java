package com.apuliacreativehub.eculturetool.ui.places.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.data.entity.Path;
import com.apuliacreativehub.eculturetool.data.entity.Place;
import com.apuliacreativehub.eculturetool.ui.component.TransactionHelper;
import com.apuliacreativehub.eculturetool.ui.paths.fragment.EditPathFragment;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class PlacePathsAdapter extends RecyclerView.Adapter<PlacePathsAdapter.ViewHolder> {

    private final List<Path> dataSet;
    private final Place place;
    private final Context context;

    public PlacePathsAdapter(Context context, List<Path> dataSet, Place place) {
        this.context = context;
        this.dataSet = dataSet;
        this.place = place;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getTextPathName().setText(dataSet.get(position).getName());
        viewHolder.getTextPlaceNameAndAddress().setText(place.getName() + " - " + place.getAddress());
        viewHolder.getPathCard().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransactionHelper.transactionWithAddToBackStack((FragmentActivity) context, R.id.fragment_container_layout, new EditPathFragment(place, dataSet.get(viewHolder.getBindingAdapterPosition())));
            }
        });
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.component_card_curator_path, viewGroup, false);
        return new ViewHolder(view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textPathName;
        private final TextView textPlaceNameAndAddress;
        private final ImageView imagePath;
        private final MaterialCardView pathCard;

        public ViewHolder(View view) {
            super(view);
            textPathName = view.findViewById(R.id.listComponentPathName);
            textPlaceNameAndAddress = view.findViewById(R.id.listComponentPathPlace);
            imagePath = view.findViewById(R.id.listComponentPathImage);
            pathCard = view.findViewById(R.id.cardCuratorPath);
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

        public MaterialCardView getPathCard() {
            return pathCard;
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}