package com.apuliacreativehub.eculturetool.ui.places.adapter;

import android.content.Context;
import android.provider.ContactsContract;
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
import com.apuliacreativehub.eculturetool.ui.places.NodeObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.common.collect.Iterables;
import com.google.common.graph.MutableGraph;
import com.google.common.graph.Traverser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class ListObjectsCreateAdapter extends RecyclerView.Adapter<ListObjectsCreateAdapter.ViewHolder> {

    private final int layout;
    private final Context context;
    private final ArrayList<NodeObject> dataSet;
    private final MutableGraph<NodeObject> circleDataset;
    private ListCircleObjectsAdapter listCircleObjectsAdapter;

    private NodeObject utilNodeTemp;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final View view;
        private boolean isChecked;
        private final TextView txtTitle;
        private final Button btnDescription;
        private final ImageView checkView;
        private final ImageView imgObject;

        public ViewHolder(View view, Context context) {
            super(view);
            this.view = view;
            isChecked = false;
            imgObject = view.findViewById(R.id.imgObject);
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

        public ImageView getImgObject(){return imgObject;}

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

    public ListObjectsCreateAdapter(int layout, ArrayList<NodeObject> dataSet, MutableGraph<NodeObject> graphArtifactDataset, Context mContext) {
        this.layout = layout;
        this.dataSet = dataSet;
        this.context = mContext;
        this.circleDataset = graphArtifactDataset;
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
        viewHolder.getTitle().setText(dataSet.get(position).getName());
        Glide.with(context)
                .load("https://hiddenfile.ml/ecultureapi/" + this.dataSet.get(position)
                        //.load("http://10.0.2.2:8080/" + this.dataSet.get(position)
                        .getNormalSizeImg())
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(viewHolder.getImgObject());
        viewHolder.getBtnDescription().setOnClickListener(v -> new Dialog(context.getString(R.string.description), dataSet.get(position).getDescription(), "OBJECT_DESCRIPTION").show(((AppCompatActivity)context).getSupportFragmentManager(), Dialog.TAG));
    }

    /**
     * If utilNodeTemp is null means that the graph is empty. -> We have to add a node. (to trace operation we save the last value added to node in a variable)
     * if utilNodeTemp is not null means that there is almost one node -> We have to link the new node to the last node.
     * @param newNode
     */
    private void increaseGraph(NodeObject newNode) {
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
    private void decreaseGraph(NodeObject nodeToRemove) {
        Set<NodeObject> adjacentNode = circleDataset.adjacentNodes(nodeToRemove);
        Iterator<NodeObject> iteratorNode = adjacentNode.iterator();
        NodeObject leftNode, rightNode;
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

    public NodeObject getLastNode(){
        return utilNodeTemp;
    }

    public ListCircleObjectsAdapter getListCircleObjectsAdapter(){
        return this.listCircleObjectsAdapter;
    }

    public void setListCircleObjectsAdapter(ListCircleObjectsAdapter listCircleObjectsAdapter, NodeObject lastNode){
        this.listCircleObjectsAdapter = listCircleObjectsAdapter;
        this.utilNodeTemp = lastNode;
    }
}