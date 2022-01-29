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
import com.apuliacreativehub.eculturetool.data.entity.Object;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.graph.MutableGraph;

public class ListCircleObjectsAdapter extends RecyclerView.Adapter<ListCircleObjectsAdapter.ViewHolder> {

    private MutableGraph<NodeArtifact> dataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private ShapeableImageView circle;
        private LinearLayout dropLeftContainer;
        private LinearLayout dropRightContainer;
        private ImageView arrowIcon;

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
     * DON'T TOUCH THIS FUNCTION (SHOULD BE MODIFIED BY ME)
     * @param view
     */
    private void setOnDropListener(LinearLayout view) {
        view.setOnDragListener((V, e) -> {
            if(e.getAction() == DragEvent.ACTION_DROP) {
                String moveOrientationAction = V.getTag().toString();
                View parent = (View) V.getParent();

                int rebornDragArtifactId =  Integer.valueOf(e.getClipData().getItemAt(0).getText().toString());
                NodeArtifact rebornDragArtifact = Iterables.find(dataSet.nodes(), new Predicate<NodeArtifact>() {
                    @Override
                    public boolean apply(NodeArtifact input) {
                        return input.getId() == rebornDragArtifactId;
                    }
                });

                int rebornDropAtifactId = (Integer) parent.getTag(R.id.artifact_tag_id);
                NodeArtifact rebornDropArtifact = Iterables.find(dataSet.nodes(), new Predicate<NodeArtifact>() {
                    @Override
                    public boolean apply(NodeArtifact input) {
                        return input.getId() == rebornDropAtifactId;
                    }
                });

                NodeArtifact nodeArtifactDraggedLeft = null;
                NodeArtifact nodeArtifactDraggedRight = null;
                NodeArtifact[] rawLeftNodes = dataSet.predecessors(rebornDragArtifact).toArray(new NodeArtifact[0]);
                NodeArtifact[] rawRightNodes = dataSet.successors(rebornDragArtifact).toArray(new NodeArtifact[0]);
                if(rawLeftNodes.length == 0 && rawRightNodes.length == 0) return true;

                if(rawLeftNodes.length == 1) nodeArtifactDraggedLeft = rawLeftNodes[0];
                if(rawRightNodes.length == 1) nodeArtifactDraggedRight = rawRightNodes[0];

                dataSet.removeNode(rebornDragArtifact);
                if(nodeArtifactDraggedLeft != null && nodeArtifactDraggedRight != null) dataSet.putEdge(nodeArtifactDraggedLeft, nodeArtifactDraggedRight);

                NodeArtifact nodeArtifactDroppedLeft = null;
                NodeArtifact nodeArtifactDroppedRight = null;
                rawLeftNodes = dataSet.predecessors(rebornDropArtifact).toArray(new NodeArtifact[0]);
                rawRightNodes = dataSet.successors(rebornDropArtifact).toArray(new NodeArtifact[0]);

                if(rawLeftNodes.length == 1) nodeArtifactDroppedLeft = rawLeftNodes[0];
                if(rawRightNodes.length == 1) nodeArtifactDroppedRight = rawRightNodes[0];

                if(nodeArtifactDroppedLeft != null && nodeArtifactDroppedRight != null) {
                    if(moveOrientationAction == "LEFT") {
                        rebornDragArtifact.setWeight((nodeArtifactDroppedLeft.getWeight() + rebornDropArtifact.getWeight())/2);
                        dataSet.removeEdge(nodeArtifactDroppedLeft, rebornDropArtifact);
                        dataSet.putEdge(nodeArtifactDroppedLeft, rebornDragArtifact);
                        dataSet.putEdge(rebornDragArtifact, rebornDropArtifact);
                    } else {
                        rebornDragArtifact.setWeight((rebornDropArtifact.getWeight() + nodeArtifactDroppedRight.getWeight())/2);
                        dataSet.removeEdge(rebornDropArtifact, nodeArtifactDroppedRight);
                        dataSet.putEdge(rebornDropArtifact, rebornDragArtifact);
                        dataSet.putEdge(rebornDragArtifact, nodeArtifactDroppedRight);
                    }
                } else {
                    if(nodeArtifactDroppedLeft == null) {
                        if(moveOrientationAction == "LEFT") {
                            rebornDropArtifact.setWeight(rebornDropArtifact.getWeight()/2);
                            dataSet.putEdge(rebornDragArtifact, rebornDropArtifact);
                        } else {
                            rebornDragArtifact.setWeight((rebornDropArtifact.getWeight() + nodeArtifactDroppedRight.getWeight())/2);
                            dataSet.removeEdge(rebornDropArtifact, nodeArtifactDroppedRight);
                            dataSet.putEdge(rebornDropArtifact, rebornDragArtifact);
                            dataSet.putEdge(rebornDragArtifact, nodeArtifactDroppedRight);
                        }
                    } else {
                        if(moveOrientationAction == "LEFT") {
                            rebornDragArtifact.setWeight((nodeArtifactDroppedLeft.getWeight() + rebornDropArtifact.getWeight())/2);
                            dataSet.removeEdge(nodeArtifactDroppedLeft, rebornDropArtifact);
                            dataSet.putEdge(nodeArtifactDroppedLeft, rebornDragArtifact);
                            dataSet.putEdge(rebornDragArtifact, rebornDropArtifact);
                        } else {
                            rebornDragArtifact.setWeight(rebornDropArtifact.getWeight()*2);
                            dataSet.putEdge(rebornDropArtifact, rebornDragArtifact);
                        }
                    }
                }
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
        ShapeableImageView shapeableImageView = holder.getCircle();
        view.setTag(R.id.artifact_tag_id, node[position].getId());

        //ONLY FOR SEE THE CIRCLE DIFFERENCE
        if(node[position].getId() == 100)  holder.getCircle().setImageResource(R.mipmap.outline_qr_code_scanner_black_18);
        if(node[position].getId() == 101)  holder.getCircle().setImageResource(R.mipmap.outline_add_photo_alternate_black_20);
        if(node[position].getId() == 102)  holder.getCircle().setImageResource(R.mipmap.outline_search_black_18);
        LinearLayout linearRightLayout = holder.getRightDropContainer();
        LinearLayout linearLeftLayout = holder.getLeftDropContainer();
        setOnDragListener(shapeableImageView);
        setOnDropListener(linearRightLayout);
        setOnDropListener(linearLeftLayout);
        if(position+1 == getItemCount()) holder.getArrowIcon().setVisibility(View.GONE);
        else holder.getArrowIcon().setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return this.dataSet.nodes().size();
    }
}
