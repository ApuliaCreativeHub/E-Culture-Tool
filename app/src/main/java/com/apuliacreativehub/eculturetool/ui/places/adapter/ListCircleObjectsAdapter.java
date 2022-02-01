package com.apuliacreativehub.eculturetool.ui.places.adapter;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.ui.component.GuavaHelper;
import com.apuliacreativehub.eculturetool.ui.places.NodeObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.common.graph.MutableGraph;

public class ListCircleObjectsAdapter extends RecyclerView.Adapter<ListCircleObjectsAdapter.ViewHolder> {

    private final MutableGraph<NodeObject> dataSet;
    private final Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final View view;
        private final ShapeableImageView circle;
        private final LinearLayout dropLeftContainer;
        private final LinearLayout dropRightContainer;
        private final ImageView arrowIcon;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.circle = view.findViewById(R.id.pathCircleImage);
            this.dropLeftContainer = view.findViewById(R.id.pathContainerLeftDropImage);
            this.dropRightContainer = view.findViewById(R.id.pathContainerRightDropImage);
            this.arrowIcon = view.findViewById(R.id.arrowLinkIcon);
        }

        public View getView() {
            return view;
        }

        public ShapeableImageView getCircle() {
            return circle;
        }

        public LinearLayout getLeftDropContainer() {
            return dropLeftContainer;
        }

        public LinearLayout getRightDropContainer() {
            return dropRightContainer;
        }

        public ImageView getArrowIcon() {
            return arrowIcon;
        }
    }

    public ListCircleObjectsAdapter(MutableGraph<NodeObject> dataSet, Context context) {
        this.dataSet = dataSet;
        this.context = context;
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
            View parent = (View) v.getParent();

            ClipData.Item itemId = new ClipData.Item(parent.getTag(R.id.artifact_tag_id).toString());
            ClipData dragData = new ClipData("node_artifact", new String[] { ClipDescription.MIMETYPE_TEXT_PLAIN }, itemId);

            View.DragShadowBuilder myShadow = new View.DragShadowBuilder(view);
            v.startDragAndDrop(dragData, myShadow, null,0);
            return true;
        });
    }

    /**
     * @param view
     */
    private void setOnDropListener(LinearLayout view) {
        view.setOnDragListener((V, e) -> {
            if(e.getAction() == DragEvent.ACTION_DROP) {
                String moveOrientationAction = V.getTag().toString();
                View parent = (View) V.getParent();
                NodeObject rebornDragArtifact = GuavaHelper.getNodeById(dataSet, Integer.valueOf(e.getClipData().getItemAt(0).getText().toString()));
                NodeObject rebornDropArtifact = GuavaHelper.getNodeById(dataSet, (Integer) parent.getTag(R.id.artifact_tag_id));
                if(rebornDragArtifact.equals(rebornDropArtifact)) return  true;
                setupGraphAfterInteraction(rebornDragArtifact, rebornDropArtifact, moveOrientationAction);
                notifyDataSetChanged();
            }
            return true;
        });
    }

    /**
     * During the binding of each view we set listeners to drag and drop on each child
     * The child dragged is identified by his tag, that contains the unique id of the position of the adapter.
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NodeObject[] node = dataSet.nodes().toArray(new NodeObject[0]);

        View view = holder.getView();
        view.setTag(R.id.artifact_tag_id, node[position].getId());
        Glide.with(context)
                .load("https://hiddenfile.ml/ecultureapi/" + node[position]
                        //.load("http://10.0.2.2:8080/" + this.dataSet.get(position)
                        .getNormalSizeImg())
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(holder.getCircle());
        setOnDragListener(holder.getCircle());
        setOnDropListener(holder.getRightDropContainer());
        setOnDropListener(holder.getLeftDropContainer());
        if(position+1 == getItemCount()) holder.getArrowIcon().setVisibility(View.GONE);
        else holder.getArrowIcon().setVisibility(View.VISIBLE);
    }

    /**
     * DON'T TOUCH THIS FUNCTION (IS IMPORTANT TO PERSIST THE ORDER TOO)
     */
    private void setupGraphAfterInteraction(NodeObject drag, NodeObject drop, String moveOrientation) {
        NodeObject nodeObjectDraggedLeft = GuavaHelper.getLeftNode(dataSet, drag);
        NodeObject nodeObjectDraggedRight = GuavaHelper.getRightNode(dataSet, drag);

        if(nodeObjectDraggedLeft == null && nodeObjectDraggedRight == null) return;
        dataSet.removeNode(drag);
        if(nodeObjectDraggedLeft != null && nodeObjectDraggedRight != null) dataSet.putEdge(nodeObjectDraggedLeft, nodeObjectDraggedRight);

        NodeObject nodeObjectDroppedLeft = GuavaHelper.getLeftNode(dataSet, drop);
        NodeObject nodeObjectDroppedRight = GuavaHelper.getRightNode(dataSet, drop);

        if(moveOrientation.equals("LEFT")) {
            if(nodeObjectDroppedLeft != null) {
                drag.setWeight((nodeObjectDroppedLeft.getWeight() + drop.getWeight())/2);
                dataSet.removeEdge(nodeObjectDroppedLeft, drop);
                dataSet.putEdge(nodeObjectDroppedLeft, drag);
                dataSet.putEdge(drag, drop);
            } else {
                drag.setWeight(drop.getWeight()/2);
                dataSet.putEdge(drag, drop);
            }
        } else {
            if(nodeObjectDroppedRight != null) {
                drag.setWeight((drop.getWeight()+ nodeObjectDroppedRight.getWeight())/2);
                dataSet.removeEdge(drop, nodeObjectDroppedRight);
                dataSet.putEdge(drop, drag);
                dataSet.putEdge(drag, nodeObjectDroppedRight);
            } else {
                drag.setWeight(drop.getWeight()*2);
                dataSet.putEdge(drop, drag);
            }
        }
    }

    @Override
    public int getItemCount() {
        if(dataSet != null)
            return this.dataSet.nodes().size();
        else
            return 0;
    }

}
