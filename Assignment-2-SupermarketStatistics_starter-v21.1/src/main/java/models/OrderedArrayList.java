package models;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.BinaryOperator;

public class OrderedArrayList<E>
        extends ArrayList<E>
        implements OrderedList<E> {

    protected Comparator<? super E> ordening;   // the comparator that has been used with the latest sort
    protected int nSorted;                      // the number of items that have been ordered by barcode in the list
    // representation-invariant
    //      all items at index positions 0 <= index < nSorted have been ordered by the given ordening comparator
    //      other items at index position nSorted <= index < size() can be in any order amongst themselves
    //              and also relative to the sorted section

    public OrderedArrayList() {
        this(null);
    }

    public OrderedArrayList(Comparator<? super E> ordening ) {
        super();
        this.ordening = ordening;
        this.nSorted = 0;
    }

    public Comparator<? super E> getOrdening() {
        return this.ordening;
    }

    @Override
    public void clear() {
        super.clear();
        this.nSorted = 0;
    }

    @Override
    public void sort(Comparator<? super E> c) {
        super.sort(c);
        this.ordening = c;
        this.nSorted = this.size();
    }

    // TODO override the ArrayList.add(index, item), ArrayList.remove(index) and Collection.remove(object) methods
    //  such that they sustain the representation invariant of OrderedArrayList
    //  (hint: only change nSorted as required to guarantee the representation invariant, do not invoke a sort)
    @Override
    public void add(int index, E item){
        super.add(index, item);
        if (nSorted >= index) nSorted = index-1;
    }

    @Override
    public E remove(int index){
        if (nSorted >= index) nSorted = index-1;
        return super.remove(index);
    }

    @Override
    public boolean remove(Object object){
        nSorted = indexOfByBinarySearch((E) object)-1;
        return super.remove(object);
    }

    @Override
    public void sort() {
        if (this.nSorted < this.size()) {
            this.sort(this.ordening);
        }
    }

    @Override
    public int indexOf(Object item) {
        if (item != null) {
            return indexOfByIterativeBinarySearch((E)item);
        } else {
            return -1;
        }
    }

    @Override
    public int indexOfByBinarySearch(E searchItem) {
        if (searchItem != null) {
            // some arbitrary choice to use the iterative or the recursive version
            return indexOfByRecursiveBinarySearch(searchItem);
        } else {
            return -1;
        }
    }

    /**
     * finds the position of the searchItem by an iterative binary search algorithm in the
     * sorted section of the arrayList, using the this.ordening comparator for comparison and equality test.
     * If the item is not found in the sorted section, the unsorted section of the arrayList shall be searched by linear search.
     * The found item shall yield a 0 result from the this.ordening comparator, and that need not to be in agreement with the .equals test.
     * Here we follow the comparator for ordening items and for deciding on equality.
     * @param searchItem    the item to be searched on the basis of comparison by this.ordening
     * @return              the position index of the found item in the arrayList, or -1 if no item matches the search item.
     */
    public int indexOfByIterativeBinarySearch(E searchItem) {
        int from = 0;
        int to = nSorted;

        while (from <= to){
            int midIndex = (to + from)/2;
            if (midIndex == this.size()){
                return -1;
            }
            int comparison = this.ordening.compare(searchItem,this.get(midIndex));

            if (comparison < 0){
                to = midIndex - 1;
            }
            else if (comparison > 0){
                from = midIndex + 1;
            }
            else return midIndex;
        }
        int linComparison;
        for (int i = nSorted; i < this.size(); i++){
            linComparison = this.ordening.compare(this.get(i),searchItem);
            if (linComparison == 0) return i;
        }

        // TODO implement an iterative binary search on the sorted section of the arrayList, 0 <= index < nSorted
        //   to find the position of an item that matches searchItem (this.ordening comparator yields a 0 result)


        // TODO if no match was found, attempt a linear search of searchItem in the section nSorted <= index < size()

        return -1;
    }

    /**
     * finds the position of the searchItem by a recursive binary search algorithm in the
     * sorted section of the arrayList, using the this.ordening comparator for comparison and equality test.
     * If the item is not found in the sorted section, the unsorted section of the arrayList shall be searched by linear search.
     * The found item shall yield a 0 result from the this.ordening comparator, and that need not to be in agreement with the .equals test.
     * Here we follow the comparator for ordening items and for deciding on equality.
     * @param searchItem    the item to be searched on the basis of comparison by this.ordening
     * @return              the position index of the found item in the arrayList, or -1 if no item matches the search item.
     */
    public int indexOfByRecursiveBinarySearch(E searchItem) {
        if (this.size() == 0) return -1;
        return indexOfByRecursiveBinarySearch(searchItem, 0, this.size());

        // TODO implement a recursive binary search on the sorted section of the arrayList, 0 <= index < nSorted
        //   to find the position of an item that matches searchItem (this.ordening comparator yields a 0 result)

        // TODO if no match was found, attempt a linear search of searchItem in the section nSorted <= index < size()
    }

    public int indexOfByRecursiveBinarySearch(E searchItem, int from, int to) {
        int midIndex = (to + from)/2;
        if (midIndex == this.size()) { return -1; }
        int comparison = this.ordening.compare(searchItem,this.get(midIndex));
        if (from > to) {
            int linComparison;
            for (int i = nSorted; i < this.size(); i++){
                linComparison = this.ordening.compare(searchItem, this.get(i));
                if (linComparison == 0) return i;
            }
            return -1;
            }
        if (comparison < 0){
            return indexOfByRecursiveBinarySearch(searchItem, from, midIndex-1);
        }
        else if (comparison > 0){
            return indexOfByRecursiveBinarySearch(searchItem, midIndex+1, to);
        }
        else return midIndex;
    }

    /**
     * finds a match of newItem in the list and applies the merger operator with the newItem to that match
     * i.e. the found match is replaced by the outcome of the merge between the match and the newItem
     * If no match is found in the list, the newItem is added to the list.
     * @param newItem
     * @param merger    a function that takes two items and returns an item that contains the merged content of
     *                  the two items according to some merging rule.
     *                  e.g. a merger could add the value of attribute X of the second item
     *                  to attribute X of the first item and then return the first item
     * @return  whether a new item was added to the list or not
     */
    @Override
    public boolean merge(E newItem, BinaryOperator<E> merger) {
        if (newItem == null) return false;
        int matchedItemIndex = this.indexOfByRecursiveBinarySearch(newItem);

        if (matchedItemIndex < 0) {
            this.add(newItem);
            return true;
        } else {
            merger.apply(this.get(matchedItemIndex), newItem);
            return false;


            // TODO retrieve the matched item and
            //  replace the matched item in the list with the merger of the matched item and the newItem


        }
    }
}
