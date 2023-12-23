package database.analytics;

import database.observer.IObserver;
import java.util.ArrayList;
import java.util.List;

public abstract class ContentCreatorAnalytics {
    private List<IObserver> subscribers;

    /* Constructor */
    public ContentCreatorAnalytics() {
        subscribers = new ArrayList<>();
    }


    public void addSubscriber(IObserver subscriber) {
        subscribers.add(subscriber);
    }


    public void removeSubscriber(IObserver subscriber) {
        subscribers.remove(subscriber);
    }

    /* Getters and Setters */
    public List<IObserver> getSubscribers() {
        return subscribers;
    }
    public void setSubscribers(List<IObserver> subscribers) {
        this.subscribers = subscribers;
    }
}
