package com.stocksensei.datastructures;

import com.stocksensei.model.Stock;

/**
 * Sector metrics aggregator for analyzing sector performance
 */
public class SectorMetrics {
    private String sector;
    private double totalChange;
    private long totalVolume;
    private int stockCount;
    private double avgChange;
    private double avgVolume;

    public SectorMetrics(String sector) {
        this.sector = sector;
        this.totalChange = 0;
        this.totalVolume = 0;
        this.stockCount = 0;
    }

    public void addStock(Stock stock) {
        totalChange += stock.getPriceChangePercent();
        totalVolume += stock.getVolume();
        stockCount++;
        calculateAverages();
    }

    private void calculateAverages() {
        avgChange = stockCount > 0 ? totalChange / stockCount : 0;
        avgVolume = stockCount > 0 ? (double) totalVolume / stockCount : 0;
    }

    // Getters
    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public double getTotalChange() {
        return totalChange;
    }

    public long getTotalVolume() {
        return totalVolume;
    }

    public int getStockCount() {
        return stockCount;
    }

    public double getAvgChange() {
        return avgChange;
    }

    public double getAvgVolume() {
        return avgVolume;
    }

    @Override
    public String toString() {
        return String.format("SectorMetrics{sector='%s', stocks=%d, avgChange=%.2f%%, avgVolume=%.0f}",
                sector, stockCount, avgChange, avgVolume);
    }
}