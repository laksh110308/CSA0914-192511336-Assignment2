
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class Team {

    final String name;
    final int points;
    final int goalDiff;

    Team(String name, int points, int goalDiff) {
        this.name = name;
        this.points = points;
        this.goalDiff = goalDiff;
    }

    @Override
    public String toString() {
        return name + "(" + points + ", " + goalDiff + ")";
    }
}

class Leaderboard<T> {

    private final List<T> items = new ArrayList<>();

    private final Comparator<? super T> order;

    Leaderboard(Comparator<? super T> order) {
        this.order = order;
    }

    void add(T item) {
        items.add(item);
    }

    // TODO 1: Return a sorted copy without modifying original list
    List<T> ranking() {

        List<T> copy = new ArrayList<>(items);

        copy.sort(order);

        return copy;
    }

    // Wildcard example
    void addAll(List<? extends T> newItems) {
        items.addAll(newItems);
    }

    int size() {
        return items.size();
    }
}

public class TeamLeaderboard {

    public static void main(String[] args) {

        // Ranking: Points descending,
        // Goal difference descending,
        // Name ascending

        Comparator<Team> teamOrder =
            Comparator.comparingInt((Team t) -> t.points)
                .reversed()
                .thenComparing(
                    Comparator.comparingInt(
                        (Team t) -> t.goalDiff
                    ).reversed()
                )
                .thenComparing(t -> t.name);

        Leaderboard<Team> board =
            new Leaderboard<>(teamOrder);

        // First dataset with several ties
        board.add(new Team("Alpha", 100, 20));
        board.add(new Team("Beta", 100, 15));
        board.add(new Team("Delta", 100, 20));
        board.add(new Team("Gamma", 90, 25));
        board.add(new Team("Epsilon", 90, 25));
        board.add(new Team("Zeta", 90, 10));

        System.out.println("DATASET 1 - CORRECT RANKING");

        List<Team> ranking = board.ranking();

        for (int i = 0; i < ranking.size(); i++) {
            System.out.println(
                (i + 1) + ". " + ranking.get(i)
            );
        }

        // Check original list is not modified
        System.out.println("\nOriginal list size: "
                + board.size());

        // Second dataset to test faulty comparator
        System.out.println("\nDATASET 2 - FAULTY COMPARATOR");

        Comparator<Team> faultyOrder =
            (a, b) -> b.points - a.points;

        List<Team> testTeams = new ArrayList<>();

        testTeams.add(new Team("Team A", 100, 10));
        testTeams.add(new Team("Team B", 100, 30));
        testTeams.add(new Team("Team C", 100, 20));

        testTeams.sort(faultyOrder);

        System.out.println("Faulty ranking:");

        for (Team t : testTeams) {
            System.out.println(t);
        }

        System.out.println(
            "\nFault: Goal difference and name "
            + "are ignored."
        );

        // Correct comparator test
        System.out.println("\nCORRECTED RANKING");

        testTeams.sort(teamOrder);

        for (Team t : testTeams) {
            System.out.println(t);
        }

        // Test comparator with extreme integer values
        System.out.println("\nINTEGER OVERFLOW TEST");

        Team high = new Team("High", Integer.MAX_VALUE, 0);
        Team low = new Team("Low", Integer.MIN_VALUE, 0);

        Comparator<Team> unsafe =
            (a, b) -> b.points - a.points;

        System.out.println(
            "Unsafe comparison: " + unsafe.compare(high, low)
        );

        System.out.println(
            "Safe comparison: "
            + Integer.compare(low.points, high.points)
        );
    }
}

