package database.audio;

import database.observer.IObservable;
import database.observer.IObserver;
import database.observer.Notification;
import database.users.Artist;
import database.users.User;
import utils.enums.AudioType;
import utils.enums.PlaylistVisibility;

public final class Playlist extends SongCollection implements IObservable {
    private final String owner;
    private PlaylistVisibility visibility;
    private int followersCnt;
    private IObserver observer; // The owner is the only observer.

    /* Constructor */
    public Playlist(final String name, final String owner) {
        super(name);
        this.owner = owner;
        this.visibility = PlaylistVisibility.PUBLIC;
        this.setType(AudioType.PLAYLIST);
        this.followersCnt = 0;
    }

    @Override
    public Playlist getDeepCopy() {
        Playlist copy = new Playlist(this.getName(), this.getOwner());

        for (Song song : this.getSongs()) {
            copy.addSong(song.getDeepCopy());
        }

        copy.setPlayingSongIndex(0);
        copy.followersCnt = this.followersCnt;
        copy.initializeShuffleArray();

        return copy;
    }

    /**
     * Increments the number of followers.
     */
    public void incrementFollowersCnt() {
        followersCnt++;
    }

    /**
     * Decrements the number of followers.
     */
    public void decrementFollowersCnt() {
        followersCnt--;
    }


    @Override
    public void updateAnalytics() {
        User listener = getListener();

        listener.getAnalytics().addArtist(getSongs().get(getPlayingSongIndex()).getArtist());
        listener.getAnalytics().addGenre(getSongs().get(getPlayingSongIndex()).getGenre());

        Song originalSong = getListener().getDatabase()
                .searchSongInDatabase(getSongs().get(getPlayingSongIndex()));
        listener.getAnalytics().addSong(originalSong);

        listener.getAnalytics().addAlbum(getSongs().get(getPlayingSongIndex()).getAlbum());

        updateArtistAnalytics(originalSong);
    }


    /**
     * Updates the analytics of the artist that owns the song,
     * if he is registered in the database.
     * @param originalSong Song instance from the database.
     */
    private void updateArtistAnalytics(Song originalSong) {
        Song currSong = getSongs().get(getPlayingSongIndex());
        Artist artist;
        try {
            artist = getListener().getDatabase().searchArtistInDatabase(currSong.getArtist());
        } catch (IllegalArgumentException exception) {
            return;
        }

        artist.getArtistAnalytics().addAlbum(currSong.getAlbum());
        artist.getArtistAnalytics().addSong(originalSong);
        artist.getArtistAnalytics().addFan(getListener().getUsername());
    }


    /**
     * Sets the observer for the playlist (should be the owner).
     */
    @Override
    public void addObserver(IObserver observer) throws IllegalStateException {
        if (this.observer != null) {
            throw new IllegalStateException("The playlist " + getName() + " already has an owner.");
        }

        this.observer = observer;
    }


    @Override
    public void removeObserver(IObserver observer) throws IllegalStateException {
        throw new IllegalStateException("Cannot remove the owner of the playlist.");
    }


    @Override
    public void notifyObservers(Notification notification) {
        observer.update(notification);
    }

    /* Getters and Setters */
    public String getOwner() {
        return owner;
    }
    public PlaylistVisibility getVisibility() {
        return visibility;
    }
    public void setVisibility(final PlaylistVisibility visibility) {
        this.visibility = visibility;
    }
    public int getFollowersCnt() {
        return followersCnt;
    }
}
