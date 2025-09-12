// OrderedArray.java
// COSC 251 Assignment 1
// Implements a sorted array with basic operations + error handling

public class OrderedArray {
    private Integer[] arr;
    private int count; // number of valid elements

    // Constructor
    public OrderedArray(int size) {
        if (size <= 0) throw new IllegalArgumentException("Size must be positive.");
        arr = new Integer[size];
        count = 0;
    }

    // Insert (keeps array sorted): O(n)
    public void insert(int x) {
        if (count >= arr.length) resize(arr.length * 2); // expand automatically
        int i;
        for (i = count - 1; i >= 0 && arr[i] > x; i--) {
            arr[i + 1] = arr[i];
        }
        arr[i + 1] = x;
        count++;
    }

    // Delete: O(n)
    public boolean delete(int x) {
        int index = find(x);
        if (index == -1) return false; // not found
        for (int i = index; i < count - 1; i++) {
            arr[i] = arr[i + 1];
        }
        arr[count - 1] = null;
        count--;
        return true;
    }

    // Find (binary search): O(log n)
    public int find(int x) {
        if (count == 0) return -1; // handle empty array
        int left = 0, right = count - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (arr[mid] == x) return mid;
            if (arr[mid] < x) left = mid + 1;
            else right = mid - 1;
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
}
