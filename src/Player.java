import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Exercise 10-3: Class Player
 * Textbook: Object-Oriented Software Engineering: Using UML, Patterns, and Java
 * Authors: Bernd Bruegge & Allen H. Dutoit (Chapter 10, page 433, Figure 10-30)
 * 
 * Associations:
 * - Tournament * --- * Player (Many-to-many association)
 * - Mapped according to Section 10.4.2 (Figure 10-11).
 * - Uses List collection with mutual cycle check via !contains().
 */
public class Player {
    private String name;
    
    // Many-to-many association with Tournament (Section 10.4.2)
    private final List<Tournament> tournaments;

    public Player() {
        this.tournaments = new ArrayList<>();
    }

    public Player(String name) {
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
     * Adds a Tournament to this Player's list.
     * Checks !contains() before calling t.addPlayer(this) to prevent infinite recursion.
     */
    public void addTournament(Tournament tournament) {
        if (tournament != null && !this.tournaments.contains(tournament)) {
            this.tournaments.add(tournament);
            tournament.addPlayer(this); // Bidirectional synchronization
        }
    }

    /**
     * Removes a Tournament from this Player's list.
     * Checks contains() before calling t.removePlayer(this) to prevent infinite recursion.
     */
    public void removeTournament(Tournament tournament) {
        if (tournament != null && this.tournaments.contains(tournament)) {
            this.tournaments.remove(tournament);
            tournament.removePlayer(this); // Bidirectional synchronization
        }
    }

    public List<Tournament> getTournaments() {
        return Collections.unmodifiableList(this.tournaments);
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", tournamentCount=" + tournaments.size() +
                '}';
    }
}
