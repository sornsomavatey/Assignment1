// TestArrays.java
// Comprehensive tests for UnorderedArray and OrderedArray (no submission)

interface Thunk { void run(); }

public class TestArrays {
    private static int tests = 0, passed = 0;

    public static void main(String[] args) {
        // ---- UnorderedArray tests ----
        section("UnorderedArray: constructor & basic insert/get/size");
        {
            UnorderedArray ua = new UnorderedArray(3);
            ua.insert(10);
            ua.insert(20);
            ua.insert(30);
            assertEq(ua.size(), 3, "size after 3 inserts");
            assertEq(ua.get(0), 10, "get(0)");
            assertEq(ua.get(1), 20, "get(1)");
            assertEq(ua.get(2), 30, "get(2)");
            printUA("UA after 3 inserts", ua);
        }

        section("UnorderedArray: find & delete (including duplicate handling)");
        {
            UnorderedArray ua = new UnorderedArray(5);
            ua.insert(5); ua.insert(7); ua.insert(7); ua.insert(9);
            int idx7 = ua.find(7);
            assertTrue(idx7 != -1, "find(7) exists");
            boolean del7 = ua.delete(7);
            assertTrue(del7, "delete(7) returns true");
            // ensure exactly one 7 remains
            int c7 = countValue(ua, 7);
            assertEq(c7, 1, "exactly one 7 remains after deleting one duplicate");
            printUA("UA after deleting one 7", ua);
            // delete non-existent
            assertFalse(ua.delete(42), "delete(42) (not present) returns false");
        }

        section("UnorderedArray: auto-resize beyond initial capacity");
        {
            UnorderedArray ua = new UnorderedArray(2);
            ua.insert(1); ua.insert(2); ua.insert(3); ua.insert(4); // should grow
            assertEq(ua.size(), 4, "size reflects 4 inserts with growth");
            printUA("UA after growth", ua);
        }

        section("UnorderedArray: get() bounds & constructor/resize validation");
        {
            // constructor invalid
            expectThrow(() -> new UnorderedArray(0), IllegalArgumentException.class, "ctor size=0 throws");
            // bounds
            UnorderedArray ua = new UnorderedArray(2);
            ua.insert(11);
            expectThrow(() -> ua.get(-1), IndexOutOfBoundsException.class, "get(-1) throws");
            expectThrow(() -> ua.get(1), IndexOutOfBoundsException.class, "get(1) out of current size throws");
            // resize invalid
            expectThrow(() -> { ua.resize(0); }, IllegalArgumentException.class, "resize(0) throws");
        }

        section("UnorderedArray: resize larger retains elements; resize smaller truncates");
        {
            UnorderedArray ua = new UnorderedArray(3);
            ua.insert(1); ua.insert(2); ua.insert(3);
            ua.resize(6);
            assertEq(ua.size(), 3, "resize larger keeps size");
            printUA("UA after resize to 6", ua);
            ua.resize(2); // truncate
            assertEq(ua.size(), 2, "resize smaller truncates size");
            assertEq(ua.get(0), 1, "after truncate, get(0)=1");
            assertEq(ua.get(1), 2, "after truncate, get(1)=2");
            printUA("UA after resize to 2 (truncated)", ua);
        }

        section("UnorderedArray: negatives and repeated values");
        {
            UnorderedArray ua = new UnorderedArray(4);
            ua.insert(-5); ua.insert(-5); ua.insert(0); ua.insert(5);
            assertEq(countValue(ua, -5), 2, "two occurrences of -5");
            assertTrue(ua.delete(-5), "delete one -5");
            assertEq(countValue(ua, -5), 1, "one -5 remains");
            printUA("UA with negatives", ua);
        }

        // ---- OrderedArray tests ----
        section("OrderedArray: constructor & sorted inserts");
        {
            OrderedArray oa = new OrderedArray(4);
            oa.insert(20); oa.insert(10); oa.insert(30); oa.insert(15);
            // must be sorted: [10, 15, 20, 30]
            assertEq(oa.size(), 4, "size after 4 inserts");
            assertEq(oa.get(0), 10, "sorted[0]=10");
            assertEq(oa.get(1), 15, "sorted[1]=15");
            assertEq(oa.get(2), 20, "sorted[2]=20");
            assertEq(oa.get(3), 30, "sorted[3]=30");
            printOA("OA after unsorted inserts (should be sorted)", oa);
        }

        section("OrderedArray: auto-resize, find (binary search), delete");
        {
            OrderedArray oa = new OrderedArray(2);
            oa.insert(5); oa.insert(1); oa.insert(3); oa.insert(2); // grows and keeps sorted: [1,2,3,5]
            assertEq(oa.size(), 4, "size after growth");
            assertEq(oa.get(0), 1, "sorted prefix check");
            assertEq(oa.get(1), 2, "sorted prefix check");
            assertEq(oa.get(2), 3, "sorted prefix check");
            assertEq(oa.get(3), 5, "sorted prefix check");
            assertEq(oa.find(3), 2, "find(3) at index 2");
            assertEq(oa.find(42), -1, "find(42) not found");
            assertTrue(oa.delete(2), "delete existing 2");
            assertFalse(oa.delete(2), "delete(2) again returns false");
            printOA("OA after delete(2)", oa);
        }

        section("OrderedArray: duplicates stay adjacent; find returns any valid index");
        {
            OrderedArray oa = new OrderedArray(10);
            oa.insert(7); oa.insert(7); oa.insert(7); oa.insert(5); oa.insert(9);
            // sorted should be [5,7,7,7,9]
            int idx = oa.find(7);
            assertTrue(idx >= 1 && idx <= 3, "find(7) returns an index among duplicates");
            // verify the value at reported index is 7
            assertEq(oa.get(idx), 7, "value at find(7) is 7");
            printOA("OA with duplicates", oa);
        }

        section("OrderedArray: bounds, constructor/resize validation, truncation keeps order");
        {
            expectThrow(() -> new OrderedArray(0), IllegalArgumentException.class, "OA ctor size=0 throws");
            OrderedArray oa = new OrderedArray(5);
            oa.insert(1); oa.insert(4); oa.insert(2); oa.insert(3); // [1,2,3,4]
            expectThrow(() -> oa.get(-1), IndexOutOfBoundsException.class, "OA get(-1) throws");
            expectThrow(() -> oa.get(oa.size()), IndexOutOfBoundsException.class, "OA get(size) throws");
            expectThrow(() -> { oa.resize(0); }, IllegalArgumentException.class, "OA resize(0) throws");
            oa.resize(3); // should truncate to [1,2,3]
            assertEq(oa.size(), 3, "OA size after truncate");
            assertEq(oa.get(0), 1, "OA[0]=1 after truncate");
            assertEq(oa.get(1), 2, "OA[1]=2 after truncate");
            assertEq(oa.get(2), 3, "OA[2]=3 after truncate");
            printOA("OA after resize to 3 (truncated)", oa);
        }

        // ---- Summary ----
        System.out.println();
        System.out.printf("=== TEST SUMMARY: %d/%d passed ===%n", passed, tests);
    }

