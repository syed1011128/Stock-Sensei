package com.stocksensei.model;


import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Stock Entity representing a stock in the market
 */
public class Stock implements Comparable<Stock> {
    private String symbol;
    private String name;
    private double currentPrice;
    private double previousClose;
    private double priceChange;
    private double priceChangePercent;
    private long volume;
    private double volatility;
    private LocalDateTime lastUpdated;
    private String sector;

    // Constructors
    public Stock() {
        this.lastUpdated = LocalDateTime.now();
    }

    public Stock(String symbol, String name, double currentPrice,
                 double previousClose, long volume, String sector) {
        this.symbol = symbol;
        this.name = name;
        this.currentPrice = currentPrice;
        this.previousClose = previousClose;
        this.volume = volume;
        this.sector = sector;
        this.lastUpdated = LocalDateTime.now();
        calculateMetrics();
    }

    // Calculate derived metrics
    public void calculateMetrics() {
        this.priceChange = currentPrice - previousClose;
        this.priceChangePercent = (previousClose != 0)
                ? (priceChange / previousClose) * 100
                : 0;
        this.volatility = Math.abs(priceChangePercent);
    }

    // Getters and Setters
    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getCurrentPrice() { return currentPrice; }
    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
        calculateMetrics();
    }

    public double getPreviousClose() { return previousClose; }
    public void setPreviousClose(double previousClose) {
        this.previousClose = previousClose;
        calculateMetrics();
    }

    public double getPriceChange() { return priceChange; }
    public double getPriceChangePercent() { return priceChangePercent; }

    public long getVolume() { return volume; }
    public void setVolume(long volume) { this.volume = volume; }

    public double getVolatility() { return volatility; }
    public void setVolatility(double volatility) { this.volatility = volatility; }

    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getSector() { return sector; }
    public void setSector(String sector) { this.sector = sector; }

    // Compare based on price change percentage (for default sorting)
    @Override
    public int compareTo(Stock other) {
        return Double.compare(other.priceChangePercent, this.priceChangePercent);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stock stock = (Stock) o;
        return Objects.equals(symbol, stock.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol);
    }

    @Override
    public String toString() {
        return String.format("Stock{symbol='%s', name='%s', price=%.2f, change=%.2f%%}",
                symbol, name, currentPrice, priceChangePercent);
    }
}
