package com.stocksensei.datastructures;

import com.stocksensei.model.Stock;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Graph implementation to model relationships between stocks
 * Edges represent correlations or sector relationships
 */
public class StockGraph {
    private Map<String, Stock> stocks;
    private Map<String, List<Edge>> adjacencyList;

    public StockGraph() {
        this.stocks = new HashMap<>();
        this.adjacencyList = new HashMap<>();
    }

    // Add stock as vertex
    public void addStock(Stock stock) {
        stocks.put(stock.getSymbol(), stock);
        adjacencyList.putIfAbsent(stock.getSymbol(), new ArrayList<>());
    }

    // Add edge (relationship) between stocks
    public void addEdge(String from, String to, double weight) {
        adjacencyList.get(from).add(new Edge(to, weight));
    }

    // Get related stocks based on sector
    public List<Stock> getStocksBySector(String sector) {
        List<Stock> sectorStocks = new ArrayList<>();
        for (Stock stock : stocks.values()) {
            if (stock.getSector().equals(sector)) {
                sectorStocks.add(stock);
            }
        }
        return sectorStocks;
    }

    // Find most connected stocks (hub stocks)
    public List<Stock> getHubStocks(int topK) {
        List<StockNode> nodes = new ArrayList<>();

        for (Map.Entry<String, List<Edge>> entry : adjacencyList.entrySet()) {
            String symbol = entry.getKey();
            int connections = entry.getValue().size();
            nodes.add(new StockNode(stocks.get(symbol), connections));
        }

        nodes.sort((a, b) -> Integer.compare(b.connections, a.connections));

        List<Stock> hubStocks = new ArrayList<>();
        for (int i = 0; i < Math.min(topK, nodes.size()); i++) {
            hubStocks.add(nodes.get(i).stock);
        }

        return hubStocks;
    }

    // BFS to find related stocks
    public List<Stock> findRelatedStocks(String symbol, int maxDepth) {
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        List<Stock> relatedStocks = new ArrayList<>();

        queue.offer(symbol);
        visited.add(symbol);
        int depth = 0;

        while (!queue.isEmpty() && depth < maxDepth) {
            int levelSize = queue.size();
            for (int i = 0; i < levelSize; i++) {
                String current = queue.poll();

                if (!current.equals(symbol)) {
                    relatedStocks.add(stocks.get(current));
                }

                List<Edge> edges = adjacencyList.get(current);
                if (edges != null) {
                    for (Edge edge : edges) {
                        if (!visited.contains(edge.to)) {
                            visited.add(edge.to);
                            queue.offer(edge.to);
                        }
                    }
                }
            }
            depth++;
        }

        return relatedStocks;
    }

    // DFS to detect strongly related clusters
    public List<List<Stock>> findStockClusters() {
        Set<String> visited = new HashSet<>();
        List<List<Stock>> clusters = new ArrayList<>();

        for (String symbol : stocks.keySet()) {
            if (!visited.contains(symbol)) {
                List<Stock> cluster = new ArrayList<>();
                dfs(symbol, visited, cluster);
                if (!cluster.isEmpty()) {
                    clusters.add(cluster);
                }
            }
        }

        return clusters;
    }

    private void dfs(String symbol, Set<String> visited, List<Stock> cluster) {
        visited.add(symbol);
        cluster.add(stocks.get(symbol));

        List<Edge> edges = adjacencyList.get(symbol);
        if (edges != null) {
            for (Edge edge : edges) {
                if (!visited.contains(edge.to)) {
                    dfs(edge.to, visited, cluster);
                }
            }
        }
    }

    // Calculate sector performance
    public Map<String, SectorMetrics> getSectorMetrics() {
        Map<String, SectorMetrics> metrics = new HashMap<>();

        for (Stock stock : stocks.values()) {
            String sector = stock.getSector();
            metrics.putIfAbsent(sector, new SectorMetrics(sector));
            metrics.get(sector).addStock(stock);
        }

        return metrics;
    }

    // Get all stocks
    public Collection<Stock> getAllStocks() {
        return stocks.values();
    }

    public Stock getStock(String symbol) {
        return stocks.get(symbol);
    }

    // Inner class for edge representation
    public static class Edge {
        String to;
        double weight;

        public Edge(String to, double weight) {
            this.to = to;
            this.weight = weight;
        }
    }

    // Inner class for stock node with connection count
    static class StockNode {
        Stock stock;
        int connections;

        StockNode(Stock stock, int connections) {
            this.stock = stock;
            this.connections = connections;
        }
    }
}