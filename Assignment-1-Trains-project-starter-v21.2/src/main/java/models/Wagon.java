package models;

public abstract class Wagon {
    protected int id;               // some unique ID of a Wagon
    private Wagon nextWagon;        // another wagon that is appended at the tail of this wagon
    // a.k.a. the successor of this wagon in a sequence
    // set to null if no successor is connected
    private Wagon previousWagon;    // another wagon that is prepended at the front of this wagon
    // a.k.a. the predecessor of this wagon in a sequence
    // set to null if no predecessor is connected


    // representation invariant propositions:
    // tail-connection-invariant:   wagon.nextWagon == null or wagon == wagon.nextWagon.previousWagon
    // front-connection-invariant:  wagon.previousWagon == null or wagon = wagon.previousWagon.nextWagon

    public Wagon(int wagonId) {
        this.id = wagonId;
    }

    public int getId() {
        return id;
    }

    public Wagon getNextWagon() {
        return nextWagon;
    }

    public Wagon getPreviousWagon() {
        return previousWagon;
    }

    /**
     * @return whether this wagon has a wagon appended at the tail
     */
    public boolean hasNextWagon() {
        return this.nextWagon != null;
    }

    /**
     * @return whether this wagon has a wagon prepended at the front
     */
    public boolean hasPreviousWagon() {
        return this.previousWagon != null;
    }

    /**
     * Returns the last wagon attached to it, if there are no wagons attached to it then this wagon is the last wagon.
     * @return the wagon
     */
    public Wagon getLastWagonAttached() {
      Wagon currentWagon = this;
      while (currentWagon.hasNextWagon()){
          currentWagon=currentWagon.nextWagon;
      }
        return currentWagon;


    }

    /**
     * @return the length of the tail of wagons towards the end of the sequence
     * excluding this wagon itself.
     */
    public int getTailLength() {
        int length = 0;
        Wagon currentWagon = this;

        while (currentWagon.hasNextWagon()) {
            length++;
            currentWagon = currentWagon.getNextWagon();
        }
        return length;

    }

    /**
     * Attaches the tail wagon and its connected successors behind this wagon,
     * if and only if this wagon has no wagon attached at its tail
     * and if the tail wagon has no wagon attached in front of it.
     *
     * @param tail the wagon to attach behind this wagon.
     * @throws IllegalStateException if this wagon already has a wagon appended to it.
     * @throws IllegalStateException if tail is already attached to a wagon in front of it.
     */
    public void attachTail(Wagon tail) {

        if (this.getNextWagon() != null) {
            throw new RuntimeException("This wagon is already attached!");
       } else if (tail.getPreviousWagon() != null) {
          throw new RuntimeException("This wagon does not fit here!");
      } else {
            tail.previousWagon = this;
           this.nextWagon = tail;
       }
    }

    /**
     * Detaches the tail from this wagon and returns the first wagon of this tail.
     *
     * @return the first wagon of the tail that has been detached
     * or <code>null</code> if it had no wagons attached to its tail.
     */
    public Wagon detachTail() {
        // TODO detach the tail from this wagon (sustaining the invariant propositions).
        //  and return the head wagon of that tail

        if (this.hasNextWagon()){
            Wagon oldNextWagon = this.getNextWagon();
            oldNextWagon.previousWagon=null;
            this.nextWagon=null;
            return oldNextWagon;
        }

        return null;
    }

    /**
     * Detaches this wagon from the wagon in front of it.
     * No action if this wagon has no previous wagon attached.
     *
     * @return the former previousWagon that has been detached from,
     * or <code>null</code> if it had no previousWagon.
     */
    public Wagon detachFront() {
        // TODO detach this wagon from its predecessor (sustaining the invariant propositions).
        //   and return that predecessor
        if(this.hasPreviousWagon()){
            Wagon oldFront = this.previousWagon;
            oldFront.nextWagon=null;
            this.previousWagon=null;
            return oldFront;
        }

        return null;
    }

    /**
     * Replaces the tail of the <code>front</code> wagon by this wagon and its connected successors
     * Before such reconfiguration can be made,
     * the method first disconnects this wagon form its predecessor,
     * and the <code>front</code> wagon from its current tail.
     *
     * @param front the wagon to which this wagon must be attached to.
     */
    public void reAttachTo(Wagon front) {

        this.detachFront();
       front.nextWagon=this;
       this.previousWagon=front;

    }

    /**
     * Removes this wagon from the sequence that it is part of,
     * and reconnects its tail to the wagon in front of it, if it exists.
     */
    public void removeFromSequence() {
        Wagon nextWagon = this.getNextWagon();
        Wagon previousWagon = this.getPreviousWagon();
        if(hasNextWagon()) {
            this.nextWagon = null;
            nextWagon.previousWagon = previousWagon;
        }
        if(hasPreviousWagon()) {
            this.previousWagon = null;
            previousWagon.nextWagon = nextWagon;
        }
    }


    /**
     * Reverses the order in the sequence of wagons from this Wagon until its final successor.
     * The reversed sequence is attached again to the wagon in front of this Wagon, if any.
     * No action if this Wagon has no succeeding next wagon attached.
     *
     * @return the new start Wagon of the reversed sequence (with is the former last Wagon of the original sequence)
     */
    public Wagon reverseSequence() {
        Wagon previousWagon = this.getPreviousWagon();
        Wagon previous = null;
        Wagon next;
        Wagon currendWagon = this;

        while(true) {
            next = currendWagon.getNextWagon();
            currendWagon.nextWagon = previous;
            currendWagon.previousWagon = next;
            previous = currendWagon;
            if(next != null) {
                currendWagon = next;
            } else {
                break;
            }
        }
        //connect cur and previousWagon
        currendWagon.previousWagon = previousWagon;
        //if it's called on the first wagon
        if (previousWagon != null) {
            previousWagon.nextWagon = currendWagon;
        }
        return previous;
    }



    @Override
    public String toString() {
        return "[Wagon-" + this.getId() + "]";
    }
}
