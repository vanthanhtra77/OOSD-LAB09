import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Exercise 10-3: Class Tournament
 * Textbook: Object-Oriented Software Engineering: Using UML, Patterns, and Java
 * Authors: Bernd Bruegge & Allen H. Dutoit (Chapter 10, page 433, Figure 10-30)
 * 
 * Associations:
 * 1. League <> 1 --- * Tournament (Aggregation, 1-to-many):
 *    - League is the whole, Tournament is part.
 *    - Immutable after creation (final league reference, no setLeague method).
 * 2. Tournament <> 1 --- * Round (Aggregation, 1-to-many):
 *    - Tournament is the whole, Round is part.
 *    - Immutable after creation (no removeRound method).
 * 3. Tournament * --- * Player (Many-to-many association):
 *    - Mapped according to Section 10.4.2 (Figure 10-11).
 *    - Bidirectional with recursion prevention using contains() check.
 */
public class Tournament {
    private String name;
    
    // Aggregation 1-N with League (immutable after creation)
    private final League league;
    
    // Aggregation 1-N with Round (immutable after creation)
    private final List<Round> rounds;
    
    // Many-to-many association with Player (Section 10.4.2)
    private final List<Player> players;

    /**
     * Constructor requires League to initialize the aggregation during object creation.
     * Fulfills: "the creator of each class must be modified so that aggregations
     * are initialized during the creation of each object."
     */
    public Tournament(League league) {
        if (league == null) {
            throw new IllegalArgumentException("A Tournament must belong to a non-null League.");
        }
        this.league = league;
        this.league.addTournament(this); // Bidirectional initialization
        
        this.rounds = new ArrayList<>();
        this.players = new ArrayList<>();
    }

    public Tournament(String name, League league) {
        this(league);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the League this Tournament belongs to.
     * No setter is provided because aggregation cannot change after creation.
     */
    public League getLeague() {
        return league;
    }

    // =========================================================================
    // Aggregation management with Round (Tournament <> 1 --- * Round)
    // =========================================================================

    /**
     * Adds a Round to this Tournament.
     * Invoked by the Round constructor upon instantiation.
     */
    public void addRound(Round round) {
        if (round != null && !this.rounds.contains(round)) {
            this.rounds.add(round);
        }
    }

    /**
     * Returns an unmodifiable list of rounds.
     * No removeRound method is provided to ensure aggregation permanence.
     */
    public List<Round> getRounds() {
        return Collections.unmodifiableList(this.rounds);
    }

    // =========================================================================
    // Many-to-many association with Player (Tournament * --- * Player)
    // Section 10.4.2, Figure 10-11
    // =========================================================================

    /**
     * Adds a Player to this Tournament.
     * Checks !contains() before propagating to Player to prevent infinite recursion.
     */
    public void addPlayer(Player player) {
        if (player != null && !this.players.contains(player)) {
            this.players.add(player);
            player.addTournament(this); // Bidirectional synchronization
        }
    }

    /**
     * Removes a Player from this Tournament.
     * Checks contains() before propagating to Player to prevent infinite recursion.
     */
    public void removePlayer(Player player) {
        if (player != null && this.players.contains(player)) {
            this.players.remove(player);
            player.removeTournament(this); // Bidirectional synchronization
        }
    }

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(this.players);
    }

    @Override
    public String toString() {
        return "Tournament{" +
                "name='" + name + '\'' +
                ", league=" + (league != null ? league.getName() : "null") +
                ", roundCount=" + rounds.size() +
                ", playerCount=" + players.size() +
                '}';
    }
}
