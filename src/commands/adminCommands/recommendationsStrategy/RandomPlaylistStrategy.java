package commands.adminCommands.recommendationsStrategy;

import client.Session;
import database.audio.Playlist;
import database.audio.Song;
import database.users.User;
import fileio.output.PrinterBasic;
import utils.MapOperations;

import java.util.*;

public class RandomPlaylistStrategy implements RecommendationStrategy {
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

        // This playlist will be returned to the user.
        String name = user.getUsername() + "'s recommendations";
        Playlist playlistRecommendation = new Playlist(name, user.getUsername());

        List<Song> songsFromGenre1 = getSongsFromGenre(top3Genres.get(0));
        List<Song> topSongsGenre1 = getTopNSongsByLikes(songsFromGenre1, 5);
        playlistRecommendation.addAllSongsFromList(topSongsGenre1);

        if (top3Genres.size() >= 2) {
            List<Song> songsFromGenre2 = getSongsFromGenre(top3Genres.get(1));
            List<Song> topSongsGenre2 = getTopNSongsByLikes(songsFromGenre2, 3);
            playlistRecommendation.addAllSongsFromList(topSongsGenre2);
        }

        if (top3Genres.size() == 3) {
            List<Song> songsFromGenre3 = getSongsFromGenre(top3Genres.get(2));
            List<Song> topSongsGenre3 = getTopNSongsByLikes(songsFromGenre3, 2);
            playlistRecommendation.addAllSongsFromList(topSongsGenre3);
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
     * Sorts the genre map and selects top 3 genres.
     * @param genresMap Genre map of type < genre, listens >.
     * @return List of top 3 genres.
     */
    private List<String> getTop3Genres(Map<String, Integer> genresMap) {
        LinkedHashMap<String, Integer> sortedGenresMap = MapOperations.sortStringMapByValue(genresMap);

        List<String> top3Genres = new ArrayList<>();
        int cnt = 0;
        for (Map.Entry<String, Integer> genreEntry : sortedGenresMap.entrySet()) {
            cnt++;
            top3Genres.add(genreEntry.getKey());
            if (cnt == 3) {
                break;
            }
        }

        return top3Genres;
    }


    /**
     * @param genre Genre of interest.
     * @return List of Songs from the database from the given genre.
     */
    private List<Song> getSongsFromGenre(String genre) {
        List<Song> songsFromGenre = new ArrayList<>();

        for (Song song : session.getDatabase().getSongs()) {
            if (song.getGenre().equalsIgnoreCase(genre)) {
                songsFromGenre.add(song);
            }
        }

        return songsFromGenre;
    }


    /**
     * Sorts the list by the like counter and gets top N songs.
     * @param songList List of Songs.
     * @param N int number.
     */
    private List<Song> getTopNSongsByLikes(List<Song> songList, int N) {
        List<Song> topNSongs = new ArrayList<>(songList);

        topNSongs.sort(Comparator.comparing(Song::getLikeCnt).reversed());
        while (topNSongs.size() > N) {
            topNSongs.remove(topNSongs.size() - 1);
        }

        return topNSongs;
    }
}
