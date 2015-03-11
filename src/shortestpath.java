import java.util.PriorityQueue;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

class Vertex implements Comparable<Vertex>
{
    public final String name;
    public Edge[] adjacencies;
    public double minDistance = Double.POSITIVE_INFINITY;
    public Vertex previous;
    public Vertex(String argName) { name = argName; }
    public String toString() { return name; }
    public int compareTo(Vertex other)
    {
        return Double.compare(minDistance, other.minDistance);
    }

}


class Edge
{
    public final Vertex target;
    public final double weight;
    public Edge(Vertex argTarget, double argWeight)
    { target = argTarget; weight = argWeight; }
}

public class shortestpath
{
    public static void computePaths(Vertex source)
    {
        source.minDistance = 0.;
        PriorityQueue<Vertex> vertexQueue = new PriorityQueue<Vertex>();
        vertexQueue.add(source);

        while (!vertexQueue.isEmpty()) {
            Vertex u = vertexQueue.poll();

            // Visit each edge exiting u
            for (Edge e : u.adjacencies)
            {
                Vertex v = e.target;
                double weight = e.weight;
                double distanceThroughU = u.minDistance + weight;
                if (distanceThroughU < v.minDistance) {
                    vertexQueue.remove(v);

                    v.minDistance = distanceThroughU ;
                    v.previous = u;
                    vertexQueue.add(v);
                }
            }
        }
    }

    public static List<Vertex> getShortestPathTo(Vertex target)
    {
        List<Vertex> path = new ArrayList<Vertex>();
        for (Vertex vertex = target; vertex != null; vertex = vertex.previous)
            path.add(vertex);

        Collections.reverse(path);
        return path;
    }

    public static void main(String[] args)
    {
        // mark all the vertices
        Vertex A = new Vertex("A");
        Vertex B = new Vertex("B");
        Vertex C = new Vertex("C");
        Vertex D = new Vertex("D");
        Vertex H = new Vertex("H");
        Vertex X = new Vertex("X");

        // set the edges and weight
        A.adjacencies = new Edge[]{ new Edge(B, 2) };
        A.adjacencies = new Edge[]{ new Edge(C, 3) };

        B.adjacencies = new Edge[]{ new Edge(A, 2) };
        B.adjacencies = new Edge[]{ new Edge(C, 1) };

        C.adjacencies = new Edge[]{ new Edge(A, 3) };
        C.adjacencies = new Edge[]{ new Edge(B, 1) };
        C.adjacencies = new Edge[]{ new Edge(D, 2) };
        C.adjacencies = new Edge[]{ new Edge(H, 7) };

        D.adjacencies = new Edge[]{ new Edge(C, 2) };
        D.adjacencies = new Edge[]{ new Edge(X, 3) };

        H.adjacencies = new Edge[]{ new Edge(C, 7) };
        H.adjacencies = new Edge[]{ new Edge(X, 4) };

        X.adjacencies = new Edge[]{ new Edge(D, 3) };
        X.adjacencies = new Edge[]{ new Edge(H, 4) };



        long tStart = System.currentTimeMillis();
        computePaths(A); // run Dijkstra
        List<Vertex> path = getShortestPathTo(D);

        long tEnd = System.currentTimeMillis();
        long tDelta = tEnd - tStart;
        double elapsedSeconds = tDelta / 1000.0;

        System.out.println("Distance to " + D + ": " + D.minDistance);
        System.out.println("Path: " + path);
        System.out.println("ElapsedTime: " + elapsedSeconds);
    }
}