    // ---------- tiny test harness ----------
    private static void section(String title) {
        System.out.println();
        System.out.println("---- " + title + " ----");
    }

    private static void assertEq(int actual, int expected, String msg) {
        tests++;
        if (actual == expected) { passed++; ok(msg); }
        else fail(msg + " (expected=" + expected + ", actual=" + actual + ")");
    }

    private static void assertEq(Integer actual, Integer expected, String msg) {
        tests++;
        if ((actual == null && expected == null) || (actual != null && actual.equals(expected))) { passed++; ok(msg); }
        else fail(msg + " (expected=" + expected + ", actual=" + actual + ")");
    }

    private static void assertTrue(boolean cond, String msg) {
        tests++;
        if (cond) { passed++; ok(msg); }
        else fail(msg + " (expected true)");
    }

    private static void assertFalse(boolean cond, String msg) {
        tests++;
        if (!cond) { passed++; ok(msg); }
        else fail(msg + " (expected false)");
    }

    private static void expectThrow(Thunk t, Class<? extends Throwable> ex, String msg) {
        tests++;
        try {
            t.run();
            fail(msg + " (no exception thrown)");
        } catch (Throwable e) {
            if (ex.isInstance(e)) { passed++; ok(msg + " (threw " + e.getClass().getSimpleName() + ")"); }
            else fail(msg + " (threw " + e.getClass().getSimpleName() + ", expected " + ex.getSimpleName() + ")");
        }
    }

    private static void ok(String msg)   { System.out.println("  [PASS] " + msg); }
    private static void fail(String msg) { System.out.println("  [FAIL] " + msg); }

    // ---------- helpers ----------
    private static int countValue(UnorderedArray ua, int value) {
        int c = 0;
        for (int i = 0; i < ua.size(); i++) if (ua.get(i) == value) c++;
        return c;
    }

    private static void printUA(String label, UnorderedArray ua) {
        StringBuilder sb = new StringBuilder();
        sb.append(label).append(": [");
        for (int i = 0; i < ua.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(ua.get(i));
        }
        sb.append("]");
        System.out.println("  " + sb.toString());
    }

    private static void printOA(String label, OrderedArray oa) {
        StringBuilder sb = new StringBuilder();
        sb.append(label).append(": [");
        for (int i = 0; i < oa.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(oa.get(i));
        }
        sb.append("]");
        System.out.println("  " + sb.toString());
    }
}
