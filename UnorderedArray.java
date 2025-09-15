// UnorderedArray.java
// COSC 251 Assignment 1
// Implements an unsorted array with basic operations

public class UnorderedArray {
    private Integer[] arr;
    private int count; // number of valid elements

    // Constructor
    public UnorderedArray(int size) {
        if (size <= 0) throw new IllegalArgumentException("Size must be positive.");
        arr = new Integer[size];
        count = 0;
    }

    // Insert: O(1)
    public void insert(int x) {
        if (count >= arr.length) resize(arr.length * 2); // expand if full
        arr[count++] = x;
    }

    // Delete: O(n)
    public boolean delete(int x) {
        int index = find(x);
        if (index == -1) return false;
        arr[index] = arr[count - 1]; // replace with last element
        arr[count - 1] = null; 
        count--;
        return true;
    }

    // Find: O(n)
    public int find(int x) {
        for (int i = 0; i < count; i++) {
            if (arr[i] != null && arr[i] == x) return i;
        }
        return -1;
    }

    // Get: O(1)
    public int get(int index) {
        if (index < 0 || index >= count) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
        return arr[index];
    }

    // Size: O(1)
    public int size() {
        return count;
    }

    // Resize: O(n)
    public void resize(int newSize) {
        if (newSize <= 0) throw new IllegalArgumentException("New size must be positive.");
        Integer[] newArr = new Integer[newSize];
        int elementsToCopy = Math.min(count, newSize);
        for (int i = 0; i < elementsToCopy; i++) {
            newArr[i] = arr[i];
        }
        arr = newArr;
        count = elementsToCopy;
    }

    // Optional Method: Display O(n)
    public void display() {
        if (count == 0) {
            System.out.println("[]");       // prints empty brackets
            return;
        }
        System.out.print("[");
        for (int i = 0; i < count; i++) {
            System.out.print(arr[i]);
            if (i < count - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }


}
