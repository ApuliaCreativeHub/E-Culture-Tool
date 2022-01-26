package com.apuliacreativehub.eculturetool.ui.places;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.ui.component.Dialog;
import com.apuliacreativehub.eculturetool.ui.component.TransactionHelper;

import java.util.ArrayList;

public class ListArtifactsManageAdapter extends RecyclerView.Adapter<ListArtifactsManageAdapter.ViewHolder> {

    private int layout;
    private final Context context;
    private final ArrayList<String> dataSet;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private final Button btnDescription;
        private final Button btnEditObject;

        public ViewHolder(View view, Context context) {
            super(view);
            this.view = view;
            btnDescription = view.findViewById(R.id.btnDescription);
            btnEditObject = view.findViewById(R.id.btnEditObject);
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

    }

    public ListArtifactsManageAdapter(int layout, ArrayList<String> dataSet, Context mContext) {
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
        //TODO: add all value to personalize single component
        // TODO: Show object description in the second message of dialog
        viewHolder.getBtnDescription().setOnClickListener(v -> new Dialog(context.getString(R.string.description), "Object description", "OBJECT_DESCRIPTION").show(((AppCompatActivity)context).getSupportFragmentManager(), Dialog.TAG));
        viewHolder.getBtnEditObject().setOnClickListener(v -> TransactionHelper.transactionWithAddToBackStack((FragmentActivity) context, R.id.fragment_container_layout, new EditObjectFragment()));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}