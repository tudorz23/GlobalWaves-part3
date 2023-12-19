package commands.searchbarCommands.searchStrategy;

/**
 * Interface for applying the Strategy Design Pattern to the Search Command.
 */
public interface ISearchStrategy {
    /**
     * Searches the Database for Audio entities that respect given filters.
     */
    void search();
}
