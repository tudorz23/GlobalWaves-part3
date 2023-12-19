package database;

import utils.enums.SearchableType;

/**
 * Extended by all objects that can be searched via search bar.
 * Offers a type field to distinguish between different extenders.
 */
public abstract class Searchable {
    private SearchableType searchableType;

    /* Getters and Setters */
    /**
     * Getter for searchableType.
     */
    public SearchableType getSearchableType() {
        return searchableType;
    }

    /**
     * Setter for searchableType.
     */
    public void setSearchableType(final SearchableType searchableType) {
        this.searchableType = searchableType;
    }
}
