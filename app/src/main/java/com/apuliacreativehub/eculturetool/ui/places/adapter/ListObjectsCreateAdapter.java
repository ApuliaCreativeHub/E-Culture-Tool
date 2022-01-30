package com.apuliacreativehub.eculturetool.ui.places.adapter;

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
import com.apuliacreativehub.eculturetool.ui.component.Dialog;
import com.apuliacreativehub.eculturetool.ui.places.NodeArtifact;
import com.google.common.collect.Iterables;
import com.google.common.graph.MutableGraph;
import com.google.common.graph.Traverser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class ListObjectsCreateAdapter extends RecyclerView.Adapter<ListObjectsCreateAdapter.ViewHolder> {

    private final int layout;
    private final Context context;
    private final ArrayList<NodeArtifact> dataSet;
    private final MutableGraph<NodeArtifact> circleDataset;
    private final ListCircleObjectsAdapter listCircleObjectsAdapter;

    private NodeArtifact utilNodeTemp;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final View view;
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

    public ListObjectsCreateAdapter(int layout, ArrayList<NodeArtifact> dataSet, MutableGraph<NodeArtifact> graphArtifactDataset, ListCircleObjectsAdapter listCircleObjectsAdapter, Context mContext) {
        this.layout = layout;
        this.dataSet = dataSet;
        this.context = mContext;
        this.circleDataset = graphArtifactDataset;
        this.listCircleObjectsAdapter = listCircleObjectsAdapter;
    }

    @Override @NonNull
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getView().findViewById(R.id.cardArtifactPath).setOnClickListener(v -> {
            if(viewHolder.switchCheck()) {
                viewHolder.getCheckBox().setVisibility(View.VISIBLE);
                increaseGraph(dataSet.get(position));
            } else {
                viewHolder.getCheckBox().setVisibility(View.INVISIBLE);
                decreaseGraph(dataSet.get(position));
            }
            listCircleObjectsAdapter.notifyDataSetChanged();
        });
        //TODO: add all value to personalize single component
        // TODO: Show object description in the second message of dialog
        viewHolder.getBtnDescription().setOnClickListener(v -> new Dialog(context.getString(R.string.description), dataSet.get(position).getDescription(), "OBJECT_DESCRIPTION").show(((AppCompatActivity)context).getSupportFragmentManager(), Dialog.TAG));
    }

    /**
     * If utilNodeTemp is null means that the graph is empty. -> We have to add a node. (to trace operation we save the last value added to node in a variable)
     * if utilNodeTemp is not null means that there is almost one node -> We have to link the new node to the last node.
     * @param newNode
     */
    private void increaseGraph(NodeArtifact newNode) {
        if(utilNodeTemp != null) {
            newNode.setWeight(utilNodeTemp.getWeight()*2);
            circleDataset.putEdge(utilNodeTemp, newNode);
        }
        else {
            newNode.setWeight(1.0);
            circleDataset.addNode(newNode);
        }
        utilNodeTemp = newNode;
    }

    /**
     * We get the adjcent nodes of the node we want to remove. They can be 2 or less (if the node is on the board). So we link the adjacent nodes of the removed node (if are two)
     * Then we get the last node of the graph (to trace if we want add another node).
     * If the node to be removed is the last -> utilNodeTempo become null (reset all the process)
     * @param nodeToRemove
     */
    private void decreaseGraph(NodeArtifact nodeToRemove) {
        Set<NodeArtifact> adjacentNode = circleDataset.adjacentNodes(nodeToRemove);
        Iterator<NodeArtifact> iteratorNode = adjacentNode.iterator();
        NodeArtifact leftNode, rightNode;
        circleDataset.removeNode(nodeToRemove);

        if(adjacentNode.size() == 2) {
            leftNode = iteratorNode.next();
            rightNode = iteratorNode.next();
            circleDataset.putEdge(leftNode, rightNode);
            if(nodeToRemove.equals(utilNodeTemp)) {
                utilNodeTemp = rightNode;
            }
        }
        if(adjacentNode.size() > 0 && nodeToRemove.equals(utilNodeTemp)) {
            utilNodeTemp = iteratorNode.next();
        } else if(adjacentNode.size() > 0){
            utilNodeTemp = Iterables.getLast(Traverser.forTree(circleDataset).breadthFirst(utilNodeTemp));
        } else {
            utilNodeTemp = null;
        }
    }
    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}