public class UnorderedArray {
    private Integer[] arr;
    private int size;

    // initialize the array
    public UnorderedArray(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than zero");
        }
        arr = new Integer[capacity];
        size = 0;
    }

    // Inserts an integer into the array (O(1) on average)
    public void insert(int x) {
        // Check if the array is full, double its size if true
        if (size == arr.length) {
            resize(arr.length * 2);
        }
        arr[size++] = x; // Insert at the current spot then increment the size
    }
    // Deletes an integer from the array if it exists and returns true, otherwise returns false (O(n) complexity)
    public boolean delete(int x) {
        for (int i = 0; i < size; i++) {
            if (arr[i] == x) {
                arr[i] = arr[--size]; // Swap the deleted element with the last element
                arr[size] = null; // Clear the deleted element's reference
                return true;
            }
        }
        return false;
    }

    // Searches for an integer in the array and returns its index, or -1 if it does not exist (O(n) complexity)
    public int find(int x) {
        for (int i = 0; i < size; i++) {
            if (arr[i] == x) {
                return i;
            }
        }
        return -1;
    }
    
    // Returns the integer at the given index (O(1) complexity)
    public int get(int index) {
        if (index >= 0 && index < size) {
            return arr[index];
        }
        throw new IndexOutOfBoundsException("Index out of bounds");
    }

    // Returns the current size of the array (O(1) complexity)
    public int size() {
        return size;
    }

    // Resizes the array to the given new size (O(n) complexity)
    public void resize(int newSize) {
        if (newSize <= 0) {
            throw new IllegalArgumentException("New size must be greater than zero");
        }
        // Create a new array with the new size
        Integer[] newArr = new Integer[newSize];
        // Copy elements to the new array
        System.arraycopy(arr, 0, newArr, 0, Math.min(size, newSize));
        arr = newArr;
        // Adjust the size to the new size if the new size is smaller than the current size
        if (newSize < size) {
            size = newSize;
        }
    }
}