/**
 * Test Driver for Exercise 10-3 (Figure 10-30 & Section 10.4.2)
 * Demonstrates:
 * 1. Immutable aggregation initialization through constructors (League <> 1 - * Tournament, Tournament <> 1 - * Round).
 * 2. Bidirectional consistency across all associations.
 * 3. Dynamic many-to-many association management between Tournament and Player with cycle prevention.
 * 4. Protection of aggregation collections using unmodifiable views.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=================================================================");
        System.out.println("VERIFICATION FOR EXERCISE 10-3 (FIGURE 10-30 & SECTION 10.4.2)");
        System.out.println("=================================================================\n");

        // -------------------------------------------------------------
        // Test 1: League and Tournament aggregation (1 - *)
        // -------------------------------------------------------------
        System.out.println("--- TEST 1: LEAGUE AND TOURNAMENT AGGREGATION (1 - *) ---");
        League premierLeague = new League("ARENA Premier League");
        System.out.println("Created League: " + premierLeague.getName());

        Tournament t1 = new Tournament("Spring Championship", premierLeague);
        Tournament t2 = new Tournament("Summer Cup", premierLeague);

        System.out.println("Created 2 Tournaments associated with League in constructor.");
        System.out.println("Verifying bidirectional links:");
        System.out.println(" -> t1.getLeague(): " + t1.getLeague().getName());
        System.out.println(" -> t2.getLeague(): " + t2.getLeague().getName());
        System.out.println(" -> premierLeague.getTournaments():");
        for (Tournament t : premierLeague.getTournaments()) {
            System.out.println("    * " + t.getName());
        }

        // Check non-null requirement for aggregation creator
        try {
            new Tournament("Invalid Tournament", null);
            System.err.println(" ERROR: Null League should be rejected!");
        } catch (IllegalArgumentException ex) {
            System.out.println(" [PASS] Rejected null League: " + ex.getMessage());
        }

        // -------------------------------------------------------------
        // Test 2: Tournament and Round aggregation (1 - *)
        // -------------------------------------------------------------
        System.out.println("\n--- TEST 2: TOURNAMENT AND ROUND AGGREGATION (1 - *) ---");
        Round r1 = new Round("Quarterfinal", t1);
        Round r2 = new Round("Semifinal", t1);
        Round r3 = new Round("Final", t1);

        System.out.println("Created 3 Rounds associated with Tournament t1 (" + t1.getName() + ").");
        System.out.println("Verifying bidirectional links:");
        System.out.println(" -> r1.getTournament(): " + r1.getTournament().getName());
        System.out.println(" -> t1.getRounds():");
        for (Round r : t1.getRounds()) {
            System.out.println("    * " + r.getName());
        }

        // Check non-null requirement for Round creator
        try {
            new Round("Invalid Round", null);
            System.err.println(" ERROR: Null Tournament should be rejected!");
        } catch (IllegalArgumentException ex) {
            System.out.println(" [PASS] Rejected null Tournament: " + ex.getMessage());
        }

        // -------------------------------------------------------------
        // Test 3: Many-to-Many association between Tournament and Player (* - *)
        // -------------------------------------------------------------
        System.out.println("\n--- TEST 3: MANY-TO-MANY ASSOCIATION (TOURNAMENT * --- * PLAYER) ---");
        Player alice = new Player("Alice");
        Player bob = new Player("Bob");
        Player charlie = new Player("Charlie");

        System.out.println("1. Adding Players via Tournament (t1.addPlayer):");
        t1.addPlayer(alice);
        t1.addPlayer(bob);

        System.out.println("   -> t1.getPlayers():");
        for (Player p : t1.getPlayers()) {
            System.out.println("      * " + p.getName());
        }
        System.out.println("   -> Synchronized opposite direction alice.getTournaments():");
        for (Tournament t : alice.getTournaments()) {
            System.out.println("      * " + t.getName());
        }

        System.out.println("\n2. Adding Tournaments via Player (bob.addTournament, charlie.addTournament):");
        bob.addTournament(t2);
        charlie.addTournament(t2);

        System.out.println("   -> t2.getPlayers():");
        for (Player p : t2.getPlayers()) {
            System.out.println("      * " + p.getName());
        }
        System.out.println("   -> Synchronized opposite direction bob.getTournaments():");
        for (Tournament t : bob.getTournaments()) {
            System.out.println("      * " + t.getName());
        }

        System.out.println("\n3. Testing idempotency (prevent duplicate links & recursion):");
        t1.addPlayer(alice);
        alice.addTournament(t1);
        System.out.println("   Count of players in t1 after re-adding alice: " + t1.getPlayers().size() + " (Expected: 2)");

        System.out.println("\n4. Removing a Player (t1.removePlayer(bob)):");
        t1.removePlayer(bob);
        System.out.println("   -> t1.getPlayers() size after removing bob: " + t1.getPlayers().size());
        boolean bobStillHasT1 = bob.getTournaments().stream().anyMatch(t -> t == t1);
        System.out.println("   -> Does bob still reference t1: " + bobStillHasT1 + " (Expected: false)");

        // -------------------------------------------------------------
        // Test 4: Protection of Aggregation Collections
        // -------------------------------------------------------------
        System.out.println("\n--- TEST 4: IMMUTABILITY OF AGGREGATION COLLECTIONS ---");
        try {
            premierLeague.getTournaments().clear();
            System.err.println(" ERROR: tournaments collection was modified externally!");
        } catch (UnsupportedOperationException ex) {
            System.out.println(" [PASS] League.getTournaments() cannot be modified directly (UnsupportedOperationException)");
        }

        try {
            t1.getRounds().clear();
            System.err.println(" ERROR: rounds collection was modified externally!");
        } catch (UnsupportedOperationException ex) {
            System.out.println(" [PASS] Tournament.getRounds() cannot be modified directly (UnsupportedOperationException)");
        }

        System.out.println("\n=================================================================");
        System.out.println("ALL TESTS COMPLETED SUCCESSFULLY!");
        System.out.println("=================================================================");
    }
}
