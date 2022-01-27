package com.apuliacreativehub.eculturetool.ui.places;

import android.content.ClipData;
import android.content.ClipDescription;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.data.entity.Artifact;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class ListCircleArtifactsAdapter extends RecyclerView.Adapter<ListCircleArtifactsAdapter.ViewHolder> {

    private ArrayList<Artifact> dataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private ShapeableImageView circle;
        private LinearLayout dropContainer;
        private ImageView arrowIcon;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.circle = view.findViewById(R.id.pathCircleImage);
            this.dropContainer = view.findViewById(R.id.pathContainerDropImage);
            this.arrowIcon = view.findViewById(R.id.arrowLinkIcon);
        }

        public ShapeableImageView getCircle() {
            return circle;
        }

        public LinearLayout getDropContainer() {
            return dropContainer;
        }

        public ImageView getArrowIcon() {
            return arrowIcon;
        }
    }

    public ListCircleArtifactsAdapter(ArrayList<Artifact> dataSet) {
        this.dataSet = dataSet;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.component_circle_artifact, viewGroup, false);
        return new ViewHolder(view);
    }

    private void setOnDragListener(ShapeableImageView view) {
        view.setOnLongClickListener(v ->  {
            ClipData.Item item = new ClipData.Item(view.getTag().toString());
            ClipData dragData = new ClipData(view.getTag().toString(), new String[] { ClipDescription.MIMETYPE_TEXT_PLAIN }, item);
            View.DragShadowBuilder myShadow = new View.DragShadowBuilder(view);
            v.startDragAndDrop(dragData, myShadow, null,0);
            return true;
        });
    }

    private void setOnDropListener(LinearLayout view) {
        view.setOnDragListener((V, e) -> {
            if(e.getAction() == DragEvent.ACTION_DROP) {
                int idResult = Integer.valueOf(e.getClipData().getItemAt(0).getText().toString());
                moveValue(idResult, Integer.valueOf(V.getTag().toString()));
            }
            return true;
        });
    }

    private void moveValue(int posStart, int posEnd) {
        Artifact artifactStart = dataSet.get(posStart); //TODO: ADD ALL DROP CONTAINER TO RENDER THE DRAG&DROP INTUITIVE
        dataSet.remove(posStart);
        dataSet.add(posEnd, artifactStart);
        notifyDataSetChanged();
    }

    /**
     * During the binding of each view we set listeners to drag and drop on each child
     * The child dragged is identified by his tag, that contains the unique id of the position of the adapter.
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getCircle().setImageResource(R.mipmap.outline_qr_code_scanner_black_18);
        ShapeableImageView shapeableImageView = holder.getCircle();
        LinearLayout linearLayout = holder.getDropContainer();
        shapeableImageView.setTag(position);
        linearLayout.setTag(position);
        setOnDragListener(shapeableImageView);
        setOnDropListener(linearLayout);
        if(position+1 == dataSet.size()) holder.getArrowIcon().setVisibility(View.GONE);
        else holder.getArrowIcon().setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return this.dataSet.size();
    }
}
