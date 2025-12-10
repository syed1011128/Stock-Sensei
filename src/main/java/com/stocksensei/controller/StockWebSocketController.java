package com.stocksensei.controller;

import com.stocksensei.model.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * WebSocket controller for broadcasting stock updates to connected clients
 */
@Controller
public class StockWebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * Broadcast stock update to all connected clients
     */
    public void broadcastStockUpdate(Stock stock) {
        messagingTemplate.convertAndSend("/topic/stocks", stock);
    }

    /**
     * Broadcast market summary to all connected clients
     */
    public void broadcastMarketSummary(Object summary) {
        messagingTemplate.convertAndSend("/topic/summary", summary);
    }

    /**
     * Broadcast top stocks to all connected clients
     */
    public void broadcastTopStocks(List<Stock> topStocks) {
        messagingTemplate.convertAndSend("/topic/top-stocks", topStocks);
    }
}
