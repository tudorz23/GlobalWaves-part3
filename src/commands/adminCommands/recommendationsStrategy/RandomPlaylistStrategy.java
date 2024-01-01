package commands.adminCommands.recommendationsStrategy;

import client.Session;
import database.audio.Playlist;
import database.audio.Song;
import database.users.User;
import fileio.output.PrinterBasic;
import utils.MapOperations;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Comparator;

import static utils.Constants.SONGS_FROM_GENRE1;
import static utils.Constants.SONGS_FROM_GENRE2;
import static utils.Constants.SONGS_FROM_GENRE3;
import static utils.Constants.MAX_TOP_GENRES_CNT;
import static utils.Constants.AT_LEAST_2_GENRES;
import static utils.Constants.AT_LEAST_3_GENRES;


public final class RandomPlaylistStrategy implements RecommendationStrategy {
    private final Session session;
    private final User user;
    private final PrinterBasic printer;

    /* Constructor */
    public RandomPlaylistStrategy(final Session session, final User user,
                                  final PrinterBasic printer) {
        this.session = session;
        this.user = user;
        this.printer = printer;
    }

    @Override
    public void recommend() {
        Map<String, Integer> genresMap = getGenresMap();

        if (genresMap.isEmpty()) {
            printer.print("No new recommendations were found");
            return;
        }

        List<String> top3Genres = getTop3Genres(genresMap);

        //  Playlist to be returned to the user.
        String name = user.getUsername() + "'s recommendations";
        Playlist playlistRecommendation = new Playlist(name, user.getUsername());

        addSongsFromGenre(playlistRecommendation, top3Genres.get(0), SONGS_FROM_GENRE1);

        if (top3Genres.size() >= AT_LEAST_2_GENRES) {
            addSongsFromGenre(playlistRecommendation, top3Genres.get(1), SONGS_FROM_GENRE2);
        }

        if (top3Genres.size() == AT_LEAST_3_GENRES) {
            addSongsFromGenre(playlistRecommendation, top3Genres.get(2), SONGS_FROM_GENRE3);
        }

        user.getAnalytics().addPlaylistRecommendation(playlistRecommendation);
        user.getAnalytics().updateLatestRecommendation(playlistRecommendation);

        printer.print("The recommendations for user " + user.getUsername()
                + " have been updated successfully.");
    }


    /**
     * @return Map of type < genre, listens >.
     */
    private Map<String, Integer> getGenresMap() {
        Map<String, Integer> genresMap = new HashMap<>();

        for (Song song : user.getLikedSongs()) {
            int listens = genresMap.getOrDefault(song.getGenre(), 0);
            genresMap.put(song.getGenre(), listens + 1);
        }

        for (Playlist playlist : user.getPlaylists()) {
            for (Song song : playlist.getSongs()) {
                int listens = genresMap.getOrDefault(song.getGenre(), 0);
                genresMap.put(song.getGenre(), listens + 1);
            }
        }

        for (Playlist playlist : user.getFollowedPlaylists()) {
            for (Song song : playlist.getSongs()) {
                int listens = genresMap.getOrDefault(song.getGenre(), 0);
                genresMap.put(song.getGenre(), listens + 1);
            }
        }

        return genresMap;
    }


    /**
     * Sorts the genre map by the listens and selects top 3 genres.
     * @param genresMap Genre map of type < genre, listens >.
     * @return List of top 3 genres.
     */
    private List<String> getTop3Genres(final Map<String, Integer> genresMap) {
        LinkedHashMap<String, Integer> sortedGenresMap =
                                    MapOperations.sortStringMapByValue(genresMap);

        List<String> top3Genres = new ArrayList<>();

        int cnt = 0;
        for (Map.Entry<String, Integer> genreEntry : sortedGenresMap.entrySet()) {
            top3Genres.add(genreEntry.getKey());

            cnt++;
            if (cnt == MAX_TOP_GENRES_CNT) {
                break;
            }
        }

        return top3Genres;
    }


    /**
     * Adds the top songNumber songs from the given genre to the playlist recommendation.
     * @param playlistRecommendation Playlist recommendation for a user.
     * @param genre Genre of interest.
     * @param songNumber Number of songs to add from the given genre.
     */
    private void addSongsFromGenre(final Playlist playlistRecommendation, final String genre,
                                   final int songNumber) {
        List<Song> songsFromGenre = getSongsFromGenre(genre);
        List<Song> topSongsFromGenre = getTopNSongsByLikes(songsFromGenre, songNumber);
        playlistRecommendation.addAllSongsFromList(topSongsFromGenre);
    }


    /**
     * @param genre Genre of interest.
     * @return List of Songs from the database from the given genre.
     */
    private List<Song> getSongsFromGenre(final String genre) {
        List<Song> songsFromGenre = new ArrayList<>();

        for (Song song : session.getDatabase().getSongs()) {
            if (song.getGenre().equalsIgnoreCase(genre)) {
                songsFromGenre.add(song);
            }
        }

        return songsFromGenre;
    }


    /**
     * Sorts the list by the like counter and gets top songNumber songs.
     * @param songList List of Songs.
     * @param songNumber Number of songs to add, i.e. N.
     */
    private List<Song> getTopNSongsByLikes(final List<Song> songList, final int songNumber) {
        List<Song> topNSongs = new ArrayList<>(songList);

        topNSongs.sort(Comparator.comparing(Song::getLikeCnt).reversed());
        while (topNSongs.size() > songNumber) {
            topNSongs.remove(topNSongs.size() - 1);
        }

        return topNSongs;
    }
}
