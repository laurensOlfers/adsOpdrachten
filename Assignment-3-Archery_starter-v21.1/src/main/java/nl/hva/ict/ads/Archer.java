package nl.hva.ict.ads;

import java.util.ArrayList;

public class Archer {
    public static int MAX_ARROWS = 3;
    public static int MAX_ROUNDS = 10;


    private final int id;    // TODO Once assigned a value is not allowed to change.
    private String firstName;
    private String lastName;
    private ArrayList<Integer> scorePerRound = new ArrayList<Integer>();
    private ArrayList<Integer> rounds = new ArrayList<Integer>();
    private static int firstID = 135788;
    private int missingCount;


// TODO add instance variable(s) to track the scores per round per arrow

    /**
     * Constructs a new instance of Archer and assigns a unique id to the instance.
     * Each new instance should be assigned a number that is 1 higher than the last one assigned.
     * The first instance created should have ID 135788;
     *
     * @param firstName the archers first name.
     * @param lastName  the archers surname.
     */
    public Archer(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = firstID++;


        //  initialise the scores of the archer  ???
    }

    /**
     * Registers the points for each of the three arrows that have been shot during a round.
     *
     * @param round  the round for which to register the points. First round has number 1.
     * @param points the points shot during the round, one for each arrow.
     */
    public void registerScoreForRound(int round, int[] points) {
        int total = 0;
        int index = 0;

        if (!rounds.contains(round)) {
            rounds.add(round);
            for (int i = 0; i < points.length; i++) {
                if (points[i] == 0) {
                    this.missingCount += 1;
                }
                total += points[i];
            }
            scorePerRound.add(total);
        } else {
            index = rounds.indexOf(round);

            scorePerRound.remove(index);
            for (int i = 0; i < points.length; i++) {
                total += points[i];
                if (points[i] == 0) {
                    this.missingCount += 1;
                }
            }
            scorePerRound.add(index, total);
        }


    }


    // TODO register the points into the archer's data structure for scores.

    /**
     * Calculates/retrieves the total score of all arrows across all rounds
     *
     * @return
     */
    public int getTotalScore() {
        int total = 0;
        for (int s : scorePerRound) {
            total += s;
        }
        return total;
    }

    /**
     * compares the scores/id of this archer with the scores/id of the other archer according to
     * the scoring scheme: highest total points -> least misses -> earliest registration
     * The archer with the lowest id has registered first
     *
     * @param other the other archer to compare against
     * @return negative number, zero or positive number according to Comparator convention
     */
    public int compareByHighestTotalScoreWithLeastMissesAndLowestId(Archer other) {
        int sum = 0;


        sum = other.getTotalScore() - this.getTotalScore();

        if (sum == 0) {

            sum = this.getMissingCount() - other.getMissingCount();

            if (sum == 0) {
                sum = this.id - other.id;

            }
        }

        // TODO compares the scores/id of this archer with the other archer
        //  and return the result according to Comparator conventions

        return sum;
    }

    public int getId() {
        return id;
    }

    public int getMissingCount() {
        return missingCount;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public String toString() {
        return id + " (" + this.getTotalScore() + ")" + " " + this.firstName + " " + this.lastName;
    }

}
