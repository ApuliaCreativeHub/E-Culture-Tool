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
import com.apuliacreativehub.eculturetool.ui.component.DialogTags;
import com.apuliacreativehub.eculturetool.ui.places.NodeObject;
import com.apuliacreativehub.eculturetool.ui.places.viewmodel.CEPathViewModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.common.collect.Iterables;
import com.google.common.graph.Traverser;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ListObjectsCreateAdapter extends RecyclerView.Adapter<ListObjectsCreateAdapter.ViewHolder> {

    private final CEPathViewModel cePathViewModel;
    private final int layout;
    private final ListCircleObjectsAdapter listCircleObjectsAdapter;
    private final String zoneName;
    private final List<NodeObject> dataSet;
    private final Context context;
    private NodeObject utilNodeTemp;

    public ListObjectsCreateAdapter(Context context, int layout, CEPathViewModel cePathViewModel, ListCircleObjectsAdapter listCircleObjectsAdapter) {
        this.layout = layout;
        this.cePathViewModel = cePathViewModel;
        this.listCircleObjectsAdapter = listCircleObjectsAdapter;
        this.zoneName = cePathViewModel.getCurrentlySelectedZoneName();
        dataSet = cePathViewModel.getObjectsDataset().getValue().getData().get(cePathViewModel.getCurrentlySelectedZoneName());
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final View view;
        private boolean isChecked;
        private final TextView txtTitle;
        private final Button btnDescription;
        private final ImageView checkView;
        private final ImageView imgObject;

        public ViewHolder(View view) {
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

        public ImageView getImgObject() {
            return imgObject;
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
        return new ViewHolder(view);
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
        viewHolder.getTitle().setText(dataSet.get(position).getName());
        Glide.with(context)
                .load("https://hiddenfile.ml/ecultureapi/" + this.dataSet.get(position)
                        //.load("http://10.0.2.2:8080/" + this.dataSet.get(position)
                        .getNormalSizeImg())
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(viewHolder.getImgObject());
        viewHolder.getBtnDescription().setOnClickListener(v -> new Dialog(context.getString(R.string.description), dataSet.get(position).getDescription(), DialogTags.OBJECT_DESCRIPTION).show(((AppCompatActivity) context).getSupportFragmentManager(), Dialog.TAG));
    }

    /**
     * If utilNodeTemp is null means that the graph is empty. -> We have to add a node. (to trace operation we save the last value added to node in a variable)
     * if utilNodeTemp is not null means that there is almost one node -> We have to link the new node to the last node.
     * @param newNode
     */
    private void increaseGraph(NodeObject newNode) {
        if (cePathViewModel.getUtilNodeTemp() != null) {
            newNode.setWeight(cePathViewModel.getUtilNodeTemp().getWeight() * 2);
            cePathViewModel.getGraphDataset().putEdge(cePathViewModel.getUtilNodeTemp(), newNode);
        } else {
            newNode.setWeight(1.0);
            cePathViewModel.getGraphDataset().addNode(newNode);
        }
        cePathViewModel.setUtilNodeTemp(newNode);
    }

    /**
     * We get the adjcent nodes of the node we want to remove. They can be 2 or less (if the node is on the board). So we link the adjacent nodes of the removed node (if are two)
     * Then we get the last node of the graph (to trace if we want add another node).
     * If the node to be removed is the last -> utilNodeTempo become null (reset all the process)
     * @param nodeToRemove
     */
    private void decreaseGraph(NodeObject nodeToRemove) {
        Set<NodeObject> adjacentNode = cePathViewModel.getGraphDataset().adjacentNodes(nodeToRemove);
        Iterator<NodeObject> iteratorNode = adjacentNode.iterator();
        NodeObject leftNode, rightNode;
        cePathViewModel.getGraphDataset().removeNode(nodeToRemove);

        if (adjacentNode.size() == 2) {
            leftNode = iteratorNode.next();
            rightNode = iteratorNode.next();
            cePathViewModel.getGraphDataset().putEdge(leftNode, rightNode);
            if (nodeToRemove.equals(cePathViewModel.getUtilNodeTemp())) {
                cePathViewModel.setUtilNodeTemp(rightNode);
            }
        }
        if (adjacentNode.size() > 0 && nodeToRemove.equals(cePathViewModel.getUtilNodeTemp())) {
            cePathViewModel.setUtilNodeTemp(iteratorNode.next());
        } else if (adjacentNode.size() > 0) {
            cePathViewModel.setUtilNodeTemp(Iterables.getLast(Traverser.forTree(cePathViewModel.getGraphDataset()).breadthFirst(cePathViewModel.getUtilNodeTemp())));
        } else {
            cePathViewModel.setUtilNodeTemp(null);
        }
    }

    @Override
    public int getItemCount() {
        if(dataSet != null)
            return this.dataSet.size();
        else
            return 0;
    }

}