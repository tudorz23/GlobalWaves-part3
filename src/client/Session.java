package client;

import database.Database;

public final class Session {
    private Database database;
    private int timestamp;

    /* Constructor */
    public Session(final Database database) {
        this.timestamp = 0;
        this.database = database;
    }

    /* Getters and Setters */
    public Database getDatabase() {
        return database;
    }
    public void setDatabase(final Database database) {
        this.database = database;
    }
    public int getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(final int timestamp) {
        this.timestamp = timestamp;
    }
}
