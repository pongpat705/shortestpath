import java.util.*;

/**
 * Created by jitsuem on 3/6/2015.
 */
final class NodeData<T> {

    private final T nodeId;
    private final Map<T, Double> heuristic;

    private double g;  // g is distance from the source
    private double h;  // h is the heuristic of destination.
    private double f;  // f = g + h

    public NodeData (T nodeId, Map<T, Double> heuristic) {
        this.nodeId = nodeId;
        this.g = Double.MAX_VALUE;
        this.heuristic = heuristic;
    }

    public T getNodeId() {
        return nodeId;
    }

    public double getG() {
        return g;
    }

    public void setG(double g) {
        this.g = g;
    }

    public void calcF(T destination) {
        this.h = heuristic.get(destination);
        this.f = g + h;
    }

    public double getH() {
        return h;
    }

    public double getF() {
        return f;
    }
}

/**
 * The graph represents an undirected graph.
 *
 * @author SERVICE-NOW\ameya.patil
 *
 * @param <T>
 */
final class GraphAStar<T> implements Iterable<T> {
    /*
     * A map from the nodeId to outgoing edge.
     * An outgoing edge is represented as a tuple of NodeData and the edge length
     */
    private final Map<T, Map<NodeData<T>, Double>> graph;
    /*
     * A map of heuristic from a node to each other node in the graph.
     */
    private final Map<T, Map<T, Double>> heuristicMap;
    /*
     * A map between nodeId and nodedata.
     */
    private final Map<T, NodeData<T>> nodeIdNodeData;

    public GraphAStar(Map<T, Map<T, Double>> heuristicMap) {
        if (heuristicMap == null) throw new NullPointerException("The huerisic map should not be null");
        graph = new HashMap<T, Map<NodeData<T>, Double>>();
        nodeIdNodeData = new HashMap<T, NodeData<T>>();
        this.heuristicMap = heuristicMap;
    }

    /**
     * Adds a new node to the graph.
     * Internally it creates the nodeData and populates the heuristic map concerning input node into node data.
     *
     * @param nodeId the node to be added
     */
    public void addNode(T nodeId) {
        if (nodeId == null) throw new NullPointerException("The node cannot be null");
        if (!heuristicMap.containsKey(nodeId)) throw new NoSuchElementException("This node is not a part of hueristic map");

        graph.put(nodeId, new HashMap<NodeData<T>, Double>());
        nodeIdNodeData.put(nodeId, new NodeData<T>(nodeId, heuristicMap.get(nodeId)));
    }

    /**
     * Adds an edge from source node to destination node.
     * There can only be a single edge from source to node.
     * Adding additional edge would overwrite the value
     *
     * @param nodeIdFirst   the first node to be in the edge
     * @param nodeIdSecond  the second node to be second node in the edge
     * @param length        the length of the edge.
     */
    public void addEdge(T nodeIdFirst, T nodeIdSecond, double length) {
        if (nodeIdFirst == null || nodeIdSecond == null) throw new NullPointerException("The first nor second node can be null.");

        if (!heuristicMap.containsKey(nodeIdFirst) || !heuristicMap.containsKey(nodeIdSecond)) {
            throw new NoSuchElementException("Source and Destination both should be part of the part of hueristic map");
        }
        if (!graph.containsKey(nodeIdFirst) || !graph.containsKey(nodeIdSecond)) {
            throw new NoSuchElementException("Source and Destination both should be part of the part of graph");
        }

        graph.get(nodeIdFirst).put(nodeIdNodeData.get(nodeIdSecond), length);
        graph.get(nodeIdSecond).put(nodeIdNodeData.get(nodeIdFirst), length);
    }

    /**
     * Returns immutable view of the edges
     *
     * @param nodeId    the nodeId whose outgoing edge needs to be returned
     * @return          An immutable view of edges leaving that node
     */
    public Map<NodeData<T>, Double> edgesFrom (T nodeId) {
        if (nodeId == null) throw new NullPointerException("The input node should not be null.");
        if (!heuristicMap.containsKey(nodeId)) throw new NoSuchElementException("This node is not a part of hueristic map");
        if (!graph.containsKey(nodeId)) throw new NoSuchElementException("The node should not be null.");

        return Collections.unmodifiableMap(graph.get(nodeId));
    }

    /**
     * The nodedata corresponding to the current nodeId.
     *
     * @param nodeId    the nodeId to be returned
     * @return          the nodeData from the
     */
    public NodeData<T> getNodeData (T nodeId) {
        if (nodeId == null) { throw new NullPointerException("The nodeid should not be empty"); }
        if (!nodeIdNodeData.containsKey(nodeId))  { throw new NoSuchElementException("The nodeId does not exist"); }
        return nodeIdNodeData.get(nodeId);
    }

    /**
     * Returns an iterator that can traverse the nodes of the graph
     *
     * @return an Iterator.
     */
    @Override public Iterator<T> iterator() {
        return graph.keySet().iterator();
    }
}

public class astar<T> {

    private final GraphAStar<T> graph;
    // setter getter

    // setter getter

    public astar(GraphAStar<T> graphAStar) {
        this.graph = graphAStar;
    }

    // extend comparator.
    public class NodeComparator implements Comparator<NodeData<T>> {
        public int compare(NodeData<T> nodeFirst, NodeData<T> nodeSecond) {
            if (nodeFirst.getF() > nodeSecond.getF()) return 1;
            if (nodeSecond.getF() > nodeFirst.getF()) return -1;
            return 0;
        }
    }

