package com.apuliacreativehub.eculturetool.ui.places;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.data.entity.Place;
import com.apuliacreativehub.eculturetool.ui.component.TransactionHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class CardPlaceAdapter extends RecyclerView.Adapter<CardPlaceAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<Place> dataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final MaterialCardView cardPlace;
        private final ImageView imgPlace;
        private final TextView txtName;
        private final TextView txtAddress;
        private final TextView txtDescription;

        public ViewHolder(View view) {
            super(view);
            cardPlace = view.findViewById(R.id.cardPlace);
            imgPlace = view.findViewById(R.id.imgPlace);
            txtName = view.findViewById(R.id.txtName);
            txtAddress = view.findViewById(R.id.txtAddress);
            txtDescription = view.findViewById(R.id.txtDescription);
        }

        public MaterialCardView getCardPlace() {
            return cardPlace;
        }

        public ImageView getImgPlace() {
            return imgPlace;
        }

        public TextView getTxtName() {
            return txtName;
        }

        public TextView getTxtAddress() {
            return txtAddress;
        }

        public TextView getTxtDescription() {
            return txtDescription;
        }
    }

    public CardPlaceAdapter(Context context, ArrayList<Place> dataSet) {
        this.context = context;
        this.dataSet = dataSet;
    }

    @Override @NonNull
    public CardPlaceAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.component_card_place, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CardPlaceAdapter.ViewHolder viewHolder, final int position) {
        viewHolder.getTxtName().setText(this.dataSet.get(position).getName());
        viewHolder.getTxtAddress().setText(this.dataSet.get(position).getAddress());
        viewHolder.getTxtDescription().setText(this.dataSet.get(position).getDescription());
        viewHolder.getCardPlace().setOnClickListener(view -> TransactionHelper.transactionWithAddToBackStack((FragmentActivity) context, R.id.fragment_container_layout, new ManagePlaceFragment(this.dataSet.get(position))));

        Log.i("CardPosizion", "https://hiddenfile.ml/ecultureapi/" + this.dataSet.get(position).getNormalSizeImg());
        Glide.with(context)
                .load("https://hiddenfile.ml/ecultureapi/" + this.dataSet.get(position)
                //.load("http://10.0.2.2:8080/" + this.dataSet.get(position)
                .getNormalSizeImg())
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(viewHolder.getImgPlace());
        viewHolder.getCardPlace().setOnClickListener(view -> TransactionHelper.transactionWithAddToBackStack((FragmentActivity) context, R.id.fragment_container_layout, new ManagePlaceFragment(this.dataSet.get(position))));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}