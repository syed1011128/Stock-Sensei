package com.stocksensei.datastructures;
import java.util.*;


public class BFSGraph {
    private int vertices;
    private LinkedList<Integer>[] adj;

    BFSGraph(int v) {
        vertices = v;
        adj = new LinkedList[v];

        for (int i=0; i < v; i++) {
            adj[i] = new LinkedList<>();
        }

    }

    void addEdge(int src, int dest) {
        adj[src].add(dest);
    }

    void bfs(int start) {
        boolean[] visited = new boolean[vertices];

        Queue<Integer> queue = new LinkedList<>();

        visited[start] = true;
        queue.add(start);

        System.out.println("BFS Traversal: ");

        while (!queue.isEmpty()) {
            int node = queue.poll();
            System.out.println(node + " ");

            for (int neighbour : adj[node]) {
                if (!visited[neighbour]) {
                    visited[neighbour] = true;
                    queue.add(neighbour);
                }
            }
        }
    }

    static void main() {
        BFSGraph g = new BFSGraph(6);

        g.addEdge(0, 1);
        g.addEdge(0,2);
        g.addEdge(1,3);
        g.addEdge(1,4);
        g.addEdge(2,5);

        g.bfs(0);
    }
}
