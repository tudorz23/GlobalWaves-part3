package database.observer;

import database.records.Notification;

/**
 * Interface for the Observable entities of the Observer Design Pattern.
 */
public interface IObservable {
    /**
     * Adds a new observer to the observers list.
     */
    void addObserver(IObserver observer);


    /**
     * Removes an observer from the observers list.
     */
    void removeObserver(IObserver observer);


    /**
     * Notifies the observers of some new event that happened.
     */
    void notifyObservers(Notification notification);
}
