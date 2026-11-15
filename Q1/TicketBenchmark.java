
public class TicketBenchmark {

    static volatile long resultCheck = 0;

    // Method 1: Using String
    static String usingString(int fields) {
        String ticket = "";

        for (int i = 1; i <= fields; i++) {
            ticket = ticket + "Field" + i + " ";
        }

        return ticket;
    }

    // Method 2: Using StringBuilder
    static String usingStringBuilder(int fields) {
        StringBuilder ticket = new StringBuilder();

        for (int i = 1; i <= fields; i++) {
            ticket.append("Field").append(i).append(" ");
        }

        return ticket.toString();
    }

    // Method 3: Using StringBuffer
    static String usingStringBuffer(int fields) {
        StringBuffer ticket = new StringBuffer();

        for (int i = 1; i <= fields; i++) {
            ticket.append("Field").append(i).append(" ");
        }

        return ticket.toString();
    }

    // Benchmark method
    static void benchmark(String method, int tickets,
                          int fields) {

        long totalTime = 0;

        for (int run = 1; run <= 5; run++) {

            long start = System.nanoTime();

            long checksum = 0;

            for (int i = 0; i < tickets; i++) {

                String ticket;

                if (method.equals("String")) {
                    ticket = usingString(fields);
                } else if (method.equals("StringBuilder")) {
                    ticket = usingStringBuilder(fields);
                } else {
                    ticket = usingStringBuffer(fields);
                }

                checksum += ticket.length();
            }

            long end = System.nanoTime();

            resultCheck = checksum;

            totalTime += (end - start);
        }

        double average = totalTime / 5.0;

        System.out.printf("%-15s %.2f ms%n",
                method, average / 1_000_000);
    }

    public static void main(String[] args) {

        int[][] workloads = {
            {1, 6},
            {10000, 50},
            {50000, 100}
        };

        String[] methods = {
            "String",
            "StringBuilder",
            "StringBuffer"
        };

        for (int[] workload : workloads) {

            int tickets = workload[0];
            int fields = workload[1];

            System.out.println("\nTickets: " + tickets);
            System.out.println("Fields per ticket: " + fields);

            for (String method : methods) {
                benchmark(method, tickets, fields);
            }
        }
    }
}

