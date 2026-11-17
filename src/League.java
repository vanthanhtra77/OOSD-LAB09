import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Exercise 10-3: Class League
 * Textbook: Object-Oriented Software Engineering: Using UML, Patterns, and Java
 * Authors: Bernd Bruegge & Allen H. Dutoit (Chapter 10, page 433, Figure 10-30)
 * 
 * Associations:
 * - League <> 1 --- * Tournament (Aggregation, 1-to-many)
 * - Constraint: Aggregation associations do not change after creation.
 *   (No removeTournament method or league reassignment).
 */
public class League {
    private String name;
    private final List<Tournament> tournaments;

    public League() {
        this.tournaments = new ArrayList<>();
    }

    public League(String name) {
        this();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Adds a Tournament to this League.
     * Invoked during Tournament creation to establish the bidirectional link.
     */
    public void addTournament(Tournament tournament) {
        if (tournament != null && !this.tournaments.contains(tournament)) {
            this.tournaments.add(tournament);
        }
    }

    /**
     * Returns an unmodifiable view of the tournaments list.
     * Prevents external code from modifying the aggregation association.
     */
    public List<Tournament> getTournaments() {
        return Collections.unmodifiableList(this.tournaments);
    }

    @Override
    public String toString() {
        return "League{" +
                "name='" + name + '\'' +
                ", tournamentCount=" + tournaments.size() +
                '}';
    }
}
