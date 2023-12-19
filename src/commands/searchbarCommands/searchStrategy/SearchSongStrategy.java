package commands.searchbarCommands.searchStrategy;

import client.Session;
import database.Searchable;
import database.audio.Song;
import database.users.User;
import fileio.input.CommandInput;
import fileio.input.FiltersInput;
import utils.enums.Criteria;
import java.util.ArrayList;
import java.util.Iterator;
import static utils.Constants.SEARCH_MAX_RESULT_SIZE;

public final class SearchSongStrategy implements ISearchStrategy {
    private final Session session;
    private final CommandInput commandInput;
    private final User user;

    /* Constructor */
    public SearchSongStrategy(final Session session, final CommandInput commandInput,
                              final User user) {
        this.session = session;
        this.commandInput = commandInput;
        this.user = user;
    }

    @Override
    public void search() {
        ArrayList<Searchable> searchResult = user.getSearchResult();
        FiltersInput filtersInput = commandInput.getFilters();

        // Add all songs, then remove those that do not respect the given filters.
        searchResult.addAll(session.getDatabase().getSongs());

        if (filtersInput.getName() != null) {
            searchSongsByName(searchResult, filtersInput.getName());
        }

        if (filtersInput.getAlbum() != null) {
            searchSongsByAlbum(searchResult, filtersInput.getAlbum());
        }

        if (filtersInput.getTags() != null) {
            searchSongsByTags(searchResult, filtersInput.getTags());
        }

        if (filtersInput.getLyrics() != null) {
            searchSongsByLyrics(searchResult, filtersInput.getLyrics());
        }

        if (filtersInput.getGenre() != null) {
            searchSongsByGenre(searchResult, filtersInput.getGenre());
        }

        if (filtersInput.getReleaseYear() != null) {
            searchSongsByReleaseYear(searchResult, filtersInput.getReleaseYear());
        }

        if (filtersInput.getArtist() != null) {
            searchSongsByArtist(searchResult, filtersInput.getArtist());
        }

        while (searchResult.size() > SEARCH_MAX_RESULT_SIZE) {
            searchResult.remove(searchResult.size() - 1);
        }
    }

    private void searchSongsByName(final ArrayList<Searchable> searchResult, final String name) {
        Iterator<Searchable> iterator = searchResult.iterator();
        while (iterator.hasNext()) {
            Song song = (Song) iterator.next();

            if (!song.getName().startsWith(name)) {
                iterator.remove();
            }
        }
    }

    private void searchSongsByAlbum(final ArrayList<Searchable> searchResult, final String album) {
        Iterator<Searchable> iterator = searchResult.iterator();
        while (iterator.hasNext()) {
            Song song = (Song) iterator.next();

            if (!song.getAlbum().equals(album)) {
                iterator.remove();
            }
        }
    }

    private void searchSongsByTags(final ArrayList<Searchable> searchResult,
                                   final ArrayList<String> tags) {
        Iterator<Searchable> iterator = searchResult.iterator();
        while (iterator.hasNext()) {
            Song song = (Song) iterator.next();

            if (!song.getTags().containsAll(tags)) {
                iterator.remove();
            }
        }
    }

    private void searchSongsByLyrics(final ArrayList<Searchable> searchResult,
                                     final String lyrics) {
        Iterator<Searchable> iterator = searchResult.iterator();
        while (iterator.hasNext()) {
            Song song = (Song) iterator.next();

            if (!stringContainsCaseInsensitive(song.getLyrics(), lyrics)) {
                iterator.remove();
            }
        }
    }

    /**
     * Checks if str1 contains str2, case-insensitive.
     * @return true if it does, false otherwise.
     */
    private boolean stringContainsCaseInsensitive(final String str1, final String str2) {
        String copy1 = str1.toLowerCase();
        String copy2 = str2.toLowerCase();
        return copy1.contains(copy2);
    }

    private void searchSongsByGenre(final ArrayList<Searchable> searchResult, final String genre) {
        Iterator<Searchable> iterator = searchResult.iterator();
        while (iterator.hasNext()) {
            Song song = (Song) iterator.next();

            if (!song.getGenre().equalsIgnoreCase(genre)) {
                iterator.remove();
            }
        }
    }

    private void searchSongsByReleaseYear(final ArrayList<Searchable> searchResult,
                                          final String releaseYear) {
        Criteria criteria;

        // Check if given criteria respects the format.
        try {
            criteria = Criteria.parseCriteria(releaseYear);
        } catch (IllegalArgumentException illegalArgumentException) {
            return;
        }

        String stringYear = releaseYear.substring(1);

        int reqYear;

        // Integer.parseInt throws exception if the string does not contain an integer.
        try {
            reqYear = Integer.parseInt(stringYear);
        } catch (NumberFormatException numberFormatException) {
            return;
        }

        Iterator<Searchable> iterator = searchResult.iterator();
        while (iterator.hasNext()) {
            Song song = (Song) iterator.next();

            if (!respectsCriteria(song.getReleaseYear(), reqYear, criteria)) {
                iterator.remove();
            }
        }
    }

    /**
     * Checks if the year respects the given criteria compared to the requested year.
     */
    private boolean respectsCriteria(final int year, final int reqYear,
                                     final Criteria criteria) {
        if (criteria == Criteria.BEFORE) {
            return (year < reqYear);
        }

        return (year > reqYear);
    }

    private void searchSongsByArtist(final ArrayList<Searchable> searchResult,
                                     final String artist) {
        Iterator<Searchable> iterator = searchResult.iterator();
        while (iterator.hasNext()) {
            Song song = (Song) iterator.next();

            if (!song.getArtist().equals(artist)) {
                iterator.remove();
            }
        }
    }
}
