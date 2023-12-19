package commands.searchbarCommands.searchStrategy;

import client.Session;
import database.Searchable;
import database.audio.Podcast;
import database.users.User;
import fileio.input.CommandInput;
import fileio.input.FiltersInput;
import java.util.ArrayList;
import java.util.Iterator;
import static utils.Constants.SEARCH_MAX_RESULT_SIZE;

public final class SearchPodcastStrategy implements ISearchStrategy {
    private final Session session;
    private final CommandInput commandInput;
    private final User user;

    /* Constructor */
    public SearchPodcastStrategy(final Session session, final CommandInput commandInput,
                                 final User user) {
        this.session = session;
        this.commandInput = commandInput;
        this.user = user;
    }

    @Override
    public void search() {
        ArrayList<Searchable> searchResult = user.getSearchResult();

        FiltersInput filtersInput = commandInput.getFilters();

        // Add all podcasts, then remove those that do not respect the given filters.
        searchResult.addAll(session.getDatabase().getPodcasts());

        if (filtersInput.getName() != null) {
            searchPodcastsByName(searchResult, filtersInput.getName());
        }

        if (filtersInput.getOwner() != null) {
            searchPodcastsByOwner(searchResult, filtersInput.getOwner());
        }

        while (searchResult.size() > SEARCH_MAX_RESULT_SIZE) {
            searchResult.remove(searchResult.size() - 1);
        }
    }

    /**
     * Traverses the Search result list and removes the podcasts
     * that do not have the indicated name.
     */
    private void searchPodcastsByName(final ArrayList<Searchable> searchResult,
                                      final String name) {
        Iterator<Searchable> iterator = searchResult.iterator();
        while (iterator.hasNext()) {
            Podcast podcast = (Podcast) iterator.next();

            if (!podcast.getName().startsWith(name)) {
                iterator.remove();
            }
        }
    }

    /**
     * Traverses the Search result list and removes the podcasts
     * that do not have the indicated owner.
     */
    private void searchPodcastsByOwner(final ArrayList<Searchable> searchResult,
                                       final String owner) {
        Iterator<Searchable> iterator = searchResult.iterator();
        while (iterator.hasNext()) {
            Podcast podcast = (Podcast) iterator.next();

            if (!podcast.getOwner().equals(owner)) {
                iterator.remove();
            }
        }
    }
}
