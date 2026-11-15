
import java.util.*;

class MedicineRecord {

    String code;
    String name;
    int quantity;

    MedicineRecord(String code, String name, int quantity) {
        this.code = code;
        this.name = name;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return code + "-" + name + "-" + quantity;
    }
}

public class PharmacyRedesign {

    static volatile long result = 0;

    static final int REPORT_RUNS = 100;
    static final int MEASURE_RUNS = 5;

    // Generate medicine code
    static String getCode(int i) {
        return String.format("M%06d", i);
    }

    // Create HashMap inventory
    static Map<String, MedicineRecord> createHashMap(int n) {

        Map<String, MedicineRecord> map = new HashMap<>();

        for (int i = n; i >= 1; i--) {

            map.put(
                getCode(i),
                new MedicineRecord(
                    getCode(i),
                    "Medicine" + i,
                    100 + i
                )
            );
        }

        return map;
    }

    // Create TreeMap inventory
    static Map<String, MedicineRecord> createTreeMap(int n) {

        Map<String, MedicineRecord> map = new TreeMap<>();

        for (int i = n; i >= 1; i--) {

            map.put(
                getCode(i),
                new MedicineRecord(
                    getCode(i),
                    "Medicine" + i,
                    100 + i
                )
            );
        }

        return map;
    }

    // Original solution:
    // Sort HashMap keys for every report
    static long originalReport(
            Map<String, MedicineRecord> inventory) {

        List<String> codes =
            new ArrayList<>(inventory.keySet());

        Collections.sort(codes);

        long sum = 0;

        for (String code : codes) {
            sum += inventory.get(code).quantity;
        }

        return sum;
    }

    // Redesigned solution:
    // TreeMap already maintains sorted keys
    static long redesignedReport(
            Map<String, MedicineRecord> inventory) {

        long sum = 0;

        for (MedicineRecord medicine : inventory.values()) {
            sum += medicine.quantity;
        }

        return sum;
    }

    // Measure original report generation
    static double measureOriginal(
            Map<String, MedicineRecord> inventory) {

        return measure(() -> {

            for (int i = 0; i < REPORT_RUNS; i++) {
                result += originalReport(inventory);
            }
        });
    }

    // Measure redesigned report generation
    static double measureRedesigned(
            Map<String, MedicineRecord> inventory) {

        return measure(() -> {

            for (int i = 0; i < REPORT_RUNS; i++) {
                result += redesignedReport(inventory);
            }
        });
    }

    // Average execution time
    static double measure(Runnable task) {

        long total = 0;

        // Warm-up runs
        for (int i = 0; i < 2; i++) {
            task.run();
        }

        for (int i = 0; i < MEASURE_RUNS; i++) {

            long start = System.nanoTime();

            task.run();

            long end = System.nanoTime();

            total += end - start;
        }

        return total / (double) MEASURE_RUNS / 1_000_000;
    }

    public static void main(String[] args) {

        int[] sizes = {1000, 10000, 50000};

        System.out.println(
            "PHARMACY INVENTORY REDESIGN COMPARISON"
        );

        System.out.println(
            "Reports generated per measurement: "
            + REPORT_RUNS
        );

        for (int n : sizes) {

            System.out.println("\nInventory Size: " + n);

            // Prepare inventories outside measurement
            Map<String, MedicineRecord> original =
                createHashMap(n);

            Map<String, MedicineRecord> redesigned =
                createTreeMap(n);

            // Verify both contain identical information
            long originalResult = originalReport(original);

            long redesignedResult =
                redesignedReport(redesigned);

            System.out.println(
                "Original report correct: "
                + (originalResult == redesignedResult)
            );

            System.out.println(
                "Redesigned report correct: "
                + (originalResult == redesignedResult)
            );

            // Measure report generation
            double oldTime = measureOriginal(original);

            double newTime =
                measureRedesigned(redesigned);

            System.out.printf(
                "Original HashMap time: %.3f ms%n",
                oldTime
            );

            System.out.printf(
                "Redesigned TreeMap time: %.3f ms%n",
                newTime
            );

            if (newTime > 0) {

                double improvement =
                    ((oldTime - newTime) / oldTime) * 100;

                System.out.printf(
                    "Time difference: %.2f%%%n",
                    improvement
                );
            }

            System.out.println(
                "Original sorting operations: "
                + REPORT_RUNS
            );

            System.out.println(
                "Redesigned sorting operations: 0"
            );
        }

        System.out.println("\nEXPERIMENT COMPLETED");
    }
}
