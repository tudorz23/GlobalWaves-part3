package database.observer;

import database.records.Notification;

public interface IObservable {
    void addObserver(IObserver observer);
    void removeObserver(IObserver observer);
    void notifyObservers(Notification notification);
}
