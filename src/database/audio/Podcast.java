package database.audio;

import database.Player;
import fileio.input.EpisodeInput;
import fileio.input.PodcastInput;
import utils.enums.AudioType;
import utils.enums.PlayerState;
import utils.enums.RepeatState;
import java.util.ArrayList;
import static utils.Constants.SKIP_REWIND_TIME;

public final class Podcast extends Audio {
    private String owner;
    private ArrayList<Episode> episodes;
    private int playingEpisodeIdx;

    /* Constructors */
    public Podcast(final String name, final String owner) {
        super(name);
        this.owner = owner;
        this.setType(AudioType.PODCAST);
        this.episodes = new ArrayList<>();
    }

    public Podcast(final PodcastInput podcastInput) {
        super(podcastInput.getName());
        this.owner = podcastInput.getOwner();
        this.episodes = initializeEpisodes(podcastInput.getEpisodes());
        this.setType(AudioType.PODCAST);
    }

    /**
     * Helper for initializing the episodes field.
     * @param episodeInputs list of EpisodeInput objects to be converted to Episode.
     */
    private ArrayList<Episode> initializeEpisodes(final ArrayList<EpisodeInput> episodeInputs) {
        ArrayList<Episode> episodeList = new ArrayList<>();

        for (EpisodeInput episodeInput : episodeInputs) {
            Episode episode = new Episode(episodeInput);
            episodeList.add(episode);
        }

        return episodeList;
    }

    @Override
    public Podcast getDeepCopy() {
        Podcast copy = new Podcast(this.getName(), this.getOwner());

        for (Episode episode : this.episodes) {
            copy.episodes.add(episode.getDeepCopy());
        }

        copy.playingEpisodeIdx = 0;
        return copy;
    }

    @Override
    public void simulateTimePass(final Player player, final int currTime) {
        if (player.getPlayerState() == PlayerState.PAUSED
                || player.getPlayerState() == PlayerState.STOPPED) {
            return;
        }

        int elapsedTime = currTime - player.getPrevTimeInfo();
        int episodeRemainingTime = episodes.get(playingEpisodeIdx).getRemainedTime();

        while (elapsedTime >= episodeRemainingTime) {
            if (player.getPlayerState() == PlayerState.STOPPED) {
                return;
            }

            changeToNextEpisode(player);
            elapsedTime -= episodeRemainingTime;

            episodeRemainingTime = episodes.get(playingEpisodeIdx).getRemainedTime();
        }

        if (elapsedTime == 0) {
            return;
        }

        Episode playingEpisode = episodes.get(playingEpisodeIdx);
        int episodeNewTimePos = playingEpisode.getTimePosition() + elapsedTime;
        playingEpisode.setTimePosition(episodeNewTimePos);
    }

    /**
     * Moves to the next episode, considering the repeat state.
     */
    private void changeToNextEpisode(final Player player) {
        if (player.getRepeatState() == RepeatState.REPEAT_INFINITE) {
            episodes.get(playingEpisodeIdx).resetTimePosition();
            return;
        }

        if (player.getRepeatState() == RepeatState.REPEAT_ONCE) {
            episodes.get(playingEpisodeIdx).resetTimePosition();
            player.setRepeatState(RepeatState.NO_REPEAT);
            return;
        }

        if (playingEpisodeIdx == episodes.size() - 1) {
            // If no repeat is enabled and last episode is reached, stop the player.
            Episode currEpisode = episodes.get(playingEpisodeIdx);
            currEpisode.setTimePosition(currEpisode.getDuration());

            player.setPlayerState(PlayerState.STOPPED);
            return;
        }

        // Set the playing Episode to the next one.
        int nextEpisodeIdx = playingEpisodeIdx + 1;
        this.playingEpisodeIdx = nextEpisodeIdx;
        Episode newEpisode = episodes.get(nextEpisodeIdx);
        newEpisode.setTimePosition(0);
    }

    @Override
    public int getRemainedTime() {
        Episode playingEpisode = episodes.get(playingEpisodeIdx);
        return playingEpisode.getRemainedTime();
    }

    @Override
    public void next(final Player player) {
        changeToNextEpisode(player);

        if (player.getPlayerState() == PlayerState.PAUSED) {
            player.setPlayerState(PlayerState.PLAYING);
        }
    }

    @Override
    public void prev(final Player player) {
        Episode playingEpisode = episodes.get(playingEpisodeIdx);

        if (playingEpisode.getTimePosition() != 0) {
            // At least 1 second passed.
            playingEpisode.resetTimePosition();

            if (player.getPlayerState() == PlayerState.PAUSED) {
                player.setPlayerState(PlayerState.PLAYING);
            }
            return;
        }

        // No second passed.
        if (playingEpisodeIdx == 0) {
            // First episode;
            if (player.getPlayerState() == PlayerState.PAUSED) {
                player.setPlayerState(PlayerState.PLAYING);
            }
            return;
        }

        int prevEpisodeIdx = playingEpisodeIdx - 1;
        playingEpisodeIdx = prevEpisodeIdx;
        Episode prevEpisode = episodes.get(prevEpisodeIdx);
        prevEpisode.setTimePosition(0);

        if (player.getPlayerState() == PlayerState.PAUSED) {
            player.setPlayerState(PlayerState.PLAYING);
        }
    }

    @Override
    public String getPlayingTrackName() {
        Episode playingEpisode = episodes.get(playingEpisodeIdx);
        return playingEpisode.getName();
    }

    /**
     * Advances the play by 90 seconds.
     */
    public void forward(final Player player) {
        Episode playingEpisode = episodes.get(playingEpisodeIdx);

        if (playingEpisode.getRemainedTime() < SKIP_REWIND_TIME) {
            changeToNextEpisode(player);

            if (player.getPlayerState() == PlayerState.PAUSED) {
                player.setPlayerState(PlayerState.PLAYING);
            }
            return;
        }

        int currTimePos = playingEpisode.getTimePosition();
        playingEpisode.setTimePosition(currTimePos + SKIP_REWIND_TIME);

        if (player.getPlayerState() == PlayerState.PAUSED) {
            player.setPlayerState(PlayerState.PLAYING);
        }
    }

    /**
     * Rewinds the play by 90 seconds.
     */
    public void backward(final Player player) {
        Episode playingEpisode = episodes.get(playingEpisodeIdx);

        if (playingEpisode.getTimePosition() < SKIP_REWIND_TIME) {
            playingEpisode.setTimePosition(0);

            if (player.getPlayerState() == PlayerState.PAUSED) {
                player.setPlayerState(PlayerState.PLAYING);
            }
            return;
        }

        int currTimePos = playingEpisode.getTimePosition();
        playingEpisode.setTimePosition(currTimePos - SKIP_REWIND_TIME);

        if (player.getPlayerState() == PlayerState.PAUSED) {
            player.setPlayerState(PlayerState.PLAYING);
        }
    }

    /**
     * Adds an episode to the episode list.
     */
    public void addEpisode(final Episode episode) {
        episodes.add(episode);
    }

    /* Getters and Setters */
    public String getOwner() {
        return owner;
    }
    public void setOwner(final String owner) {
        this.owner = owner;
    }
    public ArrayList<Episode> getEpisodes() {
        return episodes;
    }
}
