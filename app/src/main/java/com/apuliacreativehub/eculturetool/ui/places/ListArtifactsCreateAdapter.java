package com.apuliacreativehub.eculturetool.ui.places;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.data.entity.Artifact;
import com.apuliacreativehub.eculturetool.ui.component.Dialog;

import java.util.ArrayList;
import java.util.List;

public class ListArtifactsCreateAdapter extends RecyclerView.Adapter<ListArtifactsCreateAdapter.ViewHolder> {

    private final int layout;
    private final Context context;
    private ArrayList<Artifact> dataSet;
    private ArrayList<Artifact> circleDataset;
    private ListCircleArtifactsAdapter listCircleArtifactsAdapter;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private boolean isChecked;
        private final TextView txtTitle;
        private final Button btnDescription;
        private final ImageView checkView;

        public ViewHolder(View view, Context context) {
            super(view);
            this.view = view;
            isChecked = false;
            txtTitle = view.findViewById(R.id.txtTitleArtifact);
            checkView = view.findViewById(R.id.checkBoxOnPath);
            btnDescription = view.findViewById(R.id.btnDescription);
        }

        public Button getBtnDescription() {
            return btnDescription;
        }

        public View getView() {
            return view;
        }

        public ImageView getCheckBox() {
            return checkView;
        }

        public TextView getTitle() {
            return txtTitle;
        }

        public void setTitle(String title) {
            this.txtTitle.setText(title);
        }

        private boolean switchCheck() {
            isChecked = !isChecked;
            return isChecked;
        }

    }

    public ListArtifactsCreateAdapter(int layout, ArrayList<Artifact> dataSet, ArrayList<Artifact> circleDataset, ListCircleArtifactsAdapter listCircleArtifactsAdapter, Context mContext) {
        this.layout = layout;
        this.dataSet = dataSet;
        this.context = mContext;
        this.circleDataset = circleDataset;
        this.listCircleArtifactsAdapter = listCircleArtifactsAdapter;
    }

    @Override @NonNull
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.setTitle(dataSet.get(position).getName());
        View view = viewHolder.getView();
        view.setTag(dataSet.get(position).getId());

        view.findViewById(R.id.cardArtifactPath).setOnClickListener(v -> {
            if(viewHolder.switchCheck()) {
                viewHolder.getCheckBox().setVisibility(View.VISIBLE);
                circleDataset.add(dataSet.get(position));
            } else {
                viewHolder.getCheckBox().setVisibility(View.INVISIBLE);
                boolean canContinue = false;
                for(int i = 0; i < circleDataset.size() && !canContinue; i++) {
                    if(dataSet.get(position).getId() == circleDataset.get(i).getId()) {
                        canContinue = true;
                        circleDataset.remove(i);
                    }
                }
            }
            listCircleArtifactsAdapter.notifyDataSetChanged();
        });
        //TODO: add all value to personalize single component
        // TODO: Show object description in the second message of dialog
        viewHolder.getBtnDescription().setOnClickListener(v -> new Dialog(context.getString(R.string.description), dataSet.get(position).getDescription(), "OBJECT_DESCRIPTION").show(((AppCompatActivity)context).getSupportFragmentManager(), Dialog.TAG));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}