package database.audio;

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
