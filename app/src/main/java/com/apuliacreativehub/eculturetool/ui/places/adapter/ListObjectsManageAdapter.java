package com.apuliacreativehub.eculturetool.ui.places.adapter;

import android.content.Context;
import android.net.ConnectivityManager;
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
import com.apuliacreativehub.eculturetool.ui.component.Utils;
import com.apuliacreativehub.eculturetool.ui.places.fragment.EditObjectFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class ListObjectsManageAdapter extends RecyclerView.Adapter<ListObjectsManageAdapter.ViewHolder> {

    private final Bundle bundleZoneNameId;
    private final String inizialSelectedZone;
    private final int layout;
    private final Context context;
    private final ArrayList<Object> dataSet;
    private final ArrayAdapter<String> listZones;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final View view;
        private final Button btnDescription;
        private final Button btnEditObject;
        private ImageView imgObject;
        private TextView txtName;

        public ViewHolder(View view) {
            super(view);
            this.view = view;

            btnDescription = view.findViewById(R.id.btnDescription);
            btnEditObject = view.findViewById(R.id.btnEditObject);
            imgObject = view.findViewById(R.id.imgObject);
            txtName = view.findViewById(R.id.txtName);
        }

        public Button getBtnDescription() {
            return btnDescription;
        }
        
        public View getView() {
            return view;
        }

        public Button getBtnEditObject() {
            return btnEditObject;
        }

        public ImageView getImgObject() {
            return imgObject;
        }

        public TextView getTxtName() {
            return txtName;
        }

        public void setImgObject(ImageView imgObject) {
            this.imgObject = imgObject;
        }

        public void setTxtName(TextView txtName) {
            this.txtName = txtName;
        }
    }

    public ListObjectsManageAdapter(int layout, ArrayList<Object> dataSet, Context mContext, ArrayAdapter<String> listZones, Bundle zoneNameId, String inizialSelectedZone) {
        this.layout = layout;
        this.dataSet = dataSet;
        this.context = mContext;
        this.listZones = listZones;
        this.bundleZoneNameId = zoneNameId;
        this.inizialSelectedZone = inizialSelectedZone;
    }

    @Override @NonNull
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.component_card_artifact, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getTxtName().setText(this.dataSet.get(position).getName());
        Log.i("ObjectCardPosizion", "https://hiddenfile.ml/ecultureapi/" + this.dataSet.get(position).getNormalSizeImg());
        Glide.with(context)
                .load("https://hiddenfile.ml/ecultureapi/" + this.dataSet.get(position)
                        //.load("http://10.0.2.2:8080/" + this.dataSet.get(position)
                        .getNormalSizeImg())
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(viewHolder.getImgObject());
        viewHolder.getBtnDescription().setOnClickListener(v -> new Dialog(context.getString(R.string.description), this.dataSet.get(position).getDescription(), "OBJECT_DESCRIPTION").show(((AppCompatActivity)context).getSupportFragmentManager(), Dialog.TAG));
        if (!Utils.checkConnection((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE))) {
            viewHolder.getBtnEditObject().setText(R.string.edit_not_allowed);
        }
        viewHolder.getBtnEditObject().setOnClickListener(v ->{
            if (Utils.checkConnection((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE))) {
                TransactionHelper.transactionWithAddToBackStack((FragmentActivity) context, R.id.fragment_container_layout, new EditObjectFragment(this.dataSet.get(position), bundleZoneNameId, listZones, inizialSelectedZone));
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}