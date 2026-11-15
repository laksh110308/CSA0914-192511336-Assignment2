
import java.util.*;

class Medicine {

    String code;
    String name;
    int quantity;

    Medicine(String code, String name, int quantity) {
        this.code = code;
        this.name = name;
        this.quantity = quantity;
    }

    public String toString() {
        return code + " - " + name + " - " + quantity;
    }
}

public class PharmacyInventory {

    static volatile long result = 0;

    static final int LOOKUPS = 100000;
    static final int RUNS = 5;

    // Create medicine code
    static String getCode(int i) {
        return String.format("M%06d", i);
    }

    // Create medicine details
    static Medicine createMedicine(int i) {
        return new Medicine(
            getCode(i),
            "Medicine" + i,
            100 + i
        );
    }

    // Measure average execution time
    static double measure(Runnable task) {

        long total = 0;

        for (int i = 0; i < RUNS; i++) {

            long start = System.nanoTime();

            task.run();

            long end = System.nanoTime();

            total += end - start;
        }

        return total / (double) RUNS / 1_000_000;
    }

    // Populate HashMap
    static Map<String, Medicine> createHashMap(int n) {

        Map<String, Medicine> map = new HashMap<>();

        for (int i = 1; i <= n; i++) {
            map.put(getCode(i), createMedicine(i));
        }

        return map;
    }

    // Populate TreeMap
    static Map<String, Medicine> createTreeMap(int n) {

        Map<String, Medicine> map = new TreeMap<>();

        for (int i = 1; i <= n; i++) {
            map.put(getCode(i), createMedicine(i));
        }

        return map;
    }

    // Populate Hashtable
    static Map<String, Medicine> createHashtable(int n) {

        Map<String, Medicine> map = new Hashtable<>();

        for (int i = 1; i <= n; i++) {
            map.put(getCode(i), createMedicine(i));
        }

        return map;
    }

    // Populate LinkedHashMap
    static Map<String, Medicine> createLinkedHashMap(int n) {

        Map<String, Medicine> map = new LinkedHashMap<>();

        for (int i = 1; i <= n; i++) {
            map.put(getCode(i), createMedicine(i));
        }

        return map;
    }

    // Perform 100,000 lookups
    static double lookupTime(
            Map<String, Medicine> map, int n) {

        return measure(() -> {

            long sum = 0;

            for (int i = 0; i < LOOKUPS; i++) {

                int id = (i % n) + 1;

                Medicine m = map.get(getCode(id));

                if (m != null) {
                    sum += m.quantity;
                }
            }

            result += sum;
        });
    }

    // Generate code-order report
    static double codeReportTime(
            Map<String, Medicine> map) {

        return measure(() -> {

            List<String> codes =
                new ArrayList<>(map.keySet());

            Collections.sort(codes);

            long count = 0;

            for (String code : codes) {
                count += map.get(code).quantity;
            }

            result += count;
        });
    }

    // Generate insertion-order report
    static double insertionReportTime(
            Map<String, Medicine> map) {

        return measure(() -> {

            long count = 0;

            for (Medicine m : map.values()) {
                count += m.quantity;
            }

            result += count;
        });
    }

    public static void main(String[] args) {

        int[] sizes = {1000, 10000, 50000};

        for (int n : sizes) {

            System.out.println("\nMedicines: " + n);

            Map<String, Medicine> hashMap =
                createHashMap(n);

            Map<String, Medicine> treeMap =
                createTreeMap(n);

            Map<String, Medicine> hashtable =
                createHashtable(n);

            Map<String, Medicine> linkedMap =
                createLinkedHashMap(n);

            System.out.println("\n100,000 Lookups (ms)");

            System.out.printf("HashMap: %.3f%n",
                lookupTime(hashMap, n));

            System.out.printf("TreeMap: %.3f%n",
                lookupTime(treeMap, n));

            System.out.printf("Hashtable: %.3f%n",
                lookupTime(hashtable, n));

            System.out.println("\nCode-order Report (ms)");

            System.out.printf("HashMap: %.3f%n",
                codeReportTime(hashMap));

            System.out.printf("TreeMap: %.3f%n",
                codeReportTime(treeMap));

            System.out.printf("Hashtable: %.3f%n",
                codeReportTime(hashtable));

            System.out.println("\nInsertion-order Report (ms)");

            System.out.printf("LinkedHashMap: %.3f%n",
                insertionReportTime(linkedMap));

            System.out.println("\nOrder Verification");

            System.out.println("HashMap size correct: "
                + (hashMap.size() == n));

            System.out.println("TreeMap sorted: "
                + isSorted(treeMap));

            System.out.println("LinkedHashMap insertion order: "
                + isInsertionOrder(linkedMap, n));
        }
    }

    // Verify TreeMap code order
    static boolean isSorted(Map<String, Medicine> map) {

        String previous = "";

        for (String code : map.keySet()) {

            if (code.compareTo(previous) < 0) {
                return false;
            }

            previous = code;
        }

        return true;
    }

    // Verify LinkedHashMap insertion order
    static boolean isInsertionOrder(
            Map<String, Medicine> map, int n) {

        int expected = 1;

        for (String code : map.keySet()) {

            if (!code.equals(getCode(expected))) {
                return false;
            }

            expected++;
        }

        return expected == n + 1;
    }
}

