package com.stocksensei.datastructures;

public class MaxHeap <T extends Comparable<T>> {

    private Object[] heap;
    private int size;
    private static final int DEFAULT_CAPACITY = 16;

    public MaxHeap(){
        this.heap = new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    public MaxHeap(int initialCapacity){
        if(initialCapacity <= 0) initialCapacity = DEFAULT_CAPACITY;
        this.heap = new Object [initialCapacity];
        this.size = 0;
    }

    public MaxHeap(T[] items){
        int cap = Math.max(items.length, DEFAULT_CAPACITY);
        this.heap = new Object[cap];
        System.arraycopy(items,0,heap,0,items.length);
        this.size = items.length;
        for (int i = parentIndex(size -1); i >= 0; i--){
            siftDown(i);
        }
    }

    private int parentIndex(int i){
        return (i - 1) / 2;
    }

    @SuppressWarnings("unchecked")
    private void siftUp(int idx){
        int current = idx;
        T val = (T) heap[current];
        while(current > 0){
            int parent = parentIndex(current);
            T parentVal = (T) heap [parent];
            if(val.compareTo(parentVal) <= 0) break;
            heap[current] = parentVal;
            heap [parent] = val;
            current = parent;
        }
    }

    private int leftIndex(int i){
        return 2 * i + 1;
    }

    private int rightIndex(int i){
        return 2 * i + 2;
    }

    @SuppressWarnings("unchecked")
    private boolean siftDown(int idx){
        int current = idx;
        boolean moved = false;
        while(true) {
            int left = leftIndex(current);
            int right = rightIndex(current);
            int largest = current;

            if (left < size){
                T leftVal = (T) heap [left];
                if (leftVal.compareTo( (T) heap[largest]) > 0) largest = left;
            }

            if (right < size){
                T rightVal = (T) heap[right];
                if(rightVal.compareTo( (T) heap[largest]) > 0) largest = right;
            }

            if(largest == current) break;

            Object tmp = heap[current];
            heap[current] = heap[largest];
            heap[largest] = tmp;
            current = largest;
            moved = true;
        }
        return moved;
    }

    private void ensureCapacity(int capacity){
        if (capacity <= heap.length) return;
        int newCap = heap.length + (heap.length >> 1);
        if (newCap < capacity) newCap = capacity;
        Object[] newHeap = new Object[newCap];
        System.arraycopy(heap, 0, newHeap, 0, size);
        heap = newHeap;
    }

    public void insert(T value){
        ensureCapacity(size + 1);
        heap[size] = value;
        siftUp(size);
        size++;
    }

    @SuppressWarnings("unchecked")
    public Object[] toArray(){
        Object[] arr = new Object[size];
        for(int i = 0; i < size; i++) arr[i] = (T) heap[i];
        return arr;
    }

    @SuppressWarnings("unchecked")
    public T peek(){
        return size == 0 ? null : (T) heap[0];
    }

    @SuppressWarnings("unchecked")
    public T poll(){
        if (size == 0) return null;
        T max = (T) heap[0];
        heap[0] = heap[size - 1];
        heap[size -1] = null;
        size--;
        if (size > 0) siftDown(0);
        return max;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    public static void main(String[] args) {
    MaxHeap<Integer> h = new MaxHeap<>();

        System.out.println("Inserting: 5, 3, 8, 1, 2");
        h.insert(5);
        h.insert(3);
        h.insert(8);
        h.insert(1);
        h.insert(2);

        System.out.println("Heap elements in array order: ");
        for(Object o : h.toArray()) {
            Integer v = (Integer) o;
            System.out.print(v + " ");
        }
        System.out.println("\nSize: " + h.size);

        System.out.println("Peek (max): " + h.peek());
        System.out.println("Poll Sequence:");
//        while(!h.isEmpty()){
//            System.out.print(h.poll() + " ");
//        }
        System.out.println();

        Integer[] arr = {3, 1, 6, 5, 2, 4};
        MaxHeap<Integer> h2 = new MaxHeap<>(arr);
        System.out.println("Heap elements in array order: ");
        for(Object o : h2.toArray()) {
            Integer v = (Integer) o;
            System.out.print(v + " ");
        }
        System.out.println("Built max-heap from array {10,4,7,1,9,2} -> poll all:");
        while (!h2.isEmpty()) System.out.print(h2.poll() + " ");
        System.out.println();
    }
}
