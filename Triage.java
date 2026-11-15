
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.List;

class Patient {

    final String name;
    final int severity;
    final long arrival;

    Patient(String name, int severity, long arrival) {
        this.name = name;
        this.severity = severity;
        this.arrival = arrival;
    }

    @Override
    public String toString() {
        return name + "(S" + severity + ")";
    }
}

public class Triage {

    private long counter = 0;

    // TODO 1: PriorityQueue declaration
    private final PriorityQueue<Patient> queue =
        new PriorityQueue<>(
            Comparator.comparingInt((Patient p) -> p.severity)
                .thenComparingLong(p -> p.arrival)
        );

    // Admit a patient
    void admit(String name, int severity) {

        if (severity < 1 || severity > 5) {
            throw new IllegalArgumentException(
                "Severity must be between 1 and 5"
            );
        }

        queue.add(new Patient(name, severity, counter++));
    }

    // Serve the next patient
    Patient next() {
        return queue.poll();
    }

    // Display treatment order using a copied queue
    void displayTreatmentOrder() {

        PriorityQueue<Patient> copy =
            new PriorityQueue<>(queue);

        while (!copy.isEmpty()) {
            System.out.println(copy.poll());
        }
    }

    // Benchmark adding patients
    static double benchmarkAdd(int n) {

        return measure(() -> {

            PriorityQueue<Patient> pq =
                new PriorityQueue<>(
                    Comparator.comparingInt(
                        (Patient p) -> p.severity
                    ).thenComparingLong(p -> p.arrival)
                );

            for (int i = 0; i < n; i++) {

                pq.add(new Patient(
                    "Patient" + i,
                    (i % 5) + 1,
                    i
                ));
            }

            if (pq.size() != n) {
                throw new IllegalStateException();
            }
        });
    }

    // Benchmark removing patients
    static double benchmarkRemove(int n) {

        return measure(() -> {

            PriorityQueue<Patient> pq =
                new PriorityQueue<>(
                    Comparator.comparingInt(
                        (Patient p) -> p.severity
                    ).thenComparingLong(p -> p.arrival)
                );

            for (int i = 0; i < n; i++) {

                pq.add(new Patient(
                    "Patient" + i,
                    (i % 5) + 1,
                    i
                ));
            }

            long sum = 0;

            while (!pq.isEmpty()) {
                Patient p = pq.poll();
                sum += p.arrival;
            }

            if (sum < 0) {
                throw new IllegalStateException();
            }
        });
    }

    static double measure(Runnable task) {

        int runs = 5;
        long total = 0;

        for (int i = 0; i < runs; i++) {

            long start = System.nanoTime();

            task.run();

            long end = System.nanoTime();

            total += end - start;
        }

        return total / (double) runs / 1_000_000;
    }

    public static void main(String[] args) {

        Triage hospital = new Triage();

        // Eight patients
        hospital.admit("Arun", 3);
        hospital.admit("Bala", 1);
        hospital.admit("Chitra", 2);
        hospital.admit("Divya", 1);
        hospital.admit("Ezhil", 4);
        hospital.admit("Farah", 2);
        hospital.admit("Gokul", 5);
        hospital.admit("Hari", 3);

        System.out.println("HOSPITAL EMERGENCY TRIAGE");

        System.out.println("\nTreatment Order:");

        hospital.displayTreatmentOrder();

        // Show why direct PriorityQueue printing is not sorted
        Triage test = new Triage();

        test.admit("Patient A", 5);
        test.admit("Patient B", 1);
        test.admit("Patient C", 3);
        test.admit("Patient D", 2);

        System.out.println("\nDirect PriorityQueue display:");
        System.out.println(test.queue);

        System.out.println("\nActual treatment order:");
        test.displayTreatmentOrder();

        // Growth investigation
        int[] sizes = {1000, 10000, 50000};

        System.out.println("\nGROWTH INVESTIGATION");
        System.out.printf(
            "%-15s %-20s %-20s%n",
            "Queue Size", "Add Time (ms)",
            "Remove Time (ms)"
        );

        for (int n : sizes) {

            double addTime = benchmarkAdd(n);
            double removeTime = benchmarkRemove(n);

            System.out.printf(
                "%-15d %-20.3f %-20.3f%n",
                n, addTime, removeTime
            );
        }

        System.out.println("\nFAIRNESS POLICY");
        System.out.println(
            "Increase priority for patients who wait too long."
        );

        System.out.println(
            "Rebuild the priority queue whenever aging is updated."
        );
    }
}
