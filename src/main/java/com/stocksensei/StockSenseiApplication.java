package com.stocksensei;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Stock Sensei - Real-Time Stock Market Analysis System
 * Main Application Class
 *
 * @author MD. Safiul Karim & Syed Mashiur Rahman
 * @university University of Frontier Technology, Bangladesh
 * @course CSE 114 - Data Structure and Algorithm Sessional
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.stocksensei")
@EnableScheduling
public class StockSenseiApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockSenseiApplication.class, args);
        System.out.println("\n╔════════════════════════════════════════════════╗");
        System.out.println("║       Stock Sensei Application Started        ║");
        System.out.println("║   Real-Time Stock Market Analysis System      ║");
        System.out.println("╠════════════════════════════════════════════════╣");
        System.out.println("║  REST API:     http://localhost:8080/api      ║");
        System.out.println("║  Dashboard:    http://localhost:8080/         ║");
        System.out.println("║  WebSocket:    ws://localhost:8080/ws-stocks  ║");
        System.out.println("║  Swagger UI:   http://localhost:8080/swagger-ui.html ║");
        System.out.println("╠════════════════════════════════════════════════╣");
        System.out.println("║  Test endpoints:                               ║");
        System.out.println("║  • GET  /api/stocks                            ║");
        System.out.println("║  • GET  /api/stocks/summary                    ║");
        System.out.println("║  • GET  /api/stocks/sorted?criterion=volume    ║");
        System.out.println("╚════════════════════════════════════════════════╝\n");
    }
}