package database.observer;

import database.records.Notification;

public interface IObserver {
    void update(Notification notification);
}
