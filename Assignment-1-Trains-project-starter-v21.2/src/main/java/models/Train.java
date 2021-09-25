/*
* Voor Thomas*/
package models;

public class Train {
    private String origin;
    private String destination;
    private Locomotive engine;
    private Wagon firstWagon;

    /* Representation invariants:
        firstWagon == null || firstWagon.previousWagon == null
        engine != null
     */

    public Train(Locomotive engine, String origin, String destination) {
        this.engine = engine;
        this.destination = destination;
        this.origin = origin;
    }

    /* three helper methods that are usefull in other methods */
    public boolean hasWagons() {
        return firstWagon != null;
    }

    public boolean isPassengerTrain() {
        return this.firstWagon instanceof PassengerWagon;
    }

    public boolean isFreightTrain() {
        return this.firstWagon instanceof FreightWagon;
    }

    public Locomotive getEngine() {
        return engine;
    }

    public Wagon getFirstWagon() {
        return firstWagon;
    }

    /**
     * Replaces the current sequence of wagons (if any) in the train
     * by the given new sequence of wagons (if any)
     * (sustaining all representation invariants)
     *
     * @param wagon the first wagon of a sequence of wagons to be attached
     */
    public void setFirstWagon(Wagon wagon) {
        this.firstWagon = wagon;
    }

    /**
     * @return the number of Wagons connected to the train
     */
    public int getNumberOfWagons() {
        int numberOfWagons = 1;
        Wagon wagonCheckHasNext = this.firstWagon;
        while (wagonCheckHasNext.hasNextWagon() == true) {
            wagonCheckHasNext = wagonCheckHasNext.getNextWagon();
            numberOfWagons++;
        }
        return numberOfWagons;
    }

    /**
     * @return the last wagon attached to the train
     */
    public Wagon getLastWagonAttached() {
        Wagon lastWagon = this.firstWagon;
        while (lastWagon.hasNextWagon() == true) {
            lastWagon = lastWagon.getNextWagon();
        }
        return lastWagon;
    }

    /**
     * @return the total number of seats on a passenger train
     * (return 0 for a freight train)
     */
    public int getTotalNumberOfSeats() {
        int numberOfSeats = 0;
        Wagon wagonCheckHasNext = this.firstWagon;
        System.out.println(wagonCheckHasNext);
        if (isFreightTrain()) {
            return 0;
        }
        while (wagonCheckHasNext.hasNextWagon() == true) {
            numberOfSeats = numberOfSeats + ((PassengerWagon) wagonCheckHasNext).getNumberOfSeats();
            wagonCheckHasNext = wagonCheckHasNext.getNextWagon();
        }
        return numberOfSeats;
    }

    /**
     * calculates the total maximum weight of a freight train
     *
     * @return the total maximum weight of a freight train
     * (return 0 for a passenger train)
     */
    public int getTotalMaxWeight() {
        int TotalMaxWeight = 0;
        Wagon wagonCheckHasNext = this.firstWagon;
        if (isPassengerTrain()) {
            return 0;
        }
        while (wagonCheckHasNext.hasNextWagon() == true) {
            TotalMaxWeight = TotalMaxWeight + ((FreightWagon) wagonCheckHasNext).getMaxWeight();
            wagonCheckHasNext = wagonCheckHasNext.getNextWagon();
        }
        return TotalMaxWeight;
    }

    /**
     * Finds the wagon at the given position (starting at 1 for the first wagon of the train)
     *
     * @param position
     * @return the wagon found at the given position
     * (return null if the position is not valid for this train)
     */
    public Wagon findWagonAtPosition(int position) {
        int numberNumberofWagons = 1;
        Wagon wagonAtPosition = this.firstWagon;
        while (wagonAtPosition.hasNextWagon() == true) {
            wagonAtPosition = wagonAtPosition.getNextWagon();
            numberNumberofWagons++;
            if (numberNumberofWagons == position) {
                return wagonAtPosition;
            }
        }
        return null;
    }

