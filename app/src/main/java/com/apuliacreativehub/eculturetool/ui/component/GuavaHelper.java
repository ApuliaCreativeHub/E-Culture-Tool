package com.apuliacreativehub.eculturetool.ui.component;

import com.apuliacreativehub.eculturetool.ui.places.NodeObject;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.graph.ElementOrder;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;

import java.util.Comparator;

public class GuavaHelper {

    public static Comparator<NodeObject> getNodeObjectWeightedComparator() {
        return (o1, o2) -> {
            if (o1.getWeight() - o2.getWeight() > 0) return 1;
            if (o1.getWeight() - o2.getWeight() < 0) return -1;
            return 0;
        };
    }

    public static MutableGraph<NodeObject> createInstance() {
        return GraphBuilder.directed()
                .allowsSelfLoops(false)
                .nodeOrder(ElementOrder.sorted(GuavaHelper.getNodeObjectWeightedComparator()))
                .incidentEdgeOrder(ElementOrder.stable())
                .build();
    }

    public static NodeObject getNodeById(MutableGraph<NodeObject> graph, int id) {
        return Iterables.find(graph.nodes(), new Predicate<NodeObject>() {
            @Override
            public boolean apply(NodeObject input) {
                return input.getId() == id;
            }
        });
    }

    public static NodeObject getLeftNode(MutableGraph<NodeObject> graph, NodeObject node) {
        NodeObject[] temp = graph.predecessors(node).toArray(new NodeObject[0]);
        if (temp.length == 1) return temp[0];
        return null;
    }

    public static NodeObject getRightNode(MutableGraph<NodeObject> graph, NodeObject node) {
        NodeObject[] temp = graph.successors(node).toArray(new NodeObject[0]);
        if (temp.length == 1) return temp[0];
        return null;
    }
}
