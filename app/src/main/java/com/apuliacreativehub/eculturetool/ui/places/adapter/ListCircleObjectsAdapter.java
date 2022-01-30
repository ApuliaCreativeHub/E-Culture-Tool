package com.apuliacreativehub.eculturetool.ui.places.adapter;

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
import com.apuliacreativehub.eculturetool.ui.component.GuavaHelper;
import com.apuliacreativehub.eculturetool.ui.places.NodeArtifact;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.common.graph.MutableGraph;

public class ListCircleObjectsAdapter extends RecyclerView.Adapter<ListCircleObjectsAdapter.ViewHolder> {

    private final MutableGraph<NodeArtifact> dataSet;

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

    public ListCircleObjectsAdapter(MutableGraph<NodeArtifact> dataSet) {
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
                NodeArtifact rebornDragArtifact = GuavaHelper.getNodeById(dataSet, Integer.valueOf(e.getClipData().getItemAt(0).getText().toString()));
                NodeArtifact rebornDropArtifact = GuavaHelper.getNodeById(dataSet, (Integer) parent.getTag(R.id.artifact_tag_id));
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
        NodeArtifact[] node = dataSet.nodes().toArray(new NodeArtifact[0]);

        View view = holder.getView();
        view.setTag(R.id.artifact_tag_id, node[position].getId());
        //ONLY FOR SEE THE CIRCLE DIFFERENCE
        if(node[position].getId() == 100)  holder.getCircle().setImageResource(R.mipmap.outline_qr_code_scanner_black_18);
        if(node[position].getId() == 101)  holder.getCircle().setImageResource(R.mipmap.outline_add_photo_alternate_black_20);
        if(node[position].getId() == 102)  holder.getCircle().setImageResource(R.mipmap.outline_search_black_18);
        if(node[position].getId() == 103)  holder.getCircle().setImageResource(R.mipmap.outline_travel_explore_black_18);
        setOnDragListener(holder.getCircle());
        setOnDropListener(holder.getRightDropContainer());
        setOnDropListener(holder.getLeftDropContainer());
        if(position+1 == getItemCount()) holder.getArrowIcon().setVisibility(View.GONE);
        else holder.getArrowIcon().setVisibility(View.VISIBLE);
    }

    /**
     * DON'T TOUCH THIS FUNCTION (IS IMPORTANT TO PERSIST THE ORDER TOO)
     */
    private void setupGraphAfterInteraction(NodeArtifact drag, NodeArtifact drop, String moveOrientation) {
        NodeArtifact nodeArtifactDraggedLeft = GuavaHelper.getLeftNode(dataSet, drag);
        NodeArtifact nodeArtifactDraggedRight = GuavaHelper.getRightNode(dataSet, drag);

        if(nodeArtifactDraggedLeft == null && nodeArtifactDraggedRight == null) return;
        dataSet.removeNode(drag);
        if(nodeArtifactDraggedLeft != null && nodeArtifactDraggedRight != null) dataSet.putEdge(nodeArtifactDraggedLeft, nodeArtifactDraggedRight);

        NodeArtifact nodeArtifactDroppedLeft = GuavaHelper.getLeftNode(dataSet, drop);
        NodeArtifact nodeArtifactDroppedRight = GuavaHelper.getRightNode(dataSet, drop);

        if(moveOrientation.equals("LEFT")) {
            if(nodeArtifactDroppedLeft != null) {
                drag.setWeight((nodeArtifactDroppedLeft.getWeight() + drop.getWeight())/2);
                dataSet.removeEdge(nodeArtifactDroppedLeft, drop);
                dataSet.putEdge(nodeArtifactDroppedLeft, drag);
                dataSet.putEdge(drag, drop);
            } else {
                drag.setWeight(drop.getWeight()/2);
                dataSet.putEdge(drag, drop);
            }
        } else {
            if(nodeArtifactDroppedRight != null) {
                drag.setWeight((drop.getWeight()+nodeArtifactDroppedRight.getWeight())/2);
                dataSet.removeEdge(drop, nodeArtifactDroppedRight);
                dataSet.putEdge(drop, drag);
                dataSet.putEdge(drag, nodeArtifactDroppedRight);
            } else {
                drag.setWeight(drop.getWeight()*2);
                dataSet.putEdge(drop, drag);
            }
        }
    }

    @Override
    public int getItemCount() {
        return this.dataSet.nodes().size();
    }

}
