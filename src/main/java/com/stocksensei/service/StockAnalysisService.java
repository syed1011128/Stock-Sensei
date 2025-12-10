package com.stocksensei.service;


import com.stocksensei.datastructures.SectorMetrics;
import com.stocksensei.datastructures.StockGraph;
import com.stocksensei.datastructures.StockMaxHeap;
import com.stocksensei.model.Stock;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Core service for stock analysis and ranking
 */
@Service
public class StockAnalysisService {
    private StockGraph stockGraph;
    private Map<String, StockMaxHeap> heaps;
    private List<Stock> stockDatabase;

    public StockAnalysisService() {
        this.stockGraph = new StockGraph();
        this.heaps = new HashMap<>();
        this.stockDatabase = Collections.synchronizedList(new ArrayList<>());
        initializeHeaps();
    }

    private void initializeHeaps() {
        heaps.put("priceChange", new StockMaxHeap(
                Comparator.comparingDouble(Stock::getPriceChangePercent)));
        heaps.put("volume", new StockMaxHeap(
                Comparator.comparingLong(Stock::getVolume)));
        heaps.put("volatility", new StockMaxHeap(
                Comparator.comparingDouble(Stock::getVolatility)));
        heaps.put("price", new StockMaxHeap(
                Comparator.comparingDouble(Stock::getCurrentPrice)));
    }

    // Add or update stock
    public synchronized void addOrUpdateStock(Stock stock) {
        stockDatabase.removeIf(s -> s.getSymbol().equals(stock.getSymbol()));
        stockDatabase.add(stock);

        stockGraph.addStock(stock);

        for (StockMaxHeap heap : heaps.values()) {
            heap.insert(stock);
        }
    }

    // Get top K stocks by criterion
    public List<Stock> getTopStocks(String criterion, int k) {
        StockMaxHeap heap = heaps.get(criterion);
        if (heap == null) {
            return new ArrayList<>();
        }
        return heap.getTopK(k);
    }

    // Get all stocks sorted by criterion
    public List<Stock> getAllStocksSorted(String criterion) {
        List<Stock> stocks = new ArrayList<>(stockDatabase);

        switch (criterion) {
            case "priceChange":
                stocks.sort((a, b) -> Double.compare(
                        b.getPriceChangePercent(), a.getPriceChangePercent()));
                break;
            case "volume":
                stocks.sort((a, b) -> Long.compare(b.getVolume(), a.getVolume()));
                break;
            case "volatility":
                stocks.sort((a, b) -> Double.compare(
                        b.getVolatility(), a.getVolatility()));
                break;
            case "price":
                stocks.sort((a, b) -> Double.compare(
                        b.getCurrentPrice(), a.getCurrentPrice()));
                break;
            default:
                stocks.sort((a, b) -> a.getSymbol().compareTo(b.getSymbol()));
        }

        return stocks;
    }

    // Quick Sort implementation for custom sorting
    public List<Stock> quickSort(List<Stock> stocks, Comparator<Stock> comparator) {
        if (stocks.size() <= 1) {
            return stocks;
        }

        List<Stock> sorted = new ArrayList<>(stocks);
        quickSortHelper(sorted, 0, sorted.size() - 1, comparator);
        return sorted;
    }

    private void quickSortHelper(List<Stock> stocks, int low, int high,
                                 Comparator<Stock> comparator) {
        if (low < high) {
            int pi = partition(stocks, low, high, comparator);
            quickSortHelper(stocks, low, pi - 1, comparator);
            quickSortHelper(stocks, pi + 1, high, comparator);
        }
    }

    private int partition(List<Stock> stocks, int low, int high,
                          Comparator<Stock> comparator) {
        Stock pivot = stocks.get(high);
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (comparator.compare(stocks.get(j), pivot) <= 0) {
                i++;
                Collections.swap(stocks, i, j);
            }
        }
        Collections.swap(stocks, i + 1, high);
        return i + 1;
    }

    // Merge Sort implementation
    public List<Stock> mergeSort(List<Stock> stocks, Comparator<Stock> comparator) {
        if (stocks.size() <= 1) {
            return stocks;
        }

        int mid = stocks.size() / 2;
        List<Stock> left = mergeSort(stocks.subList(0, mid), comparator);
        List<Stock> right = mergeSort(stocks.subList(mid, stocks.size()), comparator);

        return merge(left, right, comparator);
    }

    private List<Stock> merge(List<Stock> left, List<Stock> right,
                              Comparator<Stock> comparator) {
        List<Stock> result = new ArrayList<>();
        int i = 0, j = 0;

        while (i < left.size() && j < right.size()) {
            if (comparator.compare(left.get(i), right.get(j)) <= 0) {
                result.add(left.get(i++));
            } else {
                result.add(right.get(j++));
            }
        }

        result.addAll(left.subList(i, left.size()));
        result.addAll(right.subList(j, right.size()));
        return result;
    }

    // Get stocks by sector
    public List<Stock> getStocksBySector(String sector) {
        return stockGraph.getStocksBySector(sector);
    }

    // Get sector performance
    public Map<String, SectorMetrics> getSectorPerformance() {
        return stockGraph.getSectorMetrics();
    }

    // Find related stocks
    public List<Stock> findRelatedStocks(String symbol, int depth) {
        return stockGraph.findRelatedStocks(symbol, depth);
    }

    // Get market summary
    public MarketSummary getMarketSummary() {
        MarketSummary summary = new MarketSummary();

        if (stockDatabase.isEmpty()) {
            return summary;
        }

        double totalChange = 0;
        long totalVolume = 0;
        int gainers = 0;
        int losers = 0;

        for (Stock stock : stockDatabase) {
            totalChange += stock.getPriceChangePercent();
            totalVolume += stock.getVolume();

            if (stock.getPriceChange() > 0) gainers++;
            else if (stock.getPriceChange() < 0) losers++;
        }

        summary.setTotalStocks(stockDatabase.size());
        summary.setAvgChange(totalChange / stockDatabase.size());
        summary.setTotalVolume(totalVolume);
        summary.setGainers(gainers);
        summary.setLosers(losers);
        summary.setUnchanged(stockDatabase.size() - gainers - losers);

        return summary;
    }

    // Search stocks
    public List<Stock> searchStocks(String query) {
        String lowerQuery = query.toLowerCase();
        return stockDatabase.stream()
                .filter(s -> s.getSymbol().toLowerCase().contains(lowerQuery) ||
                        s.getName().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
    }

    // Get all stocks
    public List<Stock> getAllStocks() {
        return new ArrayList<>(stockDatabase);
    }

    // Clear all data
    public void clearData() {
        stockDatabase.clear();
        heaps.values().forEach(StockMaxHeap::clear);
        stockGraph = new StockGraph();
    }
}


