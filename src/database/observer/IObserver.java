package database.observer;

import database.records.Notification;

/**
 * Interface for the Observer entities of the Observer Design Pattern.
 */
public interface IObserver {
    /**
     * Updates the state of the Observer according to
     * the notification received from the Observable.
     */
    void update(Notification notification);
}
