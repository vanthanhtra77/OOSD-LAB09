# OOSD Lab 09: Mapping Associations to Code (Exercise 10-3)

[![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Eclipse IDE](https://img.shields.io/badge/Eclipse-IDE-2C2255?style=for-the-badge&logo=eclipse&logoColor=white)](https://www.eclipse.org/)
[![Status](https://img.shields.io/badge/Build-Passing-27AE60?style=for-the-badge)]()

**Subject:** Object-Oriented Software Design and Development (OOSD)  
**Textbook:** *Object-Oriented Software Engineering: Using UML, Patterns, and Java* (3rd Edition)  
**Authors:** Bernd Bruegge & Allen H. Dutoit (Chapter 10, page 433, Figure 10-30)  
**Student:** Văn Thanh Trà | **Student ID:** 4651050286 | **Class:** CNTT46F  

---

## 1. Problem Statement (Exercise 10-3)

> *"Apply the appropriate transformations described in Section 10.4.2 to the associations of Figure 10-30. Assume that all associations are bidirectional, but that the aggregation associations do not change after each object has been created. In other words, the creator of each class must be modified so that aggregations are initialized during the creation of each object. Write the source code needed to manage the associations, including class, field, and method declarations, method bodies, and visibility."*

### Context Diagram (Figure 10-30)

```
+------------+       1           *       +--------------+       1           *       +---------+
|   League   |<>-------------------------|  Tournament  |<>-------------------------|  Round  |
+------------+ (Aggregation, immutable)  +--------------+ (Aggregation, immutable)  +---------+
                                                *
                                                |
                                                | (Many-to-Many, dynamic)
                                                |
                                                *
                                         +--------------+
                                         |    Player    |
                                         +--------------+
```

---

## 2. Technical Transformation Rules (Section 10.4.2)

1. **Mandatory Constructor Initialization (Constructor Injection):**
   * Aggregation parts (`Tournament`, `Round`) require the parent whole (`League`, `Tournament`) in their constructors (`new Tournament(league)`, `new Round(tournament)`).
   * The constructor automatically registers itself with the parent (`league.addTournament(this);`, `tournament.addRound(this);`) ensuring instantaneous bidirectional synchronization.
2. **Immutability Enforcement:**
   * Parent reference fields are declared `private final`.
   * No `set...()` or `remove...()` methods are exposed for aggregation links.
   * Collections returned by `getTournaments()` and `getRounds()` are wrapped via `Collections.unmodifiableList(...)` to protect against external mutation.
3. **Infinite Recursion Prevention in Many-to-Many Associations:**
   * In the bidirectional many-to-many relationship between `Tournament` and `Player`, mutual calls check `!contains()` before delegating to the counterpart (preventing `StackOverflowError`).

---

## 3. Repository Structure

```text
OOSD-LAB09/
├── .classpath                   # Eclipse JRE container & source folder settings
├── .project                     # Eclipse project descriptor
├── .settings/
│   └── org.eclipse.jdt.core.prefs # Java 17 compiler compliance
├── Main (lab09).launch          # Eclipse Run launcher
├── run.bat                      # One-click Windows batch script to compile & run
├── src/                         # Production Java source code
│   ├── League.java              # Aggregate root for Tournaments
│   ├── Tournament.java          # Core entity managing Rounds & Players
│   ├── Round.java               # Immutable part of Tournament
│   ├── Player.java              # Dynamic participant in Tournaments
│   └── Main.java                # Comprehensive automated test driver
└── README.md
```

---

## 4. How to Run

### Option A: Using Eclipse IDE
1. Open Eclipse $\rightarrow$ **File** $\rightarrow$ **Open Projects from File System...**
2. Choose repository directory $\rightarrow$ **Finish**.
3. Right-click `src/Main.java` $\rightarrow$ **Run As** $\rightarrow$ **Java Application** (or select `Main (lab09)` from the Run menu).

### Option B: Using Command Line (Windows / Linux / macOS)
```bash
# Compile
javac -d bin src/*.java

# Run test driver
java -cp bin Main
```

Or on Windows, simply double-click `run.bat`.

---

## 5. Verification & Test Results

The test driver `Main.java` rigorously verifies all 4 invariant requirements:

| Test Case | Description | Verification Criterion | Status |
|:---:|---|---|:---:|
| **Test 1** | League $\diamondsuit$ Tournament (1-N) | 2-way link created in constructor; Rejects null parent with `IllegalArgumentException`. | **PASS** |
| **Test 2** | Tournament $\diamondsuit$ Round (1-N) | 2-way link created in constructor; Rejects null parent with `IllegalArgumentException`. | **PASS** |
| **Test 3** | Tournament $\leftrightarrow$ Player (*-*) | Bidirectional add/remove; Idempotency prevents duplicates; No stack overflow. | **PASS** |
| **Test 4** | Aggregation Immutability | Calling `clear()` / `remove()` on collection throws `UnsupportedOperationException`. | **PASS** |

### Execution Console Output

```text
=================================================================
VERIFICATION FOR EXERCISE 10-3 (FIGURE 10-30 & SECTION 10.4.2)
=================================================================

--- TEST 1: LEAGUE AND TOURNAMENT AGGREGATION (1 - *) ---
Created League: ARENA Premier League
Created 2 Tournaments associated with League in constructor.
Verifying bidirectional links:
 -> t1.getLeague(): ARENA Premier League
 -> t2.getLeague(): ARENA Premier League
 -> premierLeague.getTournaments():
    * Spring Championship
    * Summer Cup
 [PASS] Rejected null League: A Tournament must belong to a non-null League.

--- TEST 2: TOURNAMENT AND ROUND AGGREGATION (1 - *) ---
Created 3 Rounds associated with Tournament t1 (Spring Championship).
Verifying bidirectional links:
 -> r1.getTournament(): Spring Championship
 -> t1.getRounds():
    * Quarterfinal
    * Semifinal
    * Final
 [PASS] Rejected null Tournament: A Round must belong to a non-null Tournament.

--- TEST 3: MANY-TO-MANY ASSOCIATION (TOURNAMENT * --- * PLAYER) ---
1. Adding Players via Tournament (t1.addPlayer):
   -> t1.getPlayers():
      * Alice
      * Bob
   -> Synchronized opposite direction alice.getTournaments():
      * Spring Championship

2. Adding Tournaments via Player (bob.addTournament, charlie.addTournament):
   -> t2.getPlayers():
      * Bob
      * Charlie
   -> Synchronized opposite direction bob.getTournaments():
      * Spring Championship
      * Summer Cup

3. Testing idempotency (prevent duplicate links & recursion):
   Count of players in t1 after re-adding alice: 2 (Expected: 2)

4. Removing a Player (t1.removePlayer(bob)):
   -> t1.getPlayers() size after removing bob: 1
   -> Does bob still reference t1: false (Expected: false)

--- TEST 4: IMMUTABILITY OF AGGREGATION COLLECTIONS ---
 [PASS] League.getTournaments() cannot be modified directly (UnsupportedOperationException)
 [PASS] Tournament.getRounds() cannot be modified directly (UnsupportedOperationException)

=================================================================
ALL TESTS COMPLETED SUCCESSFULLY!
=================================================================
```
