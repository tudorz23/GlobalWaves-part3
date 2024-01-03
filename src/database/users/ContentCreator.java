package database.users;

import database.Database;
import database.analytics.ContentCreatorAnalytics;
import database.observer.IObservable;
import database.observer.IObserver;
import database.records.Notification;

/**
 * To be extended by Artist and Host classes.
 * Its main role is to provide a layer of abstraction between Basic Users and
 * Artists and Hosts, mostly for handling the Notification and Subscribe
 * functionalities in an easier way.
 */
public abstract class ContentCreator extends User implements IObservable {
    /* Constructor */
    public ContentCreator(final String username, final int age, final String city,
                          final Database database) {
        super(username, age, city, database);
    }

    /**
     * @return The analytics specific to the content creator.
     */
    public abstract ContentCreatorAnalytics getContentCreatorAnalytics();


    /**
     * Adds an observer in the list of users that "observe" the creator.
     */
    @Override
    public void addObserver(final IObserver observer) {
        getContentCreatorAnalytics().addSubscriber(observer);
    }


    /**
     * Removes an observer from the list of users that "observe" the creator.
     */
    @Override
    public void removeObserver(final IObserver observer) {
        getContentCreatorAnalytics().removeSubscriber(observer);
    }


    /**
     * Notifies all the users that "observe" the creator of some news.
     */
    @Override
    public void notifyObservers(final Notification notification) {
        for (IObserver subscriber : getContentCreatorAnalytics().getSubscribers()) {
            subscriber.update(notification);
        }
    }
}
