package commands.adminCommands.recommendationsStrategy;

import client.Session;
import database.audio.Playlist;
import database.audio.Song;
import database.users.Artist;
import database.users.User;
import fileio.output.PrinterBasic;
import utils.MapOperations;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Comparator;

import static utils.Constants.MAX_TOP_FANS_CNT;
import static utils.Constants.MAX_TOP_SONGS_CNT;


public final class FansPlaylistStrategy implements RecommendationStrategy {
    private final Session session;
    private final User user;
    private final PrinterBasic printer;

    /* Constructor */
    public FansPlaylistStrategy(final Session session, final User user,
                                final PrinterBasic printer) {
        this.session = session;
        this.user = user;
        this.printer = printer;
    }

    @Override
    public void recommend() {
        Song playingSong = (Song) user.getPlayer().getCurrPlaying();

        Artist artist;
        try {
            artist = session.getDatabase().searchArtistInDatabase(playingSong.getArtist());
        } catch (IllegalArgumentException exception) {
            printer.print(exception.getMessage());
            return;
        }

        List<String> topFans = getTopFans(artist);

        // Playlist to be returned to the user.
        String playlistName = artist.getUsername() + " Fan Club recommendations";
        Playlist playlistRecommendation = new Playlist(playlistName, user.getUsername());

        for (String fan : topFans) {
            User userFan = session.getDatabase().searchUserInDatabase(fan);

            List<Song> topSongs = getTopSongsOfUser(userFan);
            playlistRecommendation.addAllSongsFromList(topSongs);
        }

        if (playlistRecommendation.isEmpty()) {
            printer.print("No new recommendations were found");
            return;
        }

        user.getAnalytics().addPlaylistRecommendation(playlistRecommendation);
        user.getAnalytics().updateLatestRecommendation(playlistRecommendation);

        printer.print("The recommendations for user " + user.getUsername()
                + " have been updated successfully.");
    }


    /**
     * Sorts the map of top fans of the artist and selects top 5 of them.
     */
    private List<String> getTopFans(final Artist artist) {
        LinkedHashMap<String, Integer> sortedFans = MapOperations
                .sortStringMapByValue(artist.getArtistAnalytics().getArtistTopFans());

        List<String> topFans = new ArrayList<>();

        int cnt = 0;
        for (Map.Entry<String, Integer> fanEntry : sortedFans.entrySet()) {
            cnt++;
            topFans.add(fanEntry.getKey());

            if (cnt == MAX_TOP_FANS_CNT) {
                break;
            }
        }

        return topFans;
    }


    /**
     * Sorts the liked songs of the user and selects top 5 of them.
     */
    private List<Song> getTopSongsOfUser(final User requestedUser) {
        List<Song> topSongs = new ArrayList<>(requestedUser.getLikedSongs());

        topSongs.sort(Comparator.comparing(Song::getLikeCnt).reversed());
        while (topSongs.size() > MAX_TOP_SONGS_CNT) {
            topSongs.remove(topSongs.size() - 1);
        }

        return topSongs;
    }
}
