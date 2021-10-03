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
        if (firstWagon == null){
            return 0;
        }
        int numberOfWagons = 1;
        Wagon wagonCheckHasNext = this.firstWagon;
        while (wagonCheckHasNext.hasNextWagon()) {
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
        if (firstWagon == null){
            return lastWagon;
        }
        while (lastWagon.hasNextWagon()) {
            lastWagon = lastWagon.getNextWagon();
        }
        return lastWagon;
    }

    /**
     * @return the total number of seats on a passenger train
     * (return 0 for a freight train)
     */
    public int getTotalNumberOfSeats() {
        Wagon wagonCheckHasNext = this.firstWagon;
        if (isFreightTrain()||wagonCheckHasNext == null) {
            return 0;
        }
        int numberOfSeats = 0;
        while (wagonCheckHasNext.hasNextWagon()||wagonCheckHasNext.hasPreviousWagon()) {
            numberOfSeats = numberOfSeats + ((PassengerWagon) wagonCheckHasNext).getNumberOfSeats();
            wagonCheckHasNext = wagonCheckHasNext.getNextWagon();
            if (wagonCheckHasNext == null){
                break;
            }
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
        if (isPassengerTrain()||wagonCheckHasNext == null) {
            return 0;
        }
        while (wagonCheckHasNext.hasNextWagon()||wagonCheckHasNext.hasPreviousWagon()) {
            TotalMaxWeight = TotalMaxWeight + ((FreightWagon) wagonCheckHasNext).getMaxWeight();
            wagonCheckHasNext = wagonCheckHasNext.getNextWagon();
            if (wagonCheckHasNext == null){
                break;
            }
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
        int numberOfWagons = 1;
        Wagon wagonAtPosition = this.firstWagon;
        if (wagonAtPosition == null) {
            return wagonAtPosition;
        }
        while (true) {
            if (numberOfWagons == position) {
                return wagonAtPosition;
            }
            wagonAtPosition = wagonAtPosition.getNextWagon();
            numberOfWagons++;
            if (wagonAtPosition == null){
                break;
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
        if (wagonById == null) {
            return wagonById;
        }
        while (wagonById.hasNextWagon()||wagonById.hasPreviousWagon()) {
            if (wagonById.getId() == wagonId) {
                return wagonById;
            }
            wagonById = wagonById.getNextWagon();
            if (wagonById == null){
                break;
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
        if (wagon == null){
            return false;
        }
        if (this.engine.getMaxWagons() <= 0) {
            return false;
        }
        if (firstWagon == null){
            return true;
        }
        if ((wagon.getTailLength()+this.getNumberOfWagons()) > this.engine.getMaxWagons()) {
            return false;
        }
        Wagon newWagon = wagon;
        Wagon existingWagon = firstWagon;
        while (existingWagon != null){
            while (newWagon != null){
                if (existingWagon.getId() == newWagon.getId()){
                    return false;
                }
                newWagon = newWagon.getNextWagon();
            }
            existingWagon = existingWagon.getNextWagon();
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
        }
        if (firstWagon == null) {
            this.firstWagon = wagon;
            return true;
        }
        Wagon lastWagonOfTrain = firstWagon;
        while (lastWagonOfTrain.hasNextWagon()) {
            lastWagonOfTrain = lastWagonOfTrain.getNextWagon();
        }
        lastWagonOfTrain.attachTail(wagon);
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
        if (canAttach(wagon) == false) {
            return false;
        }
        if (firstWagon == null) {
            this.firstWagon = wagon;
            return true;
        }
        Wagon lastOfSequence = wagon;
        while (lastOfSequence.hasNextWagon()){
            lastOfSequence = lastOfSequence.getNextWagon();
        }
        lastOfSequence.attachTail(firstWagon);
        setFirstWagon(wagon);
        return true;
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
        if (canAttach(wagon) == false) {
            return false;
        }
        if (firstWagon == null && position == 1){
            this.attachToRear(wagon);
            return true;
        }
        if (position > getNumberOfWagons()){
            return false;
        }
        Wagon lastOfSequence = wagon;
        while (lastOfSequence.hasNextWagon()){
            lastOfSequence = wagon.getNextWagon();
        }
        Wagon oldWagon = findWagonAtPosition(position);
        oldWagon.reAttachTo(wagon);
        lastOfSequence.attachTail(oldWagon);
        return true;
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
        Wagon wagonToBeMoved = findWagonById(wagonId);
        if (toTrain.canAttach(wagonToBeMoved)==false){
            return false;
        }
        if (wagonToBeMoved==firstWagon){
            firstWagon = wagonToBeMoved.getNextWagon();
        }
        wagonToBeMoved.removeFromSequence();
        toTrain.attachToRear(wagonToBeMoved);
        return true;
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
        if (firstWagon == null){
            return false;
        }
        Wagon wagonToBeMoved = findWagonAtPosition(position);
        if (wagonToBeMoved == firstWagon && !wagonToBeMoved.hasNextWagon()){
            firstWagon = null;
            toTrain.attachToRear(wagonToBeMoved);
            return true;
        }
        wagonToBeMoved.detachFront();
        toTrain.attachToRear(wagonToBeMoved);
        return true;
    }

    /**
     * Reverses the sequence of wagons in this train (if any)
     * i.e. the last wagon becomes the first wagon
     * the previous wagon of the last wagon becomes the second wagon
     * etc.
     * (No change if the train has no wagons or only one wagon)
     */
    public void reverse() {
        if (firstWagon != null){
            firstWagon = firstWagon.reverseSequence();
        }
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(engine);
        Wagon wagon = firstWagon;
        if (wagon != null){
            while (true){
                sb.append(wagon);
                wagon = wagon.getNextWagon();
                if (wagon == null){
                    break;
                }
            }
        }
        sb.append(" with " + getNumberOfWagons() + " wagons from " + origin + " to " + destination);
        return sb.toString();
    }
}