    /**
     * Finds the wagon with a given wagonId
     *
     * @param wagonId
     * @return the wagon found
     * (return null if no wagon was found with the given wagonId)
     */
    public Wagon findWagonById(int wagonId) {
        Wagon wagonById = firstWagon;
        while (wagonById.hasNextWagon() == true) {
            wagonById = wagonById.getNextWagon();
            if (wagonById.getId() == wagonId) {
                return wagonById;
            }
        }
        return null;
    }

    /**
     * Determines if the given sequence of wagons can be attached to the train
     * Verfies of the type of wagons match the type of train (Passenger or Freight)
     * Verfies that the capacity of the engine is sufficient to pull the additional wagons
     *
     * @param wagon the first wagon of a sequence of wagons to be attached
     * @return
     */
    public boolean canAttach(Wagon wagon) {
        if (getNumberOfWagons() >= this.engine.getMaxWagons()) {
            return false;
        }
        if (this.firstWagon == null) {
            System.out.println(this.firstWagon.getClass());
            return true;
        }
        if ((isFreightTrain() && wagon instanceof FreightWagon) ||
                (isPassengerTrain() && wagon instanceof PassengerWagon)) {
            return true;
        }
        return false;
    }

    /**
     * Tries to attach the given sequence of wagons to the rear of the train
     * No change is made if the attachment cannot be made.
     * (when the sequence is not compatible or the engine has insufficient capacity)
     * if attachment is possible, the head wagon is first detached from its predecessors
     *
     * @param wagon the first wagon of a sequence of wagons to be attached
     * @return whether the attachment could be completed successfully
     */
    public boolean attachToRear(Wagon wagon) {
        if (canAttach(wagon) == false) {
            return false;
            //Ben nu hier mee bezig
        }
        return true;
    }


    /**
     * Tries to insert the given sequence of wagons at the front of the train
     * No change is made if the insertion cannot be made.
     * (when the sequence is not compatible or the engine has insufficient capacity)
     * if insertion is possible, the head wagon is first detached from its predecessors
     *
     * @param wagon the first wagon of a sequence of wagons to be attached
     * @return whether the insertion could be completed successfully
     */
    public boolean insertAtFront(Wagon wagon) {
        // TODO

        return false;
    }

    /**
     * Tries to insert the given sequence of wagons at/before the given wagon position in the train
     * No change is made if the insertion cannot be made.
     * (when the sequence is not compatible or the engine has insufficient capacity
     * or the given position is not valid in this train)
     * if insertion is possible, the head wagon is first detached from its predecessors
     *
     * @param wagon the first wagon of a sequence of wagons to be attached
     * @return whether the insertion could be completed successfully
     */
    public boolean insertAtPosition(int position, Wagon wagon) {
        // TODO

        return false;
    }

    /**
     * Tries to remove one Wagon with the given wagonId from this train
     * and attach it at the rear of the given toTrain
     * No change is made if the removal or attachment cannot be made
     * (when the wagon cannot be found, or the trains are not compatible
     * or the engine of toTrain has insufficient capacity)
     *
     * @param wagonId
     * @param toTrain
     * @return whether the move could be completed successfully
     */
    public boolean moveOneWagon(int wagonId, Train toTrain) {
        // TODO

        return false;
    }

    /**
     * Tries to split this train before the given position and move the complete sequence
     * of wagons from the given position to the rear of toTrain.
     * No change is made if the split or re-attachment cannot be made
     * (when the position is not valid for this train, or the trains are not compatible
     * or the engine of toTrain has insufficient capacity)
     *
     * @param position
     * @param toTrain
     * @return whether the move could be completed successfully
     */
    public boolean splitAtPosition(int position, Train toTrain) {
        // TODO

        return false;
    }

    /**
     * Reverses the sequence of wagons in this train (if any)
     * i.e. the last wagon becomes the first wagon
     * the previous wagon of the last wagon becomes the second wagon
     * etc.
     * (No change if the train has no wagons or only one wagon)
     */
    public void reverse() {
        // TODO

    }

    // TODO
}
