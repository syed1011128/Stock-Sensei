package com.stocksensei.service;

import com.stocksensei.model.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Service to simulate real-time stock market data
 */
@Service
public class StockDataSimulator {

    @Autowired
    private StockAnalysisService analysisService;

    private final List<StockTemplate> stockTemplates = new ArrayList<>();
    private boolean isInitialized = false;

    /**
     * Initialize sample stocks
     */
    public void initializeStocks() {
        if (isInitialized) return;

        stockTemplates.add(new StockTemplate("AAPL", "Apple Inc.", 175.50, "Technology"));
        stockTemplates.add(new StockTemplate("GOOGL", "Alphabet Inc.", 140.25, "Technology"));
        stockTemplates.add(new StockTemplate("MSFT", "Microsoft Corp.", 378.90, "Technology"));
        stockTemplates.add(new StockTemplate("AMZN", "Amazon.com Inc.", 152.30, "E-commerce"));
        stockTemplates.add(new StockTemplate("TSLA", "Tesla Inc.", 242.80, "Automotive"));
        stockTemplates.add(new StockTemplate("META", "Meta Platforms", 485.60, "Technology"));
        stockTemplates.add(new StockTemplate("NVDA", "NVIDIA Corp.", 875.45, "Technology"));
        stockTemplates.add(new StockTemplate("JPM", "JPMorgan Chase", 195.30, "Finance"));
        stockTemplates.add(new StockTemplate("BAC", "Bank of America", 38.75, "Finance"));
        stockTemplates.add(new StockTemplate("V", "Visa Inc.", 272.90, "Finance"));
        stockTemplates.add(new StockTemplate("WMT", "Walmart Inc.", 167.50, "Retail"));
        stockTemplates.add(new StockTemplate("JNJ", "Johnson & Johnson", 156.80, "Healthcare"));
        stockTemplates.add(new StockTemplate("PFE", "Pfizer Inc.", 28.45, "Healthcare"));
        stockTemplates.add(new StockTemplate("XOM", "Exxon Mobil", 108.30, "Energy"));
        stockTemplates.add(new StockTemplate("CVX", "Chevron Corp.", 155.20, "Energy"));
        stockTemplates.add(new StockTemplate("DIS", "Walt Disney Co.", 96.75, "Entertainment"));
        stockTemplates.add(new StockTemplate("NFLX", "Netflix Inc.", 612.40, "Entertainment"));
        stockTemplates.add(new StockTemplate("BA", "Boeing Co.", 186.90, "Aerospace"));
        stockTemplates.add(new StockTemplate("INTC", "Intel Corp.", 42.35, "Technology"));
        stockTemplates.add(new StockTemplate("AMD", "AMD Inc.", 167.80, "Technology"));

        for (StockTemplate template : stockTemplates) {
            Stock stock = createStockFromTemplate(template);
            analysisService.addOrUpdateStock(stock);
        }

        isInitialized = true;
    }

    /**
     * Scheduled task to update stock prices (every 3 seconds)
     */
    @Scheduled(fixedRate = 3000)
    public void updateStockPrices() {
        if (!isInitialized) {
            initializeStocks();
            return;
        }

        for (StockTemplate template : stockTemplates) {
            Stock updatedStock = simulateStockUpdate(template);
            analysisService.addOrUpdateStock(updatedStock);
        }
    }

    /**
     * Create initial stock from template
     */
    private Stock createStockFromTemplate(StockTemplate template) {
        double previousClose = template.basePrice;
        double currentPrice = previousClose + randomChange(previousClose, 0.02);
        long volume = randomVolume();

        return new Stock(
                template.symbol,
                template.name,
                currentPrice,
                previousClose,
                volume,
                template.sector
        );
    }

    /**
     * Simulate stock price update
     */
    private Stock simulateStockUpdate(StockTemplate template) {
        Stock currentStock = analysisService.getAllStocks().stream()
                .filter(s -> s.getSymbol().equals(template.symbol))
                .findFirst()
                .orElse(createStockFromTemplate(template));

        double previousPrice = currentStock.getCurrentPrice();
        double priceChange = randomChange(previousPrice, 0.005);
        double newPrice = previousPrice + priceChange;

        if (ThreadLocalRandom.current().nextDouble() < 0.1) {
            newPrice += randomChange(newPrice, 0.02);
        }

        long volumeChange = (long) (currentStock.getVolume() *
                (1 + ThreadLocalRandom.current().nextDouble(-0.2, 0.2)));

        Stock updatedStock = new Stock(
                currentStock.getSymbol(),
                currentStock.getName(),
                Math.max(newPrice, 0.01),
                currentStock.getPreviousClose(),
                Math.max(volumeChange, 1000),
                currentStock.getSector()
        );

        return updatedStock;
    }

    /**
     * Generate random price change
     */
    private double randomChange(double baseValue, double maxChangePercent) {
        double changePercent = ThreadLocalRandom.current()
                .nextDouble(-maxChangePercent, maxChangePercent);
        return baseValue * changePercent;
    }

    /**
     * Generate random volume
     */
    private long randomVolume() {
        return ThreadLocalRandom.current().nextLong(100000, 10000000);
    }

    /**
     * Reset simulation
     */
    public void resetSimulation() {
        isInitialized = false;
        analysisService.clearData();
        initializeStocks();
    }

    /**
     * Template for stock initialization
     */
    static class StockTemplate {
        String symbol;
        String name;
        double basePrice;
        String sector;

        StockTemplate(String symbol, String name, double basePrice, String sector) {
            this.symbol = symbol;
            this.name = name;
            this.basePrice = basePrice;
            this.sector = sector;
        }
    }
}