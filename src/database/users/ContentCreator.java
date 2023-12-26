package database.users;

import database.Database;
import database.analytics.ContentCreatorAnalytics;
import database.observer.IObservable;
import database.observer.IObserver;
import database.records.Notification;

public abstract class ContentCreator extends User implements IObservable {
    /* Constructor */
    public ContentCreator(final String username, final int age, final String city,
                          Database database) {
        super(username, age, city, database);
    }

    /**
     * @return The analytics specific to the content creator.
     */
    public abstract ContentCreatorAnalytics getContentCreatorAnalytics();


    @Override
    public void addObserver(IObserver observer) {
        getContentCreatorAnalytics().addSubscriber(observer);
    }

    @Override
    public void removeObserver(IObserver observer) {
        getContentCreatorAnalytics().removeSubscriber(observer);
    }

    @Override
    public void notifyObservers(Notification notification) {
        for (IObserver subscriber : getContentCreatorAnalytics().getSubscribers()) {
            subscriber.update(notification);
        }
    }
}
