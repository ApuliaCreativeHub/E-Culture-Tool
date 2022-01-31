package com.apuliacreativehub.eculturetool.ui.places.adapter;

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
import androidx.recyclerview.widget.RecyclerView;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.ui.component.Dialog;
import com.apuliacreativehub.eculturetool.ui.places.NodeArtifact;
import com.apuliacreativehub.eculturetool.ui.places.viewmodel.CreatePathViewModel;
import com.google.common.collect.Iterables;
import com.google.common.graph.MutableGraph;
import com.google.common.graph.Traverser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class ListObjectsCreateAdapter extends RecyclerView.Adapter<ListObjectsCreateAdapter.ViewHolder> {

    private CreatePathViewModel createPathViewModel;
    private final int layout;
    private final ListCircleObjectsAdapter listCircleObjectsAdapter;
    private final String zoneName;
    private final ArrayList<NodeArtifact> dataSet;
    private final Context context;

    public ListObjectsCreateAdapter(int layout, CreatePathViewModel createPathViewModel, ListCircleObjectsAdapter listCircleObjectsAdapter, String zoneName) {
       this.layout = layout;
       this.createPathViewModel = createPathViewModel;
       this.listCircleObjectsAdapter = listCircleObjectsAdapter;
       this.zoneName = zoneName;
       dataSet = createPathViewModel.getObjectsDataset().get(zoneName);
       this.context = createPathViewModel.getContext();
    }


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

        private boolean isChecked() {
            return isChecked;
        }

        private void setCheck(boolean check) {
            this.isChecked = check;
        }
    }

    @Override @NonNull
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);
        return new ViewHolder(view, createPathViewModel.getContext());
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Log.i("DATA",dataSet.get(position).getName());
        Log.i("CHECK?", String.valueOf(dataSet.get(position).isChecked()));

        if(dataSet.get(position).isChecked()) {
            viewHolder.getCheckBox().setVisibility(View.VISIBLE);
            viewHolder.setCheck(true);
        }

        viewHolder.getView().findViewById(R.id.cardArtifactPath).setOnClickListener(v -> {
            if(!dataSet.get(position).isChecked()) {
                dataSet.get(position).check();
                viewHolder.getCheckBox().setVisibility(View.VISIBLE);
                increaseGraph(dataSet.get(position));
            } else {
                dataSet.get(position).uncheck();
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
        if(createPathViewModel.getUtilNodeTemp() != null) {
            newNode.setWeight(createPathViewModel.getUtilNodeTemp().getWeight()*2);
            createPathViewModel.getGraphDataset().putEdge(createPathViewModel.getUtilNodeTemp(), newNode);
        }
        else {
            newNode.setWeight(1.0);
            createPathViewModel.getGraphDataset().addNode(newNode);
        }
        createPathViewModel.setUtilNodeTemp(newNode);
    }

    /**
     * We get the adjcent nodes of the node we want to remove. They can be 2 or less (if the node is on the board). So we link the adjacent nodes of the removed node (if are two)
     * Then we get the last node of the graph (to trace if we want add another node).
     * If the node to be removed is the last -> utilNodeTempo become null (reset all the process)
     * @param nodeToRemove
     */
    private void decreaseGraph(NodeArtifact nodeToRemove) {
        Set<NodeArtifact> adjacentNode = createPathViewModel.getGraphDataset().adjacentNodes(nodeToRemove);
        Iterator<NodeArtifact> iteratorNode = adjacentNode.iterator();
        NodeArtifact leftNode, rightNode;
        createPathViewModel.getGraphDataset().removeNode(nodeToRemove);

        if(adjacentNode.size() == 2) {
            leftNode = iteratorNode.next();
            rightNode = iteratorNode.next();
            createPathViewModel.getGraphDataset().putEdge(leftNode, rightNode);
            if(nodeToRemove.equals(createPathViewModel.getUtilNodeTemp())) {
                createPathViewModel.setUtilNodeTemp(rightNode);
            }
        }
        if(adjacentNode.size() > 0 && nodeToRemove.equals(createPathViewModel.getUtilNodeTemp())) {
            createPathViewModel.setUtilNodeTemp(iteratorNode.next());
        } else if(adjacentNode.size() > 0){
            createPathViewModel.setUtilNodeTemp(Iterables.getLast(Traverser.forTree(createPathViewModel.getGraphDataset()).breadthFirst(createPathViewModel.getUtilNodeTemp())));
        } else {
            createPathViewModel.setUtilNodeTemp(null);
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}