package com.stocksensei.controller;

import com.stocksensei.model.Stock;
import com.stocksensei.service.StockAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for stock market operations
 */
@RestController
@RequestMapping("/api/stocks")
@CrossOrigin(origins = "*")
public class StockController {

    @Autowired
    private StockAnalysisService analysisService;

    /**
     * Get all stocks
     */
    @GetMapping
    public ResponseEntity<List<Stock>> getAllStocks() {
        return ResponseEntity.ok(analysisService.getAllStocks());
    }

    /**
     * Get stocks sorted by criterion
     * @param criterion: priceChange, volume, volatility, price
     */
    @GetMapping("/sorted")
    public ResponseEntity<List<Stock>> getSortedStocks(
            @RequestParam(defaultValue = "priceChange") String criterion) {
        return ResponseEntity.ok(analysisService.getAllStocksSorted(criterion));
    }

    /**
     * Get top K stocks by criterion
     */
    @GetMapping("/top")
    public ResponseEntity<List<Stock>> getTopStocks(
            @RequestParam(defaultValue = "priceChange") String criterion,
            @RequestParam(defaultValue = "10") int k) {
        return ResponseEntity.ok(analysisService.getTopStocks(criterion, k));
    }

    /**
     * Add or update stock
     */
    @PostMapping
    public ResponseEntity<Stock> addStock(@RequestBody Stock stock) {
        analysisService.addOrUpdateStock(stock);
        return ResponseEntity.ok(stock);
    }

    /**
     * Get stocks by sector
     */
    @GetMapping("/sector/{sector}")
    public ResponseEntity<List<Stock>> getStocksBySector(
            @PathVariable String sector) {
        return ResponseEntity.ok(analysisService.getStocksBySector(sector));
    }

    /**
     * Get sector performance metrics
     */
    @GetMapping("/sectors/performance")
    public ResponseEntity<Map<String, ?>> getSectorPerformance() {
        return ResponseEntity.ok(analysisService.getSectorPerformance());
    }

    /**
     * Find related stocks
     */
    @GetMapping("/related/{symbol}")
    public ResponseEntity<List<Stock>> getRelatedStocks(
            @PathVariable String symbol,
            @RequestParam(defaultValue = "2") int depth) {
        return ResponseEntity.ok(analysisService.findRelatedStocks(symbol, depth));
    }

    /**
     * Search stocks by symbol or name
     */
    @GetMapping("/search")
    public ResponseEntity<List<Stock>> searchStocks(
            @RequestParam String query) {
        return ResponseEntity.ok(analysisService.searchStocks(query));
    }

    /**
     * Get market summary
     */
    @GetMapping("/summary")
    public ResponseEntity<?> getMarketSummary() {
        return ResponseEntity.ok(analysisService.getMarketSummary());
    }

    /**
     * Clear all data
     */
    @DeleteMapping
    public ResponseEntity<String> clearData() {
        analysisService.clearData();
        return ResponseEntity.ok("All data cleared successfully");
    }
}
