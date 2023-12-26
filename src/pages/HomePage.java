package pages;

import database.audio.Audio;
import database.audio.Playlist;
import database.audio.Song;
import database.users.User;
import utils.enums.PageType;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import static utils.Constants.MAX_PLAYLIST_RANK_NUMBER;
import static utils.Constants.MAX_SONG_RANK_NUMBER;

public final class HomePage extends Page {
    private List<Song> likedSongs;
    private List<Playlist> likedPlaylists;
    private List<Song> songRecommendations;
    private List<Playlist> playlistRecommendations;

    /* Constructor */
    public HomePage(final User owningUser) {
        super(owningUser);
        setType(PageType.HOME);
    }

    @Override
    public String printPage() {
        refreshPage();
        StringBuilder stringBuilder = new StringBuilder("Liked songs:\n\t[");
        appendAudioArray(likedSongs, stringBuilder);

        stringBuilder.append("]\n\nFollowed playlists:\n\t[");
        appendAudioArray(likedPlaylists, stringBuilder);

        stringBuilder.append("]\n\nSong recommendations:\n\t[");
        appendAudioArray(songRecommendations, stringBuilder);

        stringBuilder.append("]\n\nPlaylists recommendations:\n\t[");
        appendAudioArray(playlistRecommendations, stringBuilder);

        stringBuilder.append("]");
        return stringBuilder.toString();
    }


    /**
     * Appends the audio names from the audio list to the builder.
     */
    private void appendAudioArray(List<? extends Audio> audioList, StringBuilder builder) {
        Iterator<?> audioIterator = audioList.iterator();
        while (audioIterator.hasNext()) {
            Audio audio = (Audio) audioIterator.next();
            builder.append(audio.getName());

            if (audioIterator.hasNext()) {
                builder.append(", ");
            }
        }
    }


    /**
     * Refreshes the recommendations displayed on the page.
     */
    private void refreshPage() {
        refreshSongRecommendation();
        refreshPlaylistRecommendation();

        songRecommendations = getOwningUser().getAnalytics().getSongRecommendations();
        playlistRecommendations = getOwningUser().getAnalytics().getPlaylistRecommendations();
    }


    /**
     * Helper for refreshing the song recommendations.
     */
    private void refreshSongRecommendation() {
        likedSongs = new ArrayList<>(getOwningUser().getLikedSongs());

        likedSongs.sort(Comparator.comparing(Song::getLikeCnt).reversed());

        while (likedSongs.size() > MAX_SONG_RANK_NUMBER) {
            likedSongs.remove(likedSongs.size() - 1);
        }
    }


    /**
     * Helper for refreshing the playlist recommendations.
     */
    private void refreshPlaylistRecommendation() {
        likedPlaylists = new ArrayList<>(getOwningUser().getFollowedPlaylists());

        likedPlaylists.sort(Comparator.comparing(Playlist::computeLikeCnt).reversed());

        while (likedPlaylists.size() > MAX_PLAYLIST_RANK_NUMBER) {
            likedPlaylists.remove(likedPlaylists.size() - 1);
        }
    }
}
