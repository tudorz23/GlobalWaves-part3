package database.audio;

import fileio.input.EpisodeInput;

public final class Episode {
    private String name;
    private Integer duration;
    private String description;
    private int timePosition; // Episode time at last known moment.

    /* Constructors */
    private Episode() {
    }

    public Episode(final EpisodeInput episodeInput) {
        this.name = episodeInput.getName();
        this.duration = episodeInput.getDuration();
        this.description = episodeInput.getDescription();
        this.timePosition = 0;
    }

    /**
     * @return Deep copy of the Episode object.
     */
    public Episode getDeepCopy() {
        Episode copy = new Episode();
        copy.name = this.name;
        copy.duration = this.duration;
        copy.description = this.description;
        copy.timePosition = 0;

        return copy;
    }

    /**
     * @return Time remained until episode ends.
     */
    public int getRemainedTime() {
        return (duration - timePosition);
    }

    /**
     * Sets the time position to 0.
     */
    public void resetTimePosition() {
        timePosition = 0;
    }

    /* Getters and Setters */
    public String getName() {
        return name;
    }
    public void setName(final String name) {
        this.name = name;
    }
    public Integer getDuration() {
        return duration;
    }
    public String getDescription() {
        return description;
    }
    public int getTimePosition() {
        return timePosition;
    }
    public void setTimePosition(final int timePosition) {
        this.timePosition = timePosition;
    }
}
