package commands.searchbarCommands.searchStrategy;

import client.Session;
import database.Searchable;
import database.audio.Album;
import database.users.User;
import fileio.input.CommandInput;
import fileio.input.FiltersInput;
import java.util.ArrayList;
import java.util.Iterator;
import static utils.Constants.SEARCH_MAX_RESULT_SIZE;

public final class SearchAlbumStrategy implements ISearchStrategy {
    private final Session session;
    private final CommandInput commandInput;
    private final User user;

    /* Constructor */
    public SearchAlbumStrategy(final Session session, final CommandInput commandInput,
                               final User user) {
        this.session = session;
        this.commandInput = commandInput;
        this.user = user;
    }

    @Override
    public void search() {
        ArrayList<Searchable> searchResult = user.getSearchResult();
        FiltersInput filtersInput = commandInput.getFilters();

        // Add all albums, then remove those that do not respect the given filters.
        searchResult.addAll(session.getDatabase().getAlbums());

        if (filtersInput.getName() != null) {
            searchAlbumsByName(searchResult, filtersInput.getName());
        }

        if (filtersInput.getOwner() != null) {
            searchAlbumsByOwner(searchResult, filtersInput.getOwner());
        }

        if (filtersInput.getDescription() != null) {
            searchAlbumsByDescription(searchResult, filtersInput.getDescription());
        }

        while (searchResult.size() > SEARCH_MAX_RESULT_SIZE) {
            searchResult.remove(searchResult.size() - 1);
        }
    }

    /**
     * Traverses the Search result list and removes the albums
     * that do not have the indicated name.
     */
    private void searchAlbumsByName(final ArrayList<Searchable> searchResult, final String name) {
        Iterator<Searchable> iterator = searchResult.iterator();
        while (iterator.hasNext()) {
            Album album = (Album) iterator.next();

            if (!album.getName().startsWith(name)) {
                iterator.remove();
            }
        }
    }

    /**
     * Traverses the Search result list and removes the albums
     * that do not have the indicated owner.
     */
    private void searchAlbumsByOwner(final ArrayList<Searchable> searchResult, final String owner) {
        Iterator<Searchable> iterator = searchResult.iterator();
        while (iterator.hasNext()) {
            Album album = (Album) iterator.next();

            if (!album.getOwner().startsWith(owner)) {
                iterator.remove();
            }
        }
    }

    /**
     * Traverses the Search result list and removes the albums
     * that do not have the indicated description.
     */
    private void searchAlbumsByDescription(final ArrayList<Searchable> searchResult,
                                           final String description) {
        Iterator<Searchable> iterator = searchResult.iterator();
        while (iterator.hasNext()) {
            Album album = (Album) iterator.next();

            if (!album.getDescription().startsWith(description)) {
                iterator.remove();
            }
        }
    }
}
