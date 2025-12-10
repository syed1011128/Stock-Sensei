package com.stocksensei.service;

public class MarketSummary {
    private int totalStocks;
    private double avgChange;
    private long totalVolume;
    private int gainers;
    private int losers;
    private int unchanged;

    public MarketSummary() {
        this.totalStocks = 0;
        this.avgChange = 0.0;
        this.totalVolume = 0;
        this.gainers = 0;
        this.losers = 0;
        this.unchanged = 0;
    }

    public int getTotalStocks() { return totalStocks; }
    public void setTotalStocks(int totalStocks) { this.totalStocks = totalStocks; }

    public double getAvgChange() { return avgChange; }
    public void setAvgChange(double avgChange) { this.avgChange = avgChange; }

    public long getTotalVolume() { return totalVolume; }
    public void setTotalVolume(long totalVolume) { this.totalVolume = totalVolume; }

    public int getGainers() { return gainers; }
    public void setGainers(int gainers) { this.gainers = gainers; }

    public int getLosers() { return losers; }
    public void setLosers(int losers) { this.losers = losers; }

    public int getUnchanged() { return unchanged; }
    public void setUnchanged(int unchanged) { this.unchanged = unchanged; }
}