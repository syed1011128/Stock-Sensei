package com.stocksensei.datastructures;

import com.stocksensei.model.Stock;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Max Heap implementation for efficient stock ranking
 * Supports different ranking criteria through comparators
 */
public class StockMaxHeap {
    private List<Stock> heap;
    private Comparator<Stock> comparator;

    public StockMaxHeap(Comparator<Stock> comparator) {
        this.heap = new ArrayList<>();
        this.comparator = comparator;
    }

    // Insert stock into heap
    public void insert(Stock stock) {
        heap.add(stock);
        heapifyUp(heap.size() - 1);
    }

    // Extract maximum (top-ranked) stock
    public Stock extractMax() {
        if (heap.isEmpty()) {
            return null;
        }

        Stock max = heap.get(0);
        Stock last = heap.remove(heap.size() - 1);

        if (!heap.isEmpty()) {
            heap.set(0, last);
            heapifyDown(0);
        }

        return max;
    }

    // Peek at maximum without removing
    public Stock peekMax() {
        return heap.isEmpty() ? null : heap.get(0);
    }

    // Get top K stocks
    public List<Stock> getTopK(int k) {
        List<Stock> topStocks = new ArrayList<>();
        List<Stock> tempHeap = new ArrayList<>(heap);

        for (int i = 0; i < Math.min(k, heap.size()); i++) {
            topStocks.add(heap.get(i));
        }

        // Sort the top K using heap sort principle
        topStocks.sort(comparator.reversed());
        return topStocks;
    }

    // Heapify up (bubble up)
    private void heapifyUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (comparator.compare(heap.get(index), heap.get(parent)) > 0) {
                swap(index, parent);
                index = parent;
            } else {
                break;
            }
        }
    }

    // Heapify down (bubble down)
    private void heapifyDown(int index) {
        int size = heap.size();
        while (true) {
            int largest = index;
            int left = 2 * index + 1;
            int right = 2 * index + 2;

            if (left < size && comparator.compare(heap.get(left), heap.get(largest)) > 0) {
                largest = left;
            }

            if (right < size && comparator.compare(heap.get(right), heap.get(largest)) > 0) {
                largest = right;
            }

            if (largest != index) {
                swap(index, largest);
                index = largest;
            } else {
                break;
            }
        }
    }

    // Swap two elements
    private void swap(int i, int j) {
        Stock temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    // Update stock and re-heapify
    public void updateStock(Stock stock) {
        int index = heap.indexOf(stock);
        if (index != -1) {
            heap.set(index, stock);
            heapifyUp(index);
            heapifyDown(index);
        }
    }

    // Clear heap
    public void clear() {
        heap.clear();
    }

    public int size() {
        return heap.size();
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    public List<Stock> getAllStocks() {
        return new ArrayList<>(heap);
    }
}