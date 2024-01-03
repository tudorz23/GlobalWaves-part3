package database.analytics;

import database.observer.IObserver;
import java.util.ArrayList;
import java.util.List;

/**
 * To be extended by specialized Artist and Host analytics classes.
 * Its main use is for the Notification and Subscribe functionalities.
 */
public abstract class ContentCreatorAnalytics {
    private List<IObserver> subscribers;

    /* Constructor */
    public ContentCreatorAnalytics() {
        subscribers = new ArrayList<>();
    }


    /**
     * Adds a new subscriber (i.e. an instance of a class implementing
     * IObserver) to creator's list of subscribers.
     */
    public void addSubscriber(final IObserver subscriber) {
        subscribers.add(subscriber);
    }


    /**
     * Removes a subscriber from creator's list of subscribers.
     */
    public void removeSubscriber(final IObserver subscriber) {
        subscribers.remove(subscriber);
    }

    /* Getters and Setters */
    /**
     * Getter for subscribers.
     */
    public List<IObserver> getSubscribers() {
        return subscribers;
    }
}
