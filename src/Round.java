/**
 * Exercise 10-3: Class Round
 * Textbook: Object-Oriented Software Engineering: Using UML, Patterns, and Java
 * Authors: Bernd Bruegge & Allen H. Dutoit (Chapter 10, page 433, Figure 10-30)
 * 
 * Associations:
 * - Tournament <> 1 --- * Round (Aggregation, 1-to-many)
 * - Constraint: Aggregation associations do not change after creation.
 *   The tournament field is final, required in constructor, and cannot be reassigned.
 */
public class Round {
    private String name;
    
    // Aggregation 1-N with Tournament (immutable after creation)
    private final Tournament tournament;

    /**
     * Constructor requires Tournament to initialize aggregation during object creation.
     * Fulfills: "the creator of each class must be modified so that aggregations
     * are initialized during the creation of each object."
     */
    public Round(Tournament tournament) {
        if (tournament == null) {
            throw new IllegalArgumentException("A Round must belong to a non-null Tournament.");
        }
        this.tournament = tournament;
        this.tournament.addRound(this); // Bidirectional initialization
    }

    public Round(String name, Tournament tournament) {
        this(tournament);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the Tournament this Round belongs to.
     * No setter is provided because aggregation cannot change after creation.
     */
    public Tournament getTournament() {
        return tournament;
    }

    @Override
    public String toString() {
        return "Round{" +
                "name='" + name + '\'' +
                ", tournament=" + (tournament != null ? tournament.getName() : "null") +
                '}';
    }
}
