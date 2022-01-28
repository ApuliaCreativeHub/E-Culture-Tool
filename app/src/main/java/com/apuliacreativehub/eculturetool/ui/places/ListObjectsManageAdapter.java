package com.apuliacreativehub.eculturetool.ui.places;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class ListObjectsManageAdapter extends RecyclerView.Adapter<ListObjectsManageAdapter.ViewHolder> {

    private int layout;
    private final Context context;
    private final ArrayList<Object> dataSet;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private final Button btnDescription;
        private final Button btnEditObject;
        private ImageView imgObject;
        private TextView txtName;

        public ViewHolder(View view, Context context) {
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

    public ListObjectsManageAdapter(int layout, ArrayList<Object> dataSet, Context mContext) {
        this.layout = layout;
        this.dataSet = dataSet;
        this.context = mContext;
    }

    @Override @NonNull
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.component_card_artifact, viewGroup, false);
        return new ViewHolder(view, context);
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
        viewHolder.getBtnEditObject().setOnClickListener(v -> TransactionHelper.transactionWithAddToBackStack((FragmentActivity) context, R.id.fragment_container_layout, new EditObjectFragment()));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}