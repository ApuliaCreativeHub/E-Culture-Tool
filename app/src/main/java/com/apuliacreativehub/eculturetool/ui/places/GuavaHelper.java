package com.apuliacreativehub.eculturetool.ui.places;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.graph.ElementOrder;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;

import java.util.Comparator;

public class GuavaHelper {

    public static Comparator<NodeArtifact> getNodeArtifactWeightedComparator() {
       return (o1, o2) -> {
           if(o1.getWeight() - o2.getWeight() > 0) return 1;
           if(o1.getWeight() - o2.getWeight() < 0) return -1;
           return 0;
       };
    }

    public static MutableGraph<NodeArtifact> createInstance() {
        return GraphBuilder.directed()
                .allowsSelfLoops(false)
                .nodeOrder(ElementOrder.sorted(GuavaHelper.getNodeArtifactWeightedComparator()))
                .incidentEdgeOrder(ElementOrder.stable())
                .build();
    }

    public static NodeArtifact getNodeById(MutableGraph<NodeArtifact> graph, int id) {
        return Iterables.find(graph.nodes(), new Predicate<NodeArtifact>() {
            @Override
            public boolean apply(NodeArtifact input) {
                return input.getId() == id;
            }
        });
    }

    public static NodeArtifact getLeftNode(MutableGraph<NodeArtifact> graph, NodeArtifact node) {
        NodeArtifact[] temp = graph.predecessors(node).toArray(new NodeArtifact[0]);
        if (temp.length == 1) return temp[0];
        return null;
    }

    public static NodeArtifact getRightNode(MutableGraph<NodeArtifact> graph, NodeArtifact node) {
        NodeArtifact[] temp = graph.successors(node).toArray(new NodeArtifact[0]);
        if (temp.length == 1) return temp[0];
        return null;
    }
}