    /**
     * Implements the A-star algorithm and returns the path from source to destination
     *
     * @param source        the source nodeid
     * @param destination   the destination nodeid
     * @return              the path from source to destination
     */
    public List<T> astar(T source, T destination) {
        /**
         * http://stackoverflow.com/questions/20344041/why-does-priority-queue-has-default-initial-capacity-of-11
         */

        final Queue<NodeData<T>> openQueue = new PriorityQueue<NodeData<T>>(11, new NodeComparator());

        NodeData<T> sourceNodeData = graph.getNodeData(source);
        System.out.println("sourceNodeData = graph.getNodeData(source): "+sourceNodeData.getNodeId());
        sourceNodeData.setG(0);
        sourceNodeData.calcF(destination);
        openQueue.add(sourceNodeData);
        System.out.println("after add source to openqueue "+openQueue.element().getNodeId());

        final Map<T, T> path = new HashMap<T, T>();
        final Set<NodeData<T>> closedList = new HashSet<NodeData<T>>();

        while (!openQueue.isEmpty()) {
            final NodeData<T> nodeData = openQueue.poll();
            System.out.println("nodeData = openqueue "+nodeData.getNodeId());

            if (nodeData.getNodeId().equals(destination)) {
                System.out.println("NodeData = destination, So return path");
                return path(path, destination);
            }else {
                System.out.println("NodeData != destination");
            }

            closedList.add(nodeData);
            System.out.println("closedList add nodeData "+nodeData.getNodeId()+"\n closedList has"+closedList);

            for (Map.Entry<NodeData<T>, Double> neighborEntry : graph.edgesFrom(nodeData.getNodeId()).entrySet()) {
                NodeData<T> neighbor = neighborEntry.getKey();
                System.out.println("neighbor = neighborentry.getkey "+neighbor.getNodeId());

                if (closedList.contains(neighbor)) continue;

                double distanceBetweenTwoNodes = neighborEntry.getValue();
                System.out.println("distanceBetween2node "+distanceBetweenTwoNodes);
                double tentativeG = distanceBetweenTwoNodes + nodeData.getG();
                System.out.println("tentativeG "+tentativeG);

                if (tentativeG < neighbor.getG()) {
                    neighbor.setG(tentativeG);
                    neighbor.calcF(destination);

                    path.put(neighbor.getNodeId(), nodeData.getNodeId());
                    if (!openQueue.contains(neighbor)) {
                        openQueue.add(neighbor);
                    }
                }
            }
        }

        return null;
    }



    private List<T> path(Map<T, T> path, T destination) {
        assert path != null;
        assert destination != null;

        final List<T> pathList = new ArrayList<T>();
        pathList.add(destination);
        while (path.containsKey(destination)) {
            destination = path.get(destination);
            pathList.add(destination);
        }
        Collections.reverse(pathList);
        return pathList;
    }


    public static void main(String[] args) {
        Map<String, Map<String, Double>> heuristic = new HashMap<String, Map<String, Double>>();
        // map for A
        Map<String, Double> mapA = new HashMap<String, Double>();
        mapA.put("A",  0.0);
        mapA.put("B", 10.0);
        mapA.put("C", 17.0);
        mapA.put("E", 20.0);
        mapA.put("F", 26.0);


        // map for B
        Map<String, Double> mapB = new HashMap<String, Double>();
        mapB.put("A", 10.0);
        mapB.put("B",  0.0);
        mapB.put("C", 7.0);
        mapB.put("E", 8.0);
        mapB.put("F", 16.0);



        // map for C
        Map<String, Double> mapC = new HashMap<String, Double>();
        mapC.put("A", 17.0);
        mapC.put("B", 7.0);
        mapC.put("C", 0.0);
        mapC.put("E", 8.0);
        mapC.put("F", 9.0);


        // map for X
        Map<String, Double> mapX = new HashMap<String, Double>();
        mapX.put("A", 20.0);
        mapX.put("B", 15.0);
        mapX.put("C", 8.0);
        mapX.put("E",  0.0);
        mapX.put("F", 6.0);

        // map for X
        Map<String, Double> mapZ = new HashMap<String, Double>();
        mapZ.put("A", 26.0);
        mapZ.put("B", 16.0);
        mapZ.put("C", 9.0);
        mapZ.put("E", 6.0);
        mapZ.put("F", 0.0);

        heuristic.put("A", mapA);
        heuristic.put("B", mapB);
        heuristic.put("C", mapC);
        heuristic.put("E", mapX);
        heuristic.put("F", mapZ);

        GraphAStar<String> graph = new GraphAStar<String>(heuristic);
        graph.addNode("A");
        graph.addNode("B");
        graph.addNode("C");
        graph.addNode("E");
        graph.addNode("F");

        graph.addEdge("A", "B", 10);
        graph.addEdge("A", "E", 20);
        graph.addEdge("B", "C", 7);
        graph.addEdge("C", "E", 8);
        graph.addEdge("C", "F", 9);
        graph.addEdge("E", "F", 6);

    /*    System.out.println(mapA);
        System.out.println(mapB);
        System.out.println(mapC);
        System.out.println(mapX);
        System.out.println(mapZ);
        System.out.println(heuristic);*/

        astar<String> aStar = new astar<String>(graph);

        for (String path : aStar.astar("A", "F")) {
            System.out.print("  " + path);
        }
    }
}
