package database.audio;

import database.users.User;
import utils.enums.AudioType;

public final class Album extends SongCollection {
    private String owner;
    private final int releaseYear;
    private final String description;

    /* Constructor */
    public Album(final String name, final String owner,
                 final int releaseYear, final String description) {
        super(name);
        this.owner = owner;
        this.releaseYear = releaseYear;
        this.description = description;
        this.setType(AudioType.ALBUM);
    }

    @Override
    public Album getDeepCopy() {
        Album copy = new Album(this.getName(), this.owner,
                                this.releaseYear, this.description);

        for (Song song : this.getSongs()) {
            copy.addSong(song.getDeepCopy());
        }

        copy.setPlayingSongIndex(0);
        copy.initializeShuffleArray();

        return copy;
    }


    @Override
    public void updateAnalytics() {
        User listener = getListener();

        listener.getAnalytics().addAlbum(getName());

        // Song from the database.
        Song originalSong = listener.getDatabase()
                .searchSongInDatabase(getSongs().get(getPlayingSongIndex()));
        listener.getAnalytics().addSong(originalSong);

        listener.getAnalytics().addGenre(getSongs().get(getPlayingSongIndex()).getGenre());
        listener.getAnalytics().addArtist(getOwner());

        originalSong.updateArtistAnalytics(listener);
        originalSong.updateMonetization(listener);
    }


    /* Getters and Setters */
    public int getReleaseYear() {
        return releaseYear;
    }
    public String getDescription() {
        return description;
    }
    public String getOwner() {
        return owner;
    }
}
