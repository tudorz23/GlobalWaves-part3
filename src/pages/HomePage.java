package pages;

import database.audio.Playlist;
import database.audio.Song;
import database.users.User;
import utils.enums.PageType;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

import static utils.Constants.MAX_PLAYLIST_RANK_NUMBER;
import static utils.Constants.MAX_SONG_RANK_NUMBER;

public final class HomePage extends Page {
    private ArrayList<Song> songRecommendation;
    private ArrayList<Playlist> playlistRecommendation;

    /* Constructor */
    public HomePage(final User owningUser) {
        super(owningUser);
        setType(PageType.HOME);
    }

    @Override
    public String printPage() {
        refreshPage();
        StringBuilder stringBuilder = new StringBuilder("Liked songs:\n\t[");

        Iterator<Song> songIterator = songRecommendation.iterator();
        while (songIterator.hasNext()) {
            Song song = songIterator.next();
            stringBuilder.append(song.getName());

            if (songIterator.hasNext()) {
                stringBuilder.append(", ");
            }
        }

        stringBuilder.append("]\n\nFollowed playlists:\n\t[");
        Iterator<Playlist> playlistIterator = playlistRecommendation.iterator();
        while (playlistIterator.hasNext()) {
            Playlist playlist = playlistIterator.next();
            stringBuilder.append(playlist.getName());

            if (playlistIterator.hasNext()) {
                stringBuilder.append(", ");
            }
        }

        stringBuilder.append("]");
        return stringBuilder.toString();
    }


    /**
     * Refreshes the recommendations displayed on the page.
     */
    private void refreshPage() {
        refreshSongRecommendation();
        refreshPlaylistRecommendation();
    }


    /**
     * Helper for refreshing the song recommendations.
     */
    private void refreshSongRecommendation() {
        songRecommendation = new ArrayList<>(getOwningUser().getLikedSongs());

        songRecommendation.sort(Comparator.comparing(Song::getLikeCnt).reversed());

        while (songRecommendation.size() > MAX_SONG_RANK_NUMBER) {
            songRecommendation.remove(songRecommendation.size() - 1);
        }
    }


    /**
     * Helper for refreshing the playlist recommendations.
     */
    private void refreshPlaylistRecommendation() {
        playlistRecommendation = new ArrayList<>(getOwningUser().getFollowedPlaylists());

        playlistRecommendation.sort(Comparator.comparing(Playlist::computeLikeCnt).reversed());

        while (playlistRecommendation.size() > MAX_PLAYLIST_RANK_NUMBER) {
            playlistRecommendation.remove(playlistRecommendation.size() - 1);
        }
    }
}